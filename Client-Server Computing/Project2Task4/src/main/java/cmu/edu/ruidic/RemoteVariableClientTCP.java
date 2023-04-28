/**
 * @ClassName: RemoteVariableClientTCP
 * @Author: Ruidi Chang
 * @Date 2022/10/8 12:52 PM
 * @Version 1.0
 */
package cmu.edu.ruidic;

import java.io.*;
import java.net.DatagramPacket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class RemoteVariableClientTCP {
    static BufferedReader in;
    static PrintWriter out;
    public static void main(String args[]) {
        // arguments supply hostname
        Socket clientSocket = null;
        try {
            int serverPort = 7777;
            clientSocket = new Socket("localhost", serverPort);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            String nextLine;
            String id;
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
                        int num1 = Integer.parseInt(typed.readLine());
                        System.out.println("Enter your ID:");
                        id = typed.readLine();
                        System.out.println("The result is " + add(num1, id));
                        break;
                    // User enter 2: subtract
                    case 2:
                        System.out.println("Enter value to subtract:");
                        int num2 = Integer.parseInt(typed.readLine());
                        System.out.println("Enter your ID:");
                        id = typed.readLine();
                        System.out.println("The result is " + subtract(num2, id));
                        break;
                    // User enter 3: get
                    case 3:
                        System.out.println("Enter your ID:");
                        id = typed.readLine();
                        System.out.println("The result is " + get(id));
                        break;
                    // handle close
                    case 4:
                        exit();
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
    public static String add(int i,String id) throws IOException {
        String message_add = "Add "+id+" to "+i;
        // send message to the server
        out.println(message_add);
        out.flush();
        // receive the sum from the server
        String sum = in.readLine();
        return sum;
    }
    public static String subtract(int i,String id) throws IOException {
        String message_subtract = "Subtract "+id+" to "+i;
        // send message to the server
        out.println(message_subtract);
        out.flush();
        // receive the sum from the server
        String sum = in.readLine();
        return sum;
    }
    public static String get(String id) throws IOException {
        String message_get = "Get "+id;
        // send message to the server
        out.println(message_get);
        out.flush();
        // receive the sum from the server
        String sum = in.readLine();
        return sum;
    }
    public static void exit() throws IOException {
        String message_get = "Exit";
        out.println(message_get);
        out.flush();
    }
}
