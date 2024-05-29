const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const https = require('https');
const fs = require('fs');
const path = require('path');

// Load SSL/TLS certificates
const privateKey = fs.readFileSync(path.resolve(__dirname, 'cert/server.key'), 'utf8');
const certificate = fs.readFileSync(path.resolve(__dirname, 'cert/server.cert'), 'utf8');

const credentials = { key: privateKey, cert: certificate };

const app = express();
const PORT = 3000;

// Serve HTML files
app.use(express.static('public'));

// Setup WebSocket Secure (wss) reverse proxy
app.use('/wss', createProxyMiddleware({
    target: 'wss://192.168.0.168:8765',
    changeOrigin: true,
    ws: true,
    secure: false, // If using self-signed certificates, set this to false
    logLevel: 'debug'
}));

// Create HTTPS server
const httpsServer = https.createServer(credentials, app);

httpsServer.listen(PORT, () => {
    console.log(`HTTPS Server is running on https://localhost:${PORT}`);
});