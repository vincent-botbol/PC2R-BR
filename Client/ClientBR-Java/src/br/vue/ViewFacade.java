package br.vue;

import java.awt.Color;
import java.awt.Container;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.common.UpdateArguments;
import br.model.ModelFacade;
import br.vue.components.ConnectionPane;
import br.vue.components.GamePane;
import br.vue.components.MainFrame;

public class ViewFacade implements Observer {

	private ModelFacade model;

	private MainFrame mf;
	private ConnectionPane conn;
	private GamePane game;

	public ViewFacade(ModelFacade model) {
		this.model = model;

		model.addObserver(this);

		Runnable r = new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		};

		SwingUtilities.invokeLater(r);
	}

	private synchronized void createAndShowGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			System.err.println("Unable to load System's LooknFeel");
		}

		mf = new MainFrame();
		conn = new ConnectionPane();
		game = new GamePane();

		setFrameContentPane(conn);

		this.notifyAll();
	}

	public void setFrameContentPane(Container c) {
		mf.setContentPane(c);
		mf.pack();
	}

	public MainFrame getMf() {
		return mf;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg != null && arg instanceof UpdateArguments) {
			switch ((UpdateArguments) arg) {
			case CONNECTION_FAILED:
				getConnexionPane().getConnect().setEnabled(true);
				getConnexionPane().showInfo(
						"Impossible de se connecter au serveur\n"
								+ "Vérifiez vos données");
				break;
			case CONNECTION_INIT:
				getConnexionPane().getConnect().setEnabled(false);
				break;
			case CONNECTION_SUCCESS:
				System.out.println("CONNECTION SUCCESS !!");
				setFrameContentPane(game);
				mf.pack();
				game.getLog().ajouterMessage("Server",
						"Bienvenue " + model.getGrid().getLogin(), Color.GREEN);
				break;
			default:
				break;
			}
		}
	}

	public ConnectionPane getConnexionPane() {
		return conn;
	}

}
