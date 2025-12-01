package repositorio;

import modelo.Cliente;

public class RepositorioCliente extends CRUDRepositorio<Cliente> {
    private static RepositorioCliente instancia;

    private RepositorioCliente(){}

    public static RepositorioCliente getInstancia(){
        if(instancia == null)
            instancia = new RepositorioCliente();
        return instancia;
    }
}