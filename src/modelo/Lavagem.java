package modelo;

import java.util.ArrayList;
import java.util.List;

public class Lavagem {
    private int id;
    private String data;
    private Cliente cliente;
    private List<Service> servicos;  
    
    public Lavagem(String data, Cliente cliente) {
        this.id = 0;
        this.data = data;
        this.cliente = cliente;
        this.servicos = new ArrayList<>(); 
        if (cliente != null) {
            cliente.addLavagem(this);
        }
    }
    
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
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
    
    public List<Service> getServicos() { return servicos; }
    
    public void addServico(Service servico) { this.servicos.add(servico); }
    
    public void removeServico(Service servico) { 
    	
    	if (this.servicos != null) {
    		this.servicos.remove(servico);
    		
    	   }
    	}
    
    
    public double getValorTotal() {
        double total = 0.0;
        for (Service servico : servicos) {
            total += servico.getPreco();
        }
        return total;
    }
    
    public String getMes() {
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