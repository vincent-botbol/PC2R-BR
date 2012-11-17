package br.model.net;

public class TestReg {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] requests = new String[] { "Connect/ren�/",
				"PLAYERS/michel/ren�/bertrand/", "CONNECT/re\\/n�/",
				"CONNECT/re\\/n�\\\\e/" };

		for (String r : requests) {
			System.out.print(r + " : ");
			for (String s : r.split("(?<!\\\\)/")) {
				System.out.print(s + " ");
			}

			System.out.println();
		}

	}

}
