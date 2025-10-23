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
import modelo.Servico;



public class Util {
	private static ObjectContainer manager;
	private static String ipservidor;

	public static ObjectContainer conectarBD() {
		manager = conectarBancoLocal();		// banco local (pasta do projeto)
		//manager = conectarBancoRemoto();	// banco remoto (precisa de um servidor ativo)
		
		ControleID.ativar(true, manager); 	// ativa gera��o de IDs autom�ticos para as classes com atributo "int id"
		return manager;
	}

	private static ObjectContainer conectarBancoLocal() {
		if (manager != null)
			return manager; // ja tem uma conexao

		// ---------------------------------------------------------------
		// configurar, criar e abrir banco local (pasta do projeto)
		// ---------------------------------------------------------------
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().messageLevel(0); // mensagens na tela 0(desliga),1,2,3...

		// habilitar cascata na altera��o, remo��o e leitura
	//	config.common().objectClass(Livro.class).cascadeOnDelete(false);
	//	config.common().objectClass(Livro.class).cascadeOnUpdate(true);
	//	config.common().objectClass(Livro.class).cascadeOnActivate(true);
	//	config.common().objectClass(Autor.class).cascadeOnDelete(false);
	//	config.common().objectClass(Autor.class).cascadeOnUpdate(true);
	//	config.common().objectClass(Autor.class).cascadeOnActivate(true);

		config.common().objectClass(Lavagem.class).cascadeOnDelete(false);
		config.common().objectClass(Lavagem.class).cascadeOnUpdate(true);
		config.common().objectClass(Lavagem.class).cascadeOnActivate(true);

		config.common().objectClass(Cliente.class).cascadeOnDelete(false);
		config.common().objectClass(Cliente.class).cascadeOnUpdate(true);
		config.common().objectClass(Cliente.class).cascadeOnActivate(true);

		config.common().objectClass(Servico.class).cascadeOnDelete(false);
		config.common().objectClass(Servico.class).cascadeOnUpdate(true);
		config.common().objectClass(Servico.class).cascadeOnActivate(true);
		
		// conexao local
		manager = Db4oEmbedded.openFile(config, "banco.db4o");
		return manager;
	}

	private static ObjectContainer conectarBancoRemoto() {
		if (manager != null)
			return manager; // ja tem uma conexao

		// ---------------------------------------
		// configurar e conectar banco remoto
		// ---------------------------------------
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().messageLevel(0); // 0,1,2,3...

		// habilitar cascata na altera��o, remo��o e leitura
		// Habilitar cascata nas suas classes (Lavagem, Cliente, Servico)
		config.common().objectClass(Lavagem.class).cascadeOnDelete(false);
		config.common().objectClass(Lavagem.class).cascadeOnUpdate(true);
		config.common().objectClass(Lavagem.class).cascadeOnActivate(true);

		config.common().objectClass(Cliente.class).cascadeOnDelete(false);
		config.common().objectClass(Cliente.class).cascadeOnUpdate(true);
		config.common().objectClass(Cliente.class).cascadeOnActivate(true);

		config.common().objectClass(Servico.class).cascadeOnDelete(false);
		config.common().objectClass(Servico.class).cascadeOnUpdate(true);
		config.common().objectClass(Servico.class).cascadeOnActivate(true);

		// Conex�o remota
		// **************************************
		try {
			JComboBox<String> combo = new JComboBox<>(new String[] {"10.0.71.50", "54.163.92.174" });
			JOptionPane.showConfirmDialog(null, combo, "Selecione o IP do servidor", JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE);

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

	public static void desconectar() {
		if (manager != null) {
			manager.close();
			manager = null;
		}
	}

	static String getIPservidor() {
		return ipservidor;
	}

	static ObjectContainer getManager() {
		return manager;
	}

}// fim da classe Util

//**********************************************
// classe interna 
// Controla a gera��o automatica de IDs para 
// as classes que possuem um atributo id
//**********************************************
class ControleID {
	private static ObjectContainer sequencia; // bd auxiliar de sequencias DE IDs
	private static TreeMap<String, RegistroID> registros = new TreeMap<String, RegistroID>(); //cache de registros de ids
	private static boolean salvar; // indica se precisa salvar os registros de id

	public static void ativar(boolean ativa, ObjectContainer manager) {
		if (!ativa)
			return; // controle de ids nao ser� feito
		if (manager == null)
			throw new RuntimeException("Ativar controle de id - manager desconhecido"); // desativado

		if (manager instanceof EmbeddedObjectContainer) {
			// banco de sequencia no local
			Db4oEmbedded.newConfiguration();
			sequencia = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "sequencia.db4o");
			//System.out.println("conectou sequencia local");
		} else {
			// banco de sequencia no servidor remoto
			String ipservidor = Util.getIPservidor();
			sequencia = Db4oClientServer.openClient(Db4oClientServer.newClientConfiguration(), ipservidor, 35000,
					"usuario0", "senha0");
			//System.out.println("conectou no banco de sequencia remoto ip=" + ipservidor);
		}
		lerRegistrosID(); // ler do banco os registros de id

		// CRIAR GERENTE DE TRIGGERS PARA O MANAGER
		 EventRegistry eventRegistry = EventRegistryFactory.forObjectContainer(manager);

		// Resgistrar trigger "BEFORE PERSIST" causado pelo manager.store(objeto)
		eventRegistry.creating().addListener((event, args) -> {
			try {
				//System.out.println("trigger creating");
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
			}
		});

		// Resgistrar trigger "AFTER COMMIT" causado pelo manager.commit()
		eventRegistry.created().addListener((event, args) -> {
			//System.out.println("trigger commit");
			salvarRegistrosID(); // salvar registros de id alterados 
		});

		// Resgistrar trigger "BEFORE CLOSE" causado pelo manager.close()
		eventRegistry.closing().addListener((event, args) -> {
			//System.out.println("trigger close");
			if (sequencia != null && !sequencia.ext().isClosed())
				sequencia.close(); // fecha o banco de sequencias
		});
	}

	private static void lerRegistrosID() {
		Query q = sequencia.query();
		q.constrain(RegistroID.class);
		List<RegistroID> resultados = q.execute();
		for (RegistroID reg : resultados) {
			//System.out.println("lendo do bd sequencia: " + reg);
			registros.put(reg.getNomedaclasse(), reg);
		}
		salvar = false;

	}

	private static void salvarRegistrosID() {
		if (salvar) {
			for (RegistroID reg : registros.values()) {
				if (reg.isModificado()) {
					//System.out.println("gravando no bd sequencia: " + reg);
					sequencia.store(reg);
					sequencia.commit();
					reg.setModificado(false);
				}
			}
			salvar = false;
		}
	}

	private static RegistroID obterRegistroID(String nomeclasse) {
		RegistroID reg = null;
		if (registros.containsKey(nomeclasse))
			reg = registros.get(nomeclasse);
		else
			reg = new RegistroID(nomeclasse); // aninhamento

		return reg;
	}

} // fim classe interna ControleID

// *************************************************************
// classe interna
// Encapsula o ultimo ID gerado para uma classe
// *************************************************************
class RegistroID {
	private String nomedaclasse;
	private int ultimoid;
	transient private boolean modificado = false; // nao sera persistido

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

} // fim classe RegistroID
