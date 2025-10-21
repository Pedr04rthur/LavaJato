package appconsole;

import modelo.*;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import java.util.List;

public class Listar {
    protected ObjectContainer db;

    public Listar() {
        db = Util.conectarBD();
        System.out.println("Listando dados...\n");
    }

    public void listar() {
        // Listar Clientes
        System.out.println("=== CLIENTES ===");
        Query queryClientes = db.query();
        queryClientes.constrain(Cliente.class);
        List<Cliente> clientes = queryClientes.execute();
        
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
            for (Lavagem lavagem : cliente.getLavagens()) {
                System.out.println("  - " + lavagem);
            }
        }

        // Listar Lavagens
        System.out.println("\n=== LAVAGENS ===");
        Query queryLavagens = db.query();
        queryLavagens.constrain(Lavagem.class);
        List<Lavagem> lavagens = queryLavagens.execute();
        
        for (Lavagem lavagem : lavagens) {
            System.out.println(lavagem);
            for (Servico servico : lavagem.getServicos()) {
                System.out.println("    * " + servico);
            }
        }

        // Listar Serviços
        System.out.println("\n=== SERVIÇOS ===");
        Query queryServicos = db.query();
        queryServicos.constrain(Servico.class);
        List<Servico> servicos = queryServicos.execute();
        
        for (Servico servico : servicos) {
            System.out.println(servico);
        }
    }

    public static void main(String[] args) {
        new Listar().listar();
        Util.desconectarBD();
    }
}