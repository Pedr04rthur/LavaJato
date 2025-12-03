package repositorio;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import modelo.Cliente;
import java.util.List;

public class RepositorioCliente extends CRUDRepositorio<Cliente> {
    private static RepositorioCliente instancia;
    
    private RepositorioCliente(ObjectContainer manager) {
        super(manager);
    }
    
    public static RepositorioCliente getInstancia(ObjectContainer manager) {
        if (instancia == null) {
            instancia = new RepositorioCliente(manager);
        }
        return instancia;
    }
    
    @Override
    public Cliente ler(Object chave) {
        String cpf = (String) chave;
        Query q = manager.query();
        q.constrain(Cliente.class);
        q.descend("cpf").constrain(cpf);
        List<Cliente> resultados = q.execute();
        return resultados.size() > 0 ? resultados.get(0) : null;
    }
    
    @Override
    public Class<Cliente> getClasse() {
        return Cliente.class;
    }
}