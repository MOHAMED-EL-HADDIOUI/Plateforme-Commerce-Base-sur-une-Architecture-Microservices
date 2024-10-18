# Utilisation de l'image de base OpenJDK 17 pour exécuter une application Java
FROM openjdk:17-jdk

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Définition de la variable ARG pour pointer vers le fichier .jar généré
ARG JAR_FILE=target/*.jar

# Copier le fichier JAR dans le conteneur et le renommer orderservice.jar
COPY ${JAR_FILE} order-service-0.0.1-SNAPSHOT.jar

# Commande d'entrée pour exécuter l'application Java
ENTRYPOINT ["java", "-jar", "order-service-0.0.1-SNAPSHOT.jar"]

# Exposer le port 8081 (port utilisé par le Order Service)
EXPOSE 8081
