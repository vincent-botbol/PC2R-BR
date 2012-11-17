package br.model.logic;

import br.model.ModelFacade;

public class Chat {
	private ModelFacade observable;

	public Chat(ModelFacade modelFacade) {
		this.observable = modelFacade;
	}
}
