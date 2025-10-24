package appconsole;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Cliente;
import modelo.Lavagem;

public class Consultar {
    protected ObjectContainer db;

    public Consultar() {
        db = Util.conectarBDSimples();  
        System.out.println("Realizando consultas...\n");
    }

    public void consultar() {
        // CONSULTA 1: Quais as visitas no mês X (março = "03")
        System.out.println("=== CONSULTA 1: Lavagens no mês 03 (Março) ===");
        Query query1 = db.query();
        query1.constrain(Lavagem.class);
        query1.descend("data").constrain("/03/2024").like(); // SODA
        List<Lavagem> lavagensMarco = query1.execute();
        
        for (Lavagem lavagem : lavagensMarco) {
            System.out.println("ID: " + lavagem.getId() + 
                             ", Data: " + lavagem.getData() + 
                             ", Cliente: " + lavagem.getCliente().getNome() +
                             ", Valor: R$" + lavagem.getValorTotal());
        }
        System.out.println("Total: " + lavagensMarco.size() + " lavagens\n");

        // CONSULTA 2: Quais as visitas do cliente X
        System.out.println("=== CONSULTA 2: Lavagens do cliente João Silva ===");
        Query query2 = db.query();
        query2.constrain(Lavagem.class);
        query2.descend("cliente").descend("nome").constrain("João Silva"); // SODA
        List<Lavagem> lavagensJoao = query2.execute();
        
        for (Lavagem lavagem : lavagensJoao) {
            System.out.println("ID: " + lavagem.getId() + 
                             ", Data: " + lavagem.getData() + 
                             ", Serviços: " + lavagem.getServicos().size() +
                             ", Valor: R$" + lavagem.getValorTotal());
        }
        System.out.println("Total: " + lavagensJoao.size() + " lavagens\n");

        // CONSULTA 3: Quais os clientes que tem mais de N visitas (N=1)
        System.out.println("=== CONSULTA 3: Clientes com mais de 1 lavagem ===");
        Query query3 = db.query();
        query3.constrain(Cliente.class);
        List<Cliente> todosClientes = query3.execute();
        
        for (Cliente cliente : todosClientes) {
            if (cliente.getLavagens().size() > 1) {
                System.out.println("Cliente: " + cliente.getNome() + 
                                 ", CPF: " + cliente.getCpf() + 
                                 ", Lavagens: " + cliente.getLavagens().size());
            }
        }
    }

    public static void main(String[] args) {
        new Consultar().consultar();
        Util.desconectar();
    }
}