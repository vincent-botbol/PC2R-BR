package br.controller.net;

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
import java.util.Arrays;
import java.util.List;

public class ClientSocket extends Socket {

	private BufferedReader readStream;
	private PrintWriter writeStream;

	/*
	 * Je veux une implem de thread : Qui soit stoppable pendant la connexion
	 * Qui puisse attendre la fin de la connexion Qui puisse lire en mode
	 * bloquant
	 */

	public ClientSocket(String pseudo, String host, int port)
			throws UnknownHostException, IOException {
		super(host, port);

		readStream = new BufferedReader(new InputStreamReader(getInputStream()));
		writeStream = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(getOutputStream())), true);
		makeRequest(new Request(ERequest.CONNECT, Arrays.asList(pseudo)));
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

	public void close() throws IOException {
		readStream.close();
		writeStream.close();
		super.close();
	}

	private static Response parseResponse(String s) throws ParseException {

		String[] command = s.split("(?<!\\\\)/");
		List<String> list = new ArrayList<>();
		for (int i = 1; i < command.length; i++)
			list.add(command[i]);

		return new Response(command[0], list);
	}

}
