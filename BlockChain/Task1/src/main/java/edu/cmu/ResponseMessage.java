package edu.cmu;

import com.google.gson.Gson;

import java.math.BigInteger;

/**
 * ResponseMessage class
 * @author Ruidi Chang
 * @email ruidic@andrew.cmu.edu
 */
public class ResponseMessage {
    int selection;
    int size;
    String chainHash;
    double totalHashes;
    int totalDiff;
    BigInteger recentNonce;
    int diff;
    int hps;
    String responses;
    String verification;

    public ResponseMessage(int selection, int size, String chainHash, double totalHashes, BigInteger recentNonce, int diff, int hps){
        this.selection = selection;
        this.size = size;
        this.chainHash = chainHash;
        this.totalHashes = totalHashes;
        this.recentNonce = recentNonce;
        this.diff = diff;
        this.hps = hps;
    }
    // option 0
    public ResponseMessage(int selection, BlockChain chain){
        if (selection==0){
            this.selection = selection;
            this.size = chain.getChainSize();
            this.chainHash = chain.getChainHash();
            this.totalHashes = chain.getTotalExpectedHashes();
            this.recentNonce = chain.getLatestBlock().getNonce();
            this.diff = chain.getLatestBlock().getDifficulty();
            this.hps = chain.getHashesPerSecond();
        }else{
            this.responses = chain.toString();
        }
    }
    // option 1, 4, 5
    public ResponseMessage(int selection, String responses){
        this.selection = selection;
        this.responses = responses;
    }
    // option 2
    public ResponseMessage(int selection, String responses, String verification){
        this.selection = selection;
        this.responses = responses;
        this.verification = verification;
    }
    /**
     * Format the RequestMessage to Json
     * @return Json
     */
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
