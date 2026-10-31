#!/bin/bash

# 1. Start Xvfb virtual display on :0 and wait for the lock file to ensure it's ready
echo "Starting Xvfb..."
Xvfb :0 -screen 0 1024x768x24 &

# Wait until the X11 socket appears
while [ ! -e /tmp/.X11-unix/X0 ]; do
  sleep 0.1
done

# 2. Export the display variable
export DISPLAY=:0

# 3. Start a lightweight window manager
echo "Starting fluxbox..."
fluxbox &

# 4. Start x11vnc attached explicitly to display 0 and listening locally
echo "Starting x11vnc..."
x11vnc -display :0 -nopw -listen localhost -xkb -shared -forever &

# 5. Start websockify / noVNC on port 8080 proxying to 5900
echo "Starting websockify..."
websockify --web /usr/share/novnc/ 8080 localhost:5900 &

# 6. Strict check to guarantee port 5900 is open before launching Java
echo "Waiting for port 5900..."
count=0
while ! (echo > /dev/tcp/localhost/5900) >/dev/null 2>&1; do
  sleep 0.5
  count=$((count+1))
  if [ $count -gt 20 ]; then
    echo "ERROR: VNC server failed to start on port 5900!"
    exit 1
  fi
done
echo "VNC server is up and listening on port 5900!"

# 7. Finally, start your JavaFX application in the foreground
echo "Starting JavaFX application..."
exec java --module-path /app/javafx-sdk-21.0.2/lib \
         --add-modules javafx.controls,javafx.fxml \
         -cp /app/app.jar finalproject.Main