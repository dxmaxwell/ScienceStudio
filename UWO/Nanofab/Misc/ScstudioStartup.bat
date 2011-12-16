REM Place this file in the "Startup" folder to launch automatically on login.

REM Start Tight VNC remote desktop server.
START "Starting TightVNC" /MIN "C:\Program Files\TightVNC\WinVNC.exe"

REM Start PuTTY SSH client and load 'Nanofab' session.
START "Starting PuTTY" /MIN "C:\Program Files\PuTTY\putty.exe" -load Nanofab
