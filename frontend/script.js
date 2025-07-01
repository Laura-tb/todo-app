//// Escucha el evento submit del formulario para añadir una nueva tarea
document.getElementById('formulario-tarea').addEventListener('submit', function (e) {
    e.preventDefault(); // Evita que el formulario se envíe de forma tradicional (recarga de página)

     // Obtener valores de los campos de texto y eliminar espacios al inicio y final
    const descripcion = document.getElementById('title').value.trim();
    const detalle = document.getElementById('description').value.trim();

    //Conectar el frontend con fetch
    // Solo procede si la descripción no está vacía
    if (descripcion) {
        // Enviar una petición POST al backend para crear una nueva tarea
        fetch('http://localhost:8000/tareas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ descripcion: descripcion /*, puedes enviar detalle si quieres*/ })
        })
            .then(response => {
                if (response.ok) {
                    cargarTareas(); // recarga la lista
                    // Limpiar campos del formulario
                    document.getElementById('title').value = '';
                    document.getElementById('description').value = '';
                } else {
                    alert('Error al añadir tarea');
                }
            })
            .catch(() => alert('Error en la conexión con el servidor'));
    }
});

// Función para obtener las tareas del backend y mostrarlas en las columnas TO DO y DONE
function cargarTareas() {
    fetch('http://localhost:8000/tareas')
        .then(res => res.json()) // Parsear la respuesta JSON
        .then(tareas => {
            // Obtener los contenedores donde se mostrarán las tareas pendientes y completadas
            const todoContainer = document.getElementById('tareas-todo-container');
            const doneContainer = document.getElementById('tareas-done-container');

            // Limpiar contenido previo
            todoContainer.innerHTML = '';
            doneContainer.innerHTML = '';

             // Recorrer todas las tareas recibidas para crear sus tarjetas
            tareas.forEach(tarea => {
                // Crear contenedor principal de la tarjeta
                const card = document.createElement('div');
                card.className = 'card blue-grey darken-1';

                 // Crear sección de contenido de la tarjeta
                const cardContent = document.createElement('div');
                cardContent.className = 'card-content white-text';

                // Crear y asignar título con la descripción de la tarea
                const title = document.createElement('span');
                title.className = 'card-title';
                title.textContent = tarea.descripcion;

                 // Agregar título al contenido de la tarjeta
                cardContent.appendChild(title);

                // Si quieres mostrar detalle, lo agregas aquí, ej:
                // const p = document.createElement('p');
                // p.textContent = tarea.detalle;
                // cardContent.appendChild(p);

                // Añadir contenido a la tarjeta
                card.appendChild(cardContent);

                // Crear sección para botones de acción (completar, eliminar)
                const cardAction = document.createElement('div');
                cardAction.className = 'card-action';

                // Si la tarea NO está completada, añadir botón para marcarla como completada
                if (!tarea.completada) {
                    const btnCompletar = document.createElement('button');
                    btnCompletar.className = 'btn waves-effect waves-light';
                    btnCompletar.textContent = 'Completada';

                    // Evento click para marcar la tarea como completada mediante petición PUT
                    btnCompletar.onclick = () => {
                        fetch(`http://localhost:8000/tareas?id=${tarea.id}`, {
                            method: 'PUT'
                        })
                            .then(response => {
                                if (response.ok) {
                                    cargarTareas(); // Actualiza la vista
                                } else {
                                    alert('Error al completar la tarea');
                                }
                            })
                            .catch(() => alert('Error en la conexión al marcar como completada'));
                    };
                    cardAction.appendChild(btnCompletar);
                }

                // Botón para eliminar la tarea
                const btnEliminar = document.createElement('button');
                btnEliminar.className = 'btn waves-effect waves-light red';
                btnEliminar.textContent = 'Eliminar';
                 // Confirmar antes de eliminar, y llamar a función eliminarTarea si acepta
                btnEliminar.onclick = () => {
                    if (confirm('¿Estás seguro de que quieres eliminar esta tarea?')) {
                        eliminarTarea(tarea.id);
                    }
                };
                cardAction.appendChild(btnEliminar);

                 // Añadir la sección de acciones a la tarjeta
                card.appendChild(cardAction);

                // Añadir la tarjeta al contenedor correcto según su estado (completada o no)
                if (tarea.completada) {
                    doneContainer.appendChild(card);
                } else {
                    todoContainer.appendChild(card);
                }
            });
        })
        .catch(() => {
            alert('Error al cargar las tareas desde el servidor');
        });
}
// Función para eliminar una tarea enviando petición DELETE al backend
function eliminarTarea(id) {
    fetch(`http://localhost:8000/tareas?id=${id}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                cargarTareas(); // Actualizar la lista
            } else {
                alert('Error al eliminar tarea');
            }
        });
}

// Al cargar la página, cargar y mostrar todas las tareas existentes
cargarTareas();
