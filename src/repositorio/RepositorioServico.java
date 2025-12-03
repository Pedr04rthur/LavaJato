package repositorio;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import modelo.Servico;
import java.util.List;

public class RepositorioServico extends CRUDRepositorio<Servico> {
    private static RepositorioServico instancia;
    
    private RepositorioServico(ObjectContainer manager) {
        super(manager);
    }
    
    public static RepositorioServico getInstancia(ObjectContainer manager) {
        if (instancia == null) {
            instancia = new RepositorioServico(manager);
        }
        return instancia;
    }
    
    @Override
    public Servico ler(Object chave) {
        String nome = (String) chave;
        Query q = manager.query();
        q.constrain(Servico.class);
        q.descend("nome").constrain(nome);
        List<Servico> resultados = q.execute();
        return resultados.size() > 0 ? resultados.get(0) : null;
    }
    
    @Override
    public Class<Servico> getClasse() {
        return Servico.class;
    }
}