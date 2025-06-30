document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("formulario-tarea");
    const titleInput = document.getElementById("title");
    const descriptionInput = document.getElementById("description");

    const todoList = document.getElementById("todo-list");
    const doneList = document.getElementById("done-list");

    //Envio del formulario
    form.addEventListener("submit", (e) => {
        e.preventDefault();

        const title = titleInput.value.trim();
        const description = descriptionInput.value.trim();

        if (!title) {
            M.toast({ html: "El título no puede estar vacío", classes: "red darken-1" });
            return;
        }

        const card = crearTareaCard(title, description);

        todoList.appendChild(card);

        form.reset(); // limpia el formulario
    });

    // Funcion que crea la tarjeta
    function crearTareaCard(title, description) {
        const card = document.createElement("div");
        card.className = "card blue-grey darken-1 white-text";

        card.innerHTML = `
            <div class="card-content">
            <span class="card-title">${title}</span>
            <p>${description}</p>
            </div>
            <div class="card-action">
                <button class="btn green lighten-1 completar" type="button" aria-label="Marcar como completada">Completada</button>
                <button class="btn red darken-1 eliminar" type="button" aria-label="Eliminar tarea">Eliminar</button>
            </div>
        `;

        // Botón de eliminar
        card.querySelector(".eliminar").addEventListener("click", () => {
            card.remove();
        });

        // Botón de completada
        card.querySelector(".completar").addEventListener("click", () => {
            // Quita botón "Completada" y mueve al DONE
            card.querySelector(".completar").remove();
            doneList.appendChild(card);
        });

        return card;
    }


});
