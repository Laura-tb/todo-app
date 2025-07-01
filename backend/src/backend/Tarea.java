/*Clase Tarea que contiene los atributos de la tabla tareas en MySQL*/
package backend;

/**
 * Clase que representa una tarea en el gestor de tareas. Contiene los atributos
 * básicos y sus métodos para acceder y modificar.
 *
 * @author laura
 */
public class Tarea {

  private int id; // Identificador único de la tarea en la base de datos
  private String nombre; // Nombre o título de la tarea
  private String description; // Descripción detallada de la tarea
  private boolean completada; // Estado que indica si la tarea está completada o no

  // Constructor vacío (por si se necesita)
  public Tarea() {
  }

  /**
   * Constructor para crear una tarea sin ID, útil para insertar nuevas tareas
   *
   * @param nombre Nombre o título de la tarea
   * @param descripcion Descripción de la tarea
   * @param completada Estado de completada (false al crear normalmente)
   */
  public Tarea(String nombre, String description, boolean completada) {
    this.nombre = nombre;
    this.description = description;
    this.completada = completada;
  }

  /**
   * Constructor completo con ID, útil para tareas leídas desde la base de datos
   *
   * @param id Identificador único de la tarea
   * @param nombre Nombre o título de la tarea
   * @param descripcion Descripción detallada de la tarea
   * @param completada Estado de completada
   */
  public Tarea(int id, String nombre, String description, boolean completada) {
    this.id = id;
    this.nombre = nombre;
    this.description = description;
    this.completada = completada;
  }

  // Getters y setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isCompletada() {
    return completada;
  }

  public void setCompletada(boolean completada) {
    this.completada = completada;
  }

  /**
   * Método para representar el objeto Tarea como String, útil para depuración
   *
   * @return String con la información de la tarea
   */
  @Override
  public String toString() {
    return "Tarea{"
            + "id=" + id
            + ", nombre='" + nombre + '\''
            + ", descripcion='" + description + '\''
            + ", completada=" + completada
            + '}';
  }
}
