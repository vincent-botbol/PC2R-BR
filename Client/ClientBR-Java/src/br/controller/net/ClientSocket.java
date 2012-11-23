package br.controller.net;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.SocketChannel;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ClientSocket {

	private SocketChannel socketChan;

	/**
	 * 
	 * @param pseudo
	 * @param host
	 * @param port
	 * @throws IOException
	 *             Normalement ça arrive pas
	 */
	public ClientSocket(String pseudo, String host, int port)
			throws IOException {
		System.out.println("avant");
		socketChan = SocketChannel.open();
		socketChan.configureBlocking(false);
		socketChan.connect(new InetSocketAddress(host, port));

		System.out.println("avant2");

		socketChan.finishConnect();
		System.out.println("après");
		socketChan.configureBlocking(true);
		System.out.println("après2");
	}

	public void makeRequest(Request r) throws IOException {
		// charset?
		socketChan.write(ByteBuffer.wrap(r.toString().getBytes()));
	}

	/**
	 * 
	 * @return
	 * @throws ParseException
	 *             Quand la commande reçue est inconnue
	 * @throws IOException
	 *             quand il lit rien
	 */
	public Response receiveResponse() throws ParseException, IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteBuffer buff = ByteBuffer.allocate(1024);

		socketChan.read(buff);

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(baos.toByteArray())));

		return parseResponse(br.readLine().trim());
	}

	/**
	 * 
	 * @throws AsynchronousCloseException
	 *             close reçu pendant la connexion async de la socketchannel
	 * @throws IOException
	 *             le reste
	 */
	public void close() throws AsynchronousCloseException, IOException {
		socketChan.close();
	}

	private static Response parseResponse(String s) throws ParseException {

		String[] command = s.split("(?<!\\\\)/");
		List<String> list = new ArrayList<>();
		for (int i = 1; i < command.length; i++)
			list.add(command[i]);

		return new Response(command[0], list);
	}

}
