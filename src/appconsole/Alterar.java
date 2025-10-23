package appconsole;


import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Lavagem;
import modelo.Servico;

public class Alterar {
    protected ObjectContainer db;

    public Alterar() {
        db = Util.conectarBD();
        System.out.println("Alterando relacionamento...");

    }

    public void alterar() {
        // Encontrar a lavagem com ID 1
        Query queryLavagem = db.query();
        queryLavagem.constrain(Lavagem.class);
        queryLavagem.descend("id").constrain(1);
        List<Lavagem> lavagens = queryLavagem.execute();
        
        if (!lavagens.isEmpty()) {
            Lavagem lavagem = lavagens.get(0);
            
            // Encontrar o serviço "Polimento" para remover
            Query queryServico = db.query();
            queryServico.constrain(Servico.class);
            queryServico.descend("nome").constrain("Polimento");
            List<Servico> servicos = queryServico.execute();
            
            if (!servicos.isEmpty()) {
                Servico polimento = servicos.get(0);
                
                // Remover o relacionamento
                System.out.println("Removendo serviço: " + polimento.getNome() + " da lavagem ID: " + lavagem.getId());
                lavagem.removeServico(polimento);
                db.store(lavagem);
                db.commit();
                System.out.println("Relacionamento removido com sucesso!");
            } else {
                System.out.println("Serviço 'Polimento' não encontrado!");
            }
        } else {
            System.out.println("Lavagem com ID 1 não encontrada!");
        }
    }

    public static void main(String[] args) {
        new Alterar().alterar();
    }
}