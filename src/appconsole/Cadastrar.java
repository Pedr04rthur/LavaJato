package appconsole;

import java.util.List;
import java.util.Scanner;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Cliente;

public class Cadastrar {
    protected ObjectContainer db;
    private Scanner scanner; 

    public Cadastrar() {
        db = Util.conectarBD(); // Conexão que ativa o ControleID
        scanner = new Scanner(System.in);
        System.out.println("Iniciando cadastro...");
    }

    /**
     * Lógica principal para solicitar e armazenar um novo cliente.
     */
    public void cadastrarCliente() {
        try {
            System.out.println("\n--- CADASTRO DE NOVO CLIENTE ---");
            
            // 1. INPUT e Validação de CPF/CNPJ
            System.out.print("Digite o CPF do cliente: ");
            String cpf = scanner.nextLine().trim();

            if (cpf.isEmpty()) {
                System.out.println("Atençao! CPF não pode ser vazio.");
                return;
            }

            // Verifica se o cliente já existe (Boas Práticas!)
            Query verificaCpf = db.query();
            verificaCpf.constrain(Cliente.class);
            verificaCpf.descend("cpf").constrain(cpf);
            if (!verificaCpf.execute().isEmpty()) {
                System.out.println("Cliente com este CPF já existe! Tente outro.");
                return;
            }

            // 2. INPUT do Nome
            System.out.print("Digite o nome completo do cliente: ");
            String nome = scanner.nextLine();
            
            double latitude, longitude;
            try {
                System.out.print("Digite a Latitude: ");
                latitude = Double.parseDouble(scanner.nextLine());

                System.out.print("Digite a Longitude: ");
                longitude = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                 System.out.println("ERRO! Latitude ou Longitude devem ser números válidos.");
                 return;
            }
            
            // 4. CRIAÇÃO E ARMAZENAMENTO
            Cliente novoCliente = new Cliente(cpf, nome, latitude, longitude);
            db.store(novoCliente);
            db.commit(); // Persiste a alteração imediatamente

            System.out.println("----------------------------------------");
            System.out.println("✅ Cliente " + nome + " cadastrado com sucesso!");
            System.out.println("----------------------------------------");
            
        } catch (Exception e) {
            System.out.println("Erro durante o cadastro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Cadastrar c = null;
        try {
            c = new Cadastrar();
            String opcao;
            
            do {
                c.cadastrarCliente(); // Tenta cadastrar um cliente
                
                System.out.print("Deseja cadastrar outro cliente? (S/N): ");
                opcao = c.scanner.nextLine().toUpperCase();
                
            } while (opcao.equals("S"));
            
            System.out.println("\nCadastro finalizado.");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            if (c != null) {
                c.scanner.close();
                Util.desconectar();
            }
        }
    }
}