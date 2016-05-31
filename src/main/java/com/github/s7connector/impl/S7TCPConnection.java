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

import com.github.s7connector.exception.S7Exception;
import com.github.s7connector.impl.nodave.DaveArea;
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
public class S7TCPConnection extends S7BaseConnection
{

	/**
	 * Creates a new Instance to the given host
	 * @param host
	 * @throws EthernetControlException
	 */
	public S7TCPConnection(String host) throws S7Exception
	{
		this(host, 0, 2);
	}


	/**
	 * Creates a new Instance to the given host, rack and slot
	 * @param host
	 * @throws EthernetControlException
	 */
	public S7TCPConnection(String host, int rack, int slot) throws S7Exception
	{
		this.host = host;
		this.rack = rack;
		this.slot = slot;
		this.setupSocket();
	}

	/**
	 * Rack and slot number
	 */
	private int rack, slot;

	/**
	 * The Host to connect to
	 */
	private String host;

	/**
	 * Sets up the socket
	 */
	private void setupSocket()
	{
		try
		{
			socket = new Socket();
			socket.setSoTimeout(2000);
			socket.connect(new InetSocketAddress(host, 102));
			
			di = new PLCinterface(
					socket.getOutputStream(),
					socket.getInputStream(),
					"IF1",
					DaveArea.LOCAL.getCode(), //TODO Local MPI-Address?
					Nodave.PROTOCOL_ISOTCP
					);

			dc = new TCPConnection(di, rack, slot);
			int res = dc.connectPLC();
			checkResult(res);

			super.init(dc);
		}
		catch (Exception e)
		{
			throw new S7Exception("constructor", e);
		}

	}

	/**
	 * The Interface
	 */
	private PLCinterface di;

	/**
	 * The COnnection
	 */
	private TCPConnection dc;

	/**
	 * The Socket
	 */
	private Socket socket;

	@Override
	public void close()
	{
		try
		{
			socket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


}
