/**
 * @ClassName: SigningClientTCP
 * @Author: Ruidi Chang
 * @Date 2022/10/8 13:23 PM
 * @Version 1.0
 */
package cmu.edu.ruidic;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class SigningClientTCP {
    static BufferedReader in;
    static PrintWriter out;
    public static void main(String args[]) {
        Socket clientSocket = null;
        try {
            int serverPort = 7777;
            clientSocket = new Socket("localhost", serverPort);
            // Set up "in" to read from the server socket
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // Set up "out" to write to the server socket
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
            RSA rsa = new RSA();
            System.out.println("Your private key is: "+rsa.getD()+" "+rsa.getN());
            System.out.println("Your public key is: "+rsa.getE()+" "+rsa.getN());
            String id = rsa.getID(rsa.getE(),rsa.getN());
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            String nextLine;
            System.out.println("1. Add a value to your sum.\n" +
                    "2. Subtract a value from your sum.\n" +
                    "3. Get your sum.\n" +
                    "4. Exit client.");
            while ((nextLine = typed.readLine()) != null) {
                int option = Integer.parseInt(nextLine);
                switch (option){
                    // User enter 1: add
                    case 1:
                        System.out.println("Enter value to add:");
                        String num1 = typed.readLine();
                        System.out.println("The result is " + add(num1, id, rsa));
                        break;
                    // User enter 2: subtract
                    case 2:
                        System.out.println("Enter value to subtract:");
                        String num2 = typed.readLine();
                        System.out.println("The result is " + subtract(num2, id, rsa));
                        break;
                    // User enter 3: get
                    case 3:
                        System.out.println("The result is " + get(id,rsa));
                        break;
                    // User enter 4: exit
                    case 4:
                        exit(id,rsa);
                        System.out.println("Client side quitting. The remote variable server is still running.");
                        clientSocket.close();
                        return;
                }
                System.out.println();
                System.out.println("1. Add a value to your sum.\n" +
                        "2. Subtract a value from your sum.\n" +
                        "3. Get your sum.\n" +
                        "4. Exit client.");
            }
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
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


    public static String add(String i, String id, RSA rsa) throws IOException, NoSuchAlgorithmException {
        // make standard form of add message
        String encryptedHashStr = rsa.sign(id+rsa.getE().toString()+rsa.getN().toString()+"add"+i);
        String message = id+" "+rsa.getE().toString()+" "+rsa.getN().toString()+" "+"add "+i+" "+encryptedHashStr;
        // send the message to the server
        out.println(message);
        out.flush();
        // receive the sum from the server
        String sum = in.readLine();
        return sum;
    }
    public static String subtract(String i, String id, RSA rsa) throws IOException, NoSuchAlgorithmException {
        // make standard form of subtract message
        String encryptedHashStr = rsa.sign(id+rsa.getE().toString()+rsa.getN().toString()+"subtract"+i);
        String message = id+" "+rsa.getE().toString()+" "+rsa.getN().toString()+" "+"subtract "+i+" "+encryptedHashStr;
        // send the message to the server
        out.println(message);
        out.flush();
        // receive the sum from the server
        String sum = in.readLine();
        return sum;
    }
    public static String get(String id, RSA rsa) throws IOException, NoSuchAlgorithmException {
        // make standard form of get message
        String encryptedHashStr = rsa.sign(id+rsa.getE().toString()+rsa.getN().toString()+"get");
        String message = id+" "+rsa.getE().toString()+" "+rsa.getN().toString()+" "+"get "+encryptedHashStr;
        // send the message to the server
        out.println(message);
        out.flush();
        // receive the sum from the server
        String sum = in.readLine();
        return sum;
    }
    public static void exit(String id, RSA rsa) throws IOException, NoSuchAlgorithmException {
        // make standard form of add message
        String encryptedHashStr = rsa.sign(id+rsa.getE().toString()+rsa.getN().toString()+"exit");
        String message = id+" "+rsa.getE().toString()+" "+rsa.getN().toString()+" "+"exit"+" "+encryptedHashStr;
        // send the message to the server
        out.println(message);
        out.flush();
    }
}
