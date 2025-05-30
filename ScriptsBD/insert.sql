
--Insert de datos 

USE punto_venta;

-- se agregan categorias de juguetes 
INSERT INTO categorias (nombre, detalles) VALUES
('Electrónicos', 'Dispositivos electrónicos y gadgets'),
('Alimentos', 'Productos alimenticios y comestibles'),
('Bebidas', 'Bebidas alcohólicas y no alcohólicas'),
('Limpieza', 'Productos de limpieza para el hogar'),
('Papelería', 'Artículos de oficina y papelería'),
('Ropa', 'Prendas de vestir para todas las edades'),
('Juguetes', 'Juguetes para niños y niñas'),
('Hogar', 'Artículos para el hogar y decoración'),
('Frutas y Verduras', 'Productos frescos del campo'),
('Carnes', 'Carnes y embutidos de diferentes tipos');

-- se insertan provedores

INSERT INTO proveedores (nombre_empresa, contacto_principal, telefono, email) VALUES
('TecnoSuministros S.A.', 'Juan Pérez', '5551234567', 'ventas@tecnosuministros.com'),
('Distribuidora Alimenticia Mexicana', 'María González', '5552345678', 'contacto@dam.com.mx'),
('Bebidas del Valle', 'Roberto Sánchez', '5553456789', 'pedidos@bebidasvalle.com'),
('Productos Limpieza Total', 'Laura Ramírez', '5554567890', 'info@limpiezatotal.mx'),
('Papelería Educativa', 'Carlos Mendoza', '5555678901', 'ventas@papeleriaeducativa.com'),
('Moda y Estilo S.A.', 'Ana Torres', '5556789012', 'compras@modayestilo.com'),
('Juguetes Creativos', 'Pedro Vázquez', '5557890123', 'distribucion@juguetescreativos.mx'),
('Hogar y Decoración', 'Sofía Ruiz', '5558901234', 'atencion@hogarydecoracion.com'),
('Frutas Frescas del Campo', 'Miguel Ángel Castro', '5559012345', 'frutas@campofresco.com'),
('Carnicería Premium', 'José Luis Morales', '5550123456', 'proveedores@carnespremium.mx');

-- Insert de productos en cada categoria 

-- Electrónicos
INSERT INTO productos (nombre, id_categoria, id_proveedor, precio_compra, precio_venta, existencia, unidad_medida) VALUES
('Smartphone X10', 1, 1, 4500.00, 5999.00, 25, 'PIEZA'),
('Tablet Pro 8"', 1, 1, 2800.00, 3799.00, 15, 'PIEZA');

-- Alimentos
INSERT INTO productos (nombre, id_categoria, id_proveedor, precio_compra, precio_venta, existencia, unidad_medida) VALUES
('Arroz Extra 1kg', 2, 2, 18.50, 24.90, 120, 'PAQUETE'),
('Frijol Bayo 1kg', 2, 2, 22.00, 29.90, 80, 'PAQUETE');

-- Bebidas
INSERT INTO productos (nombre, id_categoria, id_proveedor, precio_compra, precio_venta, existencia, unidad_medida) VALUES
('Refresco Cola 600ml', 3, 3, 8.50, 14.90, 200, 'PIEZA'),
('Agua Mineral 1L', 3, 3, 4.00, 8.50, 150, 'PIEZA');

-- Limpieza
INSERT INTO productos (nombre, id_categoria, id_proveedor, precio_compra, precio_venta, existencia, unidad_medida) VALUES
('Detergente líquido 3L', 4, 4, 45.00, 69.90, 60, 'LITRO'),
('Cloro 1L', 4, 4, 12.50, 19.90, 90, 'LITRO');

-- Papelería
INSERT INTO productos (nombre, id_categoria, id_proveedor, precio_compra, precio_venta, existencia, unidad_medida) VALUES
('Cuaderno profesional 100 hojas', 5, 5, 32.00, 49.90, 75, 'PIEZA'),
('Paquete de plumas azules 12pz', 5, 5, 45.00, 69.90, 40, 'PAQUETE');

-- Ropa
INSERT INTO productos (nombre, id_categoria, id_proveedor, precio_compra, precio_venta, existencia, unidad_medida) VALUES
('Camisa manga larga talla M', 6, 6, 120.00, 199.90, 30, 'PIEZA'),
('Pantalón de mezclilla talla 32', 6, 6, 180.00, 299.90, 25, 'PIEZA');

