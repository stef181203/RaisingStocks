### 1. Build Docker Image

To build the Docker image for the Technical Indicators Service application:

```bash
docker build -t <your-dockerhub-username>/technical-indicator-service:latest .
```

### 2. Push to DockerHub

After building the Docker image, push it to DockerHub:

```bash
docker login
docker push <your-dockerhub-username>/technical-indicator-service:latest
```