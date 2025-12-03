package requisito;

import repositorio.RepositorioCliente;
import repositorio.RepositorioLavagem;
import repositorio.RepositorioServico;
import modelo.Cliente;
import modelo.Lavagem;
import modelo.Servico;
import util.Util;
import java.util.List;
import java.util.ArrayList;

public class Fachada {
    private static RepositorioCliente clientes;
    private static RepositorioServico servicos;
    private static RepositorioLavagem lavagens;
    
    static {
        inicializar();
    }
    
    private static void inicializar() {
        var manager = Util.conectar();
        clientes = RepositorioCliente.getInstancia(manager);
        servicos = RepositorioServico.getInstancia(manager);
        lavagens = RepositorioLavagem.getInstancia(manager);
    }
    
    // --------- CLIENTES -----------
    public static Cliente criarCliente(String cpf, String nome) throws Exception {
        // Verifica unicidade por CPF
        Cliente existente = clientes.ler(cpf);
        if (existente != null) {
            throw new Exception("Já existe cliente com esse CPF.");
        }
        Cliente c = new Cliente(cpf, nome);
        clientes.criar(c);
        return c;
    }
    
    public static List<Cliente> listarClientes() {
        return clientes.listar();
    }
    
    public static Cliente buscarCliente(String cpf) {
        return clientes.ler(cpf);
    }
    
    public static void apagarCliente(String cpf) throws Exception {
        Cliente c = clientes.ler(cpf);
        if (c == null) {
            throw new Exception("Cliente não encontrado.");
        }
        // Apaga lavagens do cliente primeiro
        for (Lavagem l : c.getLavagens()) {
            lavagens.apagar(l);
        }
        clientes.apagar(c);
    }
    
    // --------- SERVIÇOS -----------
    public static Servico criarServico(String nome, double preco) throws Exception {
        // Verifica unicidade por nome
        Servico existente = servicos.ler(nome);
        if (existente != null) {
            throw new Exception("Já existe um serviço com esse nome.");
        }
        Servico s = new Servico(nome, preco);
        servicos.criar(s);
        return s;
    }
    
    public static List<Servico> listarServicos() {
        return servicos.listar();
    }
    
    public static Servico buscarServico(String nome) {
        return servicos.ler(nome);
    }
    
    // --------- LAVAGENS -----------
    public static Lavagem criarLavagem(String data, String cpfCliente) throws Exception {
        Cliente cliente = clientes.ler(cpfCliente);
        if (cliente == null) {
            throw new Exception("Cliente não encontrado.");
        }
        Lavagem l = new Lavagem(data, cliente);
        lavagens.criar(l);
        return l;
    }
    
    public static List<Lavagem> listarLavagens() {
        return lavagens.listar();
    }
    
    public static void apagarLavagem(int id) throws Exception {
        Lavagem l = lavagens.ler(id);
        if (l == null) {
            throw new Exception("Lavagem não encontrada.");
        }
        lavagens.apagar(l);
    }
    
    public static void adicionarServicoNaLavagem(int idLavagem, String nomeServico) throws Exception {
        Lavagem lavagem = lavagens.ler(idLavagem);
        if (lavagem == null) {
            throw new Exception("Lavagem não encontrada.");
        }
        
        Servico servico = servicos.ler(nomeServico);
        if (servico == null) {
            throw new Exception("Serviço não encontrado.");
        }
        
        // Verifica se o serviço já está na lavagem (regra de negócio)
        if (lavagem.getServicos().contains(servico)) {
            throw new Exception("Este serviço já está na lavagem.");
        }
        
        lavagem.adicionarServico(servico);
        lavagens.atualizar(lavagem);
    }
    
    public static void removerServicoDaLavagem(int idLavagem, String nomeServico) throws Exception {
        Lavagem lavagem = lavagens.ler(idLavagem);
        if (lavagem == null) {
            throw new Exception("Lavagem não encontrada.");
        }
        
        Servico servico = servicos.ler(nomeServico);
        if (servico == null) {
            throw new Exception("Serviço não encontrado.");
        }
        
        if (!lavagem.getServicos().contains(servico)) {
            throw new Exception("Este serviço não está na lavagem.");
        }
        
        lavagem.removerServico(servico);
        lavagens.atualizar(lavagem);
    }
    
    // --------- CONSULTAS -----------
    public static List<Lavagem> consultarLavagensPorMes(String mesAno) {
        // Formato: "03/2024" para março de 2024
        return lavagens.listarPorMes(mesAno);
    }
    
    public static List<Lavagem> consultarLavagensPorCliente(String nomeCliente) {
        return lavagens.listarPorCliente(nomeCliente);
    }
    
    public static List<Cliente> consultarClientesComMaisDeNLavagens(int n) {
        List<Cliente> resultado = new ArrayList<>();
        for (Cliente c : clientes.listar()) {
            if (c.getLavagens().size() > n) {
                resultado.add(c);
            }
        }
        return resultado;
    }
}