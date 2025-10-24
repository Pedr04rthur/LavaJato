package modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private int id;
    private String cpf;
    private String nome;
    private double latitude;
    private double longitude; 
    private List<Lavagem> lavagens;
    
    public Cliente(String cpf, String nome, double latitude, double longitude) {
        this.id = 0;
        this.cpf = cpf;
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lavagens = new ArrayList<>();
    }
    
    // Getters e Setters
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public String getCpf() { 
        return cpf; 
    }
    
    public void setCpf(String cpf) { 
        this.cpf = cpf; 
    }
    
    public String getNome() { 
        return nome; 
    }
    
    public void setNome(String nome) { 
        this.nome = nome; 
    }
    
    public double getLatitude() { 
        return latitude; 
    }
    
    public double getLongitude() { 
        return longitude; 
    }
    
    public void setLocalizacao(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public List<Lavagem> getLavagens() { 
        return lavagens; 
    }
    
    // Métodos para gerenciar lavagens
    public void addLavagem(Lavagem lavagem) { 
        if (!this.lavagens.contains(lavagem)) {
            this.lavagens.add(lavagem); 
        }
    }
    
    public void removeLavagem(Lavagem lavagem) { 
        this.lavagens.remove(lavagem); 
    }
    
    @Override
    public String toString() {
        return "Cliente[ID=" + id + 
               ", CPF=" + cpf + 
               ", Nome=" + nome + 
               ", Localização=(" + latitude + ", " + longitude + ")" + 
               ", Lavagens=" + lavagens.size() + "]";
    }
}