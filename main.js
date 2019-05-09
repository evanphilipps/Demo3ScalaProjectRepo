
const {app, BrowserWindow} = require('electron');

function createWindow() {
    win = new BrowserWindow({ titleBarStyle: 'hidden' , webPreferences: {zoomFactor: 1.0, nodeIntegration: false}});
    win.loadURL('http://localhost:8080');
    win.maximize()
}


app.on('ready', createWindow);