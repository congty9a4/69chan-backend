#!/bin/sh

# export GCP_CREDENTIALS_BASE64=$(cat gcp-credentials.json | base64 -w 0)

if [ -f .env ]; then
  echo "Creating env file..."
  export $(cat .env | sed 's/#.*//g' | xargs)
fi

if [ -f gcp-credentials.base64 ]; then
  echo "Creating GCS credentials file...."
  cat gcp-credentials.base64 | base64 -d > /tmp/gcp-credentials.json
  export GOOGLE_APPLICATION_CREDENTIALS=/tmp/gcp-credentials.json
fi

exec java -jar backend-3.4.1.jar "$@"

