package backend;

/**
 * Clase para gestionar la conexión a la base de datos MySQL
 *
 * @author laura Esta clase utiliza JDBC para conectar con la base de datos
 * 'todo_app'
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConexionBD {


  private static final String URL;
  private static final String USUARIO;
  private static final String CONTRASENA;

  static {
    String url = System.getenv("DB_URL");
    String user = System.getenv("DB_USER");
    String pass = System.getenv("DB_PASSWORD");

    if (url == null || user == null || pass == null) {
      Properties props = new Properties();
      try (InputStream in = ConexionBD.class.getClassLoader().getResourceAsStream("backend/db.properties")) {
        if (in != null) {
          props.load(in);
          if (url == null) {
            url = props.getProperty("db.url");
          }
          if (user == null) {
            user = props.getProperty("db.user");
          }
          if (pass == null) {
            pass = props.getProperty("db.password");
          }
        }
        /*System.out.println("DB from env? " + (System.getenv("DB_URL") != null));
        System.out.println("Cargando /backend/db.properties: " + (in != null));*/
      } catch (IOException e) {
        System.out.println("⚠️ No se pudo cargar db.properties: " + e.getMessage());
      }

    }

    // Si falta algo, lanzar error claro (mejor que exponer defaults personales)
    if (url == null || user == null || pass == null) {
      throw new IllegalStateException(
              "Config DB incompleta. Define DB_URL, DB_USER y DB_PASSWORD (env) o crea db.properties."
      );
    }

    URL = url;
    USUARIO = user;
    CONTRASENA = pass;
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
