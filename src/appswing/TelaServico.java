package appswing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import requisito.Fachada;
import modelo.Servico;
import java.awt.*;
import java.util.List;

public class TelaServico {
    private JFrame frame;
    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField campoNome, campoPreco;
    
    public TelaServico() {
        frame = new JFrame("Gerenciamento de Serviços");
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
        painel.setBorder(BorderFactory.createTitledBorder("Cadastro de Serviço"));
        
        painel.add(new JLabel("Nome do Serviço:"));
        campoNome = new JTextField();
        painel.add(campoNome);
        
        painel.add(new JLabel("Preço (R$):"));
        campoPreco = new JTextField();
        painel.add(campoPreco);
        
        frame.add(painel, BorderLayout.NORTH);
    }
    
    private void criarTabela() {
        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabela = new JTable(modelo);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
        JScrollPane scroll = new JScrollPane(tabela);
        frame.add(scroll, BorderLayout.CENTER);
    }
    
    private void criarPainelInferior() {
        JPanel painel = new JPanel();
        
        JButton btnCriar = new JButton("Criar Serviço");
        btnCriar.addActionListener(e -> criarServico());
        
        JButton btnListar = new JButton("Atualizar Lista");
        btnListar.addActionListener(e -> carregarDados());
        
        painel.add(btnCriar);
        painel.add(btnListar);
        
        frame.add(painel, BorderLayout.SOUTH);
    }
    
    private void criarServico() {
        try {
            String nome = campoNome.getText();
            String precoTexto = campoPreco.getText();
            
            if (nome.isEmpty() || precoTexto.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Preencha todos os campos!");
                return;
            }
            
            double preco;
            try {
                preco = Double.parseDouble(precoTexto);
                if (preco <= 0) {
                    JOptionPane.showMessageDialog(frame, "Preço deve ser maior que zero!");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Preço deve ser um número válido!");
                return;
            }
            
            Fachada.criarServico(nome, preco);
            JOptionPane.showMessageDialog(frame, "Serviço criado com sucesso!");
            
            campoNome.setText("");
            campoPreco.setText("");
            carregarDados();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage());
        }
    }
    
    private void carregarDados() {
        modelo.setRowCount(0);
        List<Servico> servicos = Fachada.listarServicos();
        
        for (Servico s : servicos) {
            modelo.addRow(new Object[]{
                s.getId(),
                s.getNome(),
                String.format("R$ %.2f", s.getPreco())
            });
        }
    }
}