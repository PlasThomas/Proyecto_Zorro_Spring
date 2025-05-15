-- Eliminar y recrear la base de datos
DROP DATABASE IF EXISTS punto_venta;
CREATE DATABASE punto_venta CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE punto_venta;

-- 1. Tabla de usuarios
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasena_hash VARCHAR(255) NOT NULL,
    tipo ENUM('ADMINISTRADOR', 'VENDEDOR', 'CLIENTE') NOT NULL DEFAULT 'CLIENTE',
    nombre_completo VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    direccion TEXT,
    rfc VARCHAR(20),
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    INDEX (tipo, activo),
    INDEX (nombre_completo)
) ENGINE=InnoDB;

-- 2. Tabla de provedores
CREATE TABLE proveedores (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre_empresa VARCHAR(100) NOT NULL,
    contacto_principal VARCHAR(100),
    telefono VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX (nombre_empresa)
) ENGINE=InnoDB;

-- 3. Tabla de categorías
CREATE TABLE categorias (
    id_categoria SMALLINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    detalles VARCHAR(300)
) ENGINE=InnoDB;

-- 4. Tabla de productos
CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    id_categoria SMALLINT,
    id_proveedor INT,
    precio_compra DECIMAL(12,2) UNSIGNED NOT NULL,
    precio_venta DECIMAL(12,2) UNSIGNED NOT NULL,
    existencia DECIMAL(10,3) UNSIGNED NOT NULL DEFAULT 0,
    unidad_medida ENUM('PIEZA', 'KILO', 'LITRO', 'PAQUETE', 'CAJA') NOT NULL DEFAULT 'PIEZA',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria) ON DELETE SET NULL,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor) ON DELETE SET NULL,
    INDEX (nombre),
    INDEX (existencia)
) ENGINE=InnoDB;

-- 5. Tabla de pedidos a proveedores
CREATE TABLE pedidos_proveedor (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_proveedor INT NOT NULL,
    fecha_pedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_entrega DATETIME,
    estado ENUM('PENDIENTE', 'RECIBIDO', 'CANCELADO') NOT NULL DEFAULT 'PENDIENTE',
    total DECIMAL(12,2) UNSIGNED NOT NULL,
    id_usuario_registro INT NOT NULL,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor),
    FOREIGN KEY (id_usuario_registro) REFERENCES usuarios(id_usuario),
    INDEX (estado, fecha_pedido)
) ENGINE=InnoDB;

-- 6. Tabla de ventas 
CREATE TABLE ventas (
    id_venta INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    fecha_venta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_cliente INT,
    id_vendedor INT NOT NULL,
    total DECIMAL(12,2) UNSIGNED NOT NULL,
    metodo_pago ENUM('EFECTIVO', 'TARJETA') NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES usuarios(id_usuario) ON DELETE SET NULL,
    FOREIGN KEY (id_vendedor) REFERENCES usuarios(id_usuario)
) ENGINE=InnoDB;


-- 7. Tabla de detalle de venta
CREATE TABLE detalle_venta (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad DECIMAL(10,3) UNSIGNED NOT NULL,
    precio_unitario DECIMAL(12,2) UNSIGNED NOT NULL,
    total_linea DECIMAL(12,2) UNSIGNED NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
) ENGINE=InnoDB;


-- 8. Tabla de historial de productos 
CREATE TABLE historial_productos (
    id_movimiento BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    fecha_movimiento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento ENUM('ENTRADA', 'SALIDA', 'AJUSTE') NOT NULL,
    cantidad DECIMAL(10,3) NOT NULL,
    existencia_actual DECIMAL(10,3) NOT NULL,
    id_referencia INT,
    tipo_referencia ENUM('PEDIDO', 'VENTA', 'AJUSTE_MANUAL'),
    id_usuario INT NOT NULL,
    notas TEXT,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    INDEX (id_producto, fecha_movimiento),
    INDEX (tipo_movimiento)
) ENGINE=InnoDB; 
-- 9 Tabla de historial de pedidos 
CREATE TABLE historial_pedidos (
    id_movimiento BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    fecha_movimiento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento ENUM('CREACION', 'ACTUALIZACION','CANCELACION') NOT NULL,
    estado_anterior ENUM('PENDIENTE', 'RECIBIDO', 'CANCELADO') NULL,
    estado_nuevo ENUM('PENDIENTE', 'RECIBIDO', 'CANCELADO') NULL,
    campo_modificado VARCHAR(50) NULL,
    valor_anterior TEXT NULL,
    valor_nuevo TEXT NULL,
    id_referencia INT NULL,
    tipo_referencia ENUM('PROVEEDOR', 'USUARIO', 'SISTEMA') NULL,
    id_usuario INT NOT NULL,
    notas TEXT,
    FOREIGN KEY (id_pedido) REFERENCES pedidos_proveedor(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    INDEX (id_pedido, fecha_movimiento),
    INDEX (tipo_movimiento)
) ENGINE=InnoDB;

-- 10. Tabla de detalle de pedido a proveedores
CREATE TABLE detalle_pedido (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad_solicitada DECIMAL(10,3) UNSIGNED NOT NULL,
    cantidad_recibida DECIMAL(10,3) UNSIGNED DEFAULT 0,
    precio_unitario DECIMAL(12,2) UNSIGNED NOT NULL,
    total_linea DECIMAL(12,2) UNSIGNED NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedidos_proveedor(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    INDEX (id_pedido)
) ENGINE=InnoDB;
