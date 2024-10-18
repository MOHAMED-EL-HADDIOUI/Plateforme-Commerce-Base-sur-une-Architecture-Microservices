# Utilisation de l'image de base OpenJDK 17 pour exécuter une application Java
FROM openjdk:17-jdk

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Définition de la variable ARG pour pointer vers le fichier .jar généré
ARG JAR_FILE=target/*.jar

# Copier le fichier JAR dans le conteneur et le renommer inventoryservice.jar
COPY ${JAR_FILE} inventory-service-0.0.1-SNAPSHOT.jar

# Commande d'entrée pour exécuter l'application Java
ENTRYPOINT ["java", "-jar", "inventory-service-0.0.1-SNAPSHOT.jar"]

# Exposer le port 8082 (port utilisé par l'Inventory Service)
EXPOSE 8082
