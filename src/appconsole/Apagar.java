package appconsole;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Cliente;
import modelo.Lavagem;

public class Apagar {
    protected ObjectContainer db;

    public Apagar() {
        // Conectar de forma simples, sem ativar ControleID
        try {
            db = Util.conectarBDSimples();
            System.out.println("Apagando cliente Pedro Costa...");
        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
    }

    public void apagar() {
        try {
            // Buscar o cliente "Pedro Costa"
            Query query = db.query();
            query.constrain(Cliente.class);
            query.descend("nome").constrain("Pedro Costa");
            List<Cliente> clientes = query.execute();
            
            if (!clientes.isEmpty()) {
                Cliente cliente = clientes.get(0);
                System.out.println("Encontrado cliente: " + cliente.getNome() + " (ID: " + cliente.getId() + ")");
                System.out.println("Lavagens associadas: " + cliente.getLavagens().size());
                
                // Método SIMPLES: Apagar apenas o cliente (deixe o cascade cuidar do resto)
                System.out.println("Apagando cliente...");
                db.delete(cliente);
                db.commit();
                
                System.out.println("Cliente apagado com sucesso!");
            } else {
                System.out.println("Cliente 'Pedro Costa' não encontrado!");
            }
            
        } catch (Exception e) {
            System.out.println("Erro durante exclusão: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Apagar().apagar();
        // Não chama Util.desconectar() para evitar conflito
    }
}