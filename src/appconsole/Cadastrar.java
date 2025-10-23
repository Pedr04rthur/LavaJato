package appconsole;

import com.db4o.ObjectContainer;

import modelo.Cliente;
import modelo.Lavagem;
import modelo.Servico;

public class Cadastrar {
    protected ObjectContainer db;
    
    public Cadastrar() {
        db = Util.conectarBD();
        System.out.println("Cadastrando dados...");
    }
    
    public void cadastrar() {
        // Criar serviços
        Servico lavagemSimples = new Servico("Lavagem Simples", 30.0);
        Servico lavagemCompleta = new Servico("Lavagem Completa", 60.0);
        Servico polimento = new Servico("Polimento", 80.0);
        Servico cera = new Servico("Aplicação de Cera", 40.0);
        
        db.store(lavagemSimples);
        db.store(lavagemCompleta);
        db.store(polimento);
        db.store(cera);

        // Criar clientes
        Cliente cliente1 = new Cliente("123.456.789-00", "João Silva", -23.5505, -46.6333);
        Cliente cliente2 = new Cliente("987.654.321-00", "Maria Santos", -23.5489, -46.6388);
        Cliente cliente3 = new Cliente("111.222.333-44", "Pedro Costa", -23.5510, -46.6350);
        
        db.store(cliente1);
        db.store(cliente2);
        db.store(cliente3);

        // Criar lavagens
        Lavagem lavagem1 = new Lavagem("15/03/2024", cliente1);
        lavagem1.addServico(lavagemSimples);
        lavagem1.addServico(polimento);
        
        Lavagem lavagem2 = new Lavagem("20/03/2024", cliente2);
        lavagem2.addServico(lavagemCompleta);
        
        Lavagem lavagem3 = new Lavagem("25/03/2024", cliente1);
        lavagem3.addServico(lavagemSimples);
        lavagem3.addServico(cera);
        
        Lavagem lavagem4 = new Lavagem("05/04/2024", cliente3);
        lavagem4.addServico(lavagemCompleta);
        lavagem4.addServico(polimento);
        lavagem4.addServico(cera);
        
        db.store(lavagem1);
        db.store(lavagem2);
        db.store(lavagem3);
        db.store(lavagem4);

        db.commit();
        System.out.println("Cadastro realizado com sucesso!");
        System.out.println("- 4 Serviços criados");
        System.out.println("- 3 Clientes criados"); 
        System.out.println("- 4 Lavagens criadas");
    }
    
    public static void main(String[] args) {
        new Cadastrar().cadastrar();
        Util.desconectar();
    }
}