package br.model;

import java.awt.Point;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JOptionPane;

import br.common.UpdateArguments;
import br.model.logic.Chat;
import br.model.logic.GameGrid;
import br.model.logic.Player;
import br.model.net.ClientSocket;
import br.model.net.ERequest;
import br.model.net.Request;

public class ModelFacade extends Observable {

	private GameGrid gg;
	// on verra plus tard pour les players
	private Player[] players;
	private Chat chat;

	private CommandDispatcher cdi;
	private ClientSocket cs;

	public ModelFacade() {
		chat = new Chat(this);
		gg = new GameGrid(this);
	}

	@Override
	public void setChanged() {
		super.setChanged();
	}

	// Méthodes = Appels du controleur
	public void establishConnection(String host, int port, final String name) {
		try {
			// On prévient la vue que l'on est en train de se connecter
			// et qu'elle doit désactiver le bouton connexion
			setChanged();
			notifyObservers(UpdateArguments.CONNECTION_INIT);

			// On initialise la connexion
			cs = new ClientSocket(host, port);

			Request r = new Request(ERequest.CONNECT, new ArrayList<String>() {
				{
					add(name);
				}
			});
			System.out.println("Envoi du connect");
			// On envoie la requête CONNNECT/xxx au serveur
			cs.makeRequest(r);
			// On crée et on commence à écouter les requêtes entrantes
			cdi = new CommandDispatcher(cs, gg, chat, this);
			// Le command dispatcher effectuera une action
			// si un welcome est reçue
			cdi.execute();
			System.out.println("En écoute");
			return;
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "L'hôte distant ne répond pas",
					"Erreur de connexion", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Erreur d'ouverture des canaux de communication",
					"Erreur I/O", JOptionPane.ERROR_MESSAGE);
		}
		// Si la connexion s'est mal passée on prévient la vue de réactiver le
		// bouton
		notifyObservers(UpdateArguments.CONNECTION_FAILED);
	}

	public void processClick(Point p) {
		gg.processClick(p);
	}

	// Getters : State Query de la vue

	public GameGrid getGrid() {
		return gg;
	}

	public Player[] getPlayers() {
		return players;
	}

	public Chat getChat() {
		return chat;
	}

}
