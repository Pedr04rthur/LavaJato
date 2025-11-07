package util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.TreeMap;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectContainer;
import com.db4o.events.EventRegistry;
import com.db4o.events.EventRegistryFactory;
import com.db4o.query.Query;

public class ControleID {

    private static ObjectContainer sequencia;
    private static TreeMap<String, RegistroID> registros = new TreeMap<String, RegistroID>();
    private static boolean salvar;
    
    public static void ativar(boolean ativa, ObjectContainer manager) {
        if (!ativa) return;
        
        if (manager == null) throw new RuntimeException("Manager nulo!");
        
        if (manager instanceof EmbeddedObjectContainer) {
            sequencia = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "sequencia.db4o");
        }
        
        lerRegistros();
        
        EventRegistry eventRegistry = EventRegistryFactory.forObjectContainer(manager);

        eventRegistry.creating().addListener((event, args) -> {
            try {
                Object objeto = args.object();
                Field field = objeto.getClass().getDeclaredField("id");
                if (field != null) {
                    String nomedaclasse = objeto.getClass().getName();
                    RegistroID registro = obterRegistroID(nomedaclasse);
                    registro.incrementarID();
                    field.setAccessible(true);
                    field.setInt(objeto, registro.getId());
                    registros.put(nomedaclasse, registro);
                    salvar = true;
                }
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            }
        });

        eventRegistry.committed().addListener((event, args) -> {
            salvarRegistrosID(); 
        });

        eventRegistry.closing().addListener((event, args) -> {
            if (sequencia != null && !sequencia.ext().isClosed())
                sequencia.close();
        });
    }
    
    private static void lerRegistros() {
        Query q = sequencia.query();
        q.constrain(RegistroID.class);
        List<RegistroID> resultados = q.execute();
        for (RegistroID reg : resultados) {
            registros.put(reg.getNome(), reg);
        }
        salvar = false;
    }
    
    private static void salvarRegistrosID() {
        if (salvar) {
            for (RegistroID reg : registros.values()) {
                if (reg.isModificado()) {
                    sequencia.store(reg);
                    sequencia.commit();
                    reg.setModificado(false);
                }
            }
            salvar = false;
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