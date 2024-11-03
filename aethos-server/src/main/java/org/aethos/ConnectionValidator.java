package org.aethos;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionValidator extends Thread
{
    private ServerSocket serverSocket;
    private ArrayList<Client> clients;

    public ConnectionValidator(String port, ArrayList<Client> clients) throws Exception
    {
        if (port == null)  throw new Exception("Port is missing.");

        try
        {
            this.serverSocket = new ServerSocket(Integer.parseInt(port));
        }
        catch (Exception error)
        {
            throw new Exception("Port is invalid.");
        }

        if (clients == null)    throw new Exception("Clients is missing");

        this.clients = clients;
    }

    public void run()
    {
        for (;;)
        {
            Socket connection = null;
            try
            {
                connection = this.serverSocket.accept();
            } catch (Exception error)
            {
                continue;
            }

            ConnectionWatcher connectionWatcher = null;
            try
            {
                connectionWatcher = new ConnectionWatcher(connection, clients);
            }
            catch (Exception error) {
                continue;
            }

            connectionWatcher.start();
        }
    }
}
