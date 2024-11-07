package main

import (
	"fmt"
	"log"
	"net"
	"strings"
)

func stringifyHeaders(headers map[string]string) string {
	// Takes in a header mapping and returns an HTTP compliant stringified result
	if len(headers) > 0 {
		var buffer strings.Builder
		for key, value := range headers {
			buffer.WriteString(fmt.Sprintf("%s: %s\r\n", key, value))
		}
		return buffer.String()
	} else {
		return "\r\n"
	}

}

type Request struct {
	// Represents an HTTP Request
	method   string
	slug     string
	protocol string
	headers  map[string]string
	content  string
}

func (r Request) String() string {
	// Formatter for doing string conversions of HTTP Requests
	responseFirstLine := fmt.Sprintf("%s %s %s\r\n", r.method, r.slug, r.protocol)
	return fmt.Sprintf("%s%s\r\n%s", responseFirstLine, stringifyHeaders(r.headers), r.content)
}

type Response struct {
	// Represents an HTTP Response
	code     int
	message  string
	protocol string
	headers  map[string]string
	content  string
}

func (r Response) String() string {
	// Formatter for doing string conversions of HTTP Responses
	responseFirstLine := fmt.Sprintf("%s %d %s\r\n", r.protocol, r.code, r.message)
	return fmt.Sprintf("%s%s\r\n\r\n%s", responseFirstLine, stringifyHeaders(r.headers), r.content)
}

func parseRequest(requestBuffer []byte) Request {
	requestContent := strings.Split(string(requestBuffer), "\n")
	headers := make(map[string]string)

	for _, line := range requestContent[1:] {
		// Get headers
		tmp := strings.Split(line, ":")
		if len(tmp) < 2 {
			break
		}
		key, value := tmp[0], tmp[1]
		headers[key] = value
	}

	firstLine := strings.Split(requestContent[0], " ")
	// TODO: Content parsing, and validation
	return Request{firstLine[0], firstLine[1], firstLine[2], headers, ""}
}

func generateResponse(request Request) Response {

	responseCSS := `
	* {
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
	}
	`

	responseHTML := fmt.Sprintf("<style>%s</style><h1>Hello world</h1>\n<p>Method: %s</p><p>Slug: %s</p><p>Protocol: %s</p><p>Headers: \n%s</p>", responseCSS, request.method, request.slug, request.protocol, stringifyHeaders(request.headers))
	// TODO: Add response headers
	headers := make(map[string]string)
	headers["Host"] = "HHTTPP"
	headers["Connection"] = "Close"

	return Response{200, "Ok", "HTTP/1.1", headers, responseHTML}
}

func handleRequest(connection net.Conn) {
	defer connection.Close()

	// Read incomming request
	requestBuffer := make([]byte, 4096)

	_, err := connection.Read(requestBuffer)

	if err != nil {
		log.Fatalf("Failed with error: %v", err)
	}

	request := parseRequest(requestBuffer)

	// fmt.Printf("Request recieved: \n%v", request)

	// Generate Response

	response := generateResponse(request)
	// fmt.Printf("Response Generated: \n%v", response)

	connection.Write([]byte(response.String()))
}

func main() {
	// Primary entrypoint
	listener, err := net.Listen("tcp", ":8118")
	fmt.Printf("Listening on %v\n\n", listener.Addr())

	if err != nil {
		log.Fatalf("Failed with error: %v", err)
	}

	defer listener.Close() // Ensure socket closes properly

	for {
		connection, err := listener.Accept()
		if err != nil {
			log.Printf("Error: %v", err)
		}
		go handleRequest(connection)
	}

}
