package net.stuxcrystal.minehack.mineping.connector.pooled;

import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketRequest extends Request<Socket> {

	SocketRequest(InetSocketAddress address) {
		super(address);
	}

	@Override
	public Socket connect() {
		// TODO Auto-generated method stub
		return null;
	}

}
