package appconsole;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Cliente;
import modelo.Lavagem;

public class Apagar {
    protected ObjectContainer db;

    public Apagar() {
    	
        try {
            db = Util.conectarBDSimples();
            System.out.println("Iniciando exclusão de cliente...");
        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            // Se a conexão falhar, o programa deve encerrar
            System.exit(1); 
        }
    }

    public void apagar() {
        try {
            // Buscar o cliente "Pedro Costa"
            Query query = db.query();
            query.constrain(Cliente.class);
            // Busca pelo campo 'nome' com valor "Pedro Costa"
            query.descend("nome").constrain("Pedro Costa"); 
            List<Cliente> clientes = query.execute();
            
            if (!clientes.isEmpty()) {
                Cliente cliente = clientes.get(0);
                
                System.out.println("----------------------------------------");
                // Nota: O método getId() e getLavagens() devem existir na classe Cliente.
                System.out.println("Encontrado cliente: " + cliente.getNome() + " (ID: " + cliente.getId() + ")");
                System.out.println("Lavagens associadas: " + cliente.getLavagens().size());
                
                // Método SIMPLES: Apagar apenas o cliente
                // ATENÇÃO: Se o cascadeOnDelete não estiver ativo, isso pode deixar órfãs as lavagens.
                System.out.println("Apagando cliente...");
                
                db.delete(cliente);
                db.commit();
                
                System.out.println("Cliente '" + cliente.getNome() + "' apagado com sucesso!");
                System.out.println("----------------------------------------");
                
         
            }
            
        } catch (Exception e) {
            System.out.println("Erro durante exclusão: " + e.getMessage());
            e.printStackTrace(); // Imprime o rastreamento do erro para debug
        } finally {
            // Boa prática: garantir que a conexão seja fechada.
            Util.desconectar();
        }
    }

    // Método principal para execução individual
    public static void main(String[] args) {
        new Apagar().apagar();
    }
}