from fastapi import FastAPI
from fastapi.responses import HTMLResponse
import uvicorn

app = FastAPI()

@app.get("/", response_class=HTMLResponse)
async def hello_world():
    return """<head><style>* {    padding: 0;    margin: 0;    font-family: system-ui;    box-sizing: border-box;}body {    max-width: 80ch;    margin: 0 auto;    background: #13beef;    color: #f0f0f0;}p, h1 {    padding: 1.3rem;}</style></head>
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
</p></body>"""

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8228)
