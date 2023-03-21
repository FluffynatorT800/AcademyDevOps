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
For an experiment an automated tool was used to generate the needed kubernetes yml files.  => to be discarded</br>

Changed Jenkinsfile to tag the java image with the build number
Added DockerHub credentials to Jenkins and presented them env variable 

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


