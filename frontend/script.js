document.getElementById('formulario-tarea').addEventListener('submit', function (e) {
    e.preventDefault();
    const descripcion = document.getElementById('title').value.trim();
    const detalle = document.getElementById('description').value.trim();

    if (descripcion) {
        fetch('http://localhost:8000/tareas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ descripcion: descripcion /*, puedes enviar detalle si quieres*/ })
        })
            .then(response => {
                if (response.ok) {
                    cargarTareas(); // recarga la lista
                    document.getElementById('title').value = '';
                    document.getElementById('description').value = '';
                } else {
                    alert('Error al añadir tarea');
                }
            })
            .catch(() => alert('Error en la conexión con el servidor'));
    }
});

// Función para cargar tareas y distribuir en las dos columnas
function cargarTareas() {
    fetch('http://localhost:8000/tareas')
        .then(res => res.json())
        .then(tareas => {
            const todoContainer = document.getElementById('tareas-todo-container');
            const doneContainer = document.getElementById('tareas-done-container');

            todoContainer.innerHTML = '';
            doneContainer.innerHTML = '';

            tareas.forEach(tarea => {
                // Crear tarjeta para cada tarea
                const card = document.createElement('div');
                card.className = 'card blue-grey darken-1';

                const cardContent = document.createElement('div');
                cardContent.className = 'card-content white-text';

                const title = document.createElement('span');
                title.className = 'card-title';
                title.textContent = tarea.descripcion;

                cardContent.appendChild(title);
                // Si quieres mostrar detalle, lo agregas aquí, ej:
                // const p = document.createElement('p');
                // p.textContent = tarea.detalle;
                // cardContent.appendChild(p);

                card.appendChild(cardContent);

                // Acciones: completar o eliminar
                const cardAction = document.createElement('div');
                cardAction.className = 'card-action';

                if (!tarea.completada) {
                    const btnCompletar = document.createElement('button');
                    btnCompletar.className = 'btn waves-effect waves-light';
                    btnCompletar.textContent = 'Completada';
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

                const btnEliminar = document.createElement('button');
                btnEliminar.className = 'btn waves-effect waves-light red';
                btnEliminar.textContent = 'Eliminar';
                btnEliminar.onclick = () => {
                    if (confirm('¿Estás seguro de que quieres eliminar esta tarea?')) {
                        eliminarTarea(tarea.id);
                    }
                };
                cardAction.appendChild(btnEliminar);

                card.appendChild(cardAction);

                // Añadir tarjeta al contenedor adecuado
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

// Cargar tareas al cargar la página
cargarTareas();
