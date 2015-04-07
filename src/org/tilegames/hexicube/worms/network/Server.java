package org.tilegames.hexicube.worms.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Server {
	
	Socket socket;
	
	public Server() {
		try {
			socket = new Socket("*", 1810);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void recieve() {
		BufferedReader in;
		PrintWriter out;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			if (in.ready()) {
				String line = in.readLine();
				Message msg = Message.create(line);
				// handle the message
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
}
