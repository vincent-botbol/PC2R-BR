package br.model.logic;

import br.model.ModelFacade;

public class Chat {
	@SuppressWarnings("unused")
	private ModelFacade observable;

	public Chat(ModelFacade modelFacade) {
		this.observable = modelFacade;
	}
}
