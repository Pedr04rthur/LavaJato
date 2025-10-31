package appconsole;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Cliente;
import modelo.Lavagem;
import modelo.Service;

public class Listar {
    protected ObjectContainer db;

    public Listar() {
        //  conecta ao banco e ativa o ControleID
        db = Util.conectarBD();
        System.out.println("Iniciando listagem de dados...\n");
    }

    public void listar() {
        try {
            // Listar Clientes
            System.out.println("=== CLIENTES ===");
            Query queryClientes = db.query();
            queryClientes.constrain(Cliente.class);
            List<Cliente> clientes = queryClientes.execute();
            
            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente cadastrado.");
            }
            
            for (Cliente cliente : clientes) {
                System.out.println("Cliente: " + cliente);
                
                // Exibe as lavagens de cada cliente
                for (Lavagem lavagem : cliente.getLavagens()) {
                    // Requer que Lavagem.toString() exista
                    System.out.println("  - " + lavagem);
                }
            }

            // Listar Lavagens
            System.out.println("\n=== LAVAGENS ===");
            Query queryLavagens = db.query();
            queryLavagens.constrain(Lavagem.class);
            List<Lavagem> lavagens = queryLavagens.execute();
            
            if (lavagens.isEmpty()) {
                System.out.println("Nenhuma lavagem cadastrada.");
            }
            
            for (Lavagem lavagem : lavagens) {
                // Requer que Lavagem.toString() e Lavagem.getServicos() existam
                System.out.println("Lavagem: " + lavagem);
                
                // Exibe os serviços de cada lavagem
                for (Service servico : lavagem.getServicos()) { // Ajustado para getListaServico
                    // Requer que Service.toString() exista
                    System.out.println("    * " + servico);
                }
            }

            // Listar Serviços
            System.out.println("\n=== SERVIÇOS ===");
            Query queryServicos = db.query();
            queryServicos.constrain(Service.class);
            List<Service> servicos = queryServicos.execute();
            
            if (servicos.isEmpty()) {
                System.out.println("Nenhum serviço cadastrado.");
            }
            
            for (Service servico : servicos) {
                // Requer que Service.toString() exista
                System.out.println("Serviço: " + servico);
            }
            
        } catch (Exception e) {
            System.out.println("Ocorreu um erro na listagem: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Util.desconectar();
        }
    }

    // Método principal para execução individual
    public static void main(String[] args) {
        new Listar().listar();
    }
}