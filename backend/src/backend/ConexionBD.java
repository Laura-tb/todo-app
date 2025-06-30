/*Crea la clase ConexionBD.java*/
package backend;

/**
 *
 * @author laura
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

  private static final String URL = "jdbc:mysql://localhost:3306/todo_app";
  private static final String USUARIO = "root";
  private static final String CONTRASENA = "root";

  public static Connection conectar() {
    try {
      Connection conn = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
      System.out.println("✅ Conexión establecida con la base de datos.");
      return conn;
    } catch (SQLException e) {
      System.out.println("❌ Error al conectar: " + e.getMessage());
      return null;
    }
  }

}
