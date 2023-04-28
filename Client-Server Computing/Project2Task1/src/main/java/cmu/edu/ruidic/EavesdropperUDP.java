/**
 * @ClassName: EavesdropperUDP
 * @Author: Ruidi Chang
 * @Date 2022/10/8 13:23 PM
 * @Version 1.0
 */
package cmu.edu.ruidic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class EavesdropperUDP {
    public static void main(String args[]){
        DatagramSocket aSocket = null;
        DatagramSocket bSocket = null;
        byte[] buffer = new byte[1000];
        try{
            // Create a new server socket
            aSocket = new DatagramSocket(null);
            System.out.println("The server is running.");
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            // get eavesdrop port number
            System.out.println("Please enter Eavesdrop port number:");
            int EavesdropPort = Integer.parseInt(typed.readLine());
            System.out.println("please enter the port that Eavesdrop will listen on: ");
            int serverPort = Integer.parseInt(typed.readLine());
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            InetAddress aHost = InetAddress.getByName("localhost");
            SocketAddress socketAddress = new InetSocketAddress(aHost, serverPort);
            // connect the sever to the port user entered
            aSocket.bind(socketAddress);
            // If we get here, then we are now connected to a client.
            while(true){
                // Receive the request message from client
                aSocket.receive(request);
                String requestString = new String(request.getData(),0,	request.getLength());
                System.out.println("Client send: "+requestString);
                // When the Eavesdropper sees the request to halt it will write a line of asterisks to its console
                if (requestString.equals("halt!")){
                    System.out.println("*******************************************************************");
                }
                // record the client address and port
                InetAddress clientAddress = request.getAddress();
                int clientPort = request.getPort();
                // forward the request message from client to server
                DatagramPacket forward = new DatagramPacket(request.getData(), request.getLength(), aHost, EavesdropPort);
                aSocket.send(forward);
                // receive the reply from server
                aSocket.receive(request);
                requestString = new String(request.getData(), 0, request.getLength());
                System.out.println("Server send: "+requestString);
                // forward the reply from server to client
                forward = new DatagramPacket(request.getData(), request.getLength(), clientAddress, clientPort);
                aSocket.send(forward);
            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
    }
}
