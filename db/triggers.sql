USE punto_venta;

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
