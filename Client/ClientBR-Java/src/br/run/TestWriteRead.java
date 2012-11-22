package br.run;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TestWriteRead {

	public TestWriteRead() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		SocketChannel sc = SocketChannel.open();

		sc.configureBlocking(false);
		sc.connect(new InetSocketAddress("localhost", 2012));

		sc.configureBlocking(true);
		sc.finishConnect();
		ByteBuffer b = ByteBuffer.allocate(128);
		while (sc.read(b) >= 0) {
			System.out.println(new String(b.array()).trim());
			b.clear();
			System.out.println("test");
		}

	}
}
