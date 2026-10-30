# --- Build Stage ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

# --- Run Stage ---
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Install GUI, Xvfb, VNC, and NoVNC dependencies
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    xvfb \
    x11vnc \
    fluxbox \
    net-tools \
    novnc \
    websockify \
    libgl1-mesa-dri \
    libgl1-mesa-glx \
    libgtk-3-0 \
    libasound2 \
    libxtst6 \
    libxrender1 \
    libxi6 \
    libfreetype6 \
    && rm -rf /var/lib/apt/lists/*

# Download JavaFX Linux SDK 21
RUN wget -q https://download2.gluonhq.com/openjfx/21.0.2/openjfx-21.0.2_linux-x64_bin-sdk.zip \
    && unzip openjfx-21.0.2_linux-x64_bin-sdk.zip \
    && rm openjfx-21.0.2_linux-x64_bin-sdk.zip

# Copy the generated jar from the build stage
COPY --from=build /app/target/*.jar /app/app.jar

# Copy entrypoint script
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Expose the web port for NoVNC
EXPOSE 8080

# Use the entrypoint script to launch everything
ENTRYPOINT ["/app/entrypoint.sh"]