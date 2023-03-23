pipeline {

    agent any
    environment {
                DOCK_CRED = credentials('dockHubCred')
                SQL_PASSTWO = credentials('SQL_PASSTWO')
                HTML_PASS = credentials('HTML_PASS')
    }
    stages{
        stage('cleanup_CloneRepo') {
            steps {
                sh 'ls'
                sh 'chmod 755 Dockerfiles'
                //sh 'cd ./Dockerfiles && docker compose down'
                sh 'rm -rf AcademyDevops'
                sh 'git clone https://github.com/FluffynatorT800/AcademyDevops.git'
            }
        }
        stage('maven build' ) {   
            steps {
                sh 'cd customerapi && mvn clean install'
            }
        }    
        stage('build image') {
            steps {
                sh 'ls'
                sh 'cp /var/lib/jenkins/.m2/repository/de/telekom/customerapi/0.0.1-SNAPSHOT/customerapi-0.0.1-SNAPSHOT.jar Dockerfiles/customerapi.jar'
                sh "cd Dockerfiles && docker build . -t ma5k/devops-demo:$BUILD_NUMBER  --build-arg HTML_PASS='$HTML_PASS' -f java-dockerfile "
            } 
        }            
        stage('docker login & push') {
            steps{
                sh "echo $DOCK_CRED_PSW | docker login -u $DOCK_CRED_USR --password-stdin"
                sh "docker push ma5k/devops-demo:$BUILD_NUMBER"
           }

        }
        stage('docker compose up') {
            steps{ 
              //  sh 'cd Dockerfiles && docker compose up -d'
                sh 'docker ps'             
                sh 'minikube profile list'
                sh 'minikube config view'
                sh 'kubectl --kubeconfig=/home/ma5k/.kube/config apply -f deploy.yml -f deploySQL.yml -f db-per.yml'
                sh 'kubectl --kubeconfig=/home/ma5k/.kube/config get all -n springboot'
                sh 'kubectl config view'
                sh "docker logout"
                sh 'echo y | docker system prune -a'
            }
        }
    }
}
