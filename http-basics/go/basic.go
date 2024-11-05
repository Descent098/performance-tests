// Very basic http server for performance comparison
package main

import (
	"fmt"
	"net/http"
)

func helloHandler(w http.ResponseWriter, r *http.Request) {
	// Set the content type to HTML
	w.Header().Set("Content-Type", "text/html")
	// Write the response

	resp := `
<head><style>* {    padding: 0;    margin: 0;    font-family: system-ui;    box-sizing: border-box;}body {    max-width: 80ch;    margin: 0 auto;    background: #13beef;    color: #f0f0f0;}p, h1 {    padding: 1.3rem;}</style></head>
<body class="vsc-initialized"><h1>Hello world</h1>
<p>Method: GET</p><p>Slug: /</p><p>Protocol: HTTP/1.1</p><p>Headers: 
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Connection: keep-alive
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36 Edg/130.0.0.0
Sec-Fetch-Site: none
Sec-Fetch-Dest: document
Host: localhost
Pragma: no-cache
Accept-Encoding: gzip, deflate, br, zstd
Sec-Fetch-Mode: navigate
sec-ch-ua: "Chromium";v="130", "Microsoft Edge";v="130", "Not?A_Brand";v="99"
sec-ch-ua-mobile: ?0
Cache-Control: no-cache
Upgrade-Insecure-Requests: 1
sec-ch-ua-platform: "Windows"
Sec-Fetch-User: ?1
Accept-Language: en-US,en;q=0.9,en-CA;q=0.8
</p></body>
	`

	fmt.Fprint(w, resp)
}

func main() {
	// Handle all requests with the helloHandler
	http.HandleFunc("/", helloHandler)
	PORT := 8118
	// Start the server on port PORT
	fmt.Println(fmt.Sprintf("Server is running on http://0.0.0.0:%d", PORT))
	err := http.ListenAndServe(fmt.Sprintf("0.0.0.0:%d", PORT), nil)
	if err != nil {
		fmt.Println("Error starting server:", err)
	}
}
