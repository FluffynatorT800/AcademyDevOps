This variant is based on the Projetct as it is described in the Dokumentaition in this folder. </br>
This repository is a fork of the original porject repository. 

NOTES: 
- 
 ____________
THE OS: </br>
For this variant a Azure VM running ubbuntu 18.04 bionic as an OS was used. </br>
Therefore the paths as described in the Dokumentaition file </br>
are not accurate and needed to be modifed.  </br>
Also the commands to install apps on the VM via CIL needed to be changed, </br>
most of the time using apt and apt-get instead of dnf was successfull. </br>
____________
THE MAVEN PROJECT: </br>
The file paths found the Dokumentaions pre-build and post-build steps needed to </br>
modifed as well. The acurate paths were found by simply running </br>
 sh "ls" commands during the pre build steps in Jenkins </br>
___________
JENKINS: </br>
In this specific case it was necessary to modifie the Network Security Rules via the Azure portal </br>
to get access to the HTTP Ports like :8080 to get access to jenkins. </br>
Modifiying the Jenkins Port to get it to switch to 8081 to clear the port for the app </br>
can only be achived by stopping the Jenkins service </br> 
-> </br>
<-> sudo service jenkins stop  </br>
-> </br>
creating a system overide file - sudo systemctl edit jenkins - </br>
 and entering the new port 8081 as an environment variable.</br>
-> </br>
<->  [Service] </br>
<-> Environment="JENKINS_PORT=8081" </br>
-> </br>
After saving the file jenkins service needs to be restarted  - sudo service jenkins start - 
______________
LOCAL INSTALLATION: </br>
The isntructions on how to run the spring boot app locally </br>
forget to mention that is necessary to install and </br>
set up an mySQL Server locally as HEIDSQL is just a client </br>
 to connect to a running mySQL server </br>
______________
______________
______________
ADDITIONS:
-
______________
JENKINSFILE: </br>
A operational Jenkinsfile was added to the top level of repository. </br>
The project now runs in Jenkins as a conventional Pipeline </br>
rather than an maven project. </br>
The Jenkinsfile contains most of the preBuild and postBuild </br>
steps from the maven project. </br>
Just a "main" stage was added to run maven. </br>
_____________
CRUD:</br>
CRUD operations have been implemented in the Spring Boot app </br>
The CustomerController was expanded and the CustomerService file </br>
was created (in the same folder) to make this possible. </br>
 ____________
FRONTEND:</br>
A build in interface has been created to make external SQL clients unnecessary.</br>
A index.html file has been created in the folder resources/static. </br>
In the next step the indexSripts.js file was created in the same folder. </br>
This JS file contains the basic REST API to allow communication with the client machine.</br>
The site gets every entry in the database, can add entries and delete them via simple html forms.
_________________
SECURITY: </br>
All passwords have been removed from the repository.</br>
The passwords are now stored in the Jenkins credentials and are injected </br>
into the build process as enverionment variables.</br>
To make this work the variables containing the passwords are added in the jenkinsfile</br>
as environment variables which are supplied to the docker compose shell command as arguments. </br>
The docker-compose.yml file uses these variables and passes them to the environment of the springboot app.</br>
In the app they overwrite the placeholder values given in the application.properites file. </br>
- Securing the Frontend </br>
To secure the frontend the springboot sercurity dependency was added in the pom.xml</br>
This added a password prompt to accss the index.html page. The password is, again, stored in Jenkins.</br>
An issue arose as the default spring boot security settings blocked the REST API on the frontend.</br>
After considerable try and error this problem could be resolved.</br>
First the SpringSecurity file was created in the same folder as the app.</br>
This file configures the security settings to allow for two things.</br>
First it requests the password and allows the user access to the full frontend afterwards.</br>
Second it enables the use of CSRF tokens, which allow the REST api to communicate again.</br>
In additon to this the indexScripts.js file was modified to store the CSRF token in a variable,</br>
which is then supplied in the header of every fetch post and delete request.</br>
_________________
_________________
_________________
KUBERNETES/MINIKUBE DEPOLOYMENT:

For test purposes a minikube version was installed on the azure VM. </br>
-> </br>
 <-> sudo apt install -y curl wget apt-transport-https </br>
 <-> curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64 </br>
 <-> sudo install minikube-linux-amd64 /usr/local/bin/minikube </br>
-> </br>
Kubectl command line tool </br>
-> </br>
 <-> curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl" </br>
 <-> sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl </br>
 <-> kubectl version --client </br>
-> </br>
A namespace, in this case "springboot" was created via the kubectl tool. </br>
-> </br>
<-> kubectl create namespace springboot </br>
-> </br>

kubernetes and Kubectl plugin was added in Jenkins, unclear if they are needed </br>

Exposing minikube on a Azure Virtual Machine: </br>
To test Minikube and make it accessable via browser run the following commands to </br>
create a test deployment on the VM </br>
-> </br>
<-> kubectl create deployment my-nginx --image=nginx </br>
-> </br>
To create a simpe nginx deployment. </br>
-> </br>
<-> kubectl expose deployment my-nginx --name=my-nginx-svc --type=NodePort --port=80 </br>
-> </br>
This should expose the deployment, port 80 is the default port for nginx </br>
To test if the exposing worked run the following commands: </br>
-> </br>
<-> kubectl get service my-nginx-svc </br>
<-> minikube ip
-> </br>
The first command should retrive information on the ports, something like 80:1234</br>
The second will give the minikube internal ip adress, something like 123.456.78.9 </br>
By running the following curl command: </br>
-> </br>
<-> curl http://123.456.78.9:1234 </br>
-> </br>
The content of an nginx default website should be deployed in the terminal, starting with "!DOCTYPE html" in the first line </br> 
To allow access to the website via external Browser a port is necessary </br>
-> </br>
<-> kubectl port-forward --address 0.0.0.0 service/my-nginx-svc 8080:80 </br>
-> </br>
The command up to service/ is default after the service/ the name of the srvice nedds to be entered </br>
The first port 8080 is a port that has been made available via Network security settings on the Azure VM, </br>
the second port 80 is the service port, 80 as it is the default nginx port. </br>
It can be neccessary to delete the deployment and service to avoid port conflicts </br>

Due to connection issues it was necessary to change the kubeconfig file, </br>
which jenkins uses in the pipline. </br>
In this case it was neccessary to use the kubeconfig file assigned to the user on the VM. </br>
An absoulte awful solution, will not recommend it. </br>

Changed Jenkinsfile to tag the java image with the build number </br>
Added DockerHub credentials to Jenkins and passed them in the jenkinsfile as env variable </br>
Added extra stages and steps in Jenkinsfiles to login to docker hub and push the image into a specified repository. </br>

Three yaml files were added on top level: </br>
deploy.yml: containes the java app deployment and service </br>
deploySQL.yml: contains the my SQL app and service </br>
db-per.yml: database-persistence; declares that a persitent Volume is needed for the database </br>


_________________
_________________
_________________
 PS.</br>
The spring boot security documentation is a bit of a mess and not that usefull.</br>
-> public class .... extends WebSecurityConfigureAdapter </br>
-> @Overrdie </br>
-> protected void configure(...) </br>
Any reference material containing one of these elements is of very limited use,</br>
as these elements are depecrated
