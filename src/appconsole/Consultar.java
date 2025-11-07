package appconsole;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Cliente;
import modelo.Lavagem;
import util.Util;

import java.util.List;

public class Consultar {
    public static void main(String[] args) {
        System.out.println("Conectando ao banco...");
        ObjectContainer manager = Util.conectar();

        
        System.out.println("\n1. Lavagens em Março de 2024:");
        Query q1 = manager.query();
        q1.constrain(Lavagem.class);
        q1.descend("data").constrain("/03/2024").like();
        List<Lavagem> lavagensMarco = q1.execute();
        lavagensMarco.forEach(System.out::println);

        
        System.out.println("\n2. Lavagens do João Silva:");
        Query q2 = manager.query();
        q2.constrain(Lavagem.class);
        q2.descend("cliente").descend("nome").constrain("João Silva");
        List<Lavagem> lavagensJoao = q2.execute();
        lavagensJoao.forEach(System.out::println);

        
        System.out.println("\n3. Clientes com mais de 1 lavagem:");
        Query q3 = manager.query();
        q3.constrain(Cliente.class);
        List<Cliente> clientes = q3.execute();
        clientes.stream()
            .filter(c -> c.getLavagens().size() > 1)
            .forEach(c -> System.out.println(c.getNome() + " - " + c.getLavagens().size() + " lavagens"));

        Util.desconectar();
        System.out.println("\nDesconectado.");
    }
}