package appswing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import requisito.Fachada;
import modelo.Lavagem;
import modelo.Cliente;
import java.awt.*;
import java.util.List;

public class TelaConsultas {
    private JFrame frame;
    private JTabbedPane abas;
    
    public TelaConsultas() {
        frame = new JFrame("Consultas do Sistema");
        frame.setSize(700, 500);
        
        abas = new JTabbedPane();
        
        // Aba 1: Lavagens por mês
        abas.addTab("Lavagens por Mês", criarAbaMes());
        
        // Aba 2: Lavagens por cliente
        abas.addTab("Lavagens por Cliente", criarAbaCliente());
        
        // Aba 3: Clientes com mais lavagens
        abas.addTab("Clientes com Mais Lavagens", criarAbaMaisLavagens());
        
        frame.add(abas);
        frame.setVisible(true);
    }
    
    private JPanel criarAbaMes() {
        JPanel painel = new JPanel(new BorderLayout());
        
        JPanel painelSuperior = new JPanel(new FlowLayout());
        JTextField campoMes = new JTextField(10);
        campoMes.setText("03/2024");
        JButton btnBuscar = new JButton("Buscar");
        
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Data", "Cliente", "Serviços", "Total"}, 0);
        JTable tabela = new JTable(modelo);
        
        btnBuscar.addActionListener(e -> {
            String mes = campoMes.getText();
            List<Lavagem> lavagens = Fachada.consultarLavagensPorMes(mes);
            
            modelo.setRowCount(0);
            for (Lavagem l : lavagens) {
                modelo.addRow(new Object[]{
                    l.getId(),
                    l.getData(),
                    l.getCliente().getNome(),
                    l.getServicos().size(),
                    "R$ " + l.getValorTotal()
                });
            }
        });
        
        painelSuperior.add(new JLabel("Mês (MM/AAAA):"));
        painelSuperior.add(campoMes);
        painelSuperior.add(btnBuscar);
        
        painel.add(painelSuperior, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarAbaCliente() {
        JPanel painel = new JPanel(new BorderLayout());
        
        JPanel painelSuperior = new JPanel(new FlowLayout());
        JTextField campoCliente = new JTextField(15);
        campoCliente.setText("João Silva");
        JButton btnBuscar = new JButton("Buscar");
        
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Data", "Serviços", "Total"}, 0);
        JTable tabela = new JTable(modelo);
        
        btnBuscar.addActionListener(e -> {
            String nome = campoCliente.getText();
            List<Lavagem> lavagens = Fachada.consultarLavagensPorCliente(nome);
            
            modelo.setRowCount(0);
            for (Lavagem l : lavagens) {
                modelo.addRow(new Object[]{
                    l.getId(),
                    l.getData(),
                    l.getServicos().size(),
                    "R$ " + l.getValorTotal()
                });
            }
        });
        
        painelSuperior.add(new JLabel("Nome do Cliente:"));
        painelSuperior.add(campoCliente);
        painelSuperior.add(btnBuscar);
        
        painel.add(painelSuperior, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarAbaMaisLavagens() {
        JPanel painel = new JPanel(new BorderLayout());
        
        JPanel painelSuperior = new JPanel(new FlowLayout());
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
        JButton btnBuscar = new JButton("Buscar");
        
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"CPF", "Nome", "Total Lavagens"}, 0);
        JTable tabela = new JTable(modelo);
        
        btnBuscar.addActionListener(e -> {
            int n = (Integer) spinner.getValue();
            List<Cliente> clientes = Fachada.consultarClientesComMaisDeNLavagens(n);
            
            modelo.setRowCount(0);
            for (Cliente c : clientes) {
                modelo.addRow(new Object[]{
                    c.getCpf(),
                    c.getNome(),
                    c.getLavagens().size()
                });
            }
        });
        
        painelSuperior.add(new JLabel("Mais de:"));
        painelSuperior.add(spinner);
        painelSuperior.add(new JLabel("lavagens"));
        painelSuperior.add(btnBuscar);
        
        painel.add(painelSuperior, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        
        return painel;
    }
}