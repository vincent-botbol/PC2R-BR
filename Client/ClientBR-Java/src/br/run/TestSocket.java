package br.run;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestSocket {

	public static void testSocket() {
		try (Socket sock = new Socket()) {
			System.out.println("avant");
			InetSocketAddress adr = new InetSocketAddress("blabla", 2012);
			System.out.println("après");
			sock.connect(adr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				JFrame f = new JFrame("test socket");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				JButton b = new JButton("test");
				b.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						testSocket();
					}
				});
				f.add(b);
				f.pack();
				f.setVisible(true);
			}
		};

		SwingUtilities.invokeLater(r);
	}
}
