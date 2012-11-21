package br.model;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.ParseException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import br.common.UpdateArguments;
import br.model.logic.Chat;
import br.model.logic.GameGrid;
import br.model.net.ClientSocket;
import br.model.net.Response;

class CommandDispatcher extends SwingWorker<Void, Response> {

	private ClientSocket cs;
	private ModelFacade model;
	private GameGrid grid;

	@SuppressWarnings("unused")
	private Chat chat;

	public CommandDispatcher(ModelFacade obs) {
		this.model = obs;
	}

	@Override
	protected Void doInBackground() {
		try {
			System.out.println("On attend les réponses");
			Response r = null;
			while (true) {
				try {
					// Problème : soit le serveur n'envoie rien
					// Soit y a un problème dans le read
					// => plus côté serveur mais bon.
					r = cs.receiveResponse();
					System.out.println("DEBUG : Response reçue = " + r);
					dispatch(r);
				} catch (ParseException e) {
					System.err
							.println("Parsing response : unknown protocol command");
				}
				publish(r);
			}
		} catch (InterruptedIOException e) {
			// dans le cas où l'on a fait un cancel(true) (i.e : après
			// l'établissement de la connexion)
			System.out.println("WORKER ARRETE PAR INTERRUPT");
		} catch (IOException e) {
			System.err.println("Error : connexion lost");
			JOptionPane.showMessageDialog(null, e.getMessage(), "I/O exc read",
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
		model.setChanged();
		model.notifyObservers(UpdateArguments.CONNECTION_SUCCESS);
	}

}
