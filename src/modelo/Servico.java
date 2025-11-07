package modelo;

public class Servico {
    private int id;
    private String nome;
    private double preco;
    
    public Servico(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    
    @Override
    public String toString() {
        return nome + " - R$" + preco;
    }
}