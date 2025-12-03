package repositorio;

import java.util.List;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;

public abstract class CRUDRepositorio<T> {
    protected ObjectContainer manager;
    
    public CRUDRepositorio(ObjectContainer manager) {
        this.manager = manager;
    }
    
    public void criar(T obj) {
        manager.store(obj);
    }
    
    public void atualizar(T obj) {
        manager.store(obj);
    }
    
    public void apagar(T obj) {
        manager.delete(obj);
    }
    
    public List<T> listar() {
        Query q = manager.query();
        q.constrain(getClasse());
        return q.execute();
    }
    
    public abstract T ler(Object chave);
    public abstract Class<T> getClasse();
}