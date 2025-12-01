package repositorio;

import modelo.Lavagem;

public class RepositorioLavagem extends CRUDRepositorio<Lavagem> {
    private static RepositorioLavagem instancia;

    private RepositorioLavagem(){}

    public static RepositorioLavagem getInstancia(){
        if(instancia == null)
            instancia = new RepositorioLavagem();
        return instancia;
    }
}