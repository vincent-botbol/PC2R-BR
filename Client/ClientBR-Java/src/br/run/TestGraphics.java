package br.run;

import javax.swing.JFrame;

import br.model.ModelFacade;
import br.vue.ViewFacade;

public class TestGraphics {

	public static void main(String[] args) {
		ModelFacade m = new ModelFacade();
		ViewFacade vf = new ViewFacade(m);
		synchronized (vf) {
			try {
				if (!vf.isReady())
					vf.wait();
			} catch (InterruptedException e) {
			}
			vf.getMf().setContentPane(vf.getGame());
			vf.getMf().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			vf.getMf().pack();
		}
	}

}
