pipeline {
    agent any

    tools {
        maven 'maven'
        jdk 'jdk'
    }

    stages {
        stage('Git Checkout') {
            steps {
                script {
                    git branch: 'main',
                    credentialsId: 'TOKEN_GITHUB',
                    url: 'https://github.com/ThomasSeiwert/HarmoGestion_web.git'
                }
            }
        }

        stage('Build Maven') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Generate Allure Report') {
            steps {
                bat 'mvn allure:report'
            }
        }

        stage('Build Docker image') {
            steps {
                script {
                    docker.build('cvert/harmogestion-web:latest', '-f Dockerfile .')
                }
            }
        }

        stage('Push image to Docker Hub') {
            steps {
                script {
                    docker.withRegistery('', 'Docker Account') {
                        docker.image('cvert/harmogestion-web:latest').push()
                    }
                }
            }
        }

        stage('Deploy Docker Compose') {
            steps {
                script {
                    bat 'docker-compose up -d --build --force-recreate --remove-orphans'
                }
            }
        }
    }

    post {
        always {
            allure([
                includeProperties: false,
                jdk: '',
                properties: [],
                reportBuildPolicy: 'ALWAYS',
                results: [[path: 'target/allure-results']]
            ])
        }
    }
}