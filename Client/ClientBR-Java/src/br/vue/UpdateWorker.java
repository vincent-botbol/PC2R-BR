package br.vue;

import java.awt.Color;

import javax.swing.SwingWorker;

import br.common.UpdateArguments;
import br.model.ModelFacade;
import br.vue.components.ConnectionPane;
import br.vue.components.GamePane;
import br.vue.components.MainFrame;

public class UpdateWorker extends SwingWorker<Void, Void> {

	private ModelFacade model;
	private MainFrame mf;
	private ConnectionPane conn;
	private GamePane game;
	private Object argument;

	private final static Object lock = new Object();
	private static boolean hasResolved = false;

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
		if (argument != null && argument instanceof UpdateArguments) {
			switch ((UpdateArguments) argument) {
			case RESOLV_INIT:

				conn.getConnect().setEnabled(false);
				conn.getCancel().setEnabled(false);
				conn.showInfo("Résolution de l'adresse");
				synchronized (lock) {
					hasResolved = true;
					lock.notify();
				}
				break;
			case RESOLV_FAILED:
				// Viens après le resolv init
				conn.getConnect().setEnabled(true);
				conn.getCancel().setEnabled(false);
				conn.showInfo("Impossible de résoudre l'adresse");
				break;
			case CONN_INIT:
				// Viens après le resolv init
				synchronized (lock) {
					if (!hasResolved)
						lock.wait();
				}
				conn.getCancel().setEnabled(true);
				conn.showInfo("Connexion en cours");
				break;
			case CONN_FAILED:
				// Viens après le conn_init
				conn.getConnect().setEnabled(true);
				conn.getCancel().setEnabled(false);
				conn.showInfo("Connexion impossible\n"
						+ "Vérifiez que le serveur est actif");
				hasResolved = false;
				break;
			case CONN_ABORTED:
				// Viens après revolv_init et/ou après conn_init
				conn.getConnect().setEnabled(true);
				conn.getCancel().setEnabled(false);
				conn.showInfo("Connexion annulée");
				hasResolved = false;
				break;
			case CONN_SUCCESS:
				connectionSuccess();
				break;
			case GAME_INIT:
				initGame();
				break;
			default:
				break;
			}
		}
		return null;
	}

	private void connectionSuccess() {
		mf.setFrameContentPane(game);
		mf.pack();
		game.getLog().ajouterMessage("Serveur",
				"Bienvenue " + model.getPlayers().getMyPseudo(), Color.GREEN);
		game.getLog().ajouterMessage("Jeu",
				"En attente du début de la partie...", Color.BLUE);
	}

	private void initGame() {
		game.getLog().ajouterMessage("Jeu", "Début de partie", Color.BLUE);
		game.getLog().ajouterMessage(
				"Serveur",
				"Vous jouez contre : "
						+ model.getPlayers().everyoneButMeToString(),
				Color.GREEN);
		game.getChat().getSendButton().setEnabled(true);
		game.getChat().updatePlayersLabel(model.getPlayers().getAllPlayers());
	}
}
