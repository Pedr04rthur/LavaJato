package appswing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import requisito.Fachada;
import modelo.Lavagem;
import modelo.Cliente;
import modelo.Servico;
import java.awt.*;
import java.util.List;

public class TelaLavagem {
    private JFrame frame;
    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField campoData, campoCpfCliente;
    private JComboBox<String> comboClientes;
    
    public TelaLavagem() {
        frame = new JFrame("Gerenciamento de Lavagens");
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());
        
        criarPainelSuperior();
        criarTabela();
        criarPainelInferior();
        criarPainelServicos();
        
        carregarDados();
        carregarClientes();
        
        frame.setVisible(true);
    }
    
    private void criarPainelSuperior() {
        JPanel painel = new JPanel(new GridLayout(3, 2, 5, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Nova Lavagem"));
        
        // Data
        painel.add(new JLabel("Data (DD/MM/AAAA):"));
        campoData = new JTextField();
        campoData.setText(java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        painel.add(campoData);
        
        // Seleção de cliente
        painel.add(new JLabel("Cliente (CPF):"));
        JPanel painelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboClientes = new JComboBox<>();
        comboClientes.setPreferredSize(new Dimension(150, 25));
        campoCpfCliente = new JTextField(11);
        campoCpfCliente.setPreferredSize(new Dimension(100, 25));
        
        painelCliente.add(comboClientes);
        painelCliente.add(new JLabel("ou CPF:"));
        painelCliente.add(campoCpfCliente);
        painel.add(painelCliente);
        
        frame.add(painel, BorderLayout.NORTH);
    }
    
    private void criarTabela() {
        modelo = new DefaultTableModel(new Object[]{"ID", "Data", "Cliente", "Serviços", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabela = new JTable(modelo);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(100);
        JScrollPane scroll = new JScrollPane(tabela);
        frame.add(scroll, BorderLayout.CENTER);
    }
    
    private void criarPainelInferior() {
        JPanel painel = new JPanel();
        
        JButton btnCriar = new JButton("Criar Lavagem");
        btnCriar.addActionListener(e -> criarLavagem());
        
        JButton btnApagar = new JButton("Apagar Selecionada");
        btnApagar.addActionListener(e -> apagarLavagem());
        
        JButton btnListar = new JButton("Atualizar Lista");
        btnListar.addActionListener(e -> carregarDados());
        
        painel.add(btnCriar);
        painel.add(btnApagar);
        painel.add(btnListar);
        
        frame.add(painel, BorderLayout.SOUTH);
    }
    
    private void criarPainelServicos() {
        JPanel painel = new JPanel(new FlowLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Gerenciar Serviços da Lavagem Selecionada"));
        
        JButton btnAdicionarServico = new JButton("Adicionar Serviço");
        btnAdicionarServico.addActionListener(e -> adicionarServico());
        
        JButton btnRemoverServico = new JButton("Remover Serviço");
        btnRemoverServico.addActionListener(e -> removerServico());
        
        painel.add(btnAdicionarServico);
        painel.add(btnRemoverServico);
        
        frame.add(painel, BorderLayout.EAST);
    }
    
    private void carregarClientes() {
        comboClientes.removeAllItems();
        comboClientes.addItem("-- Selecione --");
        
        List<Cliente> clientes = Fachada.listarClientes();
        for (Cliente c : clientes) {
            comboClientes.addItem(c.getNome() + " (" + c.getCpf() + ")");
        }
    }
    
    private void criarLavagem() {
        try {
            String data = campoData.getText();
            String cpfCliente;
            
            // Verifica se selecionou do combo ou digitou no campo
            if (comboClientes.getSelectedIndex() > 0) {
                String selecionado = (String) comboClientes.getSelectedItem();
                // Extrai CPF do texto: "Nome (CPF)"
                cpfCliente = selecionado.substring(selecionado.lastIndexOf("(") + 1, selecionado.lastIndexOf(")"));
            } else if (!campoCpfCliente.getText().isEmpty()) {
                cpfCliente = campoCpfCliente.getText();
            } else {
                JOptionPane.showMessageDialog(frame, "Selecione um cliente ou digite o CPF!");
                return;
            }
            
            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Informe a data!");
                return;
            }
            
            Fachada.criarLavagem(data, cpfCliente);
            JOptionPane.showMessageDialog(frame, "Lavagem criada com sucesso!");
            
            campoData.setText(java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            comboClientes.setSelectedIndex(0);
            campoCpfCliente.setText("");
            carregarDados();
            carregarClientes();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage());
        }
    }
    
    private void apagarLavagem() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione uma lavagem!");
            return;
        }
        
        int id = (int) modelo.getValueAt(linha, 0);
        int confirm = JOptionPane.showConfirmDialog(frame, "Apagar lavagem ID " + id + "?");
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Fachada.apagarLavagem(id);
                JOptionPane.showMessageDialog(frame, "Lavagem apagada!");
                carregarDados();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage());
            }
        }
    }
    
    private void adicionarServico() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione uma lavagem primeiro!");
            return;
        }
        
        int idLavagem = (int) modelo.getValueAt(linha, 0);
        
        // Mostrar lista de serviços disponíveis
        List<Servico> servicos = Fachada.listarServicos();
        if (servicos.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Não há serviços cadastrados!");
            return;
        }
        
        String[] nomesServicos = new String[servicos.size()];
        for (int i = 0; i < servicos.size(); i++) {
            nomesServicos[i] = servicos.get(i).getNome() + " - R$ " + servicos.get(i).getPreco();
        }
        
        String servicoSelecionado = (String) JOptionPane.showInputDialog(
            frame,
            "Selecione o serviço para adicionar:",
            "Adicionar Serviço",
            JOptionPane.QUESTION_MESSAGE,
            null,
            nomesServicos,
            nomesServicos[0]
        );
        
        if (servicoSelecionado != null) {
            // Extrair apenas o nome do serviço (antes do " - ")
            String nomeServico = servicoSelecionado.split(" - ")[0];
            
            try {
                Fachada.adicionarServicoNaLavagem(idLavagem, nomeServico);
                JOptionPane.showMessageDialog(frame, "Serviço adicionado com sucesso!");
                carregarDados();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage());
            }
        }
    }
    
    private void removerServico() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione uma lavagem primeiro!");
            return;
        }
        
        int idLavagem = (int) modelo.getValueAt(linha, 0);
        
        // Obter lavagem para ver serviços atuais
        Lavagem lavagem = null;
        try {
            lavagem = Fachada.listarLavagens().stream()
                .filter(l -> l.getId() == idLavagem)
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao buscar lavagem: " + e.getMessage());
            return;
        }
        
        if (lavagem == null || lavagem.getServicos().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Esta lavagem não tem serviços para remover!");
            return;
        }
        
        String[] servicosAtuais = new String[lavagem.getServicos().size()];
        for (int i = 0; i < lavagem.getServicos().size(); i++) {
            servicosAtuais[i] = lavagem.getServicos().get(i).getNome();
        }
        
        String servicoSelecionado = (String) JOptionPane.showInputDialog(
            frame,
            "Selecione o serviço para remover:",
            "Remover Serviço",
            JOptionPane.QUESTION_MESSAGE,
            null,
            servicosAtuais,
            servicosAtuais[0]
        );
        
        if (servicoSelecionado != null) {
            try {
                Fachada.removerServicoDaLavagem(idLavagem, servicoSelecionado);
                JOptionPane.showMessageDialog(frame, "Serviço removido com sucesso!");
                carregarDados();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage());
            }
        }
    }
    
    private void carregarDados() {
        modelo.setRowCount(0);
        List<Lavagem> lavagens = Fachada.listarLavagens();
        
        for (Lavagem l : lavagens) {
            modelo.addRow(new Object[]{
                l.getId(),
                l.getData(),
                l.getCliente().getNome(),
                l.getServicos().size(),
                String.format("R$ %.2f", l.getValorTotal())
            });
        }
    }
}