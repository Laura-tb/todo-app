/* Clase TareaDAO.java, que se encargará de:
  Insertar nuevas tareas
  Consultar todas las tareas
  Marcar una tarea como completada
  Eliminar una tarea

Contendrá métodos para:
  guardarTarea(Tarea tarea) → insertar nueva tarea
  obtenerTodas() → devolver lista de tareas
  marcarComoCompletada(int id)
  eliminarTarea(int id)
 */
package backend;

/**
 *
 * @author laura
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TareaDAO {

  //insertar una nueva fila en la tabla tareas
  public boolean guardarTarea(Tarea tarea) {
    String sql = "INSERT INTO tareas (nombre, descripcion, completada) VALUES (?, ?, ?)";

    try (Connection conn = ConexionBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, tarea.getNombre());
      stmt.setString(2, tarea.getDescripcion());
      stmt.setBoolean(3, tarea.isCompletada());

      stmt.executeUpdate();
      return true;

    } catch (SQLException e) {
      System.out.println("❌ Error al guardar tarea: " + e.getMessage());
      return false;
    }
  }

  // Consultar todas las tareas
  public List<Tarea> obtenerTodas() {
    List<Tarea> lista = new ArrayList<>();
    String sql = "SELECT * FROM tareas";

    try (Connection conn = ConexionBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        Tarea t = new Tarea(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getBoolean("completada")
        );
        lista.add(t);
      }

    } catch (SQLException e) {
      System.out.println("❌ Error al obtener tareas: " + e.getMessage());
    }

    return lista;
  }

  // Marcar una tarea como completada
  public boolean marcarComoCompletada(int id) {
    String sql = "UPDATE tareas SET completada = 1 WHERE id = ?";

    try (Connection conn = ConexionBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, id);
      int filas = stmt.executeUpdate();
      return filas > 0;

    } catch (SQLException e) {
      System.out.println("❌ Error al marcar tarea como completada: " + e.getMessage());
      return false;
    }
  }

  //Eliminar una tarea
  public boolean eliminarTarea(int id) {
    String sql = "DELETE FROM tareas WHERE id = ?";

    try (Connection conn = ConexionBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, id);
      int filas = stmt.executeUpdate();
      return filas > 0;

    } catch (SQLException e) {
      System.out.println("❌ Error al eliminar tarea: " + e.getMessage());
      return false;
    }
  }

}
