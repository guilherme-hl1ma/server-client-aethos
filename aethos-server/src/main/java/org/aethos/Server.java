package org.aethos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Server {
    public static String DEFAULT_PORT = "3000";

    public static void main(String[] args) {
        String port = Server.DEFAULT_PORT;

        ArrayList<Client> clients = new ArrayList<Client>();

        ConnectionValidator connectionValidator = null;
        try
        {
            connectionValidator = new ConnectionValidator(port, clients);
            connectionValidator.start();
        }
        catch (Exception error)
        {
            System.out.println("Port is invalid. Choose a valid one.");
            return;
        }

        System.out.println("Server is running in port: " + port);
    }
}
