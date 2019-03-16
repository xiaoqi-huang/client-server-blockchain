import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

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

    public void clientHandler(InputStream serverInputStream, OutputStream serverOutputStream) {
        // Wrap the input stream and the output stream
        BufferedReader inputReader = new BufferedReader(
                new InputStreamReader(serverInputStream));
        PrintWriter outWriter = new PrintWriter(serverOutputStream, true);

        // Read requests from the user
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

                // Wait until the reader is ready
                while (true) {
                    if (inputReader.ready()) break;
                }

                // Read and print responses until there is nothing to read
                while (inputReader.ready() && (response = inputReader.readLine()) != null) {
                    System.out.println(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}