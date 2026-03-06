pipeline {
    agent any

    environment {
        // Define application name and tag
        IMAGE_NAME = "wappy"
        IMAGE_TAG = "latest"

        // Define the Docker image name: <your-dockerhub-username>/<your-repo-name>
        DOCKER_IMAGE = "jmorales13/${IMAGE_NAME}:${env.BUILD_ID}"
        // Reference the Jenkins credential ID
        DOCKER_CREDENTIAL_ID = 'dockerhub-credentials'
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
                    // Use the withRegistry block to authenticate with Docker Hub
                    docker.withRegistry('https://registry.hub.docker.com', DOCKER_CREDENTIAL_ID) {
                        // Builds the image from the Dockerfile in root
                        def app = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                        app.push()
                    }
                }
            }
        }
    }
}