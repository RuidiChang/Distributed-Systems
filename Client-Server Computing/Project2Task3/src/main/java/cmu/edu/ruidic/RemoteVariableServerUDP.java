/**
 * @ClassName: RemoteVariableServerUDP
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
import java.util.HashMap;

public class RemoteVariableServerUDP {
    static DatagramSocket aSocket = null;
    static HashMap<String, Integer> sum = new HashMap<String, Integer>();
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
                Operation(request);
            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
    }
    public static void Operation(DatagramPacket request) throws IOException {
        // Receive the request message from client
        aSocket.receive(request);
        // Get the user's command
        String operation = new String(request.getData(),0,request.getLength());
        // if user choose 1: add
        if (operation.startsWith("Add")){
            String id = operation.split(" ")[1];
            int num_add = Integer.parseInt(operation.split(" ")[3]);
            System.out.println("ID: "+id+" Add "+num_add);
            // check if id is in the map
            // if id in the map: add the new number to sum
            // if id not in the map: put the id in the map as a key and let the value be the new number
            if (sum.containsKey(id)){
                sum.put(id, sum.get(id)+num_add);
            } else {
                sum.put(id, num_add);
            }
        } else if (operation.startsWith("Subtract")) {
            String id = operation.split(" ")[1];
            int num_sub = Integer.parseInt(operation.split(" ")[3]);
            System.out.println("ID: "+id+" Subtract "+num_sub);
            // check if id is in the map
            // if id in the map: subtract the new number to sum
            // if id not in the map: put the id in the map as a key and let the value be 0 minus the new number
            if (sum.containsKey(id)){
                sum.put(id, sum.get(id)-num_sub);
            } else {
                sum.put(id, 0-num_sub);
            }
        }else if(operation.startsWith("Get")){
            if (sum.containsKey(operation.split(" ")[1])){
            }else{
                String id = operation.split(" ")[1];
                sum.put(id, 0);
        }
    }
        byte[] Sum_Byte =  ByteBuffer.allocate(4).putInt(sum.get(operation.split(" ")[1])).array();
        System.out.println("Return: ID: "+ operation.split(" ")[1]+" value: "+sum.get(operation.split(" ")[1]));
        DatagramPacket reply = new DatagramPacket(Sum_Byte,
                Sum_Byte.length, request.getAddress(), request.getPort());
        // send the reply message to client
        aSocket.send(reply);
    }
}
