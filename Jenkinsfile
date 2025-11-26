pipeline {
    agent any

    environment {
        DOCKER_HOST = 'tcp://localhost:2376'
        DOCKER_CERT_PATH = ''
        DOCKER_TLS_VERIFY = ''
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Backend Docker Image') {
            steps {
                script {
                    sh 'docker build -t backend:latest .'
                }
            }
        }

        stage('Build Frontend Docker Image') {
            steps {
                script {
                    sh 'docker build -t frontend:latest ./frontend'
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh 'kubectl apply -f k8s/mysql.yaml'
                    sh 'kubectl apply -f k8s/backend.yaml'
                    sh 'kubectl apply -f k8s/frontend.yaml'
                    sh 'kubectl rollout restart deployment/backend'
                    sh 'kubectl rollout restart deployment/frontend'
                    sh 'kubectl rollout status deployment/backend'
                    sh 'kubectl rollout status deployment/frontend'
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                script {
                    sh 'kubectl get pods'
                    sh 'kubectl get svc'
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
