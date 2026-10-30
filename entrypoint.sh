#!/bin/bash

# 1. Start Xvfb virtual display on :0
Xvfb :0 -screen 0 1024x768x24 &

# 2. Export the display variable so GUI apps can find it
export DISPLAY=:0

# 3. Start a lightweight window manager
fluxbox &

# 4. Start x11vnc to bridge the display to port 5900
x11vnc -display :0 -bg -nopw -listen localhost -xkb &

# 5. Start websockify / noVNC on port 8080 proxying to 5900
websockify --web /usr/share/novnc/ 8080 localhost:5900 &

# 6. Finally, start your JavaFX application in the foreground
exec java --module-path /app/javafx-sdk-21.0.2/lib \
          --add-modules javafx.controls,javafx.fxml \
          -cp /app/app.jar finalproject.Main