package appconsole;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Cliente;
import modelo.Lavagem;
import util.Util;

import java.util.List;

public class Apagar {
    public static void main(String[] args) {
        System.out.println("Conectando ao banco...");
        ObjectContainer manager = Util.conectar();

        Query q = manager.query();
        q.constrain(Cliente.class);
        q.descend("nome").constrain("Pedro Costa");
        List<Cliente> clientes = q.execute();

        if (clientes.isEmpty()) {
            System.out.println("Cliente não encontrado.");
        } else {
            Cliente cliente = clientes.get(0);
            System.out.println("Cliente encontrado: " + cliente);

            System.out.println("Apagando lavagens do cliente...");
            for (Lavagem lavagem : cliente.getLavagens()) {
                manager.delete(lavagem);
                System.out.println("Lavagem apagada: " + lavagem.getId());
            }

            System.out.println("Apagando cliente...");
            manager.delete(cliente);

            manager.commit();
            System.out.println("Cliente e lavagens apagados com sucesso.");
        }

        Util.desconectar();
        System.out.println("Desconectado.");
    }
}