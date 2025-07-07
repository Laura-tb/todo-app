//// Escucha el evento submit del formulario para añadir una nueva tarea
document.getElementById('formulario-tarea').addEventListener('submit', function (e) {
    e.preventDefault(); // Evita que el formulario se envíe de forma tradicional (recarga de página)

    // Obtener valores de los campos de texto y eliminar espacios al inicio y final
    const nombre = document.getElementById('nombre').value.trim();
    const description = document.getElementById('description').value.trim();

    //Conectar el frontend con fetch
    // Solo procede si el nombre no está vacío
    if (nombre) {
        // Enviar una petición POST al backend para crear una nueva tarea

        fetch('http://localhost:8000/tareas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nombre, description, estado: 'todo' })
        })
            .then(response => {
                console.log(response);
                if (response.ok) {
                    cargarTareas(); // recarga la lista
                    M.toast({ html: 'Tarea añadida', classes: 'rounded green lighten-1' });
                    // Limpiar campos del formulario
                    document.getElementById('nombre').value = '';
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
        .then(res => {
            if (!res.ok) {
                throw new Error('Respuesta no válida del servidor');
            }
            return res.json(); // Parsear la respuesta JSON
        })
        .then(tareas => {
            // Obtener los contenedores donde se mostrarán las tareas pendientes y completadas
            const todoContainer = document.getElementById('tareas-todo-container');
            const inprogressContainer = document.getElementById('tareas-inprogress-container');
            const doneContainer = document.getElementById('tareas-done-container');

            // Limpiar contenido previo
            todoContainer.innerHTML = '';
            inprogressContainer.innerHTML = '';
            doneContainer.innerHTML = '';

            // Recorrer todas las tareas recibidas para crear sus tarjetas
            tareas.forEach(tarea => {
                // Crear contenedor principal de la tarjeta
                const card = document.createElement('div');
                card.className = 'card blue-grey darken-1';
                card.setAttribute('data-id', tarea.id); //Drag & Drop

                // Crear sección de contenido de la tarjeta
                const cardContent = document.createElement('div');
                cardContent.className = 'card-content white-text';

                // Crear y asignar título con la descripción de la tarea
                const nombre = document.createElement('span');
                nombre.className = 'card-title';
                nombre.textContent = tarea.nombre;

                // Agregar título al contenido de la tarjeta
                cardContent.appendChild(nombre);

                // Si quieres mostrar detalle, lo agregas aquí, ej:
                const p = document.createElement('p');
                p.textContent = tarea.description;
                cardContent.appendChild(p);

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
                    btnCompletar.onclick = () => moverTareaAEstado(tarea.id, 'done');
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

                // Añadir la tarjeta al contenedor correcto según su estado (completada, en progreso o no completada)
                if (tarea.estado === 'todo') {
                    todoContainer.appendChild(card);
                } else if (tarea.estado === 'inprogress') {
                    inprogressContainer.appendChild(card);
                } else if (tarea.estado === 'done') {
                    doneContainer.appendChild(card);
                }
            });
            inicializarDragAndDrop(); // Activar drag & drop después de cargar tareas
        })
        .catch(error => {
            console.error('Error al cargar las tareas:', error);
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
                M.toast({ html: 'Tarea eliminada', classes: 'rounded red lighten-1' });
            } else {
                alert('Error al eliminar tarea');
            }
        });
}


//Funcion moverTarea
function moverTareaAEstado(id, nuevoEstado) {
    fetch(`http://localhost:8000/tareas?id=${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ estado: nuevoEstado })
    })
        .then(response => {
            if (response.ok) {
                cargarTareas();
            } else {
                alert('Error al actualizar estado de la tarea');
            }
        })
        .catch(() => alert('Error en la conexión con el servidor'));
}

// Drag & Drop con SortableJS
function inicializarDragAndDrop() {
    Sortable.create(document.getElementById('tareas-todo-container'), {
        group: 'tareas',
        animation: 150,
        onAdd: function (evt) {
            const tareaElement = evt.item;
            const idTarea = tareaElement.getAttribute('data-id');
            if (idTarea) {
                moverTareaAEstado(idTarea, 'todo');
            }
        }
    });

    Sortable.create(document.getElementById('tareas-inprogress-container'), {
        group: 'tareas',
        animation: 150,
        onAdd: function (evt) {
            const tareaElement = evt.item;
            const idTarea = tareaElement.getAttribute('data-id');
            if (idTarea) {
                moverTareaAEstado(idTarea, 'inprogress');
            }
        }
    });

    Sortable.create(document.getElementById('tareas-done-container'), {
        group: 'tareas',
        animation: 150,
        onAdd: function (evt) {
            const tareaElement = evt.item;
            const idTarea = tareaElement.getAttribute('data-id');
            if (idTarea) {
                moverTareaAEstado(idTarea, 'done');
            }
        }
    });
}

// Al cargar la página, cargar y mostrar todas las tareas existentes
cargarTareas();
