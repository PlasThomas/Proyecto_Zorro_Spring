package mx.aragon.unam.util;

import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import mx.aragon.unam.model.entity.pedido.DetallePedidoEntity;
import mx.aragon.unam.model.entity.pedido.PedidoProveedorEntity;
import mx.aragon.unam.model.entity.producto.ProductoEntity;
import mx.aragon.unam.model.entity.venta.DetalleVentaEntity;
import mx.aragon.unam.model.entity.venta.VentaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PDFgenerator {
    static String basePath = "/home/thomas/Documentos/Escuela/Octavo_semestre/Spring/ProyectoFinal/Proyecto_Zorro_Spring/productos/";

    public static String generarPDFVenta(VentaEntity venta, List<DetalleVentaEntity> detalles, String logoPath, String outputPath) throws Exception {
        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, new FileOutputStream(outputPath));
        doc.open();

        // Logo
        Image logo = Image.getInstance("src/main/resources/static/images/logo.png");
        logo.scaleToFit(120, 120);
        logo.setAlignment(Image.ALIGN_LEFT);
        doc.add(logo);

        // Título
        Paragraph titulo = new Paragraph("Ticket de Venta", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        titulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(titulo);
        doc.add(Chunk.NEWLINE);

        // Datos generales
        doc.add(new Paragraph("ID Venta: " + venta.getIdVenta()));

        LocalDateTime fecha = venta.getFechaVenta();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = venta.getFechaVenta().format(formatter);

        doc.add(new Paragraph("Fecha: " + fechaFormateada));
        doc.add(new Paragraph("Cliente: " + venta.getCliente().getNombreCompleto()));
        doc.add(new Paragraph("Vendedor: " + venta.getVendedor().getNombreCompleto()));
        doc.add(new Paragraph("Método de Pago: " + venta.getMetodoPago()));
        doc.add(Chunk.NEWLINE);

        // Tabla de productos
        PdfPTable tabla = new PdfPTable(5); // Imagen + Producto + Cant + Precio + Subtotal
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{2f, 3f, 1f, 2f, 2f});

        añadirEncabezado(tabla, "Imagen");
        añadirEncabezado(tabla, "Producto");
        añadirEncabezado(tabla, "Cant.");
        añadirEncabezado(tabla, "P. Unitario");
        añadirEncabezado(tabla, "Subtotal");

        for (DetalleVentaEntity d : detalles) {
            ProductoEntity producto = d.getProducto();
            String imagePath = basePath + producto.getId() + ".png";
            try {
                Image img = Image.getInstance(imagePath);
                img.scaleToFit(30, 30);
                PdfPCell imgCell = new PdfPCell(img);
                imgCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                imgCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(imgCell);
            } catch (Exception e) {
                // Si la imagen no existe o falla, añade celda vacía
                PdfPCell emptyCell = new PdfPCell(new Phrase("Sin imagen"));
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(emptyCell);
            }
            tabla.addCell(d.getProducto().getNombre());
            tabla.addCell(d.getCantidad().toString());
            tabla.addCell(String.format("$ %.2f", d.getPrecioUnitario()));
            tabla.addCell(String.format("$ %.2f", d.getTotalLinea()));
        }

        PdfPCell total = new PdfPCell(new Phrase("Total: $" + venta.getTotal()));
        total.setColspan(5);
        total.setHorizontalAlignment(Element.ALIGN_RIGHT);
        total.setPadding(8f);
        tabla.addCell(total);

        doc.add(tabla);
        doc.close();
        return outputPath;
    }

    private static void añadirEncabezado(PdfPTable tabla, String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        celda.setBackgroundColor(new Color(210, 210, 210));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(6f);
        tabla.addCell(celda);
    }

    public static String generarPDFPedido(PedidoProveedorEntity pedido, List<DetallePedidoEntity> detalles, String logoPath, String outputPath) throws Exception {
        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, new FileOutputStream(outputPath));
        doc.open();

        // Logo
        Image logo = Image.getInstance(logoPath);
        logo.scaleToFit(120, 120);
        logo.setAlignment(Image.ALIGN_LEFT);
        doc.add(logo);

        // Título
        Paragraph titulo = new Paragraph("Resumen del Pedido", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        titulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(titulo);
        doc.add(Chunk.NEWLINE);

        // Datos del pedido
        doc.add(new Paragraph("ID Pedido: " + pedido.getId()));
        LocalDateTime fecha = pedido.getFechaPedido();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        doc.add(new Paragraph("Fecha: " + fecha.format(formatter)));
        doc.add(new Paragraph("Cliente: " + pedido.getUsuarioRegistro().getNombreCompleto()));
        doc.add(new Paragraph("Estado del Pedido: " + pedido.getEstado()));
        doc.add(Chunk.NEWLINE);

        // Tabla de productos
        PdfPTable tabla = new PdfPTable(5); // Imagen + Producto + Cant + Precio + Subtotal
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{2f, 3f, 1f, 2f, 2f});

        añadirEncabezado(tabla, "Imagen");
        añadirEncabezado(tabla, "Producto");
        añadirEncabezado(tabla, "Cant.");
        añadirEncabezado(tabla, "P. Unitario");
        añadirEncabezado(tabla, "Subtotal");

        for (DetallePedidoEntity d : detalles) {
            ProductoEntity producto = d.getProducto();
            // Ruta absoluta real de la imagen
            String imagePath = basePath + producto.getId() + ".png";
            try {
                Image img = Image.getInstance(imagePath);
                img.scaleToFit(30, 30);
                PdfPCell imgCell = new PdfPCell(img);
                imgCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                imgCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(imgCell);
            } catch (Exception e) {
                // Si la imagen no existe o falla, añade celda vacía
                PdfPCell emptyCell = new PdfPCell(new Phrase("Sin imagen"));
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(emptyCell);
            }
            tabla.addCell(d.getProducto().getNombre());
            tabla.addCell(d.getCantidadSolicitada().toString());
            tabla.addCell(String.format("$ %.2f", d.getPrecioUnitario()));
            tabla.addCell(String.format("$ %.2f", d.getTotalLinea()));
        }

        PdfPCell total = new PdfPCell(new Phrase("Total: $" + pedido.getTotal()));
        total.setColspan(5);
        total.setHorizontalAlignment(Element.ALIGN_RIGHT);
        total.setPadding(8f);
        tabla.addCell(total);

        doc.add(tabla);
        doc.close();
        return outputPath;
    }


}
