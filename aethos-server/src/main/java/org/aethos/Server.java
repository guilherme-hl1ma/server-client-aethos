package org.aethos;

import org.aethos.config.FirebaseConfig;
import org.aethos.socket.Client;
import org.aethos.socket.ConnectionValidator;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe que representa o servidor Java. Inicia o servidor, adicionalmente, instancia uma Thread que cobrirá todas
 * as conexões, ou seja, validará todas os pedidos de conexões dos clientes ao servidor.
 */
public class Server {
    public static String DEFAULT_PORT = "3000";

    public static void main(String[] args) {
        String port = Server.DEFAULT_PORT;

        ArrayList<Client> clients = new ArrayList<Client>();

        try {
            FirebaseConfig firebaseConfig = new FirebaseConfig();
        } catch (IOException error) {
            System.out.println("Error to instantiated firebase config");
        }

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
