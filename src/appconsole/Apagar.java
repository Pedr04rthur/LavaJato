package appconsole;

import modelo.*;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import java.util.List;

public class Apagar {
    protected ObjectContainer db;

    public Apagar() {
        db = Util.conectarBD();
        System.out.println("Apagando objeto com relacionamentos...");
    }

    public void apagar() {
        // Encontrar o cliente "Pedro Costa" para apagar
        Query query = db.query();
        query.constrain(Cliente.class);
        query.descend("nome").constrain("Pedro Costa");
        List<Cliente> clientes = query.execute();
        
        if (!clientes.isEmpty()) {
            Cliente cliente = clientes.get(0);
            
            System.out.println("Apagando cliente: " + cliente.getNome());
            System.out.println("Este cliente tem " + cliente.getLavagens().size() + " lavagens associadas.");
            
            // Primeiro, precisamos remover as lavagens associadas para evitar objetos órfãos
            for (Lavagem lavagem : cliente.getLavagens()) {
                System.out.println("Apagando lavagem ID: " + lavagem.getId() + " associada ao cliente");
                db.delete(lavagem);
            }
            
            // Agora podemos apagar o cliente
            db.delete(cliente);
            db.commit();
            System.out.println("Cliente e suas lavagens apagados com sucesso!");
        } else {
            System.out.println("Cliente 'Pedro Costa' não encontrado!");
        }
    }

    public static void main(String[] args) {
        new Apagar().apagar();
        Util.desconectarBD();
    }
}