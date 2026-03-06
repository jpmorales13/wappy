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
        SERVICE_NAME  = "Wappy"
    }

    stages {
        stage('Checkout') {
            steps {
                // Pulls code from your SCM
                checkout scm
            }
        }

        stage('Build and Push') {
            steps {
                script {
                    // Use the withRegistry block to authenticate with Docker Hub
                    docker.withRegistry('https://registry.hub.docker.com', DOCKER_CREDENTIAL_ID) {
                        // Builds the image from the Dockerfile in root
                        def app = docker.build("${DOCKER_IMAGE}")
                        app.push()
                    }
                }
            }
        }

        stage('Deploy Infrastructure') {
            steps {
                // Using the Global Variable AWS_REGION here
                withAWS(credentials: 'aws-credentials', region: "${env.AWS_REGION}") {
                    sh """
                        aws cloudformation deploy \
                            --template-file ecs.yml \
                            --stack-name ${SERVICE_NAME}-stack \
                            --capabilities CAPABILITY_IAM \
                            --parameter-overrides \
                                SubnetID="${env.SUBNET_ID}" \
                                ServiceName="${SERVICE_NAME}" \
                                ServiceVersion="${env.BUILD_ID}" \
                                DockerHubUsername="your-username"
                    """
                }
            }
        }
    }
}