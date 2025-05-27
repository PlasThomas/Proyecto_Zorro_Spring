-- Elimina el usuario si existe (sintaxis compatible con MariaDB)
DROP USER IF EXISTS 'userpv'@'localhost';

-- Crea el nuevo usuario
CREATE USER 'userpv'@'localhost' IDENTIFIED BY 'Spring123#';

-- Asigna privilegios
GRANT ALL PRIVILEGES ON punto_venta.* TO 'userpv'@'localhost';
FLUSH PRIVILEGES;