package mx.aragon.unam.controller.finanzas;

import mx.aragon.unam.service.pedidos.pedido.PedidoProveedorService;
import mx.aragon.unam.service.producto.productos.ProductoService;
import mx.aragon.unam.service.venta.detalleVenta.DetalleVentaService;
import mx.aragon.unam.service.venta.venta.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
@RequestMapping("/finanzas")
public class FinanzasController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PedidoProveedorService pedidoProveedorService;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private DetalleVentaService detalleVentaService;

    // Productos para Finanzas
    @GetMapping("/finanzas/productos")
    public String listaProductosFinanzas(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "vistas/finanzas/lista-productos";
    }

    // Pedidos para Finanzas
    @GetMapping("/finanzas/pedidos")
    public String listaPedidosFinanzas(Model model) {
        model.addAttribute("pedidos", pedidoProveedorService.findAll());
        return "vistas/finanzas/lista-pedidos";
    }

    // Ventas para Finanzas
    @GetMapping("/finanzas/ventas")
    public String listaVentasFinanzas(Model model) {
        model.addAttribute("ventas", ventaService.findAll());
        return "vistas/finanzas/lista-ventas";
    }

    // Detalle venta
    @GetMapping("/finanzas/ventas/detalles/{id}")
    public String listaDetalleVentaFinanzas(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("detallesVenta", detalleVentaService.findByVenta(ventaService.findById(id)));
        return "vistas/finanzas/lista-detallesVenta";
    }
}
