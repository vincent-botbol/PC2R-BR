package br.run;

import br.controller.Controller;
import br.model.ModelFacade;
import br.vue.ViewFacade;

public class Main {

	public static boolean DEBUG = false;

	public static void main(String[] args) {
		DEBUG = false;
		ModelFacade model = new ModelFacade();
		ViewFacade vue = new ViewFacade(model);
		@SuppressWarnings("unused")
		Controller cont = new Controller(model, vue);
	}

}
