/*Clase Tarea que contiene los atributos de la tabla tareas en MySQL*/
package backend;

/**
 *
 * @author laura
 */
public class Tarea {
    private int id;
    private String nombre;
    private String descripcion;
    private boolean completada;

    // Constructor vacío (por si se necesita)
    public Tarea() {}

    // Constructor sin ID (útil para insertar)
    public Tarea(String nombre, String descripcion, boolean completada) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.completada = completada;
    }

    // Constructor con ID (útil para leer desde BD)
    public Tarea(int id, String nombre, String descripcion, boolean completada) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    @Override
    public String toString() {
        return "Tarea{" +
               "id=" + id +
               ", nombre='" + nombre + '\'' +
               ", descripcion='" + descripcion + '\'' +
               ", completada=" + completada +
               '}';
    }
}
