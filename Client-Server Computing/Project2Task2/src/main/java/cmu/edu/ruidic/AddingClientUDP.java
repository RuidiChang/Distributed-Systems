/**
 * @ClassName: AddingClientUDP
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

public class AddingClientUDP {
    static InetAddress aHost = null;
    static DatagramSocket aSocket = null;
    static int serverPort = 0;

    public static void main(String args[]){
        int sum=0;
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
            while ((nextLine = typed.readLine()) != null) {
                if (nextLine.equals("halt!")){
                    aSocket.close();
                    System.out.println("Client side quitting");
                    break;
                }else{
                    int i = Integer.parseInt(nextLine);
                    sum = add(i);
                    System.out.println("The server returned " + sum);
                }
            }

        }catch (SocketException e) {System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
    }
    public static int add(int i) throws IOException {
        // Place the int into a four byte (32 bit) byte array before sending
        byte [] m = ByteBuffer.allocate(4).putInt(i).array();
        // send a new request message to the server listening at port 6789
        DatagramPacket request = new DatagramPacket(m,  m.length, aHost, serverPort);
        aSocket.send(request);
        byte[] buffer = new byte[1000];
        // receive a reply message from the server
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        aSocket.receive(reply);
        int sum = ByteBuffer.wrap(reply.getData()).getInt();
        return sum;
    }
}
