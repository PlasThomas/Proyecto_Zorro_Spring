package mx.aragon.unam.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/product-images/**")
                .addResourceLocations("file:/home/thomas/Documentos/Escuela/Octavo_semestre/Spring/ProyectoFinal/Proyecto_Zorro_Spring/productos");
        // Aqui cambias mi ruta de la carpeta personal o donde guardes la imagenes
    }
}