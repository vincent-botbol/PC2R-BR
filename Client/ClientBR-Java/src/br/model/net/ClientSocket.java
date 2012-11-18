package br.model.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

public class ClientSocket {

	private Reader readStream;
	private Writer writeStream;

	private Socket sock;

	public ClientSocket(String host, int port) throws UnknownHostException,
			IOException {

		sock = new Socket(host, port);

		readStream = new BufferedReader(new InputStreamReader(
				sock.getInputStream()));
		writeStream = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(sock.getOutputStream())), true);
	}

	public synchronized void establishConnexion(Request r) {
		makeRequest(r);
	}

	public ClientSocket() throws UnknownHostException, IOException {
		this("localhost", 2012);
	}

	public Response receiveResponse() throws IOException, ParseException {
		return parseResponse(((BufferedReader) readStream).readLine());
	}

	public void makeRequest(Request r) {
		try {
			writeStream.write(r.toString());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erreur d'envoi de requête",
					"Erreur I/O", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void close() throws IOException {
		System.out.println("Fermeture socket");
		readStream.close();
		writeStream.close();
		sock.close();
	}

	private static Response parseResponse(String s) throws ParseException {

		String[] command = s.split("(?<!\\\\)/");
		List<String> list = Arrays.asList(command);

		return new Response(list.remove(0), list);
	}

}
