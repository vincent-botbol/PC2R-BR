package br.model;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import br.common.UpdateArguments;
import br.model.logic.Chat;
import br.model.logic.GameGrid;
import br.model.net.ClientSocket;
import br.model.net.Response;

class CommandDispatcher extends SwingWorker<Void, Void> {

	private ClientSocket cs;
	private ModelFacade observable;
	private GameGrid grid;

	@SuppressWarnings("unused")
	private Chat chat;

	public CommandDispatcher(ClientSocket cs, GameGrid gg, Chat chat,
			ModelFacade obs) {
		this.cs = cs;
		this.observable = obs;
		this.grid = gg;
		this.chat = chat;
	}

	@Override
	protected Void doInBackground() throws Exception {
		try {
			System.out.println("On attend les réponses");
			Response r;
			while (true) {
				try {
					r = cs.receiveResponse();
					System.out.println("DEBUG : Response reçue = " + r);
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
		return null;
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
			welcomeProcess(r.getArguments());
			break;
		case WRONG:
			break;
		case YOURTURN:
			break;
		default:
			break;

		}
	}

	private void welcomeProcess(List<String> arg) {
		grid.setLogin(arg.get(0));
		observable.setChanged();
		observable.notifyObservers(UpdateArguments.CONNECTION_SUCCESS);
	}

}
