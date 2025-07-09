
# Offer Service (Microservicio de Ofertas de Trabajo)

Este es un microservicio desarrollado en Java con Spring Boot que gestiona ofertas de trabajo. Permite la creación, consulta, actualización y eliminación de ofertas. Además, gestiona la inscripción de participantes y se integra con otros servicios a través de una API REST y mensajería asíncrona con Kafka.

## ✨ Características Principales

*   **Gestión de Ofertas (CRUD):** Creación, lectura, actualización y eliminación de ofertas de trabajo.
*   **Paginación:** Todas las listas de ofertas están paginadas para un rendimiento óptimo.
*   **Filtros:** Permite filtrar ofertas por estado (`ACTIVAS`), por el administrador que las creó o por los participantes inscritos.
*   **Inscripción de Participantes:** Los usuarios pueden inscribirse en una oferta de trabajo.
*   **Validación de Usuarios:** Se conecta a un servicio externo de usuarios para validar la identidad de un participante antes de la inscripción.
*   **Cierre Automático de Ofertas:** Cuando una oferta alcanza el número máximo de candidatos, su estado cambia automáticamente a `CERRADA`.
*   **Notificaciones Asíncronas:** Al cerrarse una oferta, se publica un evento en un topic de Kafka para notificar a otros sistemas (por ejemplo, para agendar entrevistas).

## 🛠️ Tecnologías Utilizadas

*   **Lenguaje:** Java 17+
*   **Framework:** Spring Boot 3
*   **Acceso a Datos:** Spring Data JPA / Hibernate
*   **Base de Datos:** Compatible con cualquier base de datos SQL (ej. PostgreSQL, H2)
*   **Mensajería:** Apache Kafka
*   **Cliente REST:** Retrofit 2
*   **Utilidades:** Lombok
*   **Build Tool:** Maven / Gradle

## 🏗️ Arquitectura

El proyecto sigue una aproximación a la **Arquitectura Hexagonal (Puertos y Adaptadores)**, separando la lógica de negocio (dominio) de los detalles de infraestructura.

*   **`domain`**: Contiene la lógica de negocio principal (`JobOfferServiceImpl`), los puertos (interfaces como `JobOfferService`) y los mappers. Es el núcleo de la aplicación y no depende de ninguna tecnología externa.
*   **`infrastructure`**: Contiene los adaptadores que implementan los puertos y se comunican con el exterior:
    *   **`controller`**: Endpoints de la API REST.
    *   **`repository`**: Implementación de la persistencia con Spring Data JPA.
    *   **`messaging`**: Productores de Kafka para enviar eventos.
    *   **`restclient`**: Clientes HTTP (Retrofit) para comunicarse con otros microservicios.

## 🔌 API Endpoints

A continuación se detallan los endpoints principales de la API. El prefijo base (ej. `/api/v1`) puede variar según la configuración del controlador.

| Método | Ruta                                           | Descripción                                         |
|--------|------------------------------------------------|-----------------------------------------------------|
| `POST` | `/offers`                                      | Crea una nueva oferta de trabajo.                   |
| `GET`  | `/offers?page={n}`                             | Obtiene una lista paginada de todas las ofertas.    |
| `GET`  | `/offers/new?page={n}`                         | Obtiene una lista paginada de ofertas activas.      |
| `GET`  | `/offers/admin/{email}?page={n}`               | Obtiene las ofertas creadas por un administrador.   |
| `GET`  | `/offers/participant/{id}?page={n}`            | Obtiene las ofertas en las que un usuario participa.|
| `GET`  | `/offers/{id}`                                 | Obtiene los detalles de una oferta específica.      |
| `PUT`  | `/offers/{id}`                                 | Actualiza una oferta de trabajo existente.          |
| `DELETE`| `/offers/{id}`                                | Elimina una oferta de trabajo.                      |
| `POST` | `/offers/{offerId}/participants/{userId}`      | Inscribe a un usuario en una oferta.                |




## 🏛️ Arquitectura de Despliegue y Criterios de Calidad
El diseño y despliegue de este microservicio se rigen por estrictos criterios de calidad para garantizar la seguridad, el aislamiento y la mantenibilidad del sistema.•Entorno de Ejecución y Aislamiento:•Host: El servicio está diseñado para ser desplegado en una Máquina Virtual (VM).•Empaquetado: La aplicación se distribuye como un archivo WAR (Web Application Archive), compatible con servidores de aplicaciones Java como Apache Tomcat.•Base de Datos: La base de datos (Mysql) se ejecuta en un contenedor Docker dentro de la misma VM. Esta estrategia asegura un entorno de persistencia consistente, portátil y aislado.•Criterios de Seguridad (No Funcionales):•Aislamiento de Red de la Base de Datos: El contenedor de la base de datos está configurado para aceptar conexiones únicamente desde la propia máquina virtual (ej. localhost). Esta medida es crucial para minimizar la superficie de ataque, impidiendo cualquier intento de acceso directo desde redes externas.•Punto de Entrada Único (API Gateway): El microservicio no debe exponerse directamente a internet. Todo el tráfico entrante debe ser gestionado y enrutado a través de un API Gateway. Este componente actúa como una fachada de seguridad, centralizando responsabilidades como la autenticación de clientes, la autorización de rutas, el balanceo de carga y la limitación de peticiones (rate limiting).

## diagrama de clases

![image](https://github.com/user-attachments/assets/1004374b-19d5-4b27-957d-a4162ed0abfb)
