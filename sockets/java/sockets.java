
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import utilities.*;

public class sockets {
    private static final int PORT = 6969;

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // System.out.println("New client connected");
                
                threadPool.execute(new Worker(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }

    private static class Worker implements Runnable {
        private Socket clientSocket;

        public Worker(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                
                // Read the request line
                String rawRequest = "";
                String currentLine;
                while (!(currentLine = in.readLine()).isEmpty()){
                    rawRequest += currentLine + "\r\n";
                }

                Request request = new Request(rawRequest);
                
                // System.out.println("Received request: " + request.toString());
                
                Response response = new Response(200, request);

                // Only handle GET requests
                if (request.method.trim().toLowerCase().equals("get")) {
                    out.print(response.toString());
                }
                
                out.flush();
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }




    }
}
