What is where and what does it? </br>

General Description: </br>
This repository contains a springboot app + sql database which can be deployed</br>
as containers on minikube. </br>
After the deployment a static HTML page can be accessed via browser, on the page </br>
the database can be accessed. </br>

To make use of this repo one needs a virtual machine with docker, jenkins and minikube installed. </br>

Detailed descriptions can be found in the Dokumentation folder. </br>

Content
-

Files on Root level: </br>
   - Jenkinsfile: </br>
     The instructions for the jenkins pipeline. </br>
   - YAML Files: </br>
     These files contain all the neccessary information to deploy on minikube </br>

The spring boot app: </br>
 The Spring boot app is placed in the folder customerapi/src/main/java/de/telekom/customerapi </br>
 There are all the java files located that describe the function of the spring boot app </br>

Resources: </br>
Under customerapi/src/main/resources lies the application.properties file. </br>
This file contains references for the spring boot app i.e. path to the database. </br>
Some values are just placeholders such as xxx or yyy these values are overwritten later on</br>

Frontend: </br>
In the resources folder lies the static folder. This folder contains HTML and JavaScript files, </br>
which discribe the front end. Spring boot is preconfigerd to use HTML files from this location </br>

Dockerfiles: </br>
This folder found on the root level contains a java docker-file and docker-compose.yml file </br>
The java-dockerfile contains the instructions docker needs to build the spring boot app image </br>
The docker-compose file contains the information neede to use docker compose to build the images and start the </br>
app and database container. It is no longer in use, its funtion has been delegated to the YAML files on the root level </br>
This repository has a "backup" branch, there a version operating with docker compose instead of minikube can be found</br>

Elements in the repo that have not been mentioned have not been modified and are mostly default spring boot files.</br>

Variables
-
On several occasions enverionment variables are used to overwrite values or they are added to the build process of a container</br>

These variables are stored on the Jenkins-Server as credentials. They are listed in the Jenkinsfile. </br>
When the jenkins-pipline runs these variables are either just called, or they are added as secrets, </br>
to the kubernetes cluster. In the top level YAML files these secrets are referenced and used to add values </br>
or overwrite placeholder values. </br>
I.e. the placeholder value 'xxx' in application.properties line 3 is overwritten by the deploy.yml file, </br>
in the lines 26-30 where a secret is referenced as source of the actual value. </br>
The secret was created in the jenkins-pipline using a variable that is stored on the jenkins server. </br>