-- Juguetes
INSERT INTO productos (nombre, id_categoria, id_proveedor, precio_compra, precio_venta, existencia, unidad_medida) VALUES
('Set de construcción 100pz', 7, 7, 150.00, 249.90, 35, 'CAJA'),
('Muñeca articulada 30cm', 7, 7, 85.00, 139.90, 40, 'PIEZA');

-- Hogar
INSERT INTO productos (nombre, id_categoria, id_proveedor, precio_compra, precio_venta, existencia, unidad_medida) VALUES
('Juego de sábanas king size', 8, 8, 320.00, 499.90, 20, 'PAQUETE'),
('Florero de cristal 25cm', 8, 8, 95.00, 149.90, 15, 'PIEZA');

-- Frutas y Verduras
INSERT INTO productos (nombre, id_categoria, id_proveedor, precio_compra, precio_venta, existencia, unidad_medida) VALUES
('Manzana roja kg', 9, 9, 25.00, 39.90, 50, 'KILO'),
('Plátano kg', 9, 9, 12.00, 19.90, 60, 'KILO');

-- Carnes
INSERT INTO productos (nombre, id_categoria, id_proveedor, precio_compra, precio_venta, existencia, unidad_medida) VALUES
('Filete de res kg', 10, 10, 180.00, 249.90, 30, 'KILO'),
('Pechuga de pollo kg', 10, 10, 65.00, 89.90, 40, 'KILO');


-- Insert de 3 usuarios 

INSERT INTO usuarios (email, contrasena_hash, tipo, nombre_completo, telefono, direccion) VALUES
('admin@tienda.com', SHA2('admin123', 256), 'ADMINISTRADOR', 'Administrador del Sistema', '5551112233', 'Av. Principal 123'),
('vendedor1@tienda.com', SHA2('vendedor1', 256), 'VENDEDOR', 'Juan Vendedor', '5552223344', 'Calle Comercio 45'),
('finanzas@email.com', SHA2('finanzas1', 256), 'FINANZAS', 'María Finanzas', '5553334455', 'Privada Flores 67');


DELIMITER //

-- Trigger para actualización de detalle de pedido
CREATE TRIGGER after_detalle_pedido_update
AFTER UPDATE ON detalle_pedido
FOR EACH ROW
BEGIN
    DECLARE v_existencia_actual DECIMAL(10,3);
    DECLARE v_estado_pedido ENUM('PENDIENTE', 'RECIBIDO', 'CANCELADO');
    DECLARE v_id_usuario INT;
    
    -- Obtener el estado actual del pedido y el usuario
    SELECT estado, id_usuario_registro INTO v_estado_pedido, v_id_usuario 
    FROM pedidos_proveedor WHERE id_pedido = NEW.id_pedido;
    
    -- Solo procesar si el pedido está marcado como RECIBIDO y hay diferencia en cantidades
    IF v_estado_pedido = 'RECIBIDO' AND (OLD.cantidad_recibida <> NEW.cantidad_recibida OR OLD.cantidad_recibida IS NULL) THEN
        -- Actualizar existencia del producto primero
        UPDATE productos 
        SET existencia = existencia + (NEW.cantidad_recibida - IFNULL(OLD.cantidad_recibida, 0))
        WHERE id_producto = NEW.id_producto;
        
        -- Obtener la nueva existencia actual
        SELECT existencia INTO v_existencia_actual FROM productos WHERE id_producto = NEW.id_producto;
        
        -- Registrar el movimiento de entrada
        INSERT INTO historial_productos (
            id_producto, 
            fecha_movimiento, 
            tipo_movimiento, 
            cantidad, 
            existencia_actual, 
            id_referencia, 
            tipo_referencia, 
            id_usuario,
            notas
        ) VALUES (
            NEW.id_producto,
            NOW(),
            'ENTRADA',
            NEW.cantidad_recibida - IFNULL(OLD.cantidad_recibida, 0),
            v_existencia_actual,
            NEW.id_pedido,
            'PEDIDO',
            v_id_usuario,
            CONCAT('Recibo de pedido #', NEW.id_pedido)
        );
    END IF;
END//

