/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * POB - Persistencia de Objetos
 * Prof. Fausto Ayres
 *
 */

package appconsole;

import java.lang.reflect.Field;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.events.EventRegistry;
import com.db4o.events.EventRegistryFactory;
import com.db4o.query.Query;

import modelo.Cliente;
import modelo.Lavagem;
import modelo.Service;

public class Util {
    private static ObjectContainer manager;
    private static String ipservidor;

    // ========== CONEXÕES PRINCIPAIS ==========
    
    public static ObjectContainer conectarBD() {
        if (manager == null) {
            manager = conectarBancoLocal();
            ControleID.ativar(true, manager);
        }
        return manager;
    }

    public static ObjectContainer conectarBDSimples() {
        if (manager == null) {
            manager = conectarBancoLocalSimples();
        }
        return manager;
    }

    // ========== CONEXÃO LOCAL COM CONTROLE ID ==========
    
    private static ObjectContainer conectarBancoLocal() {
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().messageLevel(0); // 0 = desliga mensagens
        
        configurarCascata(config.common());
        
        manager = Db4oEmbedded.openFile(config, "banco.db4o");
        System.out.println("✅ Conectado ao banco local (com ControleID)");
        return manager;
    }

    // ========== CONEXÃO LOCAL SEM CONTROLE ID ==========
    
    private static ObjectContainer conectarBancoLocalSimples() {
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().messageLevel(0);
        
        configurarCascata(config.common());
        
        manager = Db4oEmbedded.openFile(config, "banco.db4o");
        System.out.println("✅ Conectado ao banco local (sem ControleID)");
        return manager;
    }

    // ========== CONEXÃO REMOTA ==========
    
    private static ObjectContainer conectarBancoRemoto() {
        ClientConfiguration config = Db4oClientServer.newClientConfiguration();
        config.common().messageLevel(0);
        
        configurarCascata(config.common());

        try {
            JComboBox<String> combo = new JComboBox<>(new String[]{"10.0.71.50", "54.163.92.174"});
            JOptionPane.showConfirmDialog(null, combo, "Selecione o IP do servidor", 
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE);

            ipservidor = (String) combo.getSelectedItem();
            manager = Db4oClientServer.openClient(config, ipservidor, 34000, "usuario1", "senha1");
            JOptionPane.showMessageDialog(null, "Conectou no banco remoto ip=" + ipservidor);
            return manager;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao conectar no banco remoto ip=" + ipservidor + "\n" + e.getMessage());
            System.exit(0);
            return null;
        }
    }

    // ========== CONFIGURAÇÃO DE CASCATA ==========
    
    private static void configurarCascata(com.db4o.config.CommonConfiguration config) {
        // Configurações para Lavagem
        config.objectClass(Lavagem.class).cascadeOnDelete(false);
        config.objectClass(Lavagem.class).cascadeOnUpdate(true);
        config.objectClass(Lavagem.class).cascadeOnActivate(true);

        // Configurações para Cliente
        config.objectClass(Cliente.class).cascadeOnDelete(false);
        config.objectClass(Cliente.class).cascadeOnUpdate(true);
        config.objectClass(Cliente.class).cascadeOnActivate(true);

        // Configurações para Service
        config.objectClass(Service.class).cascadeOnDelete(false);
        config.objectClass(Service.class).cascadeOnUpdate(true);
        config.objectClass(Service.class).cascadeOnActivate(true);
    }

    // ========== MÉTODOS UTILITÁRIOS ==========
    
    public static void desconectar() {
        if (manager != null) {
            manager.close();
            manager = null;
            System.out.println("✅ Conexão com o banco fechada");
        }
    }

    public static String getIPservidor() {
        return ipservidor;
    }

    public static ObjectContainer getManager() {
        return manager;
    }
    
    public static void limparBanco() {
        if (manager != null) {
            try {
                // Limpar todos os objetos do banco
                Query query = manager.query();
                query.constrain(Object.class);
                List<Object> objetos = query.execute();
                
                for (Object obj : objetos) {
                    manager.delete(obj);
                }
                manager.commit();
                System.out.println("✅ Banco de dados limpo");
            } catch (Exception e) {
                System.out.println("❌ Erro ao limpar banco: " + e.getMessage());
            }
        }
    }
}

// ========== CONTROLE DE IDs AUTOMÁTICOS ==========

