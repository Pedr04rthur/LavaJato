package modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String cpf;
    private String nome;
    private double latitude;
    private double longitude; 
    private List<Lavagem> lavagens;
    
    public Cliente(String cpf, String nome, double latitude, double longitude) {
        this.cpf = cpf;
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lavagens = new ArrayList<>();
    }
    
    // Getters e Setters
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public void setLocalizacao(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public List<Lavagem> getLavagens() { return lavagens; }
    public void addLavagem(Lavagem lavagem) { 
        this.lavagens.add(lavagem); 
    }
    public void removeLavagem(Lavagem lavagem) { 
        this.lavagens.remove(lavagem); 
    }
    
    @Override
    public String toString() {
        return "Cliente[CPF=" + cpf + ", Nome=" + nome + 
               ", Localização=(" + latitude + ", " + longitude + ")" + 
               ", Lavagens=" + lavagens.size() + "]";
    }
}