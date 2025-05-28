package mx.aragon.unam.controller.historial;


import mx.aragon.unam.model.entity.historial.HistorialPedidoEntity;
import mx.aragon.unam.model.entity.historial.HistorialProductoEntity;
import mx.aragon.unam.service.historial.historialPedido.HistorialPedidoService;
import mx.aragon.unam.service.historial.historialProducto.HistorialProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "admin/movimientos")
public class HistorialController {

    @Autowired
    private HistorialPedidoService historialPedidoService;
    @Autowired
    private HistorialProductoService historialProductoService;

    @GetMapping(value = "/")
    public String OpcionesMobimientos(Model model) {
        return "vistas/admin/historiales/pantalla-historiales";
    }

    @GetMapping(value = "pedidos")
    public String historialPedidos(Model model) {
        List<HistorialPedidoEntity> historial = historialPedidoService.findAll();
        model.addAttribute("historialPedidos", historial);
        return "vistas/admin/historiales/historial-pedidos";
    }

    @GetMapping(value = "productos")
    public String historialProductos(Model model) {
        List<HistorialProductoEntity> historial = historialProductoService.findAll();
        model.addAttribute("historialProductos", historial);
        return "vistas/admin/historiales/historial-productos";
    }
}
