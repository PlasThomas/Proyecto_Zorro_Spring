use punto_venta;

DELIMITER //
CREATE TRIGGER after_detalle_pedido_update
AFTER UPDATE ON detalle_pedido
FOR EACH ROW
BEGIN
    DECLARE v_existencia_actual DECIMAL(10,3);
    DECLARE v_estado_pedido ENUM('PENDIENTE', 'RECIBIDO', 'CANCELADO');
    
    -- Obtener el estado actual del pedido
    SELECT estado INTO v_estado_pedido FROM pedidos_proveedor WHERE id_pedido = NEW.id_pedido;
    
    -- Solo registrar si el pedido está marcado como RECIBIDO y hay diferencia en cantidades
    IF v_estado_pedido = 'RECIBIDO' AND (OLD.cantidad_recibida <> NEW.cantidad_recibida OR OLD.cantidad_recibida IS NULL) THEN
        -- Obtener existencia actual del producto
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
            (SELECT id_usuario_registro FROM pedidos_proveedor WHERE id_pedido = NEW.id_pedido),
            CONCAT('Recibo de pedido #', NEW.id_pedido)
        );
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_detalle_venta_insert
AFTER INSERT ON detalle_venta
FOR EACH ROW
BEGIN
    DECLARE v_existencia_actual DECIMAL(10,3);
    
    -- Obtener existencia actual del producto
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
        -NEW.cantidad, -- Negativo porque es salida
        v_existencia_actual,
        NEW.id_venta,
        'VENTA',
        (SELECT id_vendedor FROM ventas WHERE id_venta = NEW.id_venta),
        CONCAT('Venta #', NEW.id_venta)
    );
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_historial_productos_insert
AFTER INSERT ON historial_productos
FOR EACH ROW
BEGIN
    -- Actualizar la existencia del producto
    UPDATE productos 
    SET existencia = existencia + NEW.cantidad 
    WHERE id_producto = NEW.id_producto;
END//
DELIMITER ;

DELIMITER //
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
DELIMITER ;

DELIMITER //
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
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_productos_update
AFTER UPDATE ON productos
FOR EACH ROW
BEGIN
    -- Registrar ajustes manuales de existencia
    IF OLD.existencia <> NEW.existencia THEN
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
            (SELECT id_usuario FROM usuarios WHERE tipo = 'ADMINISTRADOR' ORDER BY id_usuario DESC LIMIT 1),
            CONCAT('Ajuste manual de inventario. Existencia anterior: ', OLD.existencia)
        );
    END IF;
END//
DELIMITER ;
