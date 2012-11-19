package br.model;

import java.awt.Point;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
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

	// M�thodes = Appels du controleur
	public void establishConnection(String host, final String name) {
		this.establishConnection(host, name, 2012);
	}

	public void establishConnection(String host, final String name, int port) {
		try {
			// On pr�vient la vue que l'on est en train de se connecter
			setChanged();
			notifyObservers(UpdateArguments.CONNECTION_INIT);

			// Probl�me de freeze ici : mettre un thread
			// � voir

			// On initialise la connexion
			cs = new ClientSocket(host, port);

			Request r = new Request(ERequest.CONNECT, new ArrayList<String>() {
				{
					add(name);
				}
			});
			// On envoie la requ�te CONNNECT/xxx au serveur
			cs.makeRequest(r);
			System.out.println("Apr�s 2:" + new Date());
			// On cr�e et on commence � �couter les requ�tes entrantes
			cdi = new CommandDispatcher(cs, gg, chat, this);
			// Lancement de la boucle d'�coute du dispatcher
			cdi.execute();
			return;
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "L'h�te distant ne r�pond pas",
					"Erreur de connexion", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Erreur lors de l'ouverture des canaux de communication",
					"Erreur I/O", JOptionPane.ERROR_MESSAGE);
		}
		setChanged();
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
