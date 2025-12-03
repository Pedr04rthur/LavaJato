package principal;

import appswing.TelaPrincipal;

public class Main {
    public static void main(String[] args) {
        // Para iniciar o sistema com interface Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            new TelaPrincipal();
        });
        
        /*
        // Se quiser manter o console também:
        System.out.println("=== SISTEMA LAVA JATO ===\n");
        
        try {
            System.out.println("Iniciando sistema...");
            
            // Você pode manter as chamadas antigas ou migrar tudo para Swing
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        */
    }
}