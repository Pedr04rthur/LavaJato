package appconsole;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Cliente;
import modelo.Lavagem;
import modelo.Servico;
import util.Util;

public class Cadastrar {
    public static void main(String[] args) {
        System.out.println("Conectando ao banco...");
        ObjectContainer manager = Util.conectar();
        
        
        
        
        //..
        Query q = manager.query();
        q.constrain(Object.class);
        q.execute().forEach(manager::delete);
        manager.commit();
        
        
        
        System.out.println("Cadastrando serviços...");
        Servico lavagemSimples = new Servico("Lavagem Simples", 30.0);
        Servico lavagemCompleta = new Servico("Lavagem Completa", 60.0);
        Servico polimento = new Servico("Polimento", 80.0);
        Servico cera = new Servico("Aplicação de Cera", 40.0);
        manager.store(lavagemSimples);
        manager.store(lavagemCompleta);
        manager.store(polimento);
        manager.store(cera);

        System.out.println("Cadastrando clientes...");
        Cliente c1 = new Cliente("12345678900", "João Silva");
        Cliente c2 = new Cliente("98765432100", "Maria Santos");
        Cliente c3 = new Cliente("11122233344", "Pedro Costa");
        manager.store(c1);
        manager.store(c2);
        manager.store(c3);

        System.out.println("Cadastrando lavagens...");
        Lavagem l1 = new Lavagem("15/03/2024", c1);
        l1.adicionarServico(lavagemSimples);
        l1.adicionarServico(polimento);
        
        Lavagem l2 = new Lavagem("20/03/2024", c2);
        l2.adicionarServico(lavagemCompleta);
        
        Lavagem l3 = new Lavagem("25/03/2024", c1);
        l3.adicionarServico(lavagemSimples);
        l3.adicionarServico(cera);
        
        Lavagem l4 = new Lavagem("05/04/2024", c3);
        l4.adicionarServico(lavagemCompleta);
        l4.adicionarServico(polimento);
        l4.adicionarServico(cera);
        
        manager.store(l1);
        manager.store(l2);
        manager.store(l3);
        manager.store(l4);

        manager.commit();
        System.out.println("Cadastro concluído com sucesso!");

        Util.desconectar();
        System.out.println("Desconectado.");
    }
}