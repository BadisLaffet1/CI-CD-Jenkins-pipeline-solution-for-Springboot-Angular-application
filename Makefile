# Makefile for Docker-related tasks

# Define the name of the Docker Compose file
COMPOSE_FILE = docker-compose.yml

# Define the name of the Docker Compose project (optional)
COMPOSE_PROJECT = CICD

# Docker Compose command with the specified Compose file and project
DOCKER_COMPOSE = docker-compose -f $(COMPOSE_FILE) -p $(COMPOSE_PROJECT)

# Build the Docker containers defined in the Compose file
build:
	$(DOCKER_COMPOSE) build

# Start the Docker containers in the background
docker_up:
	$(DOCKER_COMPOSE) up -d

# Stop and remove the Docker containers
docker_down:
	$(DOCKER_COMPOSE) down

# View logs for a specific service (e.g., spring-app)
docker_logs:
	$(DOCKER_COMPOSE) logs -f

# Run a specific command in a running container (e.g., spring-app)
docker_exec:
	$(DOCKER_COMPOSE) exec SERVICE_NAME COMMAND

# Other custom Docker-related tasks can be added here
