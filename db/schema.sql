CREATE DATABASE IF NOT EXISTS todo_app;
USE todo_app;

CREATE TABLE IF NOT EXISTS tareas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    completada BOOLEAN DEFAULT FALSE
);
INSERT INTO tareas (nombre, descripcion, completada) VALUES ('Estudiar Java', 'Recordatorio para 2º', false);
INSERT INTO tareas (nombre, completada) VALUES ('Hacer proyecto DAW', true);

SELECT * FROM tareas;
DESCRIBE tareas;
ALTER TABLE tareas RENAME COLUMN descripcion TO description;

-- AÑADIR COLUMNA ESTADO PARA INCLUIR ESTADO TODO, INPROGRESS Y DONE
ALTER TABLE tareas
ADD COLUMN estado VARCHAR(20) DEFAULT 'todo';

UPDATE tareas SET estado = 'done' WHERE completada = TRUE;
UPDATE tareas SET estado = 'todo' WHERE completada = FALSE;

