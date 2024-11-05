import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class basic {
    public static void main(String[] args) throws IOException {
        // Create an HTTP server listening on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 8080), 0);
        
        // Set up a handler for the root path
        server.createContext("/", new HelloHandler());
        
        // Use a default executor to handle requests concurrently
        server.setExecutor(null);
        
        System.out.println("Server is running on http://0.0.0.0:8080");
        // Start the server
        server.start();
    }

    // Define a handler to manage incoming requests
    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<head><style>* {   padding: 0;    margin: 0;    font-family: system-ui;    box-sizing: border-box;}body {    max-width: 80ch;    margin: 0 auto;    background: #13beef;    color: #f0f0f0;}p, h1 {    padding: 1.3rem;}</style></head>"
+"<body class=\"vsc-initialized\"><h1>Hello world</h1>"
+"<p>Method: GET</p><p>Slug: /</p><p>Protocol: HTTP/1.1</p><p>Headers: "
+"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"
+"Connection: keep-alive"
+"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36 Edg/130.0.0.0"
+"Sec-Fetch-Site: none"
+"Sec-Fetch-Dest: document"
+"Host: localhost"
+"Pragma: no-cache"
+"Accept-Encoding: gzip, deflate, br, zstd"
+"Sec-Fetch-Mode: navigate"
+"sec-ch-ua: \"Chromium\";v=\"130\", \"Microsoft Edge\";v=\"130\", \"Not?A_Brand\";v=\"99\""
+"sec-ch-ua-mobile: ?0"
+"Cache-Control: no-cache"
+"Upgrade-Insecure-Requests: 1"
+"sec-ch-ua-platform: \"Windows\""
+"Sec-Fetch-User: ?1"
+"Accept-Language: en-US,en;q=0.9,en-CA;q=0.8"
+"</p></body>";
            
            // Set the headers and send the response
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.length());
            
            // Write the response to the output stream
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
