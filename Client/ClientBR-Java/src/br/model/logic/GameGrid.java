package br.model.logic;

import java.awt.Graphics2D;
import java.awt.Point;

import br.model.ModelFacade;

public class GameGrid {
	@SuppressWarnings("unused")
	private ModelFacade observable;
	private String login;

	public GameGrid(ModelFacade modelFacade) {
		this.observable = modelFacade;
	}

	public void processClick(Point p, Graphics2D graphics) {

	}

	public void setLogin(String string) {
		this.login = string;
	}

	public String getLogin() {
		return login;
	}

}
