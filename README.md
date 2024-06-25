# Taller de Sistemas de Información Geográfica 2024 - Montevideo, Uruguay

## Descripción
Este proyecto es una aplicación desarrollada para gestionar el sistema de alquileres de autos en el departamento de Montevideo, Uruguay. 
La aplicación proporciona diferentes funcionalidades dependiendo del perfil del usuario: anónimo o administrador.

## Características
- **Usuarios Anónimos:**
  - Búsqueda de vehículos disponibles
  - Visualización de detalles del vehículo

- **Administradores:**
  - Gestión de automotoras, sucursales y vehículos, (añadir, editar, eliminar)
  - Sección de consultas geograficas

## Tecnologías Utilizadas
- **Backend:** Java Spring Boot 3.2.5
- **Frontend:** Vue.js + OpenLayers
- **Base de Datos:** PostgreSQL + PostGIS
- **Servidor Geográfico:**: Geoserver 2.5

## Instalación
Para ejecutar este proyecto localmente, sigue estos pasos:

### Requisitos
- **Frontend:**
  - Node.js y npm

- **Backend:**
  - Maven 3.8.6
  - Java 17 o superior

### Instalación del Frontend
1. Clona el repositorio
    ```bash
    git clone git@github.com:angeloti19/PROYECTO-TSIG.git tu-repositorio-local
    ```
2. Navega al directorio del frontend
    ```bash
    cd tu-repositorio-local/frontend
    ```
3. Instala las dependencias
    ```bash
    npm install
    ```
4. Inicia el servidor de desarrollo
    ```bash
    npm run dev
    ```

### Instalación del Backend
1. Navega al directorio del backend
    ```bash
    cd ../backend
    ```
2.  Inicia el servidor
    ```bash
    mvn spring-boot:run
    ```
    
### Configuración de Variables de Entorno
Asegúrate de configurar las variables de entorno necesarias tanto para el frontend como para el backend.

## Uso
Para utilizar la aplicación, abre tu navegador y navega a `http://localhost:8090`. Aquí encontrarás las siguientes funcionalidades:

- **Página de Inicio:** Búsqueda de vehículos disponibles
- **Panel de Administración:** Gestión de vehículos y usuarios (solo accesible para administradores)

## Capturas de Pantalla
A continuación, se presentan algunas capturas de pantalla del sistema:

### Anónimo - Solicitud de vehículos
![anonimo-solicitud](https://github.com/angeloti19/PROYECTO-TSIG/assets/111364909/b5de019d-ea5c-4d16-9772-c08679a40d3f)

### Anónimo - Búsqueda de vehículos dibujando polígono en el mapa
![poligono](https://github.com/angeloti19/PROYECTO-TSIG/assets/111364909/07638444-4bd2-4c32-a6e0-8b451d04a029)


### Panel de Administración
![admin-perfil](https://github.com/angeloti19/PROYECTO-TSIG/assets/111364909/99ae955c-c16e-49e7-9788-73d4d6553f02)

### Sección de consultas geográficas
![admin-consultas](https://github.com/angeloti19/PROYECTO-TSIG/assets/111364909/9dc0c85a-af53-43d7-9af1-b8d825d1e700)

