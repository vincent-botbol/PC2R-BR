package br.vue;

import java.awt.Color;

import javax.swing.SwingWorker;

import br.common.UpdateArguments;
import br.model.ModelFacade;
import br.vue.components.ConnectionPane;
import br.vue.components.GamePane;
import br.vue.components.MainFrame;

public class UpdateWorker extends SwingWorker<Void, Void> {

	private final static Object lock = new Object();

	private ModelFacade model;
	private MainFrame mf;
	private ConnectionPane conn;
	private GamePane game;
	private Object argument;

	public UpdateWorker(ModelFacade model, MainFrame mf, ConnectionPane conn,
			GamePane game, Object argument) {
		this.model = model;
		this.mf = mf;
		this.conn = conn;
		this.game = game;
		this.argument = argument;
	}

	@Override
	protected Void doInBackground() throws Exception {
		// Verrou pour s'assurer de la coh�rence des updates
		synchronized (lock) {
			if (argument != null && argument instanceof UpdateArguments) {
				switch ((UpdateArguments) argument) {
				case CONNECTION_INIT:
					conn.getConnect().setEnabled(false);
					conn.getCancel().setEnabled(true);
					conn.showInfo("Connexion en cours");
					break;
				case CONNECTION_ABORTED:
					conn.getConnect().setEnabled(true);
					conn.getCancel().setEnabled(false);
					conn.showInfo("Connexion annul�e");
					break;
				case CONNECTION_FAILED:
					conn.getConnect().setEnabled(true);
					conn.showInfo("Impossible de se connecter au serveur\n"
							+ "V�rifiez vos donn�es");
					conn.getCancel().setEnabled(false);
					break;

				case CONNECTION_SUCCESS:
					System.out.println("CONNECTION SUCCESS !!");
					mf.setFrameContentPane(game);
					mf.pack();
					game.getLog().ajouterMessage("Server",
							"Bienvenue " + model.getGrid().getLogin(),
							Color.GREEN);
					break;

				default:
					break;
				}
			}
			return null;
		}
	}
}
