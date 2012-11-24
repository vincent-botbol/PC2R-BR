package br.controller.net;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientSocket {

	private SocketChannel socketChan;
	private final ByteBuffer inputBuffer;

	/**
	 * est connecté mais possiblement n'a pas reçu welcome
	 */
	private boolean isConnected;

	/**
	 * 
	 * @param pseudo
	 * @param host
	 * @param port
	 * @throws IOException
	 * 
	 */
	public ClientSocket(SocketAddress addr) throws IOException {
		inputBuffer = ByteBuffer.allocate(512);
		socketChan = SocketChannel.open();
		socketChan.configureBlocking(false);
		socketChan.connect(addr);
		socketChan.configureBlocking(true);
	}

	public void connect(String pseudo) throws IOException {
		socketChan.finishConnect();
		makeRequest(new Request(ERequest.CONNECT, Arrays.asList(pseudo)));
		isConnected = true;
	}

	public void makeRequest(Request r) throws IOException {
		// charset?
		socketChan.write(ByteBuffer.wrap((r.toString() + "\n").getBytes()));
	}

	/**
	 * (supposition) Ne supporte pas le multiligne du genre
	 * COM1/arg/\nCOM2/arg2/ => peut être chiant pour le spectator
	 * 
	 * @return
	 * @throws ParseException
	 *             Quand la commande recue est inconnue
	 * @throws IOException
	 * 
	 */
	public Response receiveResponse() throws ParseException,
			ClosedChannelException, IOException {
		inputBuffer.clear();
		socketChan.read(inputBuffer);

		return parseResponse(new String(inputBuffer.array()).trim());
	}

	/**
	 * 
	 * @throws AsynchronousCloseException
	 *             close reÃ§u pendant la connexion async de la socketchannel
	 * @throws IOException
	 *             le reste
	 */
	public void close() throws IOException {
		isConnected = false;
		socketChan.close();
	}

	private static Response parseResponse(String s) throws ParseException {

		String[] command = s.split("(?<!\\\\)/");
		List<String> list = new ArrayList<>();
		for (int i = 1; i < command.length; i++)
			list.add(command[i]);

		return new Response(command[0], list);
	}

	public boolean isConnected() {
		return isConnected;
	}

}
