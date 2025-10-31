package appconsole;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Cliente;
import modelo.Lavagem;

public class Consultar {
    protected ObjectContainer db;

    public Consultar() {
        // Assume-se que Util.conectarBDSimples() conecta ao banco sem ControleID
        db = Util.conectarBDSimples();  
        System.out.println("Realizando consultas...\n");
    }

    public void consultar() {
        try {
            // CONSULTA 1: Quais as visitas no mês X (março = "03")
            System.out.println("=== CONSULTA 1: Lavagens no mês 03 (Março) ===");
            Query query1 = db.query();
            query1.constrain(Lavagem.class);
            // Busca por strings que contêm '/03/2024' no campo 'data'
            query1.descend("data").constrain("/03/2024").like(); 
            List<Lavagem> lavagensMarco = query1.execute();
            
            for (Lavagem lavagem : lavagensMarco) {
                // Métodos getCliente() e getValorTotal() são necessários
                System.out.println("ID: " + lavagem.getId() + 
                                 ", Data: " + lavagem.getData() + 
                                 ", Cliente: " + lavagem.getCliente().getNome() +
                                 ", Valor: R$" + String.format("%.2f", lavagem.getValorTotal()));
            }
            System.out.println("Total: " + lavagensMarco.size() + " lavagens\n");

            // CONSULTA 2: Quais as visitas do cliente X
            System.out.println("=== CONSULTA 2: Lavagens do cliente João Silva ===");
            Query query2 = db.query();
            query2.constrain(Lavagem.class);
            // Navega do objeto Lavagem para o objeto Cliente e busca pelo campo 'nome'
            query2.descend("cliente").descend("nome").constrain("João Silva"); 
            List<Lavagem> lavagensJoao = query2.execute();
            
            for (Lavagem lavagem : lavagensJoao) {
                // Métodos getServicos() e getValorTotal() são necessários
                System.out.println("ID: " + lavagem.getId() + 
                                 ", Data: " + lavagem.getData() + 
                                 ", Serviços: " + lavagem.getServicos().size() + // Alterado para getListaServico()
                                 ", Valor: R$" + String.format("%.2f", lavagem.getValorTotal()));
            }
            System.out.println("Total: " + lavagensJoao.size() + " lavagens\n");

            // CONSULTA 3: Quais os clientes que tem mais de N visitas (N=1)
            System.out.println("=== CONSULTA 3: Clientes com mais de 1 lavagem ===");
            Query query3 = db.query();
            query3.constrain(Cliente.class);
            List<Cliente> todosClientes = query3.execute();
            
            for (Cliente cliente : todosClientes) {
                // Requer que Cliente tenha o método getLavagens() que retorne a lista de Lavagens
                if (cliente.getLavagens().size() > 1) { 
                    System.out.println("Cliente: " + cliente.getNome() + 
                                     ", CPF: " + cliente.getCpf() + 
                                     ", Lavagens: " + cliente.getLavagens().size());
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Ocorreu um erro na consulta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Util.desconectar();
        }
    }

    // Método principal para execução individual
    public static void main(String[] args) {
        new Consultar().consultar();
    }
}