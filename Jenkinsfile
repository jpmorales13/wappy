pipeline {
    agent any

    stages {
        stage('Build') {
            agent {
                docker {
                    image 'node:18-alpine'
                    reuseNode true
                }
            }
            steps {
                sh '''
                    echo "Building docker container..."
                    docker build -t wappy
                '''
            }
        }

        stage('Run') {
            steps {
                sh '''
                    echo "Running docker container..."
                    docker run wappy
                '''
            }
        }
    }
}
