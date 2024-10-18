# Utilisation de l'image de base OpenJDK 17 pour exécuter une application Java
FROM openjdk:17-jdk

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Définition de la variable ARG pour pointer vers le fichier .jar généré
ARG JAR_FILE=target/*.jar

# Copier le fichier JAR dans le conteneur et le renommer customerservice.jar
COPY ${JAR_FILE} customer-service-0.0.1-SNAPSHOT.jar

# Commande d'entrée pour exécuter l'application Java
ENTRYPOINT ["java", "-jar", "customer-service-0.0.1-SNAPSHOT.jar"]

# Exposer le port 8086 (port utilisé par le Customer Service)
EXPOSE 8086
