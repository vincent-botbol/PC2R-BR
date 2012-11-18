package br.run;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurTest {

    InetAddress adr;
    ServerSocket sock;

    public ServeurTest() {
        try {
            sock = new ServerSocket(2012);
            System.out.println("Serveur à l'écoute sur le port : " + sock.getLocalPort());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void startServer() {
        System.out.println("En attente de client");

        try (Socket client = sock.accept()) {
            System.out.println("Client connecté");

            final PrintWriter out = new PrintWriter(
                    new BufferedWriter(
                    new OutputStreamWriter(client.getOutputStream())));
            final BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            //Lecture continue des messages du client
            Thread lecture = new Thread() {

                @Override
                public void run() {
                    while (true) {
                        synchronized (this) {
                            try {
                                System.out.println("Reçue commande client : " + in.readLine());
                                System.out.print("Command$ ");
                                System.out.flush();
                            } catch (IOException ex) {
                                break;
                            }
                        }
                    }
                    System.out.println("WESH");
                }
            };

            Thread écriture = new Thread() {

                @Override
                public void run() {
                    String command = "";
                    BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
                    while (!command.trim().equals("quit")) {
                        synchronized (this) {
                            try {
                                System.out.print("Command$ ");
                                command = consoleIn.readLine();
                                System.out.println("Envoi de " + command + " au client");
                                out.printf("%s\n", command);
                                out.flush();
                            } catch (IOException ex) {
                                break;
                            }
                        }
                    }
                    System.out.println("WESH");
                }
            };

            lecture.start();
            écriture.start();
            try {
                écriture.join();
            } catch (InterruptedException ex) {
            	ex.printStackTrace();
            }
            out.close();
            in.close();
            client.close();
        } catch (IOException ex) {
            System.out.println("Déconnexion du client ?");
            ex.printStackTrace();
        }
    }
    
     public static void main(String[] args) {
        new ServeurTest().startServer();
    }
}