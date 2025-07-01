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

public class Main {

  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

    server.createContext("/tareas", new TareasHandler());
    server.setExecutor(null);
    System.out.println("Servidor iniciado en http://localhost:8000");
    server.start();
  }

  static class TareasHandler implements HttpHandler {

    private final TareaDAO tareaDAO = new TareaDAO();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
      try {
        String method = exchange.getRequestMethod();

        // MANEJAR PETICIÓN OPTIONS (preflight CORS)
        if (method.equalsIgnoreCase("OPTIONS")) {
          exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
          exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
          exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
          exchange.sendResponseHeaders(204, -1); // Sin contenido
          exchange.close();
          return;
        }

        // Agregar header CORS a TODAS las respuestas
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        //GET
        if (method.equalsIgnoreCase("GET")) {
          List<Tarea> tareas = tareaDAO.getAll();
          String response = gson.toJson(tareas);

          exchange.getResponseHeaders().add("Content-Type", "application/json");
          exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
          OutputStream os = exchange.getResponseBody();
          os.write(response.getBytes(StandardCharsets.UTF_8));
          os.close();

          //POST
        } else if (method.equalsIgnoreCase("POST")) {
          String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
          Tarea nuevaTarea = gson.fromJson(body, Tarea.class);

          if (nuevaTarea != null && nuevaTarea.getDescripcion() != null && !nuevaTarea.getDescripcion().isEmpty()) {
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

          //DELETE
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

          //PUT
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
