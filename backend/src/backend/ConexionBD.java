package backend;

/**
 * Clase para gestionar la conexión a la base de datos MySQL
 * @author laura
 * Esta clase utiliza JDBC para conectar con la base de datos 'todo_app'
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

  // URL de conexión JDBC a la base de datos MySQL local 'todo_app' en el puerto 3306
  private static final String URL = "jdbc:mysql://localhost:3306/todo_app";
   // Usuario y contraseña para acceder a la base de datos
  private static final String USUARIO = "root";
  private static final String CONTRASENA = "root";

   /**
   * Método estático para obtener una conexión a la base de datos
   * @return Connection objeto que representa la conexión, o null si falla
   */
  
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
