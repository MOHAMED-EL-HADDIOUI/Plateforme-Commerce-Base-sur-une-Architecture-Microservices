# Utilisation de l'image de base OpenJDK 17 pour exécuter une application Java
FROM openjdk:17-jdk

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Définition de la variable ARG pour pointer vers le fichier .jar généré
ARG JAR_FILE=target/*.jar

# Copier le fichier JAR dans le conteneur et le renommer serviceregistry.jar
COPY ${JAR_FILE} service-registry-0.0.1-SNAPSHOT.jar

# Commande d'entrée pour exécuter l'application Java
ENTRYPOINT ["java", "-jar", "service-registry-0.0.1-SNAPSHOT.jar"]

# Exposer le port 8761 (port utilisé par le Service Registry)
EXPOSE 8761
