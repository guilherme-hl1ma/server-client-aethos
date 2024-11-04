package org.aethos.socket;


import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket connection;
    private BufferedReader receiver;
    private PrintWriter transmitter;

    private String nextMessage = null;

    public Client(Socket connection, BufferedReader receiver, PrintWriter transmitter) throws Exception
    {
        if (connection == null) throw new Exception("Connection is missing");

        if (receiver == null)   throw new Exception("Receiver is missing");

        if (transmitter == null)    throw new Exception("Transmitter is missing");

        this.connection = connection;
        this.receiver = receiver;
        this.transmitter = transmitter;
    }

    public void receive(Object response) {
        Gson gson = new Gson();
        this.transmitter.println(gson.toJson(response));
    }

    public String send() throws Exception {
        try {
            String line = this.receiver.readLine();
            if (line != null) {
                return line;
            } else {
                throw new Exception("Connection closed or no message received");
            }
        } catch (IOException e) {
            throw new Exception("Error reading message: " + e.getMessage());
        }
    }

    public void disconnectAll() throws Exception
    {
        try
        {
            this.transmitter.close();
            this.receiver.close();
            this.connection.close();
        }
        catch (Exception error)
        {
            throw new Exception("Disconnection Error");
        }
    }
}
