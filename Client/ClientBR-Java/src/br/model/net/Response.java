package br.model.net;

import java.text.ParseException;
import java.util.List;

public class Response extends Command<EResponse> {

	public Response(String commandName, List<String> arguments)
			throws ParseException {
		super(commandName, arguments, EResponse.class);
	}
}
