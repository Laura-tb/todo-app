/* Clase principal del servidor HTTP para la aplicación de tareas */
package backend;

/*Clase principal */
/**
 *
 * @author laura
 */
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Main {

  public static void main(String[] args) throws IOException {
    // Crear servidor HTTP escuchando en el puerto 8000, backlog 0 (por defecto)
    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

    // Registrar contexto /tareas y asignar manejador de peticiones
    server.createContext("/tareas", new TareasHandler());

    // Usar el executor por defecto (null)
    server.setExecutor(null);
    System.out.println("Servidor iniciado en http://localhost:8000");

    // Iniciar el servidor
    server.start();
  }

  /**
   * Clase interna que maneja las peticiones HTTP para /tareas
   */
  static class TareasHandler implements HttpHandler {

    private final TareaDAO tareaDAO = new TareaDAO(); // DAO para operaciones BD
    private final Gson gson = new Gson(); // Gson para JSON

    @Override
    public void handle(HttpExchange exchange) throws IOException {
      try {
        String method = exchange.getRequestMethod();

        // MANEJAR PETICIÓN OPTIONS (preflight CORS)
        if (method.equalsIgnoreCase("OPTIONS")) {
          // Añadir headers CORS para permitir peticiones desde cualquier origen
          exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
          exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
          exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

          // Responder sin contenido (204)
          exchange.sendResponseHeaders(204, -1); // Sin contenido
          exchange.close();
          return;
        }

        // Agregar header CORS a TODAS las respuestas
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        // MANEJO MÉTODO GET: devolver lista de tareas
        if (method.equalsIgnoreCase("GET")) {
          List<Tarea> tareas = tareaDAO.getAll();
          String response = gson.toJson(tareas);

          exchange.getResponseHeaders().add("Content-Type", "application/json");
          exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
          OutputStream os = exchange.getResponseBody();
          os.write(response.getBytes(StandardCharsets.UTF_8));
          os.close();

          // MANEJO MÉTODO POST: añadir nueva tarea
        } else if (method.equalsIgnoreCase("POST")) {
          String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
          Tarea nuevaTarea = gson.fromJson(body, Tarea.class);

          if (nuevaTarea != null && nuevaTarea.getNombre() != null && !nuevaTarea.getNombre().isEmpty()) {
            tareaDAO.insert(nuevaTarea);
            String resp = "{\"mensaje\":\"Tarea añadida\"}";
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(201, resp.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(resp.getBytes(StandardCharsets.UTF_8));
            os.close();
          } else {
            exchange.sendResponseHeaders(400, -1);
          }

          // MANEJO MÉTODO DELETE: eliminar tarea por ID
        } else if (method.equalsIgnoreCase("DELETE")) {
          String query = exchange.getRequestURI().getQuery();
          if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            tareaDAO.delete(id);
            String resp = "{\"mensaje\":\"Tarea eliminada\"}";
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(resp.getBytes(StandardCharsets.UTF_8));
            os.close();
          } else {
            exchange.sendResponseHeaders(400, -1);
          }

          // MANEJO MÉTODO PUT: marcar tarea como completada
        } else if (method.equalsIgnoreCase("PUT")) {
          String query = exchange.getRequestURI().getQuery();
          int id = -1;

          if (query != null && query.startsWith("id=")) {
            try {
              id = Integer.parseInt(query.substring(3));
            } catch (NumberFormatException e) {
              exchange.sendResponseHeaders(400, -1);
              return;
            }
          }

          if (id != -1) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            try {
              JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
              boolean hizoCambio = false;

              // Solo actualiza si viene el campo
              if (jsonObject.has("nombre")) {
                String nombre = jsonObject.get("nombre").getAsString();
                tareaDAO.actualizarNombre(id, nombre);
                hizoCambio = true;
              }

              if (jsonObject.has("description")) {
                String desc = jsonObject.get("description").getAsString();
                tareaDAO.actualizarDescripcion(id, desc);
                hizoCambio = true;
              }

              if (jsonObject.has("estado")) {
                String estado = jsonObject.get("estado").getAsString();
                tareaDAO.actualizarEstado(id, estado);
                hizoCambio = true;
              }

              if (hizoCambio) {
                String resp = "{\"mensaje\":\"Tarea actualizada\"}";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
                OutputStream os = exchange.getResponseBody();
                os.write(resp.getBytes(StandardCharsets.UTF_8));
                os.close();
              } else {
                exchange.sendResponseHeaders(400, -1); // No se actualizó nada
              }
            } catch (Exception e) {
              exchange.sendResponseHeaders(400, -1);
            }
          } else {
            exchange.sendResponseHeaders(400, -1);
          }
        }

      } catch (Exception e) {
        e.printStackTrace();
        exchange.sendResponseHeaders(500, -1);
      }

    }
  }
}
