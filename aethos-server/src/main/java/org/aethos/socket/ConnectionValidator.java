package org.aethos.socket;

import org.aethos.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Classe que representa uma Thread responsável por validar as conexões. Verifica a porta de conexão, instancia os
 * clientes conectados ao servidor. Além disso, lida com o fechamento e o reinício das Threads que serão usadas para
 * observar as atividades do cliente.
 */
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

    public void run() {
        while (true) {
            Socket connection = null;
            try {
                connection = this.serverSocket.accept();
            } catch (IOException error) {
                System.out.println("Failed to accept connection. Restarting server...");
                restartServerSocket();
                continue;
            }

            try {
                ConnectionWatcher connectionWatcher = new ConnectionWatcher(connection, clients);
                connectionWatcher.start();
            } catch (Exception error) {
                System.out.println("Error starting ConnectionWatcher: " + error.getMessage());
                closeConnection(connection);
            }
        }
    }

    /**
     * Método para reiniciar o ServerSocket.
     */
    private void restartServerSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            this.serverSocket = new ServerSocket(Integer.parseInt(Server.DEFAULT_PORT));
            System.out.println("Server restarted on port: " + Server.DEFAULT_PORT);
        } catch (IOException e) {
            System.out.println("Failed to restart server socket: " + e.getMessage());
        }
    }

    /**
     * Método para fechar uma conexão.
     * @param connection Conexão Socket
     */
    private void closeConnection(Socket connection) {
        if (connection != null && !connection.isClosed()) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
