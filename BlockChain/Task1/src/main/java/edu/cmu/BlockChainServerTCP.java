package edu.cmu;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Scanner;

import static edu.cmu.BlockChainClientTCP.response;
/**
 * BlockChainServerTCP class
 * @author Ruidi Chang
 * @email ruidic@andrew.cmu.edu
 */

public class BlockChainServerTCP {
    static Socket clientSocket = null;
    static Scanner in;
    static PrintWriter out;
    static RequestMessage request;
    static Gson gson = new Gson();

    public static void main(String args[]) throws IOException {
        try {
            int serverPort = 7777; // the server port we are using
            // Create a new server socket
            ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("Blockchain server running");
            BlockChain chain = new BlockChain();
            while (true) {
                try {
                    clientSocket = listenSocket.accept();
                    System.out.println("We have a visitor");
                    in = new Scanner(clientSocket.getInputStream());
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
                    while (true) {
                        if (in.hasNext()) {
                            // Receive the request message from client
                            String operation = in.nextLine();
                            request = gson.fromJson(operation, RequestMessage.class);
                            // if user choose 6, break the loop
                            if (request.option==6) break;
                            Operation(request, chain);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

    private static void Operation(RequestMessage request, BlockChain chain) throws NoSuchAlgorithmException {
        long start, end;
        switch (request.option) {
            case (0):
                response = new ResponseMessage(0, chain);
                System.out.println("Response : " + response);
                break;
            case (1):
                System.out.println("Adding a block");
                start = System.currentTimeMillis();
                Block nb = new Block(chain.getChainSize(), new Timestamp(start), request.transaction, request.difficulty);
                nb.setPreviousHash(chain.getChainHash());
                nb.proofOfWork();
                chain.addBlock(nb);
                end = System.currentTimeMillis();
                response = new ResponseMessage(1, "Total execution time to add this block was " + (end - start) + " milliseconds");
                System.out.println("...{\"selection\":"+response.selection+",\"response\":\""+response.responses+"\"}");
                break;
            case (2):
                System.out.println("Verifying entire chain");
                start = System.currentTimeMillis();
                String valid_chain = chain.isChainValid();
                end = System.currentTimeMillis();
                System.out.println("Chain verification: " + valid_chain);
                System.out.println("Total execution time to verify the chain was " + (end - start) + " milliseconds");
                System.out.println("Setting response to Total execution time to verify the chain was " + (end - start) + " milliseconds");
                response = new ResponseMessage(2, "Total execution time to verify the chain was " + (end - start) + " milliseconds", "Chain verification: " + valid_chain);
                break;
            case (3):
                System.out.println("View the Blockchain");
                response = new ResponseMessage(3, chain);
                System.out.println("Setting response to "+response.responses);
                break;
            case (4):
                System.out.println("Corrupt the Blockchain");
                chain.getBlock(request.index).setData(request.transaction);
                System.out.println("Block "+ request.index +" now holds " + request.transaction);
                response = new ResponseMessage(4,"Block "+ request.index +" now holds " + request.transaction );
                System.out.println("Setting response to "+response.responses);
                break;
            case (5):
                System.out.println("Repairing the entire chain");
                start = System.currentTimeMillis();
                chain.repairChain();
                end = System.currentTimeMillis();
                response = new ResponseMessage(5, "Total execution time required to repair the chain was "+(end-start)+" milliseconds");
                System.out.println("Setting response to Total execution time required to repair the chain was "+(end-start)+" milliseconds");
                break;
            case (6):
                break;
        }
        out.println(response);
        out.flush();
    }

}
