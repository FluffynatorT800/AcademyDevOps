MINIKUBE TO DEPLOY THE APPLICATION </br>

IMPORTANT: When using Azure, and if possible, it is wise to use the Azure Kubernetes Service AKS instead of minikube </br>
__________________
__________________
__________________
MINIKUBE TESTRUN & Set-up:

For test purposes a minikube version was installed on the azure VM. </br>
```
 sudo apt install -y curl wget apt-transport-https 
 curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64 
 sudo install minikube-linux-amd64 /usr/local/bin/minikube 
```
Kubectl command line tool </br>
```
  curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl" 
  sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl 
  kubectl version --client 
```
A namespace, in this case "springboot" was created via the kubectl tool. </br>
```
 kubectl create namespace springboot 
```

kubernetes plugin added in Jenkins</br>

Exposing minikube on a Azure Virtual Machine: </br>
To test Minikube and make it accessable via browser run the following commands to </br>
create a test deployment on the VM </br>
```
 kubectl create deployment my-nginx --image=nginx 
```
To create a simpe nginx deployment. </br>
```
 kubectl expose deployment my-nginx --name=my-nginx-svc --type=NodePort --port=80 
```
This should expose the deployment, port 80 is the default port for nginx </br>
To test if the exposing worked run the following commands: </br>
```
 kubectl get service my-nginx-svc
 minikube ip
```
The first command should retrive information on the ports, something like 80:1234</br>
The second will give the minikube internal ip adress, something like 123.456.78.9 </br>
By running the following curl command: </br>
```
 curl http://123.456.78.9:1234
```
The content of an nginx default website should be deployed in the terminal, starting with "!DOCTYPE html" in the first line </br> 
To allow access to the website via external Browser a port-forward is necessary </br>
```
 kubectl port-forward --address 0.0.0.0 service/my-nginx-svc 8080:80
```
The command up to service/ is default after the service/ the name of the service nedds to be entered </br>
The first port 8080 is a port that has been made available via Network security settings on the Azure VM, </br>
the second port 80 is the service port, 80 as it is the default nginx port. </br>
It can be neccessary to delete the kubernetes deployment and service to avoid port conflicts </br>
____________
____________
____________

MINIKUBE IMPLEMENTATION:

Jenkins had issues executing kubectl commands in the pipeline, due to issues </br>
with user rights on the virtual machine. </br>
To resolve this issue the jenkins server is set to use the kubeconfig file of another user on the VM </br>
This is achived by adding the kubeconfig commands in the jenkinspipeline. </br> 
An absoulte awful solution, will not recommend it. </br>
To make it work the "jenkins" user was given access to certain files on the vm by modifing the read rights </br>
The files in question can be found in the kubeconfig file of the vm user </br>
```
 kubectl config view
```

__________
ADDING DOCKER HUB ACCESS: </br>
In the next step DockerHub credentials were added to Jenkins and passed on in the jenkinsfile as env variable </br>
An extra stage and steps were added in the Jenkinsfiles to login to docker hub and push the image, build in the build step, into a specified repository. </br>

__________
ADDING THE KUBERNETES YAML FILES: </br>
Three yaml files were added on the root level: </br>
deploy.yml: contains the java app deployment and service </br>
=> Deployments describes how the image is supposed to be deployed in the container. </br>
=> Services discribe how the container is supposed to be connected </br>
deploySQL.yml: contains the my SQL app and service </br>
db-per.yml: database-persistence; declares that a persitent volume is needed for the database </br>

__________
REMOVE DOCKER COMPOSE FROM JENKINSFILE: </br>
The docker compose up step is replaced with a kubectl deploy step </br>
The docker compose file is therefor no longer in use </br>

___________
MODIFING THE applicaton.properties FILE </br>
The path to the database url in the application.properties file need to be modified to direct towards the mysql-servie </br>
I.e. spring.datasource.url=jdbc:mysql://mysql-service:3306/customerapi </br>

___________
PASSING ENVIREONMENT VARIABLES TO MINIKUBE: </br>
To make use of jenkins env variables they must be created as kubernetes secrets in the jenkinsfile. </br>
Then the deployment files can use them as a reference </br>
Secrects can not be created if they already exist so they need to be deletet first. </br>
Also it is bit tricky to change passwords passed to deployment.yml later on, </br>
as the persistent volume claim makes the sql password very persistent and changing the env variables has no effect </br>
Even changing the password in the docker container after entering it via 'docker exec XXX' command does not </br>
effect the password needed to access the entrypoint to the mysql container which the other containers have to use</br>

___________
MODIFING THE deploy YAML FILE: </br>
yml files can not take variables directly; in this case a 'sed' command is used in the docker login stage </br>
to override the image tag in the deploy.yml file

___________
RUNNING THE PIPELINE: </br>
After the jenkins pipeline has run succesfully the pods will be created in the designatet namespace.</br>
To access them via browser a port forward must set up.</br>
```
 kubectl port-forward --address 0.0.0.0 service/java-service 8080:8080 -n springboot
```
Port-forward can not be run in the jenkinspipeline, </br>
as it prevents the pipeline from finishing </br>

Minikube is a testing tool, and therefore not easy to be fully automated </br>
Minikube needs to be started manually in the VM-CLI: </br>
```
 minikube start
```

______________________
______________________
______________________

MINIKUBE AUTOSTART:

To launch minikube automatically when the VM is started, a file can be added to </br>
ect/systemd/system/ on the VM. </br>
Using a build in text editor like vi, a command like 
```
sudo vi newFileName
```
will work. </br>

Entering the following lines into the file:
```
 [Unit]
 Description=Kickoff Minikube Cluster
 After=docker.service

 [Service]
 Type=oneshot
 ExecStart=/usr/local/bin/minikube start
 RemainAfterExit=true
 ExecStop=/usr/local/bin/minikube stop
 StandardOutput=journal
 User= yourUserName
 Group= yourUserGroup

 [Install]
 wantedBy=multi-user.target
 ```
 Will crate a script to launch minikube whenever the VM starts </br>