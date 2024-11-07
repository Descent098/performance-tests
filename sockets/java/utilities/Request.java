package utilities;

import java.util.HashMap;

public class Request {
    public String method;
        public String slug;
        public String protocol;
        public HashMap<String, String> headers;
    
        public Request(String rawRequest) {
            boolean firstline = true;
            this.headers = new HashMap<String, String>();
            String[] tmp = null;
            for (String line : rawRequest.split("\r\n")) {
                if (firstline){
                    tmp = line.split(" ");
                    this.method = tmp[0];
                    this.slug = tmp[1];
                    this.protocol = tmp[2];
                    firstline = false;
                    continue;
                }
    
                tmp = line.split(":");
    
                this.headers.put(tmp[0].trim(), tmp[1].trim());
    
            }
        }
    
        public Request(String method, String slug, String protocol, HashMap<String, String> headers) {
            this.method = method;
            this.slug = slug;
            this.protocol = protocol;
            this.headers = headers;
        }

        public String toString(){
            return String.format("Method: %s\nSlug: %s\nProtocol: %s\nHeaders: %s", this.method, this.slug, this.protocol, stringifyHeaders(this.headers));
        }

        public static String stringifyHeaders(HashMap<String, String> headers){
            String result = "";
            for (String header : headers.keySet()) {
                result += String.format("%s: %s\r\n", header, headers.get(header));
            }
            return result;
        }
}
