### 1. Build Docker Image

To build the Docker image for the RaisingStocks application:

```bash
docker build -t <your-dockerhub-username>/stocks:latest .
```

### 2. Push to DockerHub

After building the Docker image, push it to DockerHub:

```bash
docker login
docker push <your-dockerhub-username>/stocks:latest
```