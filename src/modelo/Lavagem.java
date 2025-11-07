package modelo;

import java.util.ArrayList;
import java.util.List;

public class Lavagem {
    private int id;
    private String data;
    private Cliente cliente;
    private List<Servico> servicos;
    
    public Lavagem(String data, Cliente cliente) {
        this.data = data;
        this.cliente = cliente;
        this.servicos = new ArrayList<>();
        cliente.adicionarLavagem(this); 
    }

    public void adicionarServico(Servico servico) { 
        this.servicos.add(servico); 
    }
    
    public void removerServico(Servico servico) { 
        this.servicos.remove(servico); 
    }
    
    public double getValorTotal() {
        return servicos.stream().mapToDouble(Servico::getPreco).sum();
    }

    // Getters
    public int getId() { return id; }
    public String getData() { return data; }
    public Cliente getCliente() { return cliente; }
    public List<Servico> getServicos() { return servicos; }
    
    @Override
    public String toString() {
        return "Lavagem " + id + " - " + data + " - Cliente: " + cliente.getNome() + 
               " - Serviços: " + servicos.size() + " - Total: R$" + getValorTotal();
    }
}