package Block;

import Transaction.Transaction;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Block implements Serializable {

    private int index;
    private Date timestamp;
    private List<Transaction> transactions;
    private int proof;
    private String previous_hash;

    public Block(int index, Date timestamp, List<Transaction> transactions, int proof, String previous_hash) {
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.proof = proof;
        this.previous_hash = previous_hash;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getProof() {
        return proof;
    }

    public void setProof(int proof) {
        this.proof = proof;
    }

    public String getPrevious_hash() {
        return previous_hash;
    }

    public void setPrevious_hash(String previous_hash) {
        this.previous_hash = previous_hash;
    }
}
