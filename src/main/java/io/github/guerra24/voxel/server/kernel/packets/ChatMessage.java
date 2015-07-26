package io.github.guerra24.voxel.server.kernel.packets;

import java.io.Serializable;

public class ChatMessage implements Serializable {

	private static final long serialVersionUID = -800658599372304188L;
	public String username, message;

	public ChatMessage(String username, String message) {
		this.username = username;
		this.message = message;
	}

}