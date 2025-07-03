package backend;

/**
 * Modelo que representa una fila de la tabla {@code tareas} en la base de
 * datos.
 *
 * <p>Incluye campos para el identificador, el nombre, una descripción opcional
 * y su estado de completada. Los métodos públicos permiten manipular y obtener
 * dichos valores.</p>
 *
 * @author laura
 */
public class Tarea {

  private int id; // Identificador único de la tarea en la base de datos
  private String nombre; // Nombre o título de la tarea
  private String description; // Descripción detallada de la tarea
  private boolean completada; // Estado que indica si la tarea está completada o no

  /**
   * Constructor vacío.
   *
   * <p>Útil cuando se crea una instancia que se rellenará mediante setters o al
   * deserializar desde JSON.</p>
   */
  public Tarea() {
  }

  /**
   * Constructor para crear una tarea sin ID, útil antes de insertarla en la
   * base de datos.
   *
   * @param nombre      nombre o título de la tarea
   * @param description descripción detallada de la tarea
   * @param completada  estado inicial de la tarea
   */
  public Tarea(String nombre, String description, boolean completada) {
    this.nombre = nombre;
    this.description = description;
    this.completada = completada;
  }

  /**
   * Constructor completo con ID, útil para tareas que provienen de la base de
   * datos.
   *
   * @param id          identificador único de la tarea
   * @param nombre      nombre o título
   * @param description descripción detallada
   * @param completada  estado actual
   */
  public Tarea(int id, String nombre, String description, boolean completada) {
    this.id = id;
    this.nombre = nombre;
    this.description = description;
    this.completada = completada;
  }

  // Getters y setters
  /**
   * Devuelve el identificador único de la tarea.
   */
  public int getId() {
    return id;
  }

  /**
   * Establece el identificador de la tarea.
   *
   * @param id nuevo identificador
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Obtiene el nombre o título de la tarea.
   *
   * @return texto descriptivo de la tarea
   */
  public String getNombre() {
    return nombre;
  }

  /**
   * Modifica el nombre de la tarea.
   *
   * @param nombre nuevo nombre
   */
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  /**
   * Devuelve la descripción detallada de la tarea.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Establece la descripción de la tarea.
   *
   * @param description texto descriptivo
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Indica si la tarea ya fue completada.
   */
  public boolean isCompletada() {
    return completada;
  }

  /**
   * Cambia el estado de la tarea.
   *
   * @param completada {@code true} si la tarea está finalizada
   */
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
