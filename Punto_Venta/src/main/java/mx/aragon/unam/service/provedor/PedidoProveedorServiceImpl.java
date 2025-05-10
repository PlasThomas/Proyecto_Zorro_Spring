package mx.aragon.unam.service.provedor;

import mx.aragon.unam.entity.provedor.PedidoProveedorEntity;
import mx.aragon.unam.repository.provedor.PedidoProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoProveedorServiceImpl implements PedidoProveedorService {

    private final PedidoProveedorRepository pedidoProveedorRepository;

    @Autowired
    public PedidoProveedorServiceImpl(PedidoProveedorRepository pedidoProveedorRepository) {
        this.pedidoProveedorRepository = pedidoProveedorRepository;
    }

    @Override
    public PedidoProveedorEntity save(PedidoProveedorEntity pedido) {
        return pedidoProveedorRepository.save(pedido);
    }

    @Override
    public List<PedidoProveedorEntity> findAll() {
        return pedidoProveedorRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        pedidoProveedorRepository.deleteById(id);
    }

    @Override
    public PedidoProveedorEntity findById(Integer id) {
        return pedidoProveedorRepository.findById(id).orElse(null);
    }
}