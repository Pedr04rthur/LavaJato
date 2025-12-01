package repositorio;

import modelo.Servico;

public class RepositorioServico extends CRUDRepositorio<Servico> {
    private static RepositorioServico instancia;

    private RepositorioServico(){}

    public static RepositorioServico getInstancia(){
        if(instancia == null)
            instancia = new RepositorioServico();
        return instancia;
    }
}