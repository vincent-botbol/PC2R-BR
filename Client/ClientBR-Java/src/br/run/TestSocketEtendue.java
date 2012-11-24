package br.run;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TestSocketEtendue {

	public static void main(String[] args) throws IOException,
			InterruptedException {
		class T extends Thread {
			Socket t;

			@Override
			public void run() {
				t = new Socket();
				synchronized (this) {
					notify();
				}
				try {
					System.out.println("connect1");
					t.connect(new InetSocketAddress("localhost", 2012));
					System.out.println("connect2");
				} catch (IOException e) {
					e.printStackTrace();
				}
				super.run();
			}

			public void closeConnexion() throws IOException {
				System.out.println("close");
				t.close();
			}

		}

		T t = new T();
		t.start();

		synchronized (t) {
			t.wait();
		}

		t.closeConnexion();

	}
}
