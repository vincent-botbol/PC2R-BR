package br.controller.net;

import java.util.List;

public class Request extends Command<ERequest> {

	public Request(ERequest command, List<String> arguments) {
		super(command, arguments);
	}
}
