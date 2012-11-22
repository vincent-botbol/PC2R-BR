package br.run;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TestSocketEtendue extends Socket {

	public TestSocketEtendue() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		class T extends Thread {
			TestSocketEtendue t;

			@Override
			public void run() {
				t = new TestSocketEtendue();
				synchronized (this) {
					System.out.println("test");
					this.notify();
				}
				try {
					System.out.println("connect");
					t.connect(new InetSocketAddress("localhost", 2012));
					System.out.println("connect");
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
