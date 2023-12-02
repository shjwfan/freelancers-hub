#!/bin/bash

IMAGE_NAME="freelancers-hub"
IMAGE_TAG="latest"
DOCKERFILE="./Dockerfile"
CONTAINER_NAME="freelancers-hub"

./mvnw clean package

if [ "$(docker ps -a -q -f name=$CONTAINER_NAME)" ]; then
  echo "Stop and remove existing Freelancers Hub Docker container..."
  docker stop $CONTAINER_NAME
  docker rm $CONTAINER_NAME
fi

if [ "$(docker images -q $IMAGE_NAME:$IMAGE_TAG)" ]; then
  echo "Remove existing Freelancers Hub Docker image..."
  docker rmi $IMAGE_NAME:$IMAGE_TAG
fi

docker build -t "$IMAGE_NAME:$IMAGE_TAG" -f "$DOCKERFILE" .

if [ $? -eq 0 ]; then
  echo "Freelancers Hub Docker image has been built: $IMAGE_NAME:$IMAGE_TAG."
  docker run -d --name $CONTAINER_NAME -p 8080:8080 -p 5005:5005 "$IMAGE_NAME:$IMAGE_TAG"

  if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo "Freelancers Hub Docker container has been started."
  else
    echo "Failed to start Freelancers Hub Docker container."
  fi

else
  echo "Failed to build Freelancers Hub Docker image."
fi
