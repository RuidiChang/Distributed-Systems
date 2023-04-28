/**
 * @ClassName: RemoteVariableServerTCP
 * @Author: Ruidi Chang
 * @Date 2022/10/8 12:52 PM
 * @Version 1.0
 */
package cmu.edu.ruidic;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Scanner;

public class RemoteVariableServerTCP {
    static Socket clientSocket = null;
    static HashMap<String, Integer> sum = new HashMap<String, Integer>();
    static Scanner in;
    static PrintWriter out;

    public static void main(String args[]) throws IOException {
        try {
            int serverPort = 7777; // the server port we are using
            // Create a new server socket
            ServerSocket listenSocket = new ServerSocket(serverPort);
            // make it can still connect after re-run
            while (true) {
                try {
                    clientSocket = listenSocket.accept();
                    in = new Scanner(clientSocket.getInputStream());
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
                    while (true) {
                        if (in.hasNext()) {
                            // Receive the request message from client
                            String operation = in.nextLine();
                            // if user choose 4, break the loop
                            if (operation.equals("Exit")) break;
                            Operation(operation);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                // ignore exception on close
            }
        }
    }

    public static void Operation(String operation) throws IOException {
        // if user choose 1: add
        if (operation.startsWith("Add")) {
            String id = operation.split(" ")[1];
            int num_add = Integer.parseInt(operation.split(" ")[3]);
            System.out.println("ID: " + id + " Add " + num_add);
            // check if id is in the map
            // if id in the map: add the new number to sum
            // if id not in the map: put the id in the map as a key and let the value be the new number
            if (sum.containsKey(id)) {
                sum.put(id, sum.get(id) + num_add);
            } else {
                sum.put(id, num_add);
            }
        } else if (operation.startsWith("Subtract")) {
            String id = operation.split(" ")[1];
            int num_sub = Integer.parseInt(operation.split(" ")[3]);
            System.out.println("ID: " + id + " Subtract " + num_sub);
            // check if id is in the map
            // if id in the map: subtract the new number to sum
            // if id not in the map: put the id in the map as a key and let the value be 0 minus the new number
            if (sum.containsKey(id)) {
                sum.put(id, sum.get(id) - num_sub);
            } else {
                sum.put(id, 0 - num_sub);
            }
        } else if (operation.startsWith("Get")) {
            if (sum.containsKey(operation.split(" ")[1])) {
            } else {
                String id = operation.split(" ")[1];
                sum.put(id, 0);
            }
        }
        System.out.println(operation);
        String reply = String.valueOf(sum.get(operation.split(" ")[1]));
        System.out.println("Return: ID: " + operation.split(" ")[1] + " value: " + sum.get(operation.split(" ")[1]));
        if (operation.startsWith("Exit")) {
        } else {
            // send the output to the client
            out.println(reply);
            out.flush();
        }
    }
}
