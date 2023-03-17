A Springboot based project to depoly a springboot and a mysql database on docker via Jenkins

The repo contains a working Jenkinsfile and can be run as a pipline project without the need to run it as a maven Project

To make it more accessible there is now a static html file that can be used to view the data   ip-Adress:8080/index.html

An additional dependency  <artifactId>spring-boot-starter-security</artifactId> has been added to the spring boot project allow sercure access

The SpringSecurityFIle allows now to use csrf tokens