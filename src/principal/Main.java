package principal;

import appconsole.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA LAVA JATO ===\n");
        
        try {
            // Executar cada módulo em sequência
            System.out.println("1. CADASTRANDO DADOS...");
            Cadastrar.main(new String[]{});
            
            System.out.println("\n2. LISTANDO DADOS...");
            Listar.main(new String[]{});
            
            System.out.println("\n3. ALTERANDO RELACIONAMENTO...");
            Alterar.main(new String[]{});
            
            System.out.println("\n4. APAGANDO OBJETO...");
            Apagar.main(new String[]{});
            
            System.out.println("\n5. CONSULTAS ESPECÍFICAS...");
            Consultar.main(new String[]{});
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        
        System.out.println("\n=== FIM DO SISTEMA ===");
    }
}