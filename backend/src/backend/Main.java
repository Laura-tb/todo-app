package backend;

/*Clase principal */
/**
 *
 * @author laura
 */
public class Main {

  public static void main(String[] args) {
    //Crea un objeto de la clase TareaDAO, que es tu clase de acceso a base de datos.
    TareaDAO dao = new TareaDAO();

    // Crea un nuevo objeto de tipo Tarea
    Tarea nueva = new Tarea("Estudiar HTML", "Repasar etiquetas semánticas", false);
    dao.guardarTarea(nueva);

    // Mostrar todas
    dao.obtenerTodas().forEach(System.out::println);
  }
}
