package ch06;

import java.io.IOException;
import java.net.Socket;

import ch05.AbstractClient;

public class ChatClient extends ch06.AbstractClient {

	public ChatClient(String name) {
		super(name);
	}

	@Override
	protected void connectToServer() throws IOException {
		super.setSocket(new Socket("192.168.0.48", 5000));
	}

	public static void main(String[] args) {
		ChatClient chatClient = new ChatClient("홍길동");
		chatClient.run();
	}

}
