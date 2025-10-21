package appconsole;

import com.db4o.*;
import com.db4o.config.*;
import com.db4o.ta.TransparentActivationSupport;
import com.db4o.ta.TransparentPersistenceSupport;

import modelo.*;

public class Util {
    private static ObjectContainer db;

    public static ObjectContainer conectarBD() {
        if (db == null) {
            try {
                Configuration config = Db4oEmbedded.newConfiguration();
                
                // Configurações de cascade
                config.common().add(new TransparentActivationSupport());
                config.common().add(new TransparentPersistenceSupport());
                config.common().updateDepth(3);
                
                // Configurações específicas das classes
                config.common().objectClass(Cliente.class).cascadeOnUpdate(true);
                config.common().objectClass(Cliente.class).cascadeOnDelete(false);
                config.common().objectClass(Lavagem.class).cascadeOnUpdate(true);
                config.common().objectClass(Lavagem.class).cascadeOnDelete(true);
                config.common().objectClass(Servico.class).cascadeOnUpdate(true);
                config.common().objectClass(Servico.class).cascadeOnDelete(true);
                
                db = Db4oEmbedded.openFile(config, "banco.db4o");
                System.out.println("✓ Conectado ao DB4O!");
                
            } catch (Exception e) {
                System.out.println("✗ Erro no DB4O: " + e.getMessage());
                throw new RuntimeException("Falha ao conectar com DB4O", e);
            }
        }
        return db;
    }

    public static void desconectarBD() {
        if (db != null) {
            db.close();
            db = null;
            System.out.println("✓ Desconectado do DB4O");
        }
    }
}