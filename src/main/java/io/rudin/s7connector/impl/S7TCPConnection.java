/*
 Part of S7Connector, a connector for S7 PLC's
 
 (C) Thomas Rudin (thomas@rudin-informatik.ch) 2012.

 S7Connector is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 3, or (at your option)
 any later version.

 S7Connector is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Visual; see the file COPYING.  If not, write to
 the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.  
*/
package io.rudin.s7connector.impl;

import io.rudin.s7connector.exception.S7Exception;
import io.rudin.s7connector.impl.nodave.DaveArea;
import io.rudin.s7connector.impl.nodave.Nodave;
import io.rudin.s7connector.impl.nodave.PLCinterface;
import io.rudin.s7connector.impl.nodave.TCPConnection;

import java.net.InetSocketAddress;
import java.net.Socket;



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
