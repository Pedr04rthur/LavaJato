package appswing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaPrincipal {
    private JFrame frame;
    private JMenuBar menuBar;
    
    public TelaPrincipal() {
        frame = new JFrame("Sistema Lava Jato");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        
        criarMenu();
        
        JLabel label = new JLabel("Sistema Lava Jato - Gerenciamento de Lavagens", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(label, BorderLayout.CENTER);
        
        frame.setVisible(true);
    }
    
    private void criarMenu() {
        menuBar = new JMenuBar();
        
        JMenu menuCadastro = new JMenu("Cadastro");
        JMenuItem itemCliente = new JMenuItem("Clientes");
        JMenuItem itemServico = new JMenuItem("Serviços");
        JMenuItem itemLavagem = new JMenuItem("Lavagens");
        
        itemCliente.addActionListener(e -> new TelaCliente());
        itemServico.addActionListener(e -> new TelaServico());
        itemLavagem.addActionListener(e -> new TelaLavagem());
        
        menuCadastro.add(itemCliente);
        menuCadastro.add(itemServico);
        menuCadastro.add(itemLavagem);
        
        JMenu menuConsulta = new JMenu("Consultas");
        JMenuItem itemConsultas = new JMenuItem("Relatórios");
        itemConsultas.addActionListener(e -> new TelaConsultas());
        menuConsulta.add(itemConsultas);
        
        menuBar.add(menuCadastro);
        menuBar.add(menuConsulta);
        
        frame.setJMenuBar(menuBar);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal());
    }
}