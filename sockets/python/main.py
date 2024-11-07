from __future__ import annotations
from concurrent.futures import ThreadPoolExecutor
from dataclasses import dataclass
from typing import Union
import socket

@dataclass
class Server:
    port:int = 8228
    host:str = "0.0.0.0"
    pool:Union[None,ThreadPoolExecutor] = None
    socket: Union[None, socket.socket] = None
        
    def run(self):
        with ThreadPoolExecutor() as self.pool:
            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
                s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1) # Set internal socket to allow SO_REUSEADDR
                s.bind((self.host, self.port)) # Bind the configured socket to the server (assign ip address and port number to the socket instance)
                s.listen(1) # Listen for incoming connections

                print(f'Listening on port {self.port} ...')
                
                s.settimeout(None)            
                while True:
                    client_connection, _ = s.accept()
                    # Wait for client connections
                    self.pool.submit(handle_request, client_connection)

                    




def handle_request(connection:socket):
    try:
        # Get the client request
        raw_data = connection.recv(4096).decode()
        
        first_line, remainders = raw_data.split("\r\n")[0], raw_data.split("\r\n")[1:]
        method, slug, protocol = first_line.split(" ")
        headers = dict()
        for line in remainders:
            temp = line.split(":")
            if len(temp) == 2:
                headers[temp[0].strip()] = temp[1].strip()
            elif len(temp) > 2:
                headers[temp[0].strip()] = "".join(temp[1:]).strip()
            else:
                continue
            
        response_css = """* {
        padding: 0;
        margin: 0;
        font-family: system-ui;
        box-sizing: border-box;
    }
    body {
        max-width: 80ch;
        margin: 0 auto;
        background: #13beef;
        color: #f0f0f0;
    }

    p, h1 {
        padding: 1.3rem;
    }"""
        response_html = f"<style>{response_css}</style><h1>Hello world</h1>\n<p>Method: {method}</p><p>Slug: {slug}</p><p>Protocol: {protocol}</p><p>Headers: \n{headers}</p>"
        response = f"HTTP/1.1 200 OK\r\nHost: HHTTPP\r\nConection: Close\r\ncontent-type: text/html\r\n\r\n{response_html}"
        connection.sendall(str(response).encode())
    except Exception as e:
        if e == KeyboardInterrupt:
            exit()
        else:
            connection.sendall(str("HTTP/1.1 500 Server Error\r\nHost: HHTTPP\r\nConection: Close\r\n\r\n").encode())
            print(e)
            print(e.__traceback__.format_exc())
    finally:
        connection.shutdown(socket.SHUT_RDWR)
    
    
if __name__ == "__main__":
    Server().run()