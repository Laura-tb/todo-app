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
 * Clase DAO (Data Access Object) para gestionar operaciones CRUD sobre la tabla
 * `tareas` en la base de datos.
 *
 * Proporciona métodos para obtener, insertar, eliminar y actualizar tareas.
 *
 * @author laura
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TareaDAO {

  /**
   * Obtiene todas las tareas almacenadas en la base de datos.
   *
   * @return Lista con todas las tareas encontradas
   * @throws SQLException Si ocurre un error en la consulta o conexión
   */
  public List<Tarea> getAll() throws SQLException {
    List<Tarea> tareas = new ArrayList<>();
    try (Connection conn = ConexionBD.conectar()) {
      String sql = "SELECT * FROM tareas";
      try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
          Tarea t = new Tarea();
          t.setId(rs.getInt("id"));
          t.setNombre(rs.getString("nombre"));
          t.setDescription(rs.getString("description"));
          t.setCompletada(rs.getBoolean("completada"));
          tareas.add(t); // Añadir tarea a la lista
        }
      }
    }
    return tareas;
  }

  /**
   * Inserta una nueva tarea en la base de datos.
   *
   * @param tarea Objeto Tarea con la información a insertar
   * @throws SQLException Si ocurre un error en la inserción o conexión
   */
  public void insert(Tarea tarea) throws SQLException {
    try (Connection conn = ConexionBD.conectar()) {
      String sql = "INSERT INTO tareas (nombre, description) VALUES (?, ?)";
      try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, tarea.getNombre());
        ps.setString(2, tarea.getDescription());
        ps.executeUpdate();
      }
    }
  }

  /**
   * Elimina una tarea de la base de datos según su ID.
   *
   * @param id Identificador de la tarea a eliminar
   * @throws SQLException Si ocurre un error en la eliminación o conexión
   */
  public void delete(int id) throws SQLException {
    try (Connection conn = ConexionBD.conectar()) {
      String sql = "DELETE FROM tareas WHERE id = ?";
      try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, id);
        ps.executeUpdate();
      }
    }
  }

  /**
   * Marca una tarea como completada (completada = TRUE) según su ID.
   *
   * @param id Identificador de la tarea a actualizar
   * @throws SQLException Si ocurre un error en la actualización o conexión
   */
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
