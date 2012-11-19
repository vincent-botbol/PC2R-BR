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

	private Socket sock;

	public ClientSocket(String host, int port) throws UnknownHostException,
			IOException {
		sock = new Socket(host, port);

		readStream = new BufferedReader(new InputStreamReader(
				sock.getInputStream()));
		writeStream = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(sock.getOutputStream())), true);
	}

	public ClientSocket() throws UnknownHostException, IOException {
		this("localhost", 2012);
	}

	public Response receiveResponse() throws IOException, ParseException {
		System.out.println("WESH");
		return parseResponse(readStream.readLine());
	}

	public void makeRequest(Request r) {
		writeStream.println(r.toString());
	}

	public void close() throws IOException {
		System.out.println("Fermeture socket");
		readStream.close();
		writeStream.close();
		sock.close();
	}

	private static Response parseResponse(String s) throws ParseException {

		String[] command = s.split("(?<!\\\\)/");
		List<String> list = new ArrayList<>();
		for (int i = 1; i < command.length; i++)
			list.add(command[i]);

		return new Response(command[0], list);
	}

}
