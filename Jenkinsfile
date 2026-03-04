pipeline {
    agent any

    environment {
        // Define application name and tag
        IMAGE_NAME = "wappy"
        IMAGE_TAG = "latest"
    }

    stages {
        stage('Checkout') {
            steps {
                // Pulls code from your SCM
                checkout scm
            }
        }

        stage('Build Image') {
            steps {
                script {
                    // Builds the image from the Dockerfile in root
                    docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                }
            }
        }

        stage('Run Container') {
            steps {
                script {
                    // 1. Stop and remove existing container if it exists
                    sh "docker ps -f name=${IMAGE_NAME} -q | xargs -r docker stop"
                    sh "docker ps -a -f name=${IMAGE_NAME} -q | xargs -r docker rm"

                    // 2. Start the new container
                    // -d: detached mode, -p: port mapping, --name: container name
                    sh "docker run -d --name ${IMAGE_NAME} -p 8080:8080 ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }
    }
}