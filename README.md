# DOCUMENTACIÓN PROYECTO GESTOR DE TAREAS (TODO APP)

## Resumen de la Funcionalidad

Esta aplicación web permite gestionar una lista de tareas personales o laborales mediante las siguientes funcionalidades clave:

- **Listar tareas:** Carga y muestra todas las tareas almacenadas en la base de datos, separándolas en tareas pendientes y tareas completadas.
- **Añadir tareas:** Permite al usuario crear nuevas tareas ingresando una descripción (y opcionalmente detalles).
- **Eliminar tareas:** Opción para eliminar tareas existentes, actualizando la lista de forma inmediata.
- **Marcar tareas como completadas:** Cambiar el estado de una tarea para indicar que ya ha sido realizada.

La aplicación sigue un modelo cliente-servidor, donde el frontend consume una API REST que expone los datos y operaciones sobre las tareas.

---

## Tecnologías Utilizadas

### Backend
- Java 11+: Lenguaje principal para la lógica del servidor.
- `com.sun.net.httpserver.HttpServer`: Servidor HTTP embebido en Java para manejar peticiones.
- Gson: Biblioteca para convertir objetos Java a JSON y viceversa.
- MySQL: Base de datos relacional para almacenar las tareas.
- JDBC: API para conectarse y ejecutar consultas SQL sobre la base de datos.
- Gestión de CORS: Implementada manualmente en el servidor para permitir llamadas cross-origin desde el frontend.

### Frontend
- HTML5, CSS3 y JavaScript (Vanilla JS): Para construir la interfaz de usuario y lógica de interacción.
- Fetch API: Para hacer peticiones HTTP (GET, POST, DELETE, OPTIONS) al backend.
- Materialize CSS (o similar): Framework CSS para diseño responsivo y componentes UI (según clases CSS observadas).

---

## APIs y Endpoints

El backend expone la siguiente API REST para manejar las tareas:

> Todos los endpoints responden con JSON y gestionan adecuadamente las cabeceras CORS para permitir peticiones desde el frontend.

---

## Arquitectura y Flujo

1. **Frontend:** Renderiza la interfaz y responde a interacciones del usuario (crear, eliminar, marcar tareas).
2. **Fetch API:** El frontend envía peticiones al backend según la acción.
3. **Backend Java:** Procesa las peticiones, realiza operaciones SQL en MySQL mediante JDBC, y devuelve respuestas JSON.
4. **Base de Datos MySQL:** Guarda la información persistente de las tareas.

---

## Descripción del Proyecto

Proyecto web completo sin frameworks avanzados como Spring o Servlets, solo con Java “puro”, HTML/CSS, JavaScript y MySQL. Usamos Material Design para el front (con Materialize o Material Design Lite).

- **Proyecto:** Gestor de Tareas (ToDo App)  
- **Tecnologías a usar:**  
  - Frontend: HTML, CSS, JS, Materialize CSS  
  - Backend: Java (con consola o programa simple tipo servidor)  
  - Base de datos: MySQL  
  - Versionado: Git + GitHub  

### Estructura general del proyecto (carpetas)


---

## PASO A PASO

1. **Crea tu repositorio Git:** Utilizo Git Bash.  
2. **Diseña la base de datos** (`db/schema.sql`). Utilizo MySQL Workbench.  
   - Base de datos: `todo_app`  
   - Tabla: `tareas`  
3. **Frontend con HTML + CSS + JS (usando Materialize):** Utilizo Visual Studio Code.  
4. **Backend simple en Java:** Utilizo NetBeans IDE 18.  

Haz una pequeña aplicación Java que:  
- Lea tareas desde la base de datos  
- Inserte nuevas tareas (opcional)  
- Exporte datos a un archivo o interfaz de consola  

Clases creadas:  
- `Main.java`  
- `ConexionBD.java`  
- `Tarea.java`  
- `TareaDAO.java`  

Estoy usando Gson para convertir objetos Java <-> JSON.

5. **Testea por separado:**  
   - Primero: prueba solo el frontend con JS (sin backend).  
   - Luego: ejecuta el backend en consola y verifica inserciones en MySQL.

6. **Conectar el JS al backend usando una API REST en Java**  
   Enfoque: API REST simple con Java + `HttpServer`.  
   Para no usar frameworks como Spring o Servlets, usamos `com.sun.net.httpserver.HttpServer` para crear un servidor HTTP simple.

---

## CÓMO PROBAR LA APLICACIÓN COMPLETA PASO A PASO

1. **Asegúrate de que el backend esté corriendo**  
   - Ejecuta tu clase `Main.java` en NetBeans.  
   - Verifica en la consola que el servidor HTTP se haya iniciado (debería mostrar que la API REST está escuchando).

2. **Abre tu frontend HTML en el navegador**  
   - Abre `index.html` desde la carpeta frontend (doble clic o desde el IDE).  
   - Para evitar problemas de CORS, lo ideal es:  
     - Servir tu frontend desde un servidor local (ej. Live Server en VSCode), o  
     - Abrir el navegador con configuración que permita CORS en modo desarrollo, o  
     - Abrir el frontend también con un servidor local (`netbeans`, `python -m http.server`, etc).  
   - Si abres directo el archivo HTML con `file://`, algunas funciones fetch pueden fallar.

3. **Probar la carga inicial de tareas**  
   - Al abrir el frontend, debería ejecutarse el método `cargarTareas()` que hace una petición GET a `http://localhost:8000/tareas`.  
   - Si el backend está bien y la DB tiene datos, deberías ver listadas las tareas.

4. **Probar añadir una nueva tarea**  
   - Escribe una tarea en el formulario y presiona el botón "Añadir tarea".  
   - El fetch hace un POST a la API REST enviando la nueva tarea.  
   - Si todo funciona, la tarea se insertará en la base de datos y la lista se actualizará automáticamente.

5. **Confirmar que la tarea fue añadida**  
   - Puedes volver a cargar la página para asegurarte que la tarea sigue ahí (la lee de la base de datos).  
   - O usa un cliente MySQL para consultar directamente la tabla tareas:  
     ```sql
     SELECT * FROM tareas;
     ```

6. **Depurar posibles errores**  
   - Si no ves las tareas cargando o no se añade la tarea, abre la consola del navegador (F12 > Consola) para ver si hay errores JavaScript o de red (fetch).  
   - En el backend, revisa la consola donde ejecutas Java para ver si aparece algún error o excepción.

---

## ERRORES DETECTADOS

### Cómo habilitar CORS en tu backend Java simple

Como estás usando un servidor básico hecho en Java (probablemente con `HttpServer` o similar), debes agregar los headers CORS en las respuestas para que el navegador permita las peticiones.

Ejemplo básico para agregar CORS en tu manejador de `/tareas` (en el método `handle(HttpExchange exchange)` agrega estas líneas justo antes de enviar la respuesta):

```java
// Permitir cualquier origen (para desarrollo)
exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
