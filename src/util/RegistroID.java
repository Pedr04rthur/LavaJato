package util;

public class RegistroID {
    
    private String nome;
    private int ultimoId;
    transient boolean modificado = false;
    
    public RegistroID(String nome) {
        this.nome = nome;
        this.ultimoId = 0;
    }
    
    public void incrementarID() {
        this.ultimoId++;
        setModificado(true);
    }
    
    public boolean isModificado() {
        return modificado;
    }
    
    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }
    
    public int getId() {
        return this.ultimoId;
    }
    
    public String getNome() {
        return nome;
    }
    
    @Override
    public String toString() {
        return "RegistroID [nome=" + nome + ", ultimoId=" + ultimoId + "]";
    }
}