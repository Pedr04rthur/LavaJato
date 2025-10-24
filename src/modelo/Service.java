package modelo;

public class Service {
    private static int proxId = 1;
    private final int id;
    private String nome;
    private double preco;
    
    public Service(String nome, double preco) {
        this.id = proxId++;
        this.nome = nome;
        this.preco = preco;
    }
    
    // getters e Setters
    public int getId() { return id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public double getPreco() { return preco; }
    
    @Override
    public String toString() {
        return "Serviço[ID=" + id + ", Nome=" + nome + ", Preço=R$" + preco + "]";
    }
}