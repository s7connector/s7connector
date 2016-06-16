/*
Copyright 2016 S7connector members (github.com/s7connector)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.github.s7connector.impl;

import java.net.InetSocketAddress;
import java.net.Socket;

import com.github.s7connector.api.DaveArea;
import com.github.s7connector.exception.S7Exception;
import com.github.s7connector.impl.nodave.Nodave;
import com.github.s7connector.impl.nodave.PLCinterface;
import com.github.s7connector.impl.nodave.TCPConnection;

/**
 * TCP_Connection to a S7 PLC
 *
 * @author Thomas Rudin
 * @href http://libnodave.sourceforge.net/
 *
 */
public final class S7TCPConnection extends S7BaseConnection {

	/**
	 * The COnnection
	 */
	private TCPConnection dc;

	/**
	 * The Interface
	 */
	private PLCinterface di;

	/**
	 * The Host to connect to
	 */
	private final String host;

	/**
	 * The port to connect to
	 */
	private final int port;

	/**
	 * Rack and slot number
	 */
	private final int rack, slot;

	/**
	 * The Socket
	 */
	private Socket socket;

	/**
	 * Creates a new Instance to the given host
	 *
	 * @param host
	 * @throws EthernetControlException
	 */
	public S7TCPConnection(final String host) throws S7Exception {
		this(host, 0, 2);
	}

	/**
	 * Creates a new Instance to the given host, rack and slot Uses port 102 as
	 * default
	 *
	 * @param host
	 * @throws EthernetControlException
	 */
	public S7TCPConnection(final String host, final int rack, final int slot) throws S7Exception {
		this(host, rack, slot, 102);
	}

	/**
	 * Creates a new Instance to the given host, rack, slot and port
	 *
	 * @param host
	 * @throws EthernetControlException
	 */
	public S7TCPConnection(final String host, final int rack, final int slot, final int port) throws S7Exception {
		this.host = host;
		this.rack = rack;
		this.slot = slot;
		this.port = port;
		this.setupSocket();
	}

	@Override
	public void close() {
		try {
			this.socket.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void finalize() throws Throwable {
		this.close();
	}

	/**
	 * Sets up the socket
	 */
	private void setupSocket() {
		try {
			this.socket = new Socket();
			this.socket.setSoTimeout(2000);
			this.socket.connect(new InetSocketAddress(this.host, this.port));

			this.di = new PLCinterface(this.socket.getOutputStream(), this.socket.getInputStream(), "IF1",
					DaveArea.LOCAL.getCode(), // TODO Local MPI-Address?
					Nodave.PROTOCOL_ISOTCP);

			this.dc = new TCPConnection(this.di, this.rack, this.slot);
			final int res = this.dc.connectPLC();
			checkResult(res);

			super.init(this.dc);
		} catch (final Exception e) {
			throw new S7Exception("constructor", e);
		}

	}

}
