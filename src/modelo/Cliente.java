package modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private int id;
    private String cpf;
    private String nome;
    private Localizacao localizacao;
    private List<Lavagem> lavagens;
    
    public Cliente(String cpf, String nome) {
        this.cpf = cpf;
        this.nome = nome;
        this.localizacao = localizacao;
        this.lavagens = new ArrayList<>();
    }


	public void adicionarLavagem(Lavagem lavagem) { 
        this.lavagens.add(lavagem); 
    }
    
    public void removerLavagem(Lavagem lavagem) { 
        this.lavagens.remove(lavagem); 
    }

    
    public int getId() { return id; }
    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public Localizacao getLocalizacao() { return localizacao; }
    public List<Lavagem> getLavagens() { return lavagens; }
    
    @Override
    public String toString() {
        return nome + " (" + cpf + ") - " + localizacao + " - Lavagens: " + lavagens.size();
    }
}