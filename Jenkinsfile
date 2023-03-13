pipeline {
    agent any
    stages{
        stage('preBuild') {
            steps {
                sh 'ls'
                sh 'chmod 755 Dockerfiles'
                sh 'cd ./Dockerfiles && docker compose down'
                sh 'rm -rf AcademyDevops'
                sh 'git clone https://github.com/FluffynatorT800/AcademyDevops.git'
            }
        }
        stage('main' ) {
            steps {
                sh 'cd /var/lib/jenkins/workspace/pipeline_test/customerapi && mvn clean install'
            }
        }
        stage('postBuil') {
            steps {
                sh 'ls'
                sh 'cd Dockerfiles'
                sh 'cp /var/lib/jenkins/.m2/repository/de/telekom/customerapi/0.0.1-SNAPSHOT/customerapi-0.0.1-SNAPSHOT.jar customerapi.jar'
                sh 'docker compose up --build -d'
                sh 'docker ps' 
                sh 'echo y | docker system prune-a'
            }
        }
    }
}