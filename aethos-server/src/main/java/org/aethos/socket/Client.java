package org.aethos.socket;


import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

/**
 * Classe que representa o Cliente Socket que se comunica com o Java.
 * */
public class Client {
    private final Socket connection;
    private final BufferedReader receiver;
    private final PrintWriter transmitter;

    public Client(Socket connection, BufferedReader receiver, PrintWriter transmitter) throws Exception
    {
        if (connection == null) throw new Exception("Connection is missing");

        if (receiver == null)   throw new Exception("Receiver is missing");

        if (transmitter == null)    throw new Exception("Transmitter is missing");

        this.connection = connection;
        this.receiver = receiver;
        this.transmitter = transmitter;
    }

    /**
     * Função responsável por enviar os dados para o Socket do Cliente.
     * @param response Objeto de Resposta à requisição do Cliente.
     * */
    public void receive(Object response) {
        Gson gson = new Gson();
        this.transmitter.println(gson.toJson(response));
    }

    /**
     * Função responsável por receber os dados do Cliente e repassá-los para o Servidor.
     * */
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

    /**
     * Função responsável por desconectar o Cliente do Servidor.
     * */
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
