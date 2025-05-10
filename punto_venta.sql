-- Eliminar la base de datos si existe
DROP DATABASE IF EXISTS punto_venta;

-- Crear la base de datos 
CREATE DATABASE punto_venta CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE punto_venta;

-- 1. Tabla de usuarios
CREATE TABLE usuarios (
    email VARCHAR(100) PRIMARY KEY,
    contrasena_hash VARCHAR(255) NOT NULL,
    tipo ENUM('ADMINISTRADOR', 'FINANZAS', 'VENDEDOR', 'CLIENTE') NOT NULL DEFAULT 'CLIENTE',
    nombre_completo VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    INDEX idx_usuario_tipo (tipo, activo),
    INDEX idx_usuario_nombre (nombre_completo)
) ENGINE=InnoDB;

-- 2. Tabla de clientes
CREATE TABLE clientes (
    email VARCHAR(100) PRIMARY KEY,
    direccion TEXT,
    rfc VARCHAR(20),
    fecha_nacimiento DATE,
    FOREIGN KEY (email) REFERENCES usuarios(email) ON DELETE CASCADE,
    INDEX idx_cliente_rfc (rfc)
) ENGINE=InnoDB;

-- 3. Tabla de proveedores
CREATE TABLE proveedores (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre_empresa VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    direccion TEXT,
    dias_entrega SMALLINT UNSIGNED,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE INDEX uk_proveedor_email (email),
    INDEX idx_proveedor_nombre (nombre_empresa),
    INDEX idx_proveedor_activo (activo)
) ENGINE=InnoDB;

-- 4. Tabla de categorías
CREATE TABLE categorias_productos (
    id_categoria SMALLINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE INDEX uk_categoria_nombre (nombre),
    INDEX idx_categoria_activa (activo)
) ENGINE=InnoDB;

-- 5. Tabla de productos (fixed - removed trailing comma)
CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    id_categoria SMALLINT,
    id_proveedor INT,
    descripcion TEXT,
    precio_compra DECIMAL(12,2) UNSIGNED NOT NULL, 
    precio_venta DECIMAL(12,2) UNSIGNED NOT NULL, 
    existencia DECIMAL(10,3) UNSIGNED NOT NULL DEFAULT 0,
    unidad_medida ENUM('PIEZA', 'KILO', 'LITRO', 'PAQUETE', 'CAJA') NOT NULL DEFAULT 'PIEZA',
    activo TINYINT(1) NOT NULL DEFAULT 1,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_categoria) REFERENCES categorias_productos(id_categoria) ON DELETE SET NULL,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor) ON DELETE SET NULL,
    INDEX idx_producto_nombre (nombre),
    INDEX idx_producto_activo (activo),
    INDEX idx_producto_categoria (id_categoria, activo),
    INDEX idx_producto_existencia (existencia)
) ENGINE=InnoDB;

-- 6. Tabla de historial de productos (should work now that productos exists)
CREATE TABLE historial_productos (
    id_historial BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    fecha_cambio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_cambio ENUM('CREACION', 'MODIFICACION', 'AJUSTE_INVENTARIO', 'CAMBIO_PRECIO', 'DESACTIVACION') NOT NULL,
    usuario_email VARCHAR(100) NOT NULL,
    campo_afectado VARCHAR(30),
    valor_anterior TEXT,
    valor_nuevo TEXT,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE CASCADE,
    FOREIGN KEY (usuario_email) REFERENCES usuarios(email) ON DELETE RESTRICT,
    INDEX idx_historial_producto (id_producto, fecha_cambio),
    INDEX idx_historial_fecha (fecha_cambio),
    INDEX idx_historial_tipo (tipo_cambio)
) ENGINE=InnoDB;

-- 7. Tabla de pedidos a proveedores
CREATE TABLE pedidos_proveedores (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_proveedor INT NOT NULL,
    fecha_pedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_entrega DATETIME,
    estado ENUM('PENDIENTE', 'COMPLETADO', 'CANCELADO') NOT NULL DEFAULT 'PENDIENTE',
    subtotal DECIMAL(12,2) UNSIGNED NOT NULL DEFAULT 0,
    total DECIMAL(12,2) UNSIGNED NOT NULL DEFAULT 0,
    notas TEXT,
    usuario_registro VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor),
    FOREIGN KEY (usuario_registro) REFERENCES usuarios(email),
    INDEX idx_pedido_proveedor (id_proveedor, estado),
    INDEX idx_pedido_estado (estado, fecha_pedido),
    INDEX idx_pedido_fecha (fecha_pedido)
) ENGINE=InnoDB;

-- 8. Tabla de detalle de pedidos
CREATE TABLE detalle_pedidos (
    id_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad_solicitada DECIMAL(10,3) UNSIGNED NOT NULL,
    cantidad_recibida DECIMAL(10,3) UNSIGNED DEFAULT 0,
    precio_unitario DECIMAL(10,2) UNSIGNED NOT NULL,
    subtotal DECIMAL(12,2) UNSIGNED NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedidos_proveedores(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    INDEX idx_detalle_pedido (id_pedido, id_producto),
    INDEX idx_detalle_producto (id_producto)
) ENGINE=InnoDB;

-- 9. Tabla de ventas
CREATE TABLE ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    fecha_venta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cliente_email VARCHAR(100),
    vendedor_email VARCHAR(100) NOT NULL,
    subtotal DECIMAL(12,2) UNSIGNED NOT NULL,
    total DECIMAL(12,2) UNSIGNED NOT NULL,
    metodo_pago ENUM('EFECTIVO', 'TARJETA_DEBITO') NOT NULL,
    notas TEXT,
    FOREIGN KEY (cliente_email) REFERENCES usuarios(email) ON DELETE SET NULL,
    FOREIGN KEY (vendedor_email) REFERENCES usuarios(email),
    INDEX idx_venta_fecha (fecha_venta),
    INDEX idx_venta_cliente (cliente_email),
    INDEX idx_venta_vendedor (vendedor_email)
) ENGINE=InnoDB;

-- 10. Tabla de detalle de venta
CREATE TABLE detalle_venta (
    id_detalle BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_venta INT NOT NULL,
    id_producto INT,
    cantidad DECIMAL(10,3) UNSIGNED NOT NULL,
    precio_unitario DECIMAL(12,2) UNSIGNED NOT NULL,
    subtotal DECIMAL(12,2) UNSIGNED NOT NULL,
    total_linea DECIMAL(12,2) UNSIGNED NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE SET NULL,
    INDEX idx_detalle_venta (id_venta),
    INDEX idx_detalle_producto_venta (id_producto)
) ENGINE=InnoDB;
