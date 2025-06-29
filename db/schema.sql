/*CREATE DATABASE IF NOT EXISTS todo_app;
USE todo_app;

CREATE TABLE IF NOT EXISTS tareas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    completada BOOLEAN DEFAULT FALSE
);
INSERT INTO tareas (descripcion) VALUES ('Estudiar Java');
INSERT INTO tareas (descripcion, completada) VALUES ('Hacer proyecto DAW', true);*/

SELECT * FROM tareas;
