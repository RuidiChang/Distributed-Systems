package edu.cmu.ruidic;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
/**
 * BlockChain class
 * @author Ruidi Chang
 * @email ruidic@andrew.cmu.edu
 */

public class BlockChain {
    private ArrayList<Block> ds_chain;
    private String chainHash;
    private int hashesPerSecond;

    public BlockChain() throws NoSuchAlgorithmException {
        this.ds_chain = new ArrayList<Block>();
        Block genesis = new Block(0, new Timestamp(System.currentTimeMillis()),"Genesis", 2);
        genesis.proofOfWork();
        addBlock(genesis);
        computeHashesPerSecond();
    };

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        System.out.println("0. View basic blockchain status.\n" +
                "1. Add a transaction to the blockchain.\n" +
                "2. Verify the blockchain.\n" +
                "3. View the blockchain.\n" +
                "4. Corrupt the chain.\n" +
                "5. Hide the corruption by repairing the chain.\n" +
                "6. Exit");
        BlockChain blockChain = new BlockChain();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String nextLine;
        long start, end;
        while ((Integer.parseInt(nextLine = input.readLine()))!=6) {
            switch (Integer.parseInt(nextLine)){
                case 0:
                    System.out.println("Current size of chain: " + blockChain.getChainSize());
                    System.out.println("Difficulty of most recent block: " + blockChain.getLatestBlock().getDifficulty());
                    System.out.println("Total difficulty for all blocks: " + blockChain.getTotalDifficulty());
                    System.out.println("Approximate hashes per second on this machine: " + blockChain.getHashesPerSecond());
                    System.out.println("Expected total hashes required for the whole chain: " + blockChain.getTotalExpectedHashes());
                    System.out.println("Nonce for the most recent block: " + blockChain.getLatestBlock().getNonce());
                    System.out.println("Chain hash: " + blockChain.getChainHash());
                    break;
                case 1:
                    // The execution time increases exponentially
                    // it takes less than 10 milliseconds in difficulty 1 and 2
                    // it takes about 10-99 milliseconds in difficulty 3 and 4
                    // it takes 100-999 digits milliseconds in difficulty 5
                    // it takes a long time in difficulty 6
                    // For difficulty 7, it cannot complete
                    System.out.println("Enter difficulty > 0");
                    int diff = Integer.parseInt(input.readLine());
                    System.out.println("Enter transaction");
                    String transaction = input.readLine();
                    start = System.currentTimeMillis();
                    Block newBlock = new Block(blockChain.getChainSize(), new Timestamp(start), transaction, diff);
                    newBlock.setPreviousHash(blockChain.getChainHash());
                    newBlock.proofOfWork();
                    blockChain.addBlock(newBlock);
                    end = System.currentTimeMillis();
                    System.out.println("Total execution time to add this block was " + (end - start) + " milliseconds");
                    break;
                case 2:
                    // it is O(1), about several milliseconds
                    start = System.currentTimeMillis();
                    String valid = blockChain.isChainValid();
                    end = System.currentTimeMillis();
                    System.out.println("Chain verification: " + valid);
                    System.out.println("Total execution time to verify the chain was " + (end - start) + " milliseconds");
                    break;
                case 3:
                    System.out.println("View the block chain");
                    System.out.println(blockChain);
                    break;
                case 4:
                    System.out.println("Corrupt the Blockchain");
                    System.out.println("Enter block ID of block to corrupt");
                    int index = Integer.parseInt(input.readLine());
                    System.out.println("Enter new data for block "+ index);
                    String data = input.readLine();
                    blockChain.getBlock(index).setData(data);
                    System.out.println("Block "+ index +" now holds " + data);
                    break;
                case 5:
                    // It takes super super super long time when high difficulty
                    start = System.currentTimeMillis();
                    blockChain.repairChain();
                    end = System.currentTimeMillis();
                    System.out.println("Total execution time required to repair the chain was "+ (end - start) +" milliseconds");
                    break;
                default:
                    break;
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

    public void addBlock(Block block) throws NoSuchAlgorithmException {
        ds_chain.add(block);
        chainHash = block.calculateHash();
    }

    /**
     * This method computes exactly 2 million hashes and times how long that process takes.
     * @throws NoSuchAlgorithmException
     */
    public void computeHashesPerSecond() throws NoSuchAlgorithmException {
        String message = "00000000";
        int number = 2000000;
        long start = System.currentTimeMillis();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        for (int i = 0; i < number; i++) {
            md.digest(message.getBytes());
        }
        long end = System.currentTimeMillis();
        hashesPerSecond = (int) (number/(end-start));
    }

    public Block getBlock(int i){return ds_chain.get(i);}

    public String getChainHash() {
        return chainHash;
    }

    public int getChainSize(){
        return ds_chain.size();
    }

    public int getHashesPerSecond(){
        return hashesPerSecond;
    }

    public Block getLatestBlock(){
        return getBlock(getChainSize()-1);
    }

    public Timestamp getTime(){
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Compute and return the total difficulty of all blocks on the chain. Each block knows its own difficulty.
     * @return totalDifficulty
     */
    public int getTotalDifficulty(){
        int total_difficulty = 0;
        for(Block block : ds_chain){
            total_difficulty += block.getDifficulty();
        }
        return total_difficulty;
    }

    /**
     * Compute and return the expected number of hashes required for the entire chain.
     * @return totalExpectedHashes
     */
    public double getTotalExpectedHashes(){
        double total_ExpectedHashes = 0;
        for(Block block : ds_chain){
            total_ExpectedHashes += Math.pow(16, block.getDifficulty());
        }
        return total_ExpectedHashes;
    }

    /**
     * Check the chain is valid or not
     * @return "TRUE" if the chain is valid, otherwise return a string with an appropriate error message
     * @throws NoSuchAlgorithmException
     */
    public String isChainValid() throws NoSuchAlgorithmException {
        // If the chain only contains one block, the genesis block at position 0,
        if (getChainSize()==1){
            // this routine computes the hash of the block
            // and checks that the hash has the requisite number of leftmost 0's (proof of work)
            // as specified in the difficulty field.
            if (getLatestBlock().calculateHash().equals(getLatestBlock().proofOfWork())){
                // It also checks that the chain hash is equal to this computed hash.
                if (getLatestBlock().calculateHash().equals(getChainHash())){
                    // Otherwise, return the string "TRUE".
                    return "TRUE";
                }
                else{
                    // If either check fails, return an error message.
                    return "Chain Hash is NOT Equal to Computed Hash";
                }
            }else{
                // If either check fails, return an error message.
                return "Proof of Work Error";
            }
            // If the chain has more blocks than one,
        }else{
            // begin checking from block one.
            Block genesis = getBlock(0);
            String previous_hash = genesis.calculateHash();
            if (!getLatestBlock().calculateHash().equals(getChainHash())){
                return "Chain Hash is NOT Equal to Computed Hash";
            }
            if (!genesis.calculateHash().equals(genesis.proofOfWork())){
                return "Proof of Work Error";
            }
            // Continue checking until you have validated the entire chain.
            for (int i = 1; i < getChainSize(); i++) {
                Block b = ds_chain.get(i);
                if (!b.getPreviousHash().equals(previous_hash)) {
                    return "Block "+i+" Previous Hash Mismatch!";
                } else if (!b.calculateHash().equals(b.proofOfWork())) {
                    String out="";
                    for (int j = 0; j < b.getDifficulty(); j++) {
                        out=out+"0";
                    }
                    return "Improper hash on node " + i + " Does not begin with " + out;
                }else{
                    previous_hash = b.calculateHash();
                }
            }
        }
        // At the end, check that the chain hash is also correct.
        return "TRUE";
    }

    /**
     * This routine repairs the chain.
     * @throws NoSuchAlgorithmException
     */
    public void repairChain() throws NoSuchAlgorithmException {
        Block genesis = getBlock(0);
        if(getChainSize()==1){
            // Recompute illegal hashes
            if(!genesis.calculateHash().equals(getLatestBlock().proofOfWork())){
                genesis.proofOfWork();
            }else if(!genesis.calculateHash().equals(getChainHash())){
                chainHash = genesis.calculateHash();
            }
        }else{
            String previous_hash = genesis.calculateHash();
            if(!genesis.calculateHash().equals(genesis.proofOfWork())){
                genesis.proofOfWork();
            }
            Block b;
            // Recompute illegal hashes
            for(int i = 1; i < getChainSize(); i++){
                b = ds_chain.get(i);
                if(!b.getPreviousHash().equals(previous_hash)){
                    b.setPreviousHash(previous_hash);
                }
                if(!b.calculateHash().equals(b.proofOfWork())){
                    b.proofOfWork();
                }
                previous_hash = b.calculateHash();
            }
            if (!previous_hash.equals(getChainHash())) {
                chainHash = previous_hash;
            }
        }
    }

    /**
     * Override Java's toString method
     * @return A JSON representation of all of this block's data is returned.
     */
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}

