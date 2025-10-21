package modelo;

public class Servico {
    private static int proxId = 1;
    private final int id;
    private String nome;
    private double preco;
    
    public Servico(String nome, double preco) {
        this.id = proxId++;
        this.nome = nome;
        this.preco = preco;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }
    
    @Override
    public String toString() {
        return "Serviço[ID=" + id + ", Nome=" + nome + ", Preço=R$" + preco + "]";
    }
}