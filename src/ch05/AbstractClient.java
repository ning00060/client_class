package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// 2단계 - 상속 활용
public abstract class AbstractClient {
	private Socket socket;//
	private BufferedReader socketReader;
	private PrintWriter socketWriter;
	private BufferedReader keyboardReader;

	// set 메서드
	protected void setSocket(Socket socket) {
		this.socket = socket;
	}

	// get 메서드
	protected Socket getSocket() {
		return this.socket;

	}

	// 1. 서버 포트 입력
	protected abstract void setupSocket() throws IOException;

	// 2. 스트림 초기화
	private void setupStream() throws IOException {
		socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		socketWriter = new PrintWriter(socket.getOutputStream(), true);
		keyboardReader = new BufferedReader(new InputStreamReader(System.in));
	}

	// 3. 데이터 쓰레드 구성
	private void startChat() {
		Thread writeThread = clientWrite();
		Thread readThread = clientRead();

		writeThread.start();
		readThread.start();

		try {
			writeThread.join();
			readThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	// 3-1 read스레드
	private Thread clientRead() {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = socketReader.readLine()) != null) {
					System.out.println("서버 msg: " + msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// 3-2 write스레드
	private Thread clientWrite() {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = keyboardReader.readLine()) != null) {
					socketWriter.println(msg);
					socketWriter.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public final void run() {
		try {
			setupSocket();
			setupStream();
			startChat();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clientClean();
		}

	}

	private void clientClean() {
		try {
			if (this.socket != null) {
				this.socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
