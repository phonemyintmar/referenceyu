#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }

    stage('check java') {
        sh "java -version"
    }

    stage('clean') {
        sh "chmod +x mvnw"
        sh "./mvnw clean"
    }

    stage('build application') {
        sh "./mvnw verify -Pprod -DskipTests"
        archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
    }

    stage('sonar analysis') {
        script {
            def scannerHome = tool 'My SonarQube Server';
            withSonarQubeEnv() {
                sh """${scannerHome}/bin/sonar-scanner \
                -Dproject.settings=sonar-project.properties """
            }
        }
    }

    stage('install') {
        sh "./mvnw install -DskipTests"
    }
}
