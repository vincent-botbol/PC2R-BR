package br.controller;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import br.common.UpdateArguments;
import br.controller.net.ClientSocket;
import br.controller.net.ERequest;
import br.controller.net.Request;
import br.controller.net.Response;
import br.model.ModelFacade;
import br.vue.ViewFacade;

class CommandDispatcher extends SwingWorker<Void, Response> {

	private ModelFacade model;
	private Controller controller;
	private ViewFacade view;
	private String pseudo, host, pass;
	private boolean isRegister;
	private boolean isCleanDisconnect;
	private boolean isSpectator;

	public CommandDispatcher(ModelFacade model, Controller controller,
			ViewFacade view, String pseudo, String host, int port) {
		this.model = model;
		this.controller = controller;
		this.pseudo = pseudo;
		this.host = host;
		this.view = view;
	}

	@Override
	protected Void doInBackground() {
		ClientSocket socket = null;

		model.notifyView(UpdateArguments.CONN_INIT);

		InetAddress addr = null;
		try {
			addr = Inet4Address.getByName(host);
		} catch (UnknownHostException e) {
			model.notifyView(UpdateArguments.RESOLV_FAILED);
			return null;
		}

		try {
			socket = new ClientSocket(addr, pseudo);
		} catch (IOException e1) {
			model.notifyView(UpdateArguments.CONN_FAILED);
			return null;
		}

		controller.setSocket(socket);

		return startReadingLoop(socket);
	}

	private Void startReadingLoop(ClientSocket socket) {
		try {
			// System.out.println("DEBUG : Connexion etablie - En attente");
			Response r;
			while (true) {
				try {
					r = socket.receiveResponse();
					// System.out.println("DEBUG : Response reçue = " + r);
					publish(r);
				} catch (ParseException e) {
					System.err.println(e.getMessage());
				}
			}
		} catch (IOException e) {
			System.err.println("Error : connexion lost");
			JOptionPane.showMessageDialog(null, e.getMessage(), "Fatal error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return null;
	}

	protected void process(List<Response> chunks) {
		for (Response r : chunks)
			dispatch(r);
	}

	private void dispatch(Response r) {
		switch (r.getCommand()) {
		case ACCESSDENIED:
			break;
		case ALLYOURBASE:
			controller.getGameGridListener().setPlacing(false);
			view.getGame()
					.getLog()
					.ajouterMessage("Serveur",
							"Placement terminé. En attente du tour",
							Color.GREEN);
			break;
		case AWINNERIS:
			view.getGame()
					.getLog()
					.ajouterMessage("Serveur",
							"Le gagnant est " + r.getArguments().get(0) + "!",
							Color.BLUE);
			if (r.getArguments().get(0)
					.equals(model.getPlayers().getMyPseudo())) {
				view.getGame()
						.getLog()
						.ajouterMessage("Jeu", "A WINNER IS YOU!!", Color.GREEN);
			}
			processEndGame();
			break;
		case DEATH:
			view.getGame()
					.getLog()
					.ajouterMessage(
							"Serveur",
							"Le joueur " + r.getArguments().get(0)
									+ " est mort!", Color.magenta);
			break;
		case DRAWGAME:
			view.getGame().getLog()
					.ajouterMessage("Serveur", "Match nul!", Color.BLUE);
			processEndGame();
			break;
		case HEYLISTEN:
			processHeyListen(r.getArguments());
			break;
		case MISS:
			processActionResult(r.getArguments(), 0);
			break;
		case OK:
			controller.getGameGridListener().setPlacing(false);
			model.getGrid().putShip();
			view.getGame()
					.getLog()
					.ajouterMessage("Serveur", "Sous-marin placé!", Color.GREEN);
			break;
		case OUCH:
			processActionResult(r.getArguments(), 1);
			break;
		case PLAYERS:
			playersProcess(r.getArguments());
			break;
		case SHIP:
			controller.getGameGridListener().setPlacing(true);
			int taille = Integer.parseInt(r.getArguments().get(0));
			controller.getGameGridListener().setSizeShip(taille);
			view.getGame()
					.getLog()
					.ajouterMessage("Serveur",
							"Placez un sous-marin de taille : " + taille,
							Color.BLUE);
			break;
		case TOUCHE:
			processActionResult(r.getArguments(), 2);
			break;
		case WELCOME:
			welcomeProcess(r.getArguments().get(0));
			break;
		case WRONG:
			controller.getGameGridListener().setPlacing(true);
			view.getGame()
					.getLog()
					.ajouterMessage(
							"Serveur",
							"Impossible de placer votre sous-marin à cette position",
							Color.RED);
			break;
		case YOURTURN:
			yourTurnProcess(r.getArguments());
			break;
		default:
			break;
		}
	}

	private void processHeyListen(List<String> arguments) {
		model.getChat().ajoutMessage(arguments.get(0), arguments.get(1));
	}

	private void processActionResult(List<String> arguments, int i) {

		Point pos = toIndexes(arguments.get(0), arguments.get(1));
		switch (i) {
		// miss
		case 0:
			model.getGrid().putMiss(pos.x, pos.y);
			break;
		// ouch
		case 1:
			model.getGrid().putOuch(pos.x, pos.y);
			break;
		// touche
		default:
			model.getGrid().putTouche(pos.x, pos.y);
		}
	}

	private void playersProcess(List<String> arguments) {
		model.setPlayers(arguments);
	}

	private void welcomeProcess(String name) {
		model.gotWelcomed(name);
	}

	private void yourTurnProcess(List<String> args) {
		Point pos = toIndexes(args.get(0), args.get(1));
		int nbAction = Integer.parseInt(args.get(2));
		controller.getGameGridListener().setPlaying(true);
		model.getGrid().startTurn(pos.x, pos.y, nbAction);
		view.getGame()
				.getLog()
				.ajouterMessage(
						"Serveur",
						"A vous de jouer !\nVous avez " + nbAction
								+ " points d'actions disponibles", Color.BLUE);
	}

	private void processEndGame() {
		if (JOptionPane.showConfirmDialog(null, "Souhaitez-vous rejouer?",
				"Fin du jeu", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			controller.makeRequest(new Request(ERequest.PLAYAGAIN, Collections
					.<String> emptyList()));
			String monPseudo = model.getPlayers().getMyPseudo();
			model.resetGame();
			welcomeProcess(monPseudo);
		} else {
			controller.makeRequest(new Request(ERequest.BYE, Collections
					.<String> emptyList()));
			System.exit(0);
		}
	}

	// Transforme coordonnées jeu en coordonnées graphiques
	// 'A' -> 15 // 'P' -> 0
	public static Point toIndexes(String x, String y) {
		return new Point(Integer.parseInt(x), 'P' - y.charAt(0));
	}

}
