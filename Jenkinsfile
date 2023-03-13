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
                sh 'cd customerapi && mvn clean install'
            }
        }    
        stage('postBuil') {
                
            environment {
                SQL_PASS = credentials('SQL_PASSWORD')
            }
            steps {
                sh 'ls'
                sh "echo ${SQL_PASS}"
                sh 'cd Dockerfiles'
                sh 'cp /var/lib/jenkins/.m2/repository/de/telekom/customerapi/0.0.1-SNAPSHOT/customerapi-0.0.1-SNAPSHOT.jar Dockerfiles/customerapi.jar'
                sh "cd Dockerfiles && docker compose build --build-arg SQL_PASS='$SQL_PASS_PSW'"
                sh 'cd Dockerfiles && docker compose up -d'
                sh 'docker ps' 
                sh 'echo y | docker system prune -a'
            }
        }
    }
}