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

		System.out.println("avant");

		System.out.println("finish");
		sc.finishConnect();

		sc.configureBlocking(true);

		System.out.println("après");

		sc.write(ByteBuffer.wrap("WESH\n".getBytes()));

		System.out.println("après2");
		sc.write(ByteBuffer.wrap("WESH\n".getBytes()));

		System.out.println("après3");

		/*
		 * ByteBuffer b = ByteBuffer.allocate(256); while (sc.read(b) >= 0) {
		 * System.out.println(new String(b.array()).trim()); b.clear(); }
		 */

	}
}
