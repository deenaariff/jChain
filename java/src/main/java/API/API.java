package API;

import Block.Block;
import Blockchain.Blockchain;
import IO.JsonUtil;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.*;

public class API {

    private Blockchain blockchain;
    private String node_identifier;

    public API(Blockchain blockchain) {
        this.blockchain = blockchain;
        this.node_identifier = UUID.randomUUID().toString();
    }

    public void listen(int port_num) {
        // Start embedded server at this port
        port(port_num);

        /**
         *
         */
        post("/transactions/new", (Request request, Response response) -> {
            HashMap<String, Object> rsp = new HashMap<>();

            String sender, recipient;
            Integer amount;

            try {
                sender = request.queryParams("sender");
                recipient = request.queryParams("recipient");
                amount = Integer.parseInt(request.queryParams("amount"));

                this.blockchain.new_transaction(sender, recipient, amount);
                response.status(200);
                rsp.put("message","Transaction will be added to Block");
            } catch (Exception e) {
                response.status(401);
                rsp.put("message","Invalid Parameters Passed");
            }

            return JsonUtil.toJson(rsp);


        });

        /**
         *
         */
        post("/nodes/register", (Request request, Response response) -> {
            HashMap<String, Object> rsp = new HashMap<>();

            try {

                String stringified = request.queryParams("nodes");
                Map jsonJavaRootObject = new Gson().fromJson(stringified, Map.class);
                List<String> nodes = (List<String>) jsonJavaRootObject.get("nodes");

                for(String node : nodes) {
                    blockchain.register_node(node);
                }

                if (nodes.size() == 0) {
                    response.status(201);
                    rsp.put("message","Transaction will be added to Block");
                } else {
                    response.status(201);
                    rsp.put("message","Transaction will be added to Block");
                }

            } catch (Exception e) {
                response.status(401);
                rsp.put("message","Invalid Parameters Passed");
            }

            return JsonUtil.toJson(rsp);

        });

        /**
         *
         */
        get("/nodes/resolve", (Request request, Response response) -> {
            boolean is_updated = blockchain.resolve_conflict();
            HashMap<String, Object> rsp = new HashMap<>();

            if (is_updated) {
                rsp.put("message","our blockchain is replaced");
                rsp.put("new_chain", blockchain.getChain());
            } else {
                rsp.put("message","our blockchain is authorative");
                rsp.put("chain", blockchain.getChain());
            }

            return JsonUtil.toJson(rsp);

        });

        /**
         *
         */
        get("/mine", (Request request, Response response) -> {
            System.out.println("Mining a new Block");

            HashMap<String, Object> rsp = new HashMap<String,Object>();

            Block last_block = blockchain.last_block();
            int last_proof = last_block.getProof();
            int proof = blockchain.proof_of_work(last_proof);

            blockchain.new_transaction("0",this.node_identifier,1);

            String previous_hash = Blockchain.hash(last_block);
            Block block = blockchain.new_block(proof, previous_hash);

            rsp.put("message","New Block Forged");
            rsp.put("index",block.getIndex());
            rsp.put("transactions",block.getProof());
            rsp.put("proof", block.getProof());
            rsp.put("previous_hash", block.getPrevious_hash());

            response.status(200);
            return JsonUtil.toJson(rsp);
        });

        /**
         *
         */
        get("/chain", (Request request, Response response) -> {
            HashMap<String, Object> rsp = new HashMap<String,Object>();
            rsp.put("chain",blockchain.getChain());
            rsp.put("length",blockchain.getChain().size());

            response.status(200);
            return JsonUtil.toJson(rsp);
        });

    }


}
