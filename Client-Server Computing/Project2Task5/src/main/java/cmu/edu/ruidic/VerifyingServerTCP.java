/**
 * @ClassName: VerifyingServerTCP
 * @Author: Ruidi Chang
 * @Date 2022/10/8 13:23 PM
 * @Version 1.0
 */
package cmu.edu.ruidic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;


public class VerifyingServerTCP {
    static HashMap<String, Integer> sum = new HashMap<String, Integer>();
    static Scanner in;
    static PrintWriter out;

    public static void main(String args[]) throws IOException, NoSuchAlgorithmException {
        Socket clientSocket = null;
        HashMap<String, Integer> sum = new HashMap<String, Integer>();

        try {
            int serverPort = 7777; // the server port we are using

            // Create a new server socket
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while (true) {
                try {
                    clientSocket = listenSocket.accept();
                    in = new Scanner(clientSocket.getInputStream());
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
                    while (true) {
                        if (in.hasNext()) {
                            // Receive the request message from client
                            String message = in.nextLine();
                            // if user choose 4, break the loop
                            if (message.split(" ")[3].equals("exit")) break;
                            Operation(message);
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
    public static void Operation(String message) throws IOException, NoSuchAlgorithmException {
        String [] requestString = message.split(" ");
        // get e and n
        BigInteger e = new BigInteger(requestString[1]);
        BigInteger n = new BigInteger(requestString[2]);
        if (requestString[3].equals("add")){
            message = requestString[0]+requestString[1]+requestString[2]+requestString[3]+requestString[4];
            String encryptedHashStr = requestString[5];
            // check ID match and verify signature
            if (RSA.verify(message,encryptedHashStr,e,n)&&(requestString[0].equals(RSA.getID(e,n)))){
                System.out.println("Adding: "+ requestString[4] +" to ID "+ requestString[0]);
                // check if id is in the map
                // if id in the map: add the new number to sum
                // if id not in the map: put the id in the map as a key and let the value be the new number
                if (sum.containsKey(requestString[0])){
                    sum.put(requestString[0], sum.get(requestString[0])+Integer.parseInt(requestString[4]));
                } else {
                    sum.put(requestString[0], Integer.parseInt(requestString[4]));
                }
            }else{
                System.out.println("Error in request");
                out.println("Error in request");
                out.flush();
            }
        } else if(requestString[3].equals("subtract")) {
            message = requestString[0]+requestString[1]+requestString[2]+requestString[3]+requestString[4];
            String encryptedHashStr = requestString[5];
            // check ID match and verify signature
            if (RSA.verify(message,encryptedHashStr,e,n)&&(requestString[0].equals(RSA.getID(e,n)))){
                // subtract the value
                System.out.println("Subtracting: " + requestString[4] + " to ID " + requestString[0]);
                // check if id is in the map
                // if id in the map: add the new number to sum
                // if id not in the map: put the id in the map as a key and let the value be the new number
                if (sum.containsKey(requestString[0])) {
                    sum.put(requestString[0], sum.get(requestString[0]) - Integer.parseInt(requestString[4]));
                } else {
                    sum.put(requestString[0], 0 - Integer.parseInt(requestString[4]));
                }
            } else {
                System.out.println("Error in request");
                out.println("Error in request");
                out.flush();
            }

        }else if(requestString[3].equals("get")){
            message = requestString[0]+requestString[1]+requestString[2]+requestString[3];
            String encryptedHashStr = requestString[4];
            // check ID match and verify signature
            if (RSA.verify(message,encryptedHashStr,e,n)&&(requestString[0].equals(RSA.getID(e,n)))){
            }else{
                System.out.println("Error in request");
                out.println("Error in request");
                out.flush();
            }
        }
        if (requestString[3].equals("exit")) {
        } else {
            String reply = String.valueOf(sum.get(requestString[0]));
            System.out.println("Return: ID: " + requestString[0] + " value: " + sum.get(requestString[0]));
            // send the output to the client
            out.println(reply);
            out.flush();
        }
    }
}