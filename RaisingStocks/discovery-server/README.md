### 1. Build Docker Image

To build the Docker image for the Eureka Discovery Server application:

```bash
docker build -t <your-dockerhub-username>/eureka-discovery-server:latest .
```

### 2. Push to DockerHub

After building the Docker image, push it to DockerHub:

```bash
docker login
docker push <your-dockerhub-username>/eureka-discovery-server:latest
```