package appconsole;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Cliente;
import modelo.Lavagem;
import modelo.Servico;
import util.Util;

import java.util.List;

public class Listar {
    public static void main(String[] args) {
        System.out.println("Conectando ao banco...");
        ObjectContainer manager = Util.conectar();
        
        
        
        System.out.println("\n=== CLIENTES ===");
        Query qCliente = manager.query();
        qCliente.constrain(Cliente.class);
        List<Cliente> clientes = qCliente.execute();
        clientes.forEach(System.out::println);

        System.out.println("\n=== LAVAGENS ===");
        Query qLavagem = manager.query();
        qLavagem.constrain(Lavagem.class);
        List<Lavagem> lavagens = qLavagem.execute();
        lavagens.forEach(System.out::println);

        System.out.println("\n=== SERVIÇOS ===");
        Query qServico = manager.query();
        qServico.constrain(Servico.class);
        List<Servico> servicos = qServico.execute();
        servicos.forEach(System.out::println);

        Util.desconectar();
        System.out.println("\nDesconectado.");
    }
}