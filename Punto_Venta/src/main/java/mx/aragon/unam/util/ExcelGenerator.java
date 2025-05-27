package mx.aragon.unam.util;

import mx.aragon.unam.model.entity.pedido.DetallePedidoEntity;
import mx.aragon.unam.model.entity.pedido.PedidoProveedorEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExcelGenerator {
    public String generarExcelPedido(PedidoProveedorEntity pedido, List<DetallePedidoEntity> detalles, String outputPath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Pedido");

        // Estilos
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));

        int rowIdx = 0;

        // Datos generales del pedido
        Row headerRow = sheet.createRow(rowIdx++);
        headerRow.createCell(0).setCellValue("Pedido a Proveedor");
        headerRow.getCell(0).setCellStyle(headerStyle);

        rowIdx++;
        sheet.createRow(rowIdx++).createCell(0).setCellValue("ID Pedido: " + pedido.getId());
        sheet.createRow(rowIdx++).createCell(0).setCellValue("Proveedor: " + pedido.getProveedor().getNombreEmpresa());
        sheet.createRow(rowIdx++).createCell(0).setCellValue("Fecha Pedido: " + pedido.getFechaPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        sheet.createRow(rowIdx++).createCell(0).setCellValue("Fecha Entrega: " +
                (pedido.getFechaEntrega() != null ? pedido.getFechaEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "Pendiente"));
        sheet.createRow(rowIdx++).createCell(0).setCellValue("Registrado por: " + pedido.getUsuarioRegistro().getNombreCompleto());
        sheet.createRow(rowIdx++).createCell(0).setCellValue("Estado: " + pedido.getEstado().name());
        rowIdx++;

        // Encabezado tabla
        Row tableHeader = sheet.createRow(rowIdx++);
        String[] headers = {"Producto", "Cantidad Solicitada", "Cantidad Recibida", "Precio Unitario", "Total Línea"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = tableHeader.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Cuerpo de tabla
        for (DetallePedidoEntity detalle : detalles) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(detalle.getProducto().getNombre());
            row.createCell(1).setCellValue(detalle.getCantidadSolicitada().doubleValue());
            row.createCell(2).setCellValue(detalle.getCantidadRecibida().doubleValue());
            Cell cellPrecio = row.createCell(3);
            cellPrecio.setCellValue(detalle.getPrecioUnitario().doubleValue());
            cellPrecio.setCellStyle(currencyStyle);
            Cell cellTotal = row.createCell(4);
            cellTotal.setCellValue(detalle.getTotalLinea().doubleValue());
            cellTotal.setCellStyle(currencyStyle);
        }

        // Total
        Row totalRow = sheet.createRow(rowIdx++);
        Cell totalLabelCell = totalRow.createCell(3);
        totalLabelCell.setCellValue("Total:");
        totalLabelCell.setCellStyle(headerStyle);
        Cell totalValueCell = totalRow.createCell(4);
        totalValueCell.setCellValue(pedido.getTotal().doubleValue());
        totalValueCell.setCellStyle(currencyStyle);

        // Autosize columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Guardar archivo
        try (FileOutputStream fileOut = new FileOutputStream(outputPath)) {
            workbook.write(fileOut);
        }

        workbook.close();
        return outputPath;
    }
}
