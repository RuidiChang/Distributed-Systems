package edu.cmu;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;



import com.google.gson.Gson;
import jakarta.xml.bind.DatatypeConverter;

/**
 * Block class
 * @author Ruidi Chang
 * @email ruidic@andrew.cmu.edu
 */


public class Block {
    private int index;
    private Timestamp timestamp;
    private String Tx;
    private String previousHash;
    private BigInteger nonce = BigInteger.ZERO;
    private int difficulty;


    /**
     * This the Block constructor.
     * @param index
     * @param timestamp
     * @param Tx
     * @param difficulty
     */
    public Block(int index, Timestamp timestamp, String Tx, int difficulty){
        this.index = index;
        this.timestamp = timestamp;
        this.Tx = Tx;
        this.difficulty = difficulty;
    }

    /**
     * This method computes a hash of the concatenation of the index, timestamp, data, previousHash, nonce, and difficulty.
     * @return a String holding Hexadecimal characters
     * @throws NoSuchAlgorithmException
     */
    public String calculateHash() throws NoSuchAlgorithmException {
        String message = index + timestamp.toString() + Tx + previousHash + nonce + difficulty;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(message.getBytes());
        return DatatypeConverter.printHexBinary(md.digest());
    }

    public String getData(){return Tx;}

    /**
     * This method returns the nonce for this block.
     * @return a BigInteger representing the nonce for this block.
     */
    public BigInteger getNonce(){return nonce;}

    public int getDifficulty(){return difficulty;}

    public int getIndex(){return index;}

    public String getPreviousHash(){return previousHash;}

    public Timestamp getTimestamp(){return timestamp;}

    /**
     * The proof of work methods finds a good hash. It increments the nonce until it produces a good hash.
     * @return a String with a hash that has the appropriate number of leading hex zeroes.
     * @throws NoSuchAlgorithmException
     */
    public String proofOfWork() throws NoSuchAlgorithmException {
        String hash = calculateHash();
        while (difficulty>hash.length() - hash.replaceAll("^0+", "").length()){
            // update the nonce
            nonce = nonce.add(BigInteger.ONE);
            hash = calculateHash();
        }
        return hash;
    }

    public void setData(String Tx) {
        this.Tx = Tx;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
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

