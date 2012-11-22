package br.run;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.SocketChannel;

public class TestAsyncStop {

	// Faire des tests avec des selecteurs
	public static void main(String[] a) throws IOException,
			InterruptedException {
		class T extends Thread {
			SocketChannel sc;

			@Override
			public void run() {
				try {
					sc = SocketChannel.open();

					sc.configureBlocking(false);
					sc.connect(new InetSocketAddress("localhost", 2012));
					sc.configureBlocking(true);

					synchronized (this) {
						notifyAll();
					}
					try {
						sc.finishConnect();
					} catch (AsynchronousCloseException e) {
						System.out.println("cool.");
					}
					System.out.println("FIN FINISH");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			void stopConnect() throws IOException {
				System.out.println(sc.isConnectionPending());
				sc.close();
				System.out.println(sc.isConnectionPending());
			}
		}

		T t = new T();
		t.start();

		synchronized (t) {
			t.wait();
		}
		t.stopConnect();
	}
}
