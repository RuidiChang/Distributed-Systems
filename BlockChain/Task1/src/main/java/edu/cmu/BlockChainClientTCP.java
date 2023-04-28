package edu.cmu;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
/**
 * BlockChainClientTCP class
 * @author Ruidi Chang
 * @email ruidic@andrew.cmu.edu
 */

public class BlockChainClientTCP {
    static BufferedReader in;
    static PrintWriter out;
    static ResponseMessage response;
    static Gson gson = new Gson();
    static BufferedReader typed;
    static Socket clientSocket = null;

    public static void main(String args[]) {
        try {
            int serverPort = 7777;
            clientSocket = new Socket("localhost", serverPort);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
            typed = new BufferedReader(new InputStreamReader(System.in));
            String nextLine;
            String id;
            System.out.println("0. View basic blockchain status.\n" +
                    "1. Add a transaction to the blockchain.\n" +
                    "2. Verify the blockchain.\n" +
                    "3. View the blockchain.\n" +
                    "4. Corrupt the chain.\n" +
                    "5. Hide the corruption by repairing the chain.\n" +
                    "6. Exit");
            while ((nextLine = typed.readLine()) != null) {
                int option = Integer.parseInt(nextLine);
                Operation(option);
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

    /**
     * Handle request
     * @param option
     * @throws IOException
     */
    public static void Operation(int option) throws IOException {
        String transaction;
        switch (option) {
            case 0:
                out.println(new RequestMessage(0));
                out.flush();
                response = gson.fromJson(in.readLine(), ResponseMessage.class);
                System.out.println("Current size of chain: " + response.size);
                System.out.println("Difficulty of most recent block: " + response.diff);
                System.out.println("Total difficulty for all blocks: " + response.totalDiff);
                System.out.println("Approximate hashes per second on this machine: " + response.hps);
                System.out.println("Expected total hashes required for the whole chain: " + response.totalHashes);
                System.out.println("Nonce for the most recent block: " + response.recentNonce);
                System.out.println("Chain hash: " + response.chainHash);
                break;
            case 1:
                System.out.println("Enter difficulty > 0 ");
                int difficulty = Integer.parseInt(typed.readLine());
                System.out.println("Enter transaction");
                transaction = typed.readLine();
                out.println(new RequestMessage(1, transaction, difficulty));
                out.flush();
                response = gson.fromJson(in.readLine(), ResponseMessage.class);
                System.out.println(response.responses);
                break;
            case 2:
                out.println(new RequestMessage(2));
                out.flush();
                response = gson.fromJson(in.readLine(), ResponseMessage.class);
                System.out.println(response.verification);
                System.out.println(response.responses);
                break;
            case 3:
                System.out.println("View the block chain");
                out.println(new RequestMessage(3));
                out.flush();
                response = gson.fromJson(in.readLine(), ResponseMessage.class);
                System.out.println(response.responses);
                break;
            case 4:
                System.out.println("Corrupt the Blockchain");
                System.out.println("Enter block ID of block to corrupt");
                int id = Integer.parseInt(typed.readLine());
                System.out.println("Enter new data for block " + id);
                transaction = typed.readLine();
                out.println(new RequestMessage(4, id, transaction));
                out.flush();
                response = gson.fromJson(in.readLine(), ResponseMessage.class);
                System.out.println(response.responses);
                break;
            case 5:
                out.println(new RequestMessage(5));
                out.flush();
                response = gson.fromJson(in.readLine(), ResponseMessage.class);
                System.out.println(response.responses);
                break;
            case 6:
                // handle request for exit
                out.println(new RequestMessage(6));
                out.flush();
                out.close();
                clientSocket.close();
                System.exit(0);
        }
        System.out.println("0. View basic blockchain status.\n" +
                "1. Add a transaction to the blockchain.\n" +
                "2. Verify the blockchain.\n" +
                "3. View the blockchain.\n" +
                "4. Corrupt the chain.\n" +
                "5. Hide the corruption by repairing the chain.\n" +
                "6. Exit");
    }
}
