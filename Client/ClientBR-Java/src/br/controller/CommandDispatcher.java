package br.controller;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.UnresolvedAddressException;
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
	private Controller controller;
	private String pseudo, host;
	private int port;

	public CommandDispatcher(ModelFacade model, Controller controller,
			String pseudo, String host, int port) {
		this.model = model;
		this.controller = controller;
		this.pseudo = pseudo;
		this.host = host;
		this.port = port;
	}

	@Override
	protected Void doInBackground() {
		ClientSocket socket = null;

		model.notifyView(UpdateArguments.RESOLV_INIT);

		SocketAddress addr = new InetSocketAddress(host, port);
		try {
			// bloquant jusqu'à résolution
			socket = new ClientSocket(addr);
		} catch (UnresolvedAddressException e) {
			model.notifyView(UpdateArguments.RESOLV_FAILED);
			return null;
		} catch (IOException e) {
			System.out.println("failed créa socket");
			e.printStackTrace();
			return null;
		}

		controller.setSocket(socket);

		model.notifyView(UpdateArguments.CONN_INIT);
		// le bouton cancel va apparaitre

		try {
			// bloquant
			socket.connect(pseudo);
		} catch (ConnectException e) {
			model.notifyView(UpdateArguments.CONN_FAILED);
			return null;
		} catch (AsynchronousCloseException e) {
			model.notifyView(UpdateArguments.CONN_ABORTED);
			return null;
		} catch (IOException e) {
			model.notifyView(UpdateArguments.CONN_FAILED);
			return null;
		}

		return startReadingLoop(socket);
	}

	private Void startReadingLoop(ClientSocket socket) {
		try {
			System.out.println("DEBUG : Connexion etablie - En attente");
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
		} catch (AsynchronousCloseException e) {
			model.notifyView(UpdateArguments.CONN_ABORTED);
			return null;
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
			playersProcess(r.getArguments());
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

	private void playersProcess(List<String> arguments) {
		model.setPlayers(arguments);
	}

	private void welcomeProcess(List<String> arg) {
		model.gotWelcomed(arg.get(0));
	}

}
