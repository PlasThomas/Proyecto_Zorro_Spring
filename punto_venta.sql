-- Eliminar y recrear la base de datos
DROP DATABASE IF EXISTS punto_venta;
CREATE DATABASE punto_venta CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE punto_venta;

-- 1 Tabla de usuarios
CREATE TABLE usuarios (
    email VARCHAR(100) PRIMARY KEY,
    contrasena_hash VARCHAR(255) NOT NULL,
    tipo ENUM('ADMINISTRADOR', 'FINANZAS', 'VENDEDOR', 'CLIENTE') NOT NULL DEFAULT 'CLIENTE',
    nombre_completo VARCHAR(100) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    INDEX (tipo, activo)
) ENGINE=InnoDB;

-- 2 Tabla de proveedores
CREATE TABLE proveedores (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre_empresa VARCHAR(100) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    INDEX (nombre_empresa)
) ENGINE=InnoDB;

-- 3 Tabla de categorías
CREATE TABLE categorias (
    id_categoria SMALLINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

-- 4 Tabla de productos
CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    id_categoria SMALLINT,
    id_proveedor INT,
    precio_compra DECIMAL(12,2) UNSIGNED NOT NULL,
    precio_venta DECIMAL(12,2) UNSIGNED NOT NULL,
    existencia DECIMAL(10,3) UNSIGNED NOT NULL DEFAULT 0,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria) ON DELETE SET NULL,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor) ON DELETE SET NULL,
    INDEX (nombre)
) ENGINE=InnoDB;

-- 5 Tabla de pedidos a proveedores
CREATE TABLE pedidos_proveedor (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_proveedor INT NOT NULL,
    fecha_pedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_entrega DATETIME,
    estado ENUM('PENDIENTE', 'RECIBIDO', 'CANCELADO') NOT NULL DEFAULT 'PENDIENTE',
    total DECIMAL(12,2) UNSIGNED NOT NULL,
    usuario_email VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor),
    FOREIGN KEY (usuario_email) REFERENCES usuarios(email),
    INDEX (estado)
) ENGINE=InnoDB;

-- 6 Tabla de detalle de pedidos
CREATE TABLE detalle_pedido (
    id_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad DECIMAL(10,3) UNSIGNED NOT NULL,
    precio_unitario DECIMAL(10,2) UNSIGNED NOT NULL,
    subtotal DECIMAL(12,2) UNSIGNED NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedidos_proveedor(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
) ENGINE=InnoDB;

-- 7 Tabla de historial de pedidos 
CREATE TABLE historial_pedidos (
    id_historial BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    fecha_movimiento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado_anterior VARCHAR(20),
    estado_nuevo VARCHAR(20) NOT NULL,
    usuario_email VARCHAR(100) NOT NULL,
    notas TEXT,
    FOREIGN KEY (id_pedido) REFERENCES pedidos_proveedor(id_pedido),
    FOREIGN KEY (usuario_email) REFERENCES usuarios(email),
    INDEX (id_pedido)
) ENGINE=InnoDB;

-- 8 Tabla de ventas
CREATE TABLE ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    fecha_venta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cliente_email VARCHAR(100),
    vendedor_email VARCHAR(100) NOT NULL,
    total DECIMAL(12,2) UNSIGNED NOT NULL,
    FOREIGN KEY (cliente_email) REFERENCES usuarios(email) ON DELETE SET NULL,
    FOREIGN KEY (vendedor_email) REFERENCES usuarios(email)
) ENGINE=InnoDB;

-- 9 Tabla de detalle de venta
CREATE TABLE detalle_venta (
    id_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad DECIMAL(10,3) UNSIGNED NOT NULL,
    precio_unitario DECIMAL(12,2) UNSIGNED NOT NULL,
    total_linea DECIMAL(12,2) UNSIGNED NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
) ENGINE=InnoDB;

-- 10 Tabla de historial de inventario
CREATE TABLE historial_inventario (
    id_movimiento BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    fecha_movimiento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento ENUM('ENTRADA', 'SALIDA', 'AJUSTE') NOT NULL,
    cantidad DECIMAL(10,3) NOT NULL,
    existencia_actual DECIMAL(10,3) NOT NULL,
    usuario_email VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    FOREIGN KEY (usuario_email) REFERENCES usuarios(email),
    INDEX (id_producto)
) ENGINE=InnoDB;
