pipeline {
    agent any
    stages {
        stage('Build Image') {
            steps {
                script {
                    // Build the image and store the reference in a variable
                    dockerImage = docker.build("wappy:${env.BUILD_NUMBER}")
                }
            }
        }
        stage('Test Image') {
            steps {
                script {
                    // Run commands inside the newly built container
                    dockerImage.inside {
                        sh 'npm test'
                    }
                }
            }
        }
    }
}
