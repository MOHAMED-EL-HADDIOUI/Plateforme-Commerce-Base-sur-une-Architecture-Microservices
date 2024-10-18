
🚀 Je vous présente mon projet e-commerce avec une architecture de microservices basée sur Spring Boot ! 🛒

Je suis ravi de partager mon dernier projet sur GitHub - notre application e-commerce, nommée e-commerce-springboot, a été développée avec une architecture de microservices utilisant Spring Boot. Ce projet démontre l'application des concepts modernes de microservices et des meilleures pratiques.

🔧 Technologies et outils utilisés :

* **Spring Boot** : Cadre de base pour les microservices.
* **Spring Cloud Config** : Gestion centralisée de la configuration.
* **Spring Cloud Netflix Eureka** : Enregistrement et découverte de services.
* **Spring Cloud Gateway** : Passerelle API qui dirige les requêtes.
* **Feign Client** : Client REST déclaratif pour la communication entre services.
* **Resilience4J** : Mécanismes de disjoncteur, de nouvelle tentative et de limitation de débit.
* **Validation** : Validation des entrées avec Hibernate Validator.
* **Health Checks** : Surveillance de l'état de santé de chaque microservice.
* **RabbitMQ** : Mécanisme de communication asynchrone et de mise à jour des stocks.
* **Zipkin** : Suivi distribué et surveillance des performances.
* **Redis** : Accès rapide aux données et mise en cache.
* **Swagger UI** : Interface conviviale pour la documentation API.
* **Docker et Docker Compose** : Gestion et configuration des conteneurs pour garantir un fonctionnement fluide des composants du système.
* **Okta (à venir)** : Solution sécurisée et conviviale pour l'authentification et l'autorisation.

📌 Résumé du projet : Ce projet inclut divers concepts de microservices tels que l'enregistrement de services, la passerelle API, la tolérance aux pannes, la configuration centralisée et la gestion des paiements. L'application couvre les fonctionnalités essentielles du commerce électronique, telles que la gestion des clients, la gestion des adresses, la gestion des produits, la gestion des stocks, la gestion des commandes et la gestion des paiements. Les clients doivent d'abord s'inscrire dans le système et fournir leurs informations d'adresse lors de l'inscription pour passer des commandes. Une fois la commande passée, le paiement doit être effectué, et après le traitement du paiement, la quantité de stock est mise à jour via RabbitMQ. Lors de la mise à jour de la commande, l'état du paiement est annulé, et un nouveau paiement doit être effectué lorsque la commande est mise à jour. **Nouvelles fonctionnalités :** Un service de livraison a été ajouté ; après une commande, l'état de la livraison est préparé et la commande de livraison est finalisée après le paiement. De plus, l'intégration de Redis a permis un accès rapide aux données, l'intégration de Swagger UI a rendu la documentation API facilement accessible, et avec le support de Docker et Docker Compose, le système fonctionne de manière cohérente dans divers environnements.

📂 Structure du projet :

* **config-server** : Serveur de configuration centralisée.
* **discovery-server** : Enregistrement de services avec Eureka.
* **api-gateway** : Passerelle API utilisant Spring Cloud Gateway.
* **customer-service** : Gère les informations clients et permet l'inscription des clients dans le système.
* **address-service** : Gère les adresses des clients. (Les informations d'adresse sont créées lors de l'inscription du client et peuvent être mises à jour par la suite.)
* **product-service** : Gère les informations sur les produits.
* **inventory-service** : Gère les niveaux d'inventaire et de stock.
* **order-service** : Gère les commandes des clients.
* **payment-service** : Gère les paiements des commandes. (Le paiement doit être effectué après la commande.)
* **cargo-service** : Gère les états de livraison ; après une commande, l'état de la livraison est préparé et la commande de livraison est finalisée après le paiement.

🌐 Endpoints :

**Service Client :**

