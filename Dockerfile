# --- Build Stage ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the application (adjust for gradle if needed)
RUN mvn clean package -DskipTests

# --- Run Stage ---
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Install necessary GUI / X11 / GTK libraries required by JavaFX Prism
RUN apt-get update && apt-get install -y \
    libgl1-mesa-dri \
    libgl1-mesa-glx \
    libgtk-3-0 \
    libasound2 \
    libxtst6 \
    libxrender1 \
    libxi6 \
    libfreetype6 \
    && rm -rf /var/lib/apt/lists/*

# Copy the built jar from the build stage
COPY --from=build /app/target/your-app-name-1.0-SNAPSHOT.jar /app/app.jar

# Set environment variables for display
ENV DISPLAY=:0

# Run the JavaFX application
CMD ["java", "--module-path", "/path/to/javafx-sdk/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "/app/app.jar"]