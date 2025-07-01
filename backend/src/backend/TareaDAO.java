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

  public List<Tarea> getAll() throws SQLException {
    List<Tarea> tareas = new ArrayList<>();
    try (Connection conn = ConexionBD.conectar()) {
      String sql = "SELECT * FROM tareas";
      try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
          Tarea t = new Tarea();
          t.setId(rs.getInt("id"));
          t.setDescripcion(rs.getString("descripcion"));
          t.setCompletada(rs.getBoolean("completada"));
          tareas.add(t);
        }
      }
    }
    return tareas;
  }

  public void insert(Tarea tarea) throws SQLException {
    try (Connection conn = ConexionBD.conectar()) {
      String sql = "INSERT INTO tareas (descripcion) VALUES (?)";
      try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, tarea.getDescripcion());
        ps.executeUpdate();
      }
    }
  }

  public void delete(int id) throws SQLException {
    try (Connection conn = ConexionBD.conectar()) {
      String sql = "DELETE FROM tareas WHERE id = ?";
      try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, id);
        ps.executeUpdate();
      }
    }
  }

  public void marcarComoCompletada(int id) throws SQLException {
    try (Connection conn = ConexionBD.conectar()) {
      String sql = "UPDATE tareas SET completada = TRUE WHERE id = ?";
      try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, id);
        ps.executeUpdate();
      }
    }
  }

}
