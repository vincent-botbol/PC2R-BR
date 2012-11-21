package br.run;

import br.controller.ControllerFacade;
import br.model.ModelFacade;
import br.vue.ViewFacade;

public class Main {

	public static void main(String[] args) {
		ModelFacade model = new ModelFacade();
		ViewFacade vue = new ViewFacade(model);
		@SuppressWarnings("unused")
		ControllerFacade cont = new ControllerFacade(model, vue);

	}

}
