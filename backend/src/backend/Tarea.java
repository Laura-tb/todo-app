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
  private String estado; // Estado que indica si la tarea está todo, inprogress o done.

  // Constructor vacío (por si se necesita)
  public Tarea() {
  }

  /**
   * Constructor para crear una tarea sin ID, útil para insertar nuevas tareas
   *
   * @param nombre Nombre o título de la tarea
   * @param descripcion Descripción de la tarea
   * @param completada Estado de completada (false al crear normalmente)
   * @param estado Estado de la tarea (todo al crear normalmente)
   */
  public Tarea(String nombre, String description, boolean completada, String estado) {
    this.nombre = nombre;
    this.description = description;
    this.completada = completada;
    this.estado = estado;
  }

  /**
   * Constructor completo con ID, útil para tareas leídas desde la base de datos
   *
   * @param id Identificador único de la tarea
   * @param nombre Nombre o título de la tarea
   * @param descripcion Descripción detallada de la tarea
   * @param completada Estado de completada
   * @param estado Estado de la tarea (todo al crear normalmente)
   */
  public Tarea(int id, String nombre, String description, boolean completada, String estado) {
    this.id = id;
    this.nombre = nombre;
    this.description = description;
    this.completada = completada;
    this.estado = estado;
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

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
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
            + ", nombre=" + nombre
            + ", description=" + description
            + ", completada=" + completada
            + ", estado=" + estado + '}';
  }

}
