import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockchainServer {

    private Blockchain blockchain;

    public BlockchainServer() { blockchain = new Blockchain(); }

    // Setter
    public void setBlockchain(Blockchain blockchain) { this.blockchain = blockchain; }

    // Getter
    public Blockchain getBlockchain() { return blockchain; }

    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }
        int portNumber = Integer.parseInt(args[0]);
        BlockchainServer bcs = new BlockchainServer();

        // Builds a server listening to the specified port
        ServerSocket server;
        try {
            server = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            try {
                // Accepts the connection from the client
                Socket client = server.accept();

                // Handles the input and output streams of the client
                bcs.serverHandler(client.getInputStream(), client.getOutputStream());
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method reads requests from the client continuously and handles them.
     * It asks the handleRequests function to give the proper responses.
     * If the response is "cc", then the server will close both streams and end the method.
     * Otherwise, the server will print the response to the output stream.
     * @param clientInputStream is the stream where the server can read requests.
     * @param clientOutputStream is the stream where the server can print responses.
     */
    public void serverHandler(InputStream clientInputStream, OutputStream clientOutputStream) {
        // Wraps the input stream and the output stream
        BufferedReader inputReader = new BufferedReader(
                new InputStreamReader(clientInputStream));
        PrintWriter outWriter = new PrintWriter(clientOutputStream, true);

        // Reads requests and gives responses
        try {
            String request = inputReader.readLine();
            while (request != null) {
                String response = handleRequest(request);
                if (response.equals("close")) {
                    clientInputStream.close();
                    clientOutputStream.close();
                    break;
                } else {
                    outWriter.print(response);
                    outWriter.flush();
                }
                // Reads a new line from the input stream
                request = inputReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function can give a response according to the request.
     * If the request is "tx|...|...", a new transaction will be added to the blockchain and the responses will indicate whether the transaction is valid.
     * If the request is "pb", the response will be the string representation of the blockchain.
     * If the request is "cc", the reponses will be "close" telling the server to close the connection.
     * @param request is the request from the client. This function gives a reply according to this.
     * @return a proper response.
     */
    private String handleRequest(String request) {
        if (request == null || request.length() < 2) {
            return "Error\n\n";
        }

        String type = request.substring(0, 2);
        if (type.equals("tx")) {
            int result = blockchain.addTransaction(request);
            return result != 0 ? "Accepted\n\n" : "Rejected\n\n";
        }
        if (type.equals("pb")) {
            return blockchain.toString() + "\n";
        }
        if (type.equals("cc")) {
            return "close";
        }

        // Unrecognized request
        return "Error\n\n";
    }
}