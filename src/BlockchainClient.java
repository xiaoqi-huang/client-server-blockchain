import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * This blockchain client can create a socket to connect to the specific server and port number.
 * It listens to user inputs, forwards user requests to the server,
 * and then print all server replies on the screen.
 */
public class BlockchainClient {

    public static void main(String[] args) {
        if (args.length != 2) {
            return;
        }
        String serverName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        BlockchainClient bcc = new BlockchainClient();

        try {
            Socket socket = new Socket(serverName, portNumber);
            bcc.clientHandler(socket.getInputStream(), socket.getOutputStream());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method reads requests from the user and send them to the server.
     * Also, it receives responses from the server and print them.
     * This method stops if the gieven request is "cc".
     * @param serverInputStream is the stream where the client can read responses from the server.
     * @param serverOutputStream is the stream where the client can write user requests to the server.
     */
    public void clientHandler(InputStream serverInputStream, OutputStream serverOutputStream) {
        // Wrap the input stream and the output stream
        BufferedReader inputReader = new BufferedReader(
                new InputStreamReader(serverInputStream));
        PrintWriter outWriter = new PrintWriter(serverOutputStream, true);

        // Listen requests from the user
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String request = sc.nextLine();

            // Send the request to the server
            outWriter.print(request + "\n");
            outWriter.flush();

            // Close streams and exit, if the request is "cc"
            if (request.equals("cc")) {
                try {
                    inputReader.close();
                    outWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }

            // Receive responses from the server
            try {
                String response;

                // Read and print responses until there is nothing to read
                while ((response = inputReader.readLine()) != null) {
                    System.out.println(response);
                    // Stop reading responses once there is nothing to read
                    if (!inputReader.ready()) break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}