#!/bin/sh

if [ -f .env ]; then
  echo "Loading environment variables from .env file..."
  export $(cat .env | sed 's/#.*//g' | xargs)

 # Handle GCP credentials if provided
  if [ -n "$GCP_CREDENTIALS_BASE64" ]; then
    echo "Decoding GCP credentials..."
    echo $GCP_CREDENTIALS_BASE64 | base64 -d > /tmp/gcp-credentials.json
    export GOOGLE_APPLICATION_CREDENTIALS=/tmp/gcp-credentials.json
  fi
else
  echo "No .env file found. Using environment variables from platform."

  if [ -n "$GCP_CREDENTIALS_BASE64" ]; then
    echo "Decoding GCP credentials from environment..."
    echo $GCP_CREDENTIALS_BASE64 | base64 -d > /tmp/gcp-credentials.json
    export GOOGLE_APPLICATION_CREDENTIALS=/tmp/gcp-credentials.json
  fi
fi


echo "Starting application..."
exec java -jar backend-3.4.1.jar "$@"