class ControleID {
    private static ObjectContainer sequencia;
    private static TreeMap<String, RegistroID> registros = new TreeMap<String, RegistroID>();
    private static boolean salvar;

    public static void ativar(boolean ativa, ObjectContainer manager) {
        if (!ativa) return;
        if (manager == null) {
            throw new RuntimeException("Ativar controle de id - manager desconhecido");
        }

        try {
            if (manager instanceof EmbeddedObjectContainer) {
                // banco de sequencia no local
                sequencia = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "sequencia.db4o");
            } else {
                // banco de sequencia no servidor remoto
                String ipservidor = Util.getIPservidor();
                sequencia = Db4oClientServer.openClient(Db4oClientServer.newClientConfiguration(), 
                        ipservidor, 35000, "usuario0", "senha0");
            }
            
            lerRegistrosID();

            // CRIAR GERENTE DE TRIGGERS PARA O MANAGER
            EventRegistry eventRegistry = EventRegistryFactory.forObjectContainer(manager);

            // Registrar trigger "BEFORE PERSIST" causado pelo manager.store(objeto)
            eventRegistry.creating().addListener((event, args) -> {
                try {
                    Object objeto = args.object(); // objeto que esta sendo gravado
                    Field field = objeto.getClass().getDeclaredField("id");
                    if (field != null) { // tem campo id?
                        String nomedaclasse = objeto.getClass().getName();
                        RegistroID registro = obterRegistroID(nomedaclasse); // pega id da tabela
                        registro.incrementarID(); // incrementa o id
                        field.setAccessible(true); // habilita acesso ao campo id do objeto
                        field.setInt(objeto, registro.getid()); // atualiza o id do objeto
                        registros.put(nomedaclasse, registro); // atualiza tabela de id
                        salvar = true;
                    }
                } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
                    // Ignora classes sem campo id
                }
            });

            // Registrar trigger "AFTER COMMIT" causado pelo manager.commit()
            eventRegistry.committed().addListener((event, args) -> {
                salvarRegistrosID(); // salvar registros de id alterados 
            });

            // Registrar trigger "BEFORE CLOSE" causado pelo manager.close()
            eventRegistry.closing().addListener((event, args) -> {
                if (sequencia != null && !sequencia.ext().isClosed()) {
                    sequencia.close(); // fecha o banco de sequencias
                }
            });
            
            System.out.println("✅ ControleID ativado com sucesso");
            
        } catch (Exception e) {
            System.out.println("❌ Erro ao ativar ControleID: " + e.getMessage());
        }
    }

    private static void lerRegistrosID() {
        try {
            Query q = sequencia.query();
            q.constrain(RegistroID.class);
            List<RegistroID> resultados = q.execute();
            for (RegistroID reg : resultados) {
                registros.put(reg.getNomedaclasse(), reg);
            }
            salvar = false;
        } catch (Exception e) {
            System.out.println("❌ Erro ao ler registros ID: " + e.getMessage());
        }
    }

    private static void salvarRegistrosID() {
        if (salvar) {
            try {
                for (RegistroID reg : registros.values()) {
                    if (reg.isModificado()) {
                        sequencia.store(reg);
                        sequencia.commit();
                        reg.setModificado(false);
                    }
                }
                salvar = false;
            } catch (Exception e) {
                System.out.println("❌ Erro ao salvar registros ID: " + e.getMessage());
            }
        }
    }

    private static RegistroID obterRegistroID(String nomeclasse) {
        RegistroID reg = registros.get(nomeclasse);
        if (reg == null) {
            reg = new RegistroID(nomeclasse);
        }
        return reg;
    }
}

// ========== REGISTRO DE IDs ==========

class RegistroID {
    private String nomedaclasse;
    private int ultimoid;
    private transient boolean modificado = false;

    public RegistroID(String nomedaclasse) {
        this.nomedaclasse = nomedaclasse;
        this.ultimoid = 0;
    }

    public String getNomedaclasse() {
        return nomedaclasse;
    }

    public int getid() {
        return ultimoid;
    }

    public boolean isModificado() {
        return modificado;
    }

    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }

    public void incrementarID() {
        ultimoid++;
        setModificado(true);
    }

    @Override
    public String toString() {
        return "RegistroID [nomedaclasse=" + nomedaclasse + ", ultimoid=" + ultimoid + "]";
    }
}