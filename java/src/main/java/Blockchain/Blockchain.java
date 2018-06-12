package Blockchain;

import API.API;
import Block.Block;
import IO.HTTP;
import Transaction.Transaction;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Blockchain {

    private List<Block> chain;
    private List<Transaction> current_transactions;
    private HashSet<String> nodes;

    /**
     *
     */
    public Blockchain() {
        this.chain = new LinkedList<>();
        this.current_transactions = new LinkedList<>();
        this.new_block(100,"1");
        this.nodes = new HashSet<>();
    }

    /**
     *
     * @return
     */
    public List<Block> getChain() {
        return this.chain;
    }


    /**
     *
     * @param proof
     * @param prev_hash
     */
    public Block new_block(int proof, String prev_hash) {
        prev_hash = (prev_hash.equals(""))? hash(last_block()) : prev_hash;
        Block new_block = new Block(chain.size()-1,new Date(), current_transactions, proof, prev_hash);

        current_transactions.clear();
        this.chain.add(new_block);
        return new_block;
    }

    /**
     *
     * @param sender
     * @param recipient
     * @param amount
     * @return
     */
    public int new_transaction(String sender, String recipient, int amount) {
        Transaction new_transaction = new Transaction(sender, recipient, amount);
        current_transactions.add(new_transaction);

        return this.last_block().getIndex() + 1;
    }

    /**
     *
     * @param block
     * @return
     */
    public static String hash(Block block) {
        String stringified = block.toString();
        return Hashing.sha256()
                .hashString(stringified, StandardCharsets.UTF_8)
                .toString();
    }

    /**
     *
     * @return
     */
    public Block last_block() {
        return this.chain.get(this.chain.size()-1);
    }

    /**
     *
     * @param last_proof
     * @return
     */
    public int proof_of_work(int last_proof) {
        int proof = 0;
        while (this.valid_proof(last_proof, proof) == false) {
            System.out.println("Proof : " + proof + " not valid");
            proof += 1;
        }
        System.out.println("Proof : " + proof + " valid!");
        return proof;
    }

    /**
     *
     * @param last_proof
     * @param proof
     * @return
     */
    public boolean valid_proof(Integer last_proof, Integer proof) {
        String guess = last_proof.toString() + proof.toString();
        String guess_hash = Hashing.sha256()
                .hashString(guess, StandardCharsets.UTF_8)
                .toString();
        return guess_hash.substring(0,2).equals("00");
    }

    /**
     *
     * @param address
     */
    public void register_node(String address) {
        nodes.add(address);
    }

    /**
     *
     * @param chain
     * @return
     */
    public boolean valid_chain(List<Block> chain) {
        Block last_block = chain.get(0);
        int current_index = 1;

        while (current_index < chain.size()) {
            Block block = chain.get(current_index);
            if (block.getPrevious_hash() != hash(last_block)) {  // compare hash of block
                return false;
            }
            last_block = block;
            current_index = 1;
        }

        return true;
    }

    /**
     *
     * @return
     */
    public boolean resolve_conflict() {

        List<Block> new_chain = null;

        int max_length = this.chain.size();

        for (String node_address : this.nodes) {

            String response = HTTP.get_url(node_address+"/chain");
            Map jsonJavaRootObject = new Gson().fromJson(response, Map.class);

            int length = (Integer) jsonJavaRootObject.get("length");
            List<Block> chain = (List<Block>) jsonJavaRootObject.get("chain");

            if (length > max_length && this.valid_chain(chain)) {
                max_length = length;
                new_chain = chain;
            }
        }

        if (new_chain != null) {
            this.chain = new_chain;
            return true;
        }

        return false;
    }


    /**
     * Runs the BlockChain on port in arg1
     *
     * @param args
     */
    public static void main(String[] args) {

        Integer port = 5000;

        System.out.println("Running BlockChain on Port: " + port);

        Blockchain blockchain = new Blockchain();

        API api = new API(blockchain);
        api.listen(port);

    }


}
