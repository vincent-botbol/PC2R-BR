package br.run;

import br.controller.TestController;
import br.model.ModelFacade;
import br.vue.ViewFacade;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ModelFacade model = new ModelFacade();
		ViewFacade vue = new ViewFacade(model);
		@SuppressWarnings("unused")
		TestController tc = new TestController(model, vue);
	}

}
