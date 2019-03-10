import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class BlockchainClient {

    private class ServerListener implements Runnable {
        BufferedReader inputReader;

        ServerListener(BufferedReader inputReader) {
            this.inputReader = inputReader;
        }

        @Override
        public void run() {
            try {
                String response;
                while((response = inputReader.readLine()) != null) {
                    System.out.println(response);
                }
            } catch (IOException ignore) { }
        }
    }

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
        // Wraps the input stream and the output stream
        BufferedReader inputReader = new BufferedReader(
                new InputStreamReader(serverInputStream));
        PrintWriter outWriter = new PrintWriter(serverOutputStream, true);

        // Runs a thread to listen to responses from the server
        Thread thread = new Thread(new ServerListener(inputReader));
        thread.start();

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String request = sc.nextLine();
            outWriter.println(request);
//            outWriter.flush();
            if (request.equals("cc")) {
                thread.interrupt();
                try {
                    serverInputStream.close();
                    serverOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}