/**
 * @ClassName: EchoClientUDP
 * @Author: Ruidi Chang
 * @Date 2022/10/8 13:23 PM
 * @Version 1.0
 */
package cmu.edu.ruidic;
import java.net.*;
import java.io.*;
public class EchoClientUDP{
    public static void main(String args[]){ 
	// args give message contents and server hostname
	DatagramSocket aSocket = null;
	try {
		InetAddress aHost = InetAddress.getByName("localhost");
		// Create a new server socket
		aSocket = new DatagramSocket();
		System.out.println("The client is running.");
		// Have the client prompt the user for the server side port number.
		System.out.println("Please enter the server port number:");
		String nextLine;
		BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
		int serverPort = Integer.parseInt(typed.readLine());
		while ((nextLine = typed.readLine()) != null) {
		  byte [] m = nextLine.getBytes();
		  // send a new request message to the server listening at port 6789
		  DatagramPacket request = new DatagramPacket(m,  m.length, aHost, serverPort);
  		aSocket.send(request);
  		byte[] buffer = new byte[1000];
		// receive a reply message from the server
  		DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
  		aSocket.receive(reply);
		String reply_string = new String(reply.getData(),0,reply.getLength());
  		System.out.println("Reply: " + reply_string);
		if (reply_string.equals("halt!")){
			aSocket.close();
			System.out.println("Client side quitting");
			break;
		}
    }

	}catch (SocketException e) {System.out.println("Socket: " + e.getMessage());
	}catch (IOException e){System.out.println("IO: " + e.getMessage());
	}finally {if(aSocket != null) aSocket.close();}
    }
}
