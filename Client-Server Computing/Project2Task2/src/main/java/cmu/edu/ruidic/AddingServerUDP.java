/**
 * @ClassName: AddingServerUDP
 * @Author: Ruidi Chang
 * @Date 2022/10/8 13:23 PM
 * @Version 1.0
 */
package cmu.edu.ruidic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.ByteBuffer;

public class AddingServerUDP {
    static int sum=0;
    static DatagramSocket aSocket = null;
    public static void main(String args[]){
        byte[] buffer = new byte[1000];
        try{
            // Create a new server socket
            aSocket = new DatagramSocket(null);
            System.out.println("Server started");
            int serverPort = 6789;
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            InetAddress aHost = InetAddress.getByName("localhost");
            SocketAddress socketAddress = new InetSocketAddress(aHost, serverPort);
            // connect the sever to the port user entered
            aSocket.bind(socketAddress);
            // If we get here, then we are now connected to a client.
            while(true){
                SumOperation(request);
            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
    }
    public static void SumOperation(DatagramPacket request) throws IOException {
        // Receive the request message from client
        aSocket.receive(request);
        // Get the number need to add from client and place it to int
        int i = ByteBuffer.wrap(request.getData()).getInt();
        System.out.println("Adding: "+ i +" to "+ sum);
        // Add the new number to sum
        sum+=i;
        // Place the int into a four byte (32 bit) byte array before sending
        byte[] Sum_Byte =  ByteBuffer.allocate(4).putInt(sum).array();
        DatagramPacket reply = new DatagramPacket(Sum_Byte,
                Sum_Byte.length, request.getAddress(), request.getPort());
        String requestString = new String(request.getData(),0,request.getLength());
        System.out.println("Returning sum of "+ i +" to client");
        System.out.println();
        // send the reply message to client
        aSocket.send(reply);
    }
}
