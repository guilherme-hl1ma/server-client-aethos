package org.aethos;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;
import org.aethos.models.NotificationEvent;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Classe que representa a Thread de operação dos clientes, a que vai lidar com o recebimento de dados do Cliente.
 */
public class ConnectionWatcher extends Thread
{
    private Client client;
    private Socket connection;
    private ArrayList<Client> clients;
    private final Gson gson = new Gson();
    private final Firestore db;

    public ConnectionWatcher(Socket connection, ArrayList<Client> clients) throws Exception
    {
        if (connection == null) throw new Exception("Connection is missing.");

        if (clients == null)    throw new Exception("Clients is missing.");

        this.connection = connection;
        this.clients = clients;

        db = FirestoreClient.getFirestore();
    }

    public void run()
    {
        PrintWriter transmitter;
        try
        {
            transmitter = new PrintWriter(this.connection.getOutputStream(), true);
        }
        catch (Exception error)
        {
            return;
        }

        BufferedReader receiver;
        try
        {
            receiver = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));;
        }
        catch (Exception error)
        {
            try {
                transmitter.close();
            } catch (Exception failure) {}

            return;
        }

        try {
            this.client = new Client(this.connection, receiver, transmitter);
        } catch (Exception error) {}

        try {
            synchronized (this.clients) {
                this.clients.add(this.client);
            }

            for (;;) {
                String postNotification = this.client.send();

                if (postNotification == null) {
                    return;
                }

                System.out.println(postNotification);

                NotificationEvent notificationEvent = gson.fromJson(postNotification, NotificationEvent.class);

                Map<String, Object> docData = new HashMap<>();
                docData.put("uuidFromUser", notificationEvent.getUuidUserFrom());
                docData.put("uuidToUser", notificationEvent.getUuidUserTo());
                docData.put("like", notificationEvent.isLike());
                docData.put("follow", notificationEvent.isFollow());
                docData.put("comment", notificationEvent.isComment());
                docData.put("read", notificationEvent.isRead());

                ApiFuture<WriteResult> result = db.collection("notifications").document().set(docData);

                try {
                    WriteResult writeResult = result.get();  // Aguarda o resultado
                    System.out.println("Data saved at: " + writeResult.getUpdateTime());
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("Error saving data to Firestore: " + e.getMessage());
                    e.printStackTrace();
                }

                client.receive(gson.toJson(notificationEvent));
            }
        } catch (Exception error) {
            try {
                transmitter.close();
                receiver.close();
            } catch (Exception failure) {
                synchronized (this.clients) {
                    this.clients.remove(this.client);
                }
                try {
                    this.client.disconnectAll();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
