package org.aethos.socket;

import org.aethos.controller.NotificationEventController;
import org.aethos.socket.Client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Classe que representa a Thread de operação dos clientes, a que vai lidar com o recebimento de dados do Cliente.
 */
public class ConnectionWatcher extends Thread
{
    private Client client;
    private Socket connection;
    private ArrayList<Client> clients;
    private NotificationEventController notificationEventController;

    public ConnectionWatcher(Socket connection, ArrayList<Client> clients) throws Exception
    {
        if (connection == null) throw new Exception("Connection is missing.");

        if (clients == null)    throw new Exception("Clients is missing.");

        this.connection = connection;
        this.clients = clients;
        this.notificationEventController = new NotificationEventController();
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
            receiver = new BufferedReader(new InputStreamReader(this.connection.getInputStream(), StandardCharsets.UTF_8));
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
                String clientMessage = this.client.send();
                System.out.println(clientMessage);

                if (clientMessage == null) {
                    return;
                }

                String response = notificationEventController.handleNotification(clientMessage);

                System.out.println(response);

                if (response != null) {
                    client.receive(response);
                }
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
