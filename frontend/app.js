// ===== Config =====
const API = 'http://localhost:8000/tareas';

// ===== Util =====
const $ = (s) => document.querySelector(s);

// Contenedores
const todoContainer = $('#tareas-todo-container');
const inprogressContainer = $('#tareas-inprogress-container');
const doneContainer = $('#tareas-done-container');

// Contadores (si existen en tu HTML; si no, no pasa nada)
const $ctTodo = document.getElementById('cnt-todo');
const $ctProg = document.getElementById('cnt-progress');
const $ctDone = document.getElementById('cnt-done');

// ===== Render de tarjeta =====
function cardTemplate(tarea) {
    const estado = (tarea.estado || 'todo').toLowerCase();
    const chipClass =
        estado === 'done' ? 'green lighten-5 green-text text-darken-3'
            : estado === 'inprogress' ? 'amber lighten-5 orange-text text-darken-3'
                : 'light-blue lighten-5 cyan-text text-darken-3';

    // Card Materialize mejorada (compatible con tu lógica)
    const card = document.createElement('div');
    card.className = 'card task-card';
    card.setAttribute('data-id', tarea.id);

    card.innerHTML = `
    <div class="card-content">
      <span class="task-title">${tarea.nombre ?? ''}</span>
      <p class="task-desc">${tarea.description ?? ''}</p>
      <div class="chip ${chipClass}" style="margin-top:6px">${estado}</div>
    </div>
    <div class="card-action right-align">
      <a class="waves-effect waves-light btn btn-icon btn-edit" data-action="edit">Editar</a>
      <a class="waves-effect waves-light btn btn-icon btn-del"  data-action="del">Eliminar</a>
    </div>
  `;

    // Acciones
    card.querySelector('[data-action="edit"]').onclick = () => abrirModalEdicion(tarea);
    card.querySelector('[data-action="del"]').onclick = () => {
        if (confirm('¿Estás seguro de que quieres eliminar esta tarea?')) eliminarTarea(tarea.id);
    };

    return card;
}

// ===== Cargar tareas =====
async function cargarTareas() {
    try {
        const res = await fetch(API);
        if (!res.ok) throw new Error('Respuesta no válida del servidor');
        const tareas = await res.json();

        // Limpia columnas
        todoContainer.innerHTML = '';
        inprogressContainer.innerHTML = '';
        doneContainer.innerHTML = '';

        // Pinta
        const todo = [], prog = [], done = [];
        tareas.forEach(t => {
            const estado = (t.estado || 'todo').toLowerCase();
            const card = cardTemplate(t);
            if (estado === 'todo') { todoContainer.appendChild(card); todo.push(1); }
            else if (estado === 'inprogress') { inprogressContainer.appendChild(card); prog.push(1); }
            else if (estado === 'done') { doneContainer.appendChild(card); done.push(1); }
            else { todoContainer.appendChild(card); todo.push(1); } // fallback
        });

        // Contadores si existen
        if ($ctTodo) $ctTodo.textContent = todo.length;
        if ($ctProg) $ctProg.textContent = prog.length;
        if ($ctDone) $ctDone.textContent = done.length;

        inicializarDragAndDrop(); // activar DnD tras pintar
    } catch (e) {
        console.error('Error al cargar las tareas:', e);
        alert('Error al cargar las tareas desde el servidor');
    }
}

// ===== Crear tarea (submit formulario) =====
document.getElementById('formulario-tarea').addEventListener('submit', async (e) => {
    e.preventDefault();
    const nombre = document.getElementById('nombre').value.trim();
    const description = document.getElementById('description').value.trim();
    if (!nombre) {
        M.toast({ html: 'Añade un nombre', classes: 'rounded red lighten-1' });
        return;
    }
    try {
        const r = await fetch(API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nombre, description, estado: 'todo' })
        });
        if (!r.ok) throw new Error('Error al añadir tarea');
        document.getElementById('nombre').value = '';
        document.getElementById('description').value = '';
        M.updateTextFields();
        M.toast({ html: 'Tarea añadida', classes: 'rounded green lighten-1' });
        cargarTareas();
    } catch {
        alert('Error en la conexión con el servidor');
    }
});

// ===== Eliminar =====
async function eliminarTarea(id) {
    const r = await fetch(`${API}?id=${id}`, { method: 'DELETE' });
    if (r.ok) {
        M.toast({ html: 'Tarea eliminada', classes: 'rounded red lighten-1' });
        cargarTareas();
    } else {
        alert('Error al eliminar tarea');
    }
}

// ===== Actualizar estado (mover) =====
async function moverTareaAEstado(id, nuevoEstado) {
    try {
        const r = await fetch(`${API}?id=${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ estado: nuevoEstado })
        });
        if (!r.ok) throw new Error();
        cargarTareas();
    } catch {
        alert('Error al actualizar estado de la tarea');
    }
}

// ===== Modal edición =====
let tareaEditando = null;

function abrirModalEdicion(tarea) {
    tareaEditando = tarea;
    document.getElementById('editar-nombre').value = tarea.nombre ?? '';
    document.getElementById('editar-description').value = tarea.description ?? '';
    M.updateTextFields();
    const modal = M.Modal.getInstance(document.getElementById('modal-editar-tarea'));
    modal.open();
}

document.getElementById('guardar-edicion').addEventListener('click', async () => {
    const nombre = document.getElementById('editar-nombre').value.trim();
    const description = document.getElementById('editar-description').value.trim();
    if (!nombre) {
        M.toast({ html: 'El nombre es obligatorio', classes: 'rounded red lighten-1' });
        return;
    }
    try {
        const r = await fetch(`${API}?id=${tareaEditando.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nombre, description, estado: tareaEditando.estado })
        });
        if (!r.ok) throw new Error();
        M.Modal.getInstance(document.getElementById('modal-editar-tarea')).close();
        M.toast({ html: 'Tarea actualizada', classes: 'rounded green lighten-1' });
        cargarTareas();
    } catch {
        M.toast({ html: 'No se pudo actualizar la tarea', classes: 'rounded red lighten-1' });
    }
});

// ===== Drag & Drop (SortableJS) =====
// Requiere: <script src="https://cdn.jsdelivr.net/npm/sortablejs@1.15.2/Sortable.min.js"></script>
function inicializarDragAndDrop() {
    // To do
    Sortable.create(document.getElementById('tareas-todo-container'), {
        group: 'tareas', animation: 150,
        onAdd: (evt) => {
            const id = evt.item.getAttribute('data-id');
            if (id) moverTareaAEstado(id, 'todo');
        }
    });
    // In progress
    Sortable.create(document.getElementById('tareas-inprogress-container'), {
        group: 'tareas', animation: 150,
        onAdd: (evt) => {
            const id = evt.item.getAttribute('data-id');
            if (id) moverTareaAEstado(id, 'inprogress');
        }
    });
    // Done
    Sortable.create(document.getElementById('tareas-done-container'), {
        group: 'tareas', animation: 150,
        onAdd: (evt) => {
            const id = evt.item.getAttribute('data-id');
            if (id) moverTareaAEstado(id, 'done');
        }
    });
}

// ===== Init =====
document.addEventListener('DOMContentLoaded', () => {
    // Modales Materialize
    const modals = document.querySelectorAll('.modal');
    M.Modal.init(modals);
    cargarTareas();
});

