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

public class ConnectionWatcher extends Thread
{
    private Client client;
    private Socket connection;
    private ArrayList<Client> clients;
    private FirebaseConfig firebaseConfig;
    private final Gson gson = new Gson();
    private Firestore db;

    public ConnectionWatcher(Socket connection, ArrayList<Client> clients) throws Exception
    {
        if (connection == null) throw new Exception("Connection is missing.");

        if (clients == null)    throw new Exception("Clients is missing.");

        this.connection = connection;
        this.clients = clients;
        firebaseConfig = new FirebaseConfig();
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
            synchronized (this.client) {
                this.clients.add(this.client);
            }

            for (;;) {
                String postNotification = this.client.send();

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

                client.receive(gson.toJson(notificationEvent));
            }
        } catch (Exception error) {
            try {
                transmitter.close();
                receiver.close();
                this.connection.close();
                this.clients.remove(this.client);
            } catch (Exception failure) {}
        } finally {
            try {
                this.client.disconnectAll();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            this.clients.remove(this.client);
            System.out.println("Desconectando o cliente...");
        }
    }
}
