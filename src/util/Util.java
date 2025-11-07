package util;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;

import modelo.Cliente;
import modelo.Lavagem;
import modelo.Localizacao;
import modelo.Servico;

public final class Util {
    private static final String DB_FILE = "banco.db4o";
    private static ObjectContainer manager;
    
    public static ObjectContainer conectar() {
        if (manager != null) return manager;
        
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().messageLevel(0); // Desliga mensagens
        
        // Configurações de cascade (SIMPLES)
        config.common().objectClass(Cliente.class).cascadeOnUpdate(true);
        config.common().objectClass(Lavagem.class).cascadeOnUpdate(true);
        config.common().objectClass(Servico.class).cascadeOnUpdate(true);
        config.common().objectClass(Localizacao.class).cascadeOnUpdate(true);
        
        manager = Db4oEmbedded.openFile(config, DB_FILE);
        ControleID.ativar(true, manager);
        return manager;
    }
    
    public static void desconectar() {
        if (manager != null) {
            manager.close();
            manager = null;
        }
    }
}