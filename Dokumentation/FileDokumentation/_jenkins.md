Jenkins Declerative Pipeline: </br>
Environment variables are passed form the Jenkins credentials </br>


    environment { 
                DOCK_CRED = credentials('dockHubCred') 
                SQL_PASSTWO = credentials('SQL_PASSTWO') 
                HTML_PASS = credentials('HTML_PASS')} 


STAGE: clean_upCloneRepo: </br>
Deletes existing copies of the repository and copies an new one </br>


    steps {
            sh "ls"
            sh "chmod 755 Dockerfiles"
            sh "rm -rf AcademyDevops"
            sh "git clone https://github.com/FluffynatorT800/AcademyDevops.git"     
    }



STAGE: maven build: </br>
maven is run to compile the spring boot app. </br>

```
steps {
                sh "cd customerapi && mvn clean install"
            }
```

STAGE: build image: </br>
The .jar file from the previous step is copied over and used to build a docker image </br>
from the java-dockerfile with the Jenkins build number as an image tag.
```
 steps {
                sh "ls"
                sh "cp /var/.../customerapi-0.0.1-SNAPSHOT.jar  Dockerfiles/customerapi.jar"
                sh "cd Dockerfiles && docker build . -t ma5k/devops-demo:$BUILD_NUMBER -f java-dockerfile "
            } 
```
STAGE: docker login & push: </br>
After loggin the image, from the previous step, is pushed to docker hub. </br>
The 'sed' command is used to change the image tag in the deploy.yml file
```
  steps{
                sh "echo $DOCK_CRED_PSW | docker login -u $DOCK_CRED_USR --password-stdin"
                sh "docker push ma5k/devops-demo:$BUILD_NUMBER"
                sh """
                    sed -i 's|latest|$BUILD_NUMBER|' deploy.yml
                   """
           }   
```
STAGE: kubectl secrets creation: </br>
Jenkins environment variables are passed as kubernetes secretcs </br>
This step must only execute once to set the secrets, to change the secrets </br>
they must be deleted first. </br>
Since deleting non existing secrets, and creating existing secrets causes errors </br>
these steps are placed in a catch error section. </br>
Kubectl commands are executed with the --kubeconfig flag to allow jenkins to use minikube as a different user.
```
 steps {
                catchError(buildResult: 'SUCCESS', stageResult:'FAILURE' ) {
                    sh "kubectl --kubeconfig=/home/ma5k/.kube/config \
                        create secret generic user-pass \
                        --from-literal=user-passing='$HTML_PASS_PSW' -n springboot"
                    sh "kubectl --kubeconfig=/home/ma5k/.kube/config \
                        create secret generic sql-pass \
                        --from-literal=sql-passing='$SQL_PASSTWO_PSW' -n springboot"
                }
            }   
```
STAGE: kubectl deploy: </br>
The depoly.yml deploySql.yml and db-per.yml are deployed to minikube </br> 
```
 steps{
                sh "kubectl --kubeconfig=/home/ma5k/.kube/config \
                    apply -f db-per.yml -f deploy.yml -f deploySQL.yml"
                sh "kubectl --kubeconfig=/home/ma5k/.kube/config get all -n springboot"            
            }
```
POST: cleanup: </br>
Old docker images are deleted form the workspace and </br>
jenkins logs out of docker hub </br>
These steps are allways executed independent if the previous steps were succesfull
```
cleanup {
            sh "echo y | docker system prune -a"
            sh "docker logout"
        }
```