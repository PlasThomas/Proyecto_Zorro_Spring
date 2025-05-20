package mx.aragon.unam.controller.productos;

import jakarta.validation.Valid;
import mx.aragon.unam.model.entity.producto.CategoriaEntity;
import mx.aragon.unam.service.producto.categorias.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class CategoriasController {

    @Autowired
    private CategoriaService categoriaService;

    @RequestMapping(value = "/admin/categorias", method = RequestMethod.GET)
    public String listaCategoria(Model model){
        List<CategoriaEntity> categorias = categoriaService.findAll();
        model.addAttribute("categorias", categorias);
        return "vistas/admin/categorias/lista-categorias";
    }

    @RequestMapping(value = "/admin/alta-categorias", method = RequestMethod.GET)
    public String altaCategoria(Model model){
        CategoriaEntity categoria = new CategoriaEntity();
        model.addAttribute("categoria", categoria);
        model.addAttribute("contenido","Nueva Categoria");
        return "vistas/admin/categorias/alta-categoria";
    }

    @RequestMapping(value = "/admin/alta-categoria", method = RequestMethod.POST)
    public String guardarCategoria(@Valid @ModelAttribute(value = "categoria") CategoriaEntity categoria, BindingResult result, Model model) {
        System.out.println("ID recibido del formulario: " + categoria.getId());
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println("Error: " + error.getDefaultMessage());
            }
            model.addAttribute("mensajeError", "Error al guardar la categoria");
            return "vistas/admin/categorias/alta-categoria";
        }
        if (categoria.getId() != null) {
            CategoriaEntity existente = categoriaService.findById(categoria.getId());
            if (existente != null) {
                existente.setNombre(categoria.getNombre());
                categoriaService.save(existente);
            } else {
                model.addAttribute("mensajeError", "La categoría a modificar no existe");
                return "vistas/admin/categorias/alta-categoria";
            }
        } else {
            categoriaService.save(categoria);
        }

        model.addAttribute("mensajeExito", "Categoría guardada con éxito");
        List<CategoriaEntity> categorias = categoriaService.findAll();
        model.addAttribute("categorias", categorias);
        return "vistas/admin/categorias/lista-categorias";
    }

    @RequestMapping(value = "/admin/modificar-categoria/{id}", method = RequestMethod.GET)
    public String modificarCategoria(@PathVariable("id") short id, Model model) {
        CategoriaEntity categoria = categoriaService.findById(id);
        model.addAttribute("categoria", categoria);
        model.addAttribute("contenido","Modificar Categoria");
        return "vistas/admin/categorias/alta-categoria";
    }

    @RequestMapping(value = "/admin/eliminar-categoria/{id}", method = RequestMethod.GET)
    public String eliminarCategoria(@PathVariable("id") short id, Model model) {
        categoriaService.deleteById(id);
        return "redirect:/admin/categorias";
    }
}