* POST /api/v1/customers : Crée un nouvel enregistrement client.
* GET /api/v1/customers/all : Liste tous les clients.
* GET /api/v1/customers/{customerId} : Récupère les informations d'un client spécifique par ID.
* GET /api/v1/customers/customerByFirstName : Liste les clients avec un prénom spécifique.
* GET /api/v1/customers/track-cargo/{trackingNumber} : Permet de suivre les détails d'un envoi par le numéro de suivi.
* PUT /api/v1/customers/{customerId} : Met à jour les informations du client. (L'adresse du client n'est pas mise à jour lors de cette opération. Une endpoint séparée doit être utilisée pour mettre à jour l'adresse.)
* DELETE /api/v1/customers/{customerId} : Supprime l'enregistrement d'un client.

**Service Adresse :**

* GET /api/v1/addresses/all : Liste toutes les adresses.
* GET /api/v1/addresses/{addressId} : Récupère une adresse spécifique par ID.
* PUT /api/v1/addresses/{addressId} : Met à jour les informations d'adresse. (La mise à jour de l'adresse est effectuée séparément de l'enregistrement du client.)
* DELETE /api/v1/addresses/{addressId} : Supprime un enregistrement d'adresse spécifique.

**Service Produit :**

* POST /api/v1/products : Crée un nouveau produit.
* GET /api/v1/products/all : Liste tous les produits.
* GET /api/v1/products/{id} : Récupère un produit spécifique par ID.
* GET /api/v1/products/inventoryById/{inventoryId} : Récupère un produit par ID d'inventaire spécifique.
* GET /api/v1/products/productByPriceRange?minPrice={minPrice}&maxPrice={maxPrice} : Liste les produits dans une plage de prix spécifiée.
* GET /api/v1/products/productByQuantity?quantity={quantity} : Liste les produits d'une quantité spécifique.
* GET /api/v1/products/productByPriceGreaterThanEqual?price={price} : Liste les produits dont le prix est supérieur ou égal à un prix spécifié.
* GET /api/v1/products/productByPriceLessThanEqual?price={price} : Liste les produits dont le prix est inférieur ou égal à un prix spécifié.
* GET /api/v1/products/productByCategory?category={category} : Liste les produits d'une catégorie spécifique.
* PUT /api/v1/products : Met à jour le produit (Lorsque le produit est mis à jour, le service d'inventaire est également mis à jour).
* DELETE /api/v1/products/{id} : Supprime le produit (Lorsque le produit est supprimé, le service d'inventaire est également mis à jour).

**Service Inventaire :**

* POST /api/v1/inventories/create : Crée un nouvel inventaire.
* GET /api/v1/inventories/all : Liste tous les inventaires.
* GET /api/v1/inventories/{productId} : Récupère l'inventaire par ID de produit spécifique.
* GET /api/v1/inventories/getInventoryId/{id} : Récupère l'inventaire par ID d'inventaire spécifique.
* PUT /api/v1/inventories/{inventoryId} : Met à jour l'inventaire.
* DELETE /api/v1/inventories/{productId} : Supprime l'inventaire.

**Service Commande :**

* GET /api/v1/orders : Liste toutes les commandes.
* GET /api/v1/orders/{id} : Récupère une commande spécifique par ID.
* GET /api/v1/orders/orderDateBetween?startDateTime={startDateTime}&endDateTime={endDateTime} : Liste les commandes dans une plage de dates spécifiée.
* POST /api/v1/orders : Crée une nouvelle commande.
* PUT /api/v1/orders/{id} : Met à jour une commande existante.
* DELETE /api/v1/orders/{id} : Supprime une commande spécifique.

**Service Paiement :**

* POST /api/v1/payments : Crée un nouveau paiement (Le paiement doit être effectué après la commande. Après le paiement reçu via RabbitMQ, la mise à jour de stock est effectuée).
* GET /api/v1/payments/{orderId} : Récupère l'état du paiement par ID de commande spécifique.
* GET /api/v1/payments/paymentById/{paymentId} : Récupère les informations de paiement par ID de paiement spécifique.
* GET /api/v1/payments/paymentByType : Récupère les informations de paiement par type de paiement spécifique.
* GET /api/v1/payments/paymentDateBetween : Récupère les informations de paiement entre des dates spécifiées.
* GET /api/v1/payments/paymentCustomerById/{customerId} : Récupère les informations de paiement par ID de client spécifique.
* PUT /api/v1/payments : Met à jour le paiement (Après le paiement, la quantité de stock est mise à jour via RabbitMQ).
* DELETE /api/v1/payments/{paymentId} : Supprime le paiement.
* POST /api/v1/payments/cancelPayment/{paymentId} : Annule un paiement par ID de paiement spécifique.

**Service Cargo :**
* POST /api/v1/cargos : Crée une nouvelle expédition.
* GET /api/v1/cargos/all : Récupère la liste de toutes les expéditions.
* GET /api/v1/cargos/{cargoId} : Récupère une expédition par ID