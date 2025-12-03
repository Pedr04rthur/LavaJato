package repositorio;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import modelo.Lavagem;
import java.util.List;

public class RepositorioLavagem extends CRUDRepositorio<Lavagem> {
    private static RepositorioLavagem instancia;
    
    private RepositorioLavagem(ObjectContainer manager) {
        super(manager);
    }
    
    public static RepositorioLavagem getInstancia(ObjectContainer manager) {
        if (instancia == null) {
            instancia = new RepositorioLavagem(manager);
        }
        return instancia;
    }
    
    @Override
    public Lavagem ler(Object chave) {
        Integer id = (Integer) chave;
        Query q = manager.query();
        q.constrain(Lavagem.class);
        q.descend("id").constrain(id);
        List<Lavagem> resultados = q.execute();
        return resultados.size() > 0 ? resultados.get(0) : null;
    }
    
    @Override
    public Class<Lavagem> getClasse() {
        return Lavagem.class;
    }
    
    // Métodos de consulta específicos
    public List<Lavagem> listarPorMes(String mesAno) {
        Query q = manager.query();
        q.constrain(Lavagem.class);
        q.descend("data").constrain("/" + mesAno + "/").like();
        return q.execute();
    }
    
    public List<Lavagem> listarPorCliente(String nomeCliente) {
        Query q = manager.query();
        q.constrain(Lavagem.class);
        q.descend("cliente").descend("nome").constrain(nomeCliente);
        return q.execute();
    }
}