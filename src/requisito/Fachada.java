package requisito;
import java.util.List;

import modelo.Cliente;
import modelo.Lavagem;
import modelo.Servico;
import repositorio.RepositorioCliente;
import repositorio.RepositorioLavagem;
import repositorio.RepositorioServico;

public class Fachada {

    private static RepositorioCliente clientes = RepositorioCliente.getInstancia();
    private static RepositorioServico servicos = RepositorioServico.getInstancia();
    private static RepositorioLavagem lavagens = RepositorioLavagem.getInstancia();

    // --------- CLIENTES -----------
    public static Cliente cadastrarCliente(String nome, String cpf) throws Exception {
        // verifica unicidade por CPF
        for(Cliente c : clientes.listar()) {
            if(c.getCpf().equals(cpf)) {
                throw new Exception("Já existe cliente com esse CPF.");
            }
        }
        Cliente c = new Cliente(cpf, nome);
        clientes.adicionar(c);
        return c;
    }

    public static List<Cliente> listarClientes() {
        return clientes.listar();
    }

    // --------- SERVICOS -----------
    public static Servico cadastrarServico(String nome, double preco) throws Exception {
        for(Servico s : servicos.listar()) {
            if(s.getNome().equalsIgnoreCase(nome)) {
                throw new Exception("Já existe um serviço com esse nome.");
            }
        }
        Servico s = new Servico(nome, preco);
        servicos.adicionar(s);
        return s;
    }

    public static List<Servico> listarServicos() {
        return servicos.listar();
    }

    // --------- LAVAGENS -----------
    public static Lavagem cadastrarLavagem(Cliente cliente) {
        Lavagem l = new Lavagem(null, cliente);
        lavagens.adicionar(l);
        return l;
    }

    public static List<Lavagem> listarLavagens() {
        return lavagens.listar();
    }

    public static void apagarLavagem(Lavagem l) throws Exception {
        if(!lavagens.listar().contains(l)) {
            throw new Exception("Lavagem não encontrada.");
        }
        lavagens.remover(l);
    }

    public static void adicionarServicoNaLavagem(Lavagem lavagem, Servico servico) throws Exception {
        if(lavagem.getServicos().contains(servico)) {
            throw new Exception("A lavagem já contém esse serviço.");
        }
        lavagem.getServicos().add(servico);
    }

    public static void removerServicoDaLavagem(Lavagem lavagem, Servico servico) throws Exception {
        if(!lavagem.getServicos().contains(servico)) {
            throw new Exception("Esse serviço não está na lavagem.");
        }
        lavagem.getServicos().remove(servico);
    }
}