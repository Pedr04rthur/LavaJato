package modelo;

import java.util.Objects;

public class Service {
    // private static int proxId = 1;
    private int id;
    private String nome;
    private double preco;
    
    public Service(String nome, double preco) {
        this.id = 0;
        this.nome = nome;
        this.preco = preco;
    }
    
   
    public int getId() { return id; }
    
    public String getNome() { return nome; }
    
    public void setNome(String nome) { this.nome = nome; }
    
    public double getPreco() { return preco; }
    
    public void stPreco(double preco) { this.preco = preco;}
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        // Compara pela chave primária (id)
        return id == service.id; 
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Serviço[ID=" + id + ", Nome=" + nome + ", Preço=R$" + preco + "]";
    }
}