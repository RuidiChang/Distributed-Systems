/**
 * @ClassName: RemoteVariableClientUDP
 * @Author: Ruidi Chang
 * @Date 2022/10/8 13:23 PM
 * @Version 1.0
 */
package cmu.edu.ruidic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class RemoteVariableClientUDP {
    static InetAddress aHost = null;
    static DatagramSocket aSocket = null;
    static int serverPort = 0;

    public static void main(String args[]){
        int sum=0;
        String id;
        try {
            aHost = InetAddress.getByName("localhost");
            // Create a new server socket
            aSocket = new DatagramSocket();
            System.out.println("The client is running.");
            System.out.println("Please enter server port:");
            String nextLine;
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            // get the port number from input
            serverPort = Integer.parseInt(typed.readLine());
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
                        aSocket.close();
                        System.out.println("Client side quitting. The remote variable server is still running.");
                        return;
                }
                System.out.println();
                System.out.println("1. Add a value to your sum.\n" +
                        "2. Subtract a value from your sum.\n" +
                        "3. Get your sum.\n" +
                        "4. Exit client.");
            }

        }catch (SocketException e) {System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
    }
    public static int add(int i,String id) throws IOException {
        String message_add = "Add "+id+" to "+i;
        byte [] m_add = message_add.getBytes();
        // send a new request message to the server listening at port 6789
        DatagramPacket request = new DatagramPacket(m_add,  m_add.length, aHost, serverPort);
        aSocket.send(request);
        byte[] buffer = new byte[1000];
        // receive a reply message from the server
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        aSocket.receive(reply);
        int sum = ByteBuffer.wrap(reply.getData()).getInt();
        return sum;
    }
    public static int subtract(int i,String id) throws IOException {
        String message_subtract = "Subtract "+id+" to "+i;
        byte [] m_subtract= message_subtract.getBytes();
        // send a new request message to the server listening at port 6789
        DatagramPacket request = new DatagramPacket(m_subtract,  m_subtract.length, aHost, serverPort);
        aSocket.send(request);
        byte[] buffer = new byte[1000];
        // receive a reply message from the server
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        aSocket.receive(reply);
        int sum = ByteBuffer.wrap(reply.getData()).getInt();
        return sum;
    }
    public static int get(String id) throws IOException {
        String message_get = "Get "+id;
        byte [] m_get= message_get.getBytes();
        // send a new request message to the server listening at port 6789
        DatagramPacket request = new DatagramPacket(m_get,  m_get.length, aHost, serverPort);
        aSocket.send(request);
        byte[] buffer = new byte[1000];
        // receive a reply message from the server
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        aSocket.receive(reply);
        int sum = ByteBuffer.wrap(reply.getData()).getInt();
        return sum;
    }
}
