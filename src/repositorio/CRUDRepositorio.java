package repositorio;

import java.util.ArrayList;
import java.util.List;

public abstract class CRUDRepositorio<T> {
    protected List<T> objetos = new ArrayList<>();

    public void adicionar(T obj) {
        objetos.add(obj);
    }

    public void remover(T obj) {
        objetos.remove(obj);
    }

    public List<T> listar() {
        return objetos;
    }
}