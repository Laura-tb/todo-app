package backend;

/**
 * Clase para gestionar la conexión a la base de datos MySQL
 * @author laura
 * Esta clase utiliza JDBC para conectar con la base de datos 'todo_app'
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConexionBD {

  private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/todo_app";
  private static final String DEFAULT_USER = "root";
  private static final String DEFAULT_PASSWORD = "root";

  private static final String URL;
  private static final String USUARIO;
  private static final String CONTRASENA;

  static {
    String url = System.getenv("DB_URL");
    String user = System.getenv("DB_USER");
    String pass = System.getenv("DB_PASSWORD");

    if (url == null || user == null || pass == null) {
      Properties props = new Properties();
      try (InputStream in = ConexionBD.class.getClassLoader().getResourceAsStream("db.properties")) {
        if (in != null) {
          props.load(in);
          if (url == null) url = props.getProperty("db.url");
          if (user == null) user = props.getProperty("db.user");
          if (pass == null) pass = props.getProperty("db.password");
        }
      } catch (IOException e) {
        System.out.println("⚠️ No se pudo cargar db.properties: " + e.getMessage());
      }
    }

    URL = url != null ? url : DEFAULT_URL;
    USUARIO = user != null ? user : DEFAULT_USER;
    CONTRASENA = pass != null ? pass : DEFAULT_PASSWORD;
  }

  /**
   * Obtiene una conexión a la base de datos.
   *
   * @return Connection objeto que representa la conexión
   * @throws SQLException si ocurre un problema al conectar
   */
  public static Connection conectar() throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    System.out.println("✅ Conexión establecida con la base de datos.");
    return conn;
  }

}
