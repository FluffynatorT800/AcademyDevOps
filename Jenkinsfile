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
                sh "ls"
                sh "chmod 755 Dockerfiles"
                sh "rm -rf AcademyDevops"
                sh "git clone https://github.com/FluffynatorT800/AcademyDevops.git"
            }
        }
        stage('maven build' ) {   
            steps {
                sh "cd customerapi && mvn clean install"
            }
        }    
        stage('build image') {
            steps {
                sh "ls"
                sh "cp /var/lib/jenkins/.m2/repository/de/telekom/customerapi/0.0.1-SNAPSHOT/customerapi-0.0.1-SNAPSHOT.jar \
                    Dockerfiles/customerapi.jar"
                sh "cd Dockerfiles && docker build . -t ma5k/devops-demo:$BUILD_NUMBER -f java-dockerfile "
            } 
        }            
        stage('docker login & push') {
            steps{
                sh "echo $DOCK_CRED_PSW | docker login -u $DOCK_CRED_USR --password-stdin"
                sh "docker push ma5k/devops-demo:$BUILD_NUMBER"
                sh """
                    sed -i 's|latest|$BUILD_NUMBER|' deploy.yml
                   """
           }   
        }
        stage('kubectl secrets creation') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult:'FAILURE' ) {
                    sh "kubectl --kubeconfig=/home/ma5k/.kube/config \
                        delete secret user-pass -n springboot"
                    sh "kubectl --kubeconfig=/home/ma5k/.kube/config \
                        delete secret sql-pass -n springboot"
                }
                catchError(buildResult: 'SUCCESS', stageResult:'FAILURE' ) {
                    sh "kubectl --kubeconfig=/home/ma5k/.kube/config \
                        create secret generic user-pass \
                        --from-literal=user-passing='$HTML_PASS_PSW' -n springboot"
                    sh "kubectl --kubeconfig=/home/ma5k/.kube/config \
                        create secret generic sql-pass \
                        --from-literal=sql-passing='$SQL_PASSTWO_PSW' -n springboot"
                }
            }   
        }
        stage('kubectl deploy') {
            steps{
                sh "kubectl --kubeconfig=/home/ma5k/.kube/config \
                    apply -f db-per.yml -f deploy.yml -f deploySQL.yml"
                sh "kubectl --kubeconfig=/home/ma5k/.kube/config get all -n springboot"            
            }
        }
    }
    post {
        cleanup {
            sh "echo y | docker system prune -a"
            sh "docker logout"
        }
    }
}
