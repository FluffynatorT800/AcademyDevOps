pipeline {
    agent any
    stages {
        stage('Java-Build') {
            steps {
                // Baut Java-Application mit Maven
                sh '''
                pwd
                cd customerapi
                pwd
                mvn install
                '''
            }
        }
        stage('Docker-Build') {
            steps {
                // Stoppt und löscht alte Container
                sh '''
                cd /data/AzubiDevOps/Dockerfiles
                sudo docker compose down
                '''
                // Läd das Git-Repo neu runter
                sh '''
                cd /data/
                sudo rm -rf AzubiDevOps/
                sudo git clone https://github.com/krie13/AzubiDevOps/
                '''
                // Löscht alte Java-Datei und kopiert im Build-Prozess erstellte Datei in das /data/AzubiDevOps/Dockerfiles Verzeichnis
                sh '''
                cd /data/AzubiDevOps/Dockerfiles
                sudo rm -f /data/AzubiDevOps/Dockerfiles/customerapi.jar
                sudo cp /var/lib/jenkins/.m2/repository/de/telekom/customerapi/0.0.1-SNAPSHOT/customerapi-0.0.1-SNAPSHOT.jar /data/AzubiDevOps/Dockerfiles/customerapi.jar
                '''
                // Erstellt und stratet Container
                // Löscht alte Docker-Images
                sh '''
                sudo docker compose up --build -d
                echo y | sudo -S docker system prune -a
                '''
            }
        }
    }
}
