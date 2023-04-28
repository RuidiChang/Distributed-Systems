/**
 * @ClassName: EchoServerUDP
 * @Author: Ruidi Chang
 * @Date 2022/10/8 13:23 PM
 * @Version 1.0
 */
package cmu.edu.ruidic;
import java.net.*;
import java.io.*;
public class EchoServerUDP{
	public static void main(String args[]){ 
	DatagramSocket aSocket = null;
	byte[] buffer = new byte[1000];
	try{
		// Create a new server socket
		aSocket = new DatagramSocket(null);
		System.out.println("The server is running.");
		// Have the server prompt the user for the port number that the server is supposed to listen on.
		System.out.println("please enter the port that server will listen on: ");
		BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
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
			DatagramPacket reply = new DatagramPacket(request.getData(), 
      	   	request.getLength(), request.getAddress(), request.getPort());
			String requestString = new String(request.getData(),0,	request.getLength());
			System.out.println("Echoing: "+requestString);
			// send the reply message to client
			aSocket.send(reply);
			if (requestString.equals("halt!")){
				aSocket.close();
				System.out.println("Server side quitting");
				break;
			}
		}
	}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
	}catch (IOException e) {System.out.println("IO: " + e.getMessage());
	}finally {if(aSocket != null) aSocket.close();}
	}
}
