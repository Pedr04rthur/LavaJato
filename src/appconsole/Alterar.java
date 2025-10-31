package appconsole;


import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Lavagem;
import modelo.Service;

public class Alterar {
    protected ObjectContainer db;

    public Alterar() {
        db = Util.conectarBD();
        System.out.println("Alterando relacionamento...");

    }
    public void alterar() {
        try {
            // 1. Encontrar a lavagem com ID 1
            Query queryLavagem = db.query();
            queryLavagem.constrain(Lavagem.class);
            queryLavagem.descend("id").constrain(1);
            List<Lavagem> lavagens = queryLavagem.execute();
            
            if (!lavagens.isEmpty()) {
                Lavagem lavagem = lavagens.get(0);
                
                // 2. Encontrar o serviço "Polimento" para remover (usando Query-by-Example/Native Query)
                Query queryServico = db.query();
                queryServico.constrain(Service.class);
                queryServico.descend("nome").constrain("Polimento");
                List<Service> servicos = queryServico.execute();
                
                if (!servicos.isEmpty()) {
                    Service polimento = servicos.get(0);
                    
                    // 3. Remover o relacionamento (Requer que Lavagem tenha o método removeServico)
                    System.out.println("----------------------------------------");
                    System.out.println("Lavagem ID " + lavagem.getId() + " antes: " + lavagem.getServicos());
                    
                    if (lavagem.getServicos().contains(polimento)) {
                        System.out.println("Removendo serviço: " + polimento.getNome() + " da lavagem ID: " + lavagem.getId());
                        lavagem.removeServico(polimento);
                        
                        // 4. Armazenar (Store) a Lavagem alterada
                        db.store(lavagem);
                        db.commit(); // Persistir a mudança
                        
                        System.out.println("Relacionamento removido com sucesso!");
                        System.out.println("Lavagem ID " + lavagem.getId() + " depois: " + lavagem.getServicos());
                    } else {
                        System.out.println("Serviço 'Polimento' não encontrado na lista da Lavagem ID 1.");
                    }
                    System.out.println("----------------------------------------");
                    
                } else {
                    System.out.println("Erro: Serviço 'Polimento' não encontrado no banco de dados!");
                }
            } else {
                System.out.println("Erro: Lavagem com ID 1 não encontrada!");
            }
            
        } catch (Exception e) {
            System.out.println("Ocorreu um erro na alteração: " + e.getMessage());
        } finally {
             Util.desconectar(); // Desconecta o banco no final
        }
    }

    // Método principal para execução individual
    public static void main(String[] args) {
        new Alterar().alterar();
    }}