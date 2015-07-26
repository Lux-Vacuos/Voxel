/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.guerra24.voxel.client.kernel.network;

import io.github.guerra24.voxel.client.kernel.util.Logger;
import io.github.guerra24.voxel.server.kernel.packets.ChatMessage;

import java.util.Scanner;

import com.jmr.wrapper.client.Client;

public class NetClient {
	private Client client;
	public int port1 = 4395;
	public int port2 = 4395;
	public Scanner scanner;
	public boolean loop = true;

	public NetClient() {
		Logger.log("Write your Username");
		client = new Client("localhost", port1, port2);
		client.setListener(new ClientListener());
		client.connect();

		Scanner in = new Scanner(System.in);

		if (client.isConnected()) {
			System.out.println("Enter a username: ");
			String username = in.nextLine();
			while (loop) {
				String s = in.nextLine();
				ChatMessage msg = new ChatMessage(username, s);
				client.getServerConnection().sendTcp(msg);
			}
		}
	}

}
