package backend;

/**
 * Punto de entrada del backend de la aplicación de tareas.
 *
 * <p>Esta clase inicia un {@link com.sun.net.httpserver.HttpServer} en el
 * puerto {@code 8000} y registra el manejador que expone la API REST para las
 * tareas. Una vez iniciado, el servidor permanece escuchando peticiones hasta
 * que el proceso es detenido.</p>
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

public class Main {

  /**
   * Arranca el servidor HTTP de la aplicación.
   *
   * @param args no se utilizan, pero se mantienen para compatibilidad con la
   *             firma estándar de {@code main}
   * @throws IOException si ocurre un problema al iniciar el servidor
   */
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
   * Manejador HTTP para el endpoint {@code /tareas}.
   *
   * <p>Responde a peticiones GET, POST, DELETE y PUT empleando un
   * {@link TareaDAO} para acceder a la base de datos. También atiende a la
   * petición OPTIONS necesaria para CORS.</p>
   */
  static class TareasHandler implements HttpHandler {

    private final TareaDAO tareaDAO = new TareaDAO(); // DAO para operaciones BD
    private final Gson gson = new Gson(); // Gson para JSON

    @Override
    /**
     * Procesa una petición HTTP entrante.
     *
     * <p>Dependiendo del método de la petición se ejecutarán distintas
     * operaciones sobre las tareas almacenadas. Todas las respuestas incluyen las
     * cabeceras CORS necesarias.</p>
     *
     * @param exchange contexto de la petición proporcionado por el servidor
     * @throws IOException si ocurre algún problema al leer o escribir la
     *                     respuesta
     */
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
            tareaDAO.marcarComoCompletada(id);
            String resp = "{\"mensaje\":\"Tarea marcada como completada\"}";
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(resp.getBytes(StandardCharsets.UTF_8));
            os.close();
          } else {
            exchange.sendResponseHeaders(400, -1);
          }

          // Para métodos no permitidos, responder con 405 Method Not Allowed
        } else {
          exchange.sendResponseHeaders(405, -1);
        }
      } catch (Exception e) {
        e.printStackTrace();
        exchange.sendResponseHeaders(500, -1);
      }
    }
  }
}
