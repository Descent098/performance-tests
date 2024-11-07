package utilities;
import java.util.HashMap;

public class Response {
        private int number;
        private String message;
        private final String protocol = "HTTP/1.1";
        private String content;
        private HashMap<String, String> headers;
    
        public Response(int number, Request request){
            this.number =  number;
            switch (number) {
                case 404:
                    this.message = "Not Found";
                    break;
            
                default:
                    this.message = "OK";
                    break;
            }
            this.headers = new HashMap<String, String>();
            this.headers.put("Host", "HHTTPP");
            this.headers.put("Connection", "Close");
            String responseCSS = "* {"+
                                "    padding: 0;"+
                                "    margin: 0;"+
                                "    font-family: system-ui;"+
                                "    box-sizing: border-box;"+
                                "}"+
                                "body {"+
                                "    max-width: 80ch;"+
                                "    margin: 0 auto;"+
                                "    background: #13beef;"+
                                "    color: #f0f0f0;"+
                                "}"+
                                ""+
                                "p, h1 {"+
                                "    padding: 1.3rem;"+
                                "}";
            String responseHTML = String.format("<style>%s</style><h1>Hello world</h1>\n<p>Method: %s</p><p>Slug: %s</p><p>Protocol: %s</p><p>Headers: \n%s</p>", responseCSS, request.method, request.slug, request.protocol, stringifyHeaders(request.headers));
            this.content = responseHTML;
        }
        
    
        public String toString(){
            String responseFirstLine = String.format("%s %d %s\r\n", this.protocol, this.number, this.message);
            return String.format("%s%s\r\n\r\n%s", responseFirstLine, stringifyHeaders(this.headers), this.content);
        }

        public static String stringifyHeaders(HashMap<String, String> headers){
            String result = "";
            for (String header : headers.keySet()) {
                result += String.format("%s: %s\r\n", header, headers.get(header));
            }
            return result;
        }

    }
    
