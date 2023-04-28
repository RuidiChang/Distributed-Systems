package edu.cmu;


import com.google.gson.Gson;

import java.sql.Timestamp;
/**
 * RequestMessage class
 * @author Ruidi Chang
 * @email ruidic@andrew.cmu.edu
 */

public class RequestMessage {
    public int difficulty;
    public int index;
    public Timestamp timestamp;
    public String transaction;
    public int option;

    // option 0, 2, 3, 5, 6
    public RequestMessage(int option){ this.option = option;}
    // option 1
    public RequestMessage(int option, String transaction, int difficulty){
        this.option = option;
        this.transaction = transaction;
        this.difficulty = difficulty;
    }
    // option 4
    public RequestMessage(int option, int index, String transaction){
        this.option = option;
        this.transaction = transaction;
        this.index = index;
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
