package br.model;

import java.io.IOException;
import java.text.ParseException;

import javax.swing.JOptionPane;

import br.model.logic.Chat;
import br.model.logic.GameGrid;
import br.model.net.ClientSocket;
import br.model.net.Response;

class CommandDispatcher {

	private ClientSocket cs;
	private ModelFacade observable;
	private GameGrid grid;
	private Chat chat;

	public CommandDispatcher(ClientSocket cs, GameGrid gg, Chat chat,
			ModelFacade obs) {
		this.cs = cs;
		this.observable = obs;
		this.grid = gg;
		this.chat = chat;
	}

	public void startListening() {
		try {
			Response r;
			while (true) {
				try {
					r = cs.receiveResponse();
					dispatch(r);
				} catch (ParseException e) {
					System.err
							.println("Parsing response : unknown protocol command");
				}
			}

		} catch (IOException e) {
			System.err.println("Error : cannot read response.");
			JOptionPane.showMessageDialog(null, e.getMessage(), "I/O serveur",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void dispatch(Response r) {
		switch (r.getCommand()) {
		case ACCESSDENIED:
			break;
		case ALLYOURBASE:
			break;
		case AWINNERIS:
			break;
		case DEATH:
			break;
		case DRAWGAME:
			break;
		case HEYLISTEN:
			break;
		case MISS:
			break;
		case OK:
			break;
		case OUCH:
			break;
		case PLAYERS:
			break;
		case SHIP:
			break;
		case TOUCHE:
			break;
		case WELCOME:
			break;
		case WRONG:
			break;
		case YOURTURN:
			break;
		default:
			break;

		}
	}

}