-- Trigger para inserción de detalle de venta
CREATE TRIGGER after_detalle_venta_insert
AFTER INSERT ON detalle_venta
FOR EACH ROW
BEGIN
    DECLARE v_existencia_actual DECIMAL(10,3);
    DECLARE v_id_vendedor INT;
    
    -- Obtener el vendedor de la venta
    SELECT id_vendedor INTO v_id_vendedor FROM ventas WHERE id_venta = NEW.id_venta;
    
    -- Actualizar primero la existencia del producto
    UPDATE productos 
    SET existencia = existencia - NEW.cantidad
    WHERE id_producto = NEW.id_producto;
    
    -- Obtener la nueva existencia actual
    SELECT existencia INTO v_existencia_actual FROM productos WHERE id_producto = NEW.id_producto;
    
    -- Registrar el movimiento de salida
    INSERT INTO historial_productos (
        id_producto, 
        fecha_movimiento, 
        tipo_movimiento, 
        cantidad, 
        existencia_actual, 
        id_referencia, 
        tipo_referencia, 
        id_usuario,
        notas
    ) VALUES (
        NEW.id_producto,
        NOW(),
        'SALIDA',
        -NEW.cantidad,
        v_existencia_actual,
        NEW.id_venta,
        'VENTA',
        v_id_vendedor,
        CONCAT('Venta #', NEW.id_venta)
    );
END//

-- Trigger para inserción de pedidos a proveedores
CREATE TRIGGER after_pedidos_proveedor_insert
AFTER INSERT ON pedidos_proveedor
FOR EACH ROW
BEGIN
    INSERT INTO historial_pedidos (
        id_pedido,
        fecha_movimiento,
        tipo_movimiento,
        estado_anterior,
        estado_nuevo,
        id_usuario,
        notas
    ) VALUES (
        NEW.id_pedido,
        NOW(),
        'CREACION',
        NULL,
        NEW.estado,
        NEW.id_usuario_registro,
        'Creación de nuevo pedido'
    );
END//

-- Trigger para actualización de pedidos a proveedores
CREATE TRIGGER after_pedidos_proveedor_update
AFTER UPDATE ON pedidos_proveedor
FOR EACH ROW
BEGIN
    -- Registrar cambio de estado si hubo modificación
    IF OLD.estado <> NEW.estado THEN
        INSERT INTO historial_pedidos (
            id_pedido,
            fecha_movimiento,
            tipo_movimiento,
            estado_anterior,
            estado_nuevo,
            campo_modificado,
            valor_anterior,
            valor_nuevo,
            id_usuario,
            notas
        ) VALUES (
            NEW.id_pedido,
            NOW(),
            'ACTUALIZACION',
            OLD.estado,
            NEW.estado,
            'estado',
            OLD.estado,
            NEW.estado,
            NEW.id_usuario_registro,
            CONCAT('Cambio de estado de ', OLD.estado, ' a ', NEW.estado)
        );
    END IF;
    
    -- Registrar cancelación específicamente
    IF NEW.estado = 'CANCELADO' AND OLD.estado <> 'CANCELADO' THEN
        INSERT INTO historial_pedidos (
            id_pedido,
            fecha_movimiento,
            tipo_movimiento,
            estado_anterior,
            estado_nuevo,
            id_usuario,
            notas
        ) VALUES (
            NEW.id_pedido,
            NOW(),
            'CANCELACION',
            OLD.estado,
            NEW.estado,
            NEW.id_usuario_registro,
            'Pedido cancelado'
        );
    END IF;
END//

-- Trigger para actualización de productos
CREATE TRIGGER after_productos_update
AFTER UPDATE ON productos
FOR EACH ROW
BEGIN
    DECLARE v_id_admin INT;
    
    -- Registrar ajustes manuales de existencia
    IF OLD.existencia <> NEW.existencia THEN
        -- Obtener un usuario administrador
        SELECT id_usuario INTO v_id_admin 
        FROM usuarios 
        WHERE tipo = 'ADMINISTRADOR' AND activo = TRUE
        ORDER BY id_usuario 
        LIMIT 1;
        
        -- Registrar el ajuste
        INSERT INTO historial_productos (
            id_producto, 
            fecha_movimiento, 
            tipo_movimiento, 
            cantidad, 
            existencia_actual, 
            tipo_referencia, 
            id_usuario,
            notas
        ) VALUES (
            NEW.id_producto,
            NOW(),
            'AJUSTE',
            NEW.existencia - OLD.existencia,
            NEW.existencia,
            'AJUSTE_MANUAL',
            v_id_admin,
            CONCAT('Ajuste manual de inventario. Existencia anterior: ', OLD.existencia)
        );
    END IF;
END//

DELIMITER ;
