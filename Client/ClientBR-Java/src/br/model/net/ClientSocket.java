package br.model.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ClientSocket {

	private BufferedReader readStream;
	private PrintWriter writeStream;
	private final String host;
	private final int port;

	private Socket sock;

	private boolean isConnected;

	public ClientSocket(String host, int port) throws UnknownHostException,
			IOException {
		this.host = host;
		this.port = port;
		this.isConnected = false;
	}

	// synchronisé avec le makeRequest pour que l'écoute se réalise après
	// l'instanciation (la connexion est threadée)
	public synchronized void connect(final String pseudo)
			throws UnknownHostException, IOException {
		sock = new Socket(host, port);
		readStream = new BufferedReader(new InputStreamReader(
				sock.getInputStream()));
		writeStream = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(sock.getOutputStream())), true);
		makeRequest(new Request(ERequest.CONNECT, new ArrayList<String>() {
			{
				add(pseudo);
			}
		}));
		this.isConnected = true;
	}

	public ClientSocket() throws UnknownHostException, IOException {
		this("localhost", 2012);
	}

	// Non synchrone, peu importe d'envoyer et de recevoir au même moment
	// receive response => IO = fatal close
	public synchronized Response receiveResponse() throws IOException,
			ParseException {
		String str = readStream.readLine();
		if (str == null) {
			throw new IOException("End of reading");
		}
		return parseResponse(str);
	}

	// make request => IO = fatal close
	public void makeRequest(Request r) {
		writeStream.println(r.toString());
	}

	public void close() {
		try {
			if (readStream != null)
				readStream.close();
			if (writeStream != null)
				writeStream.close();
			if (sock != null)
				sock.close();
		} catch (IOException e) { // ignore
		}
		this.isConnected = false;
	}

	private static Response parseResponse(String s) throws ParseException {

		String[] command = s.split("(?<!\\\\)/");
		List<String> list = new ArrayList<>();
		for (int i = 1; i < command.length; i++)
			list.add(command[i]);

		return new Response(command[0], list);
	}

	public boolean isConnected() {
		// TODO Auto-generated method stub
		return this.isConnected;
	}
}
