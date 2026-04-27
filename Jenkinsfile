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
    }
}