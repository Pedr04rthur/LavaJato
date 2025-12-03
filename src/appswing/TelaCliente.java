package appswing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import requisito.Fachada;
import modelo.Cliente;
import java.awt.*;
import java.util.List;

public class TelaCliente {
    private JFrame frame;
    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField campoCpf, campoNome;
    
    public TelaCliente() {
        frame = new JFrame("Gerenciamento de Clientes");
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        
        criarPainelSuperior();
        criarTabela();
        criarPainelInferior();
        
        carregarDados();
        
        frame.setVisible(true);
    }
    
    private void criarPainelSuperior() {
        JPanel painel = new JPanel(new GridLayout(2, 2, 5, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Cadastro"));
        
        painel.add(new JLabel("CPF:"));
        campoCpf = new JTextField();
        painel.add(campoCpf);
        
        painel.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painel.add(campoNome);
        
        frame.add(painel, BorderLayout.NORTH);
    }
    
    private void criarTabela() {
        modelo = new DefaultTableModel(new Object[]{"CPF", "Nome", "Lavagens"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);
        frame.add(scroll, BorderLayout.CENTER);
    }
    
    private void criarPainelInferior() {
        JPanel painel = new JPanel();
        
        JButton btnCriar = new JButton("Criar");
        btnCriar.addActionListener(e -> criarCliente());
        
        JButton btnApagar = new JButton("Apagar");
        btnApagar.addActionListener(e -> apagarCliente());
        
        JButton btnListar = new JButton("Listar");
        btnListar.addActionListener(e -> carregarDados());
        
        painel.add(btnCriar);
        painel.add(btnApagar);
        painel.add(btnListar);
        
        frame.add(painel, BorderLayout.SOUTH);
    }
    
    private void criarCliente() {
        try {
            String cpf = campoCpf.getText();
            String nome = campoNome.getText();
            
            if (cpf.isEmpty() || nome.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Preencha todos os campos!");
                return;
            }
            
            Fachada.criarCliente(cpf, nome);
            JOptionPane.showMessageDialog(frame, "Cliente criado com sucesso!");
            
            campoCpf.setText("");
            campoNome.setText("");
            carregarDados();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage());
        }
    }
    
    private void apagarCliente() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um cliente!");
            return;
        }
        
        String cpf = (String) modelo.getValueAt(linha, 0);
        int confirm = JOptionPane.showConfirmDialog(frame, "Apagar cliente " + cpf + "?");
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Fachada.apagarCliente(cpf);
                JOptionPane.showMessageDialog(frame, "Cliente apagado!");
                carregarDados();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage());
            }
        }
    }
    
    private void carregarDados() {
        modelo.setRowCount(0);
        List<Cliente> clientes = Fachada.listarClientes();
        
        for (Cliente c : clientes) {
            modelo.addRow(new Object[]{
                c.getCpf(),
                c.getNome(),
                c.getLavagens().size()
            });
        }
    }
}