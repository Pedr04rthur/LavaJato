package appconsole;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Lavagem;
import modelo.Servico;
import util.Util;

import java.util.List;

public class Alterar {
    public static void main(String[] args) {
        System.out.println("Conectando ao banco...");
        ObjectContainer manager = Util.conectar();

        Query qLavagem = manager.query();
        qLavagem.constrain(Lavagem.class);
        qLavagem.descend("data").constrain("15/03/2024"); 
        List<Lavagem> lavagens = qLavagem.execute();
       
        if (lavagens.isEmpty()) {
            System.out.println("Lavagem de 15/03/2024 não encontrada.");
        } else {
            Lavagem lavagem = lavagens.get(0);
            System.out.println("Encontrada lavagem ID: " + lavagem.getId());
            
          
            Query qServico = manager.query();
            qServico.constrain(Servico.class);
            qServico.descend("nome").constrain("Polimento");
            List<Servico> servicos = qServico.execute();
            
            if (!servicos.isEmpty()) {
                Servico polimento = servicos.get(0);
                
                System.out.println("Removendo Polimento da lavagem " + lavagem.getId());
                lavagem.removerServico(polimento);
                
                manager.store(lavagem);
                manager.commit();
                
                System.out.println("Relacionamento removido com sucesso!");
            } else {
                System.out.println("Serviço Polimento não encontrado.");
            }
        }

        Util.desconectar();
        System.out.println("Desconectado.");
    }
}