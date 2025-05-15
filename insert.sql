
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
('cliente1@email.com', SHA2('cliente1', 256), 'CLIENTE', 'María Cliente', '5553334455', 'Privada Flores 67');
