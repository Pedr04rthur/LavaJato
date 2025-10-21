package modelo;

import java.util.ArrayList;
import java.util.List;

public class Lavagem {
    private static int proxId = 1;
    private final int id;
    private String data;
    private Cliente cliente;
    private List<Servico> servicos;
    
    public Lavagem(String data, Cliente cliente) {
        this.id = proxId++;
        this.data = data;
        this.cliente = cliente;
        this.servicos = new ArrayList<>();
        cliente.addLavagem(this);
    }
    
    // Getters e Setters
    public int getId() { return id; }
    
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { 
        if (this.cliente != null) {
            this.cliente.removeLavagem(this);
        }
        this.cliente = cliente;
        if (cliente != null) {
            cliente.addLavagem(this);
        }
    }
    
    public List<Servico> getServicos() { return servicos; }
    public void addServico(Servico servico) { this.servicos.add(servico); }
    public void removeServico(Servico servico) { this.servicos.remove(servico); }
    
    public double getValorTotal() {
        double total = 0.0;
        for (Servico servico : servicos) {
            total += servico.getPreco();
        }
        return total;
    }
    
    public String getMes() {
        // formato DD/MM/AAAA - retorna "03" para março
        return data.split("/")[1];
    }
    
    @Override
    public String toString() {
        return "Lavagem[ID=" + id + ", Data=" + data + 
               ", Cliente=" + (cliente != null ? cliente.getNome() : "Nenhum") + 
               ", Serviços=" + servicos.size() + 
               ", Valor Total=R$" + getValorTotal() + "]";
    }
}