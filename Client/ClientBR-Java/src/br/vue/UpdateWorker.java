package br.vue;

import java.awt.Color;

import javax.swing.SwingUtilities;
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
			case RESOLV_FAILED:
				// Viens après le resolv init
				conn.getConnect().setEnabled(true);
				conn.getRegister()
						.setEnabled(conn.getCheck_pass().isSelected());
				conn.showInfo("Impossible de résoudre l'adresse");
				break;
			case CONN_INIT:
				conn.getConnect().setEnabled(false);
				conn.getRegister().setEnabled(false);
				conn.showInfo("Connexion en cours");
				break;
			case CONN_FAILED:
				conn.getConnect().setEnabled(true);
				conn.getRegister()
						.setEnabled(conn.getCheck_pass().isSelected());
				conn.showInfo("Connexion impossible\n"
						+ "Vérifiez que le serveur est actif");
				break;
			case CONN_SUCCESS:
				connectionSuccess();
				break;
			case LOGIN_FAILED:
				conn.getConnect().setEnabled(true);
				conn.getRegister().setEnabled(true);
				conn.showInfo("Mot de passe incorrect");
				break;
			case REGISTER_FAILED:
				conn.getConnect().setEnabled(true);
				conn.getRegister().setEnabled(true);
				conn.showInfo("Impossible de s'enregistrer\nCe pseudo est déjà pris");
				break;
			case GAME_INIT:
				initGame(false);
				break;
			case GAME_INIT_SPECT:
				initGame(true);
				break;
			case LASER:
				game.getLog().ajouterMessage("Jeu", "Activation du laser",
						Color.MAGENTA);
				break;
			case DOTURN:
				game.getLog().ajouterMessage(
						"Jeu",
						"Il vous reste " + model.getGrid().getNbActionsLeft()
								+ " points d'actions", Color.blue);
				redraw();
				break;
			case ENDTURN:
				game.getLog().ajouterMessage("Jeu", "Fin du tour", Color.blue);
				redraw();
				break;
			case REDRAW:
				redraw();
				break;
			case NOTREACHABLE:
				game.getLog().ajouterMessage("Jeu",
						"Impossible d'atteindre cette case", Color.red);
				break;
			case NOTRIGGERABLE:
				game.getLog().ajouterMessage("Jeu",
						"Impossible de ré-activer le laser pour ce tour",
						Color.red);
				break;
			case MISS:
				game.getLog().ajouterMessage("Serveur", "Manqué!", Color.blue);
				redraw();
				break;
			case OUCH:
				game.getLog().ajouterMessage("Serveur",
						"Vous vous êtes fait touché", Color.red);
				redraw();
				break;
			case TOUCHE:
				game.getLog().ajouterMessage("Serveur",
						"Vous avez touché un sous-marin", Color.green);
				redraw();
				break;
			case CHAT_UPDATE:
				game.getChat().ajoutMessage(model.getChat().getMessage());
				break;
			default:
				break;
			}
		}
		return null;
	}

	private void redraw() {
		// hack
		Runnable r = new Runnable() {
			@Override
			public void run() {
				game.getGrid().revalidate();
				game.getGrid().repaint();
			}
		};
		SwingUtilities.invokeLater(r);
	}

	private void connectionSuccess() {
		mf.setFrameContentPane(game);
		mf.pack();
		game.getLog().ajouterMessage("Serveur",
				"Bienvenue " + model.getPlayers().getMyPseudo(), Color.GREEN);
		game.getLog().ajouterMessage("Jeu",
				"En attente du début de la partie...", Color.BLUE);
	}

	private void initGame(boolean isSpectator) {
		game.getLog().ajouterMessage("Jeu", "Début de partie", Color.BLUE);
		if (isSpectator)
			game.getLog().ajouterMessage(
					"Serveur",
					"Les joueurs sont : "
							+ model.getPlayers().allPlayersToString(),
					Color.magenta);
		else
			game.getLog().ajouterMessage(
					"Serveur",
					"Vous jouez contre : "
							+ model.getPlayers().everyoneButMeToString(),
					Color.magenta);
		game.getChat().updatePlayersLabel(model.getPlayers().getAllPlayers());
	}
}
