package br.controller;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.ParseException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import br.common.UpdateArguments;
import br.controller.net.ClientSocket;
import br.controller.net.Response;
import br.model.ModelFacade;

class CommandDispatcher extends SwingWorker<Void, Response> {

	private ModelFacade model;
	private Controller control;
	private ClientSocket socket;
	private int port;
	private String host;
	private String pseudo;

	public CommandDispatcher(Controller control, ModelFacade model,
			String pseudo, String host, int port) {
		this.control = control;
		this.model = model;
		this.pseudo = pseudo;
		this.host = host;
		this.port = port;
	}

	@Override
	protected Void doInBackground() {

		try {
			System.out.println("On se connecte");
			socket = new ClientSocket(pseudo, host, port);
			System.out.println("On se connecte pas");
		} catch (IOException e) {
			if (!isCancelled()) {
				model.notifyView(UpdateArguments.CONNECTION_FAILED);
				System.out.println("failed : " + e.getMessage());
			}
			e.printStackTrace();
			return null;
		}
		System.out.println("DEBUG.");
		control.setSocket(socket);

		return startReadingLoop();
	}

	public ClientSocket getSocket() {
		return socket;
	}

	private Void startReadingLoop() {
		try {
			System.out.println("Connexion établie - En attente");
			Response r;
			while (true) {
				try {
					r = socket.receiveResponse();
					System.out.println("DEBUG : Response reçue = " + r);
					publish(r);
				} catch (ParseException e) {
					System.err.println(e.getMessage());
				}
			}
		} catch (InterruptedIOException e) {
			try {
				socket.close();
			} catch (IOException e1) {
				System.out.println("erreur close socket");
			}
			System.out.println("WORKER ARRETE PAR INTERRUPT");
		} catch (IOException e) {
			System.err.println("Error : connexion lost");
			JOptionPane.showMessageDialog(null, e.getMessage(), "I/O exc read",
					JOptionPane.ERROR_MESSAGE);
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
		model.gotWelcomed(arg.get(0));
	}

}
