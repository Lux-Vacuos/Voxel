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

package net.guerra24.voxel.client.network;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.listener.SocketListener;

import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.universal.network.packets.NetworkPosition;
import net.guerra24.voxel.universal.network.packets.WorldTime;

public class DedicatedClientListener implements SocketListener {

	private final Voxel voxel;

	public DedicatedClientListener(Voxel voxel) {
		this.voxel = voxel;
	}

	@Override
	public void connected(Connection con) {
	}

	@Override
	public void disconnected(Connection con) {
		con.close();
	}

	@Override
	public void received(Connection con, Object obj) {
		if (obj instanceof NetworkPosition) {
			NetworkPosition pos = (NetworkPosition) obj;
			voxel.getGameResources().player.setPosition(pos.getPos());
		} else if (obj instanceof WorldTime) {
			WorldTime time = (WorldTime) obj;
			voxel.getGameResources().getSkyboxRenderer().setTime(time.getTime());
		}
	}

}
