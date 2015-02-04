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
package io.rudin.s7connector.impl.nodave;

public class TCPConnection extends S7Connection
{
	int rack;
	int slot;

	public TCPConnection(PLCinterface ifa, int rack, int slot)
	{
		super(ifa);
		this.rack = rack;
		this.slot = slot;
		PDUstartIn = 7;
		PDUstartOut = 7;
	}

	protected int readISOPacket()
	{
		int res = iface.read(msgIn, 0, 4);
		if (res == 4)
		{
			int len = (0x100 * msgIn[2]) + msgIn[3];
			res += iface.read(msgIn, 4, len);
		}
		else
		{
			return 0;
		}
		return res;
	}

	protected int sendISOPacket(int size)
	{
		size += 4;
		msgOut[0] = (byte) 0x03;
		msgOut[1] = (byte) 0x0;
		msgOut[2] = (byte) (size / 0x100);
		msgOut[3] = (byte) (size % 0x100);
		/*
		 * if (messageNumber == 0) { messageNumber = 1; msgOut[11] = (byte)
		 * ((messageNumber + 1) & 0xff); messageNumber++; messageNumber &= 0xff;
		 * //!! }
		 */

		iface.write(msgOut, 0, size);
		return 0;
	}

	@Override
	public int exchange(PDU p1)
	{
		msgOut[4] = (byte) 0x02;
		msgOut[5] = (byte) 0xf0;
		msgOut[6] = (byte) 0x80;
		sendISOPacket(3 + p1.hlen + p1.plen + p1.dlen);
		readISOPacket();
		return 0;
	}

	/**
	 * We have our own connectPLC(), but no disconnect() Open connection to a
	 * PLC. This assumes that dc is initialized by daveNewConnection and is not
	 * yet used. (or reused for the same PLC ?)
	 */
	public int connectPLC()
	{
		byte[] b4 = { (byte) 0x11, (byte) 0xE0, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0xC1,
				(byte) 0x02, (byte) 0x01, (byte) 0x00, (byte) 0xC2,
				(byte) 0x02, (byte) 0x01, (byte) 0x02, (byte) 0xC0,
				(byte) 0x01, (byte) 0x09 };

		System.arraycopy(b4, 0, msgOut, 4, b4.length);
		msgOut[17] = (byte) (rack + 1);
		msgOut[18] = (byte) slot;
		sendISOPacket(b4.length);
		readISOPacket();
		/*
		 * PDU p = new PDU(msgOut, 7); p.initHeader(1); p.addParam(b61);
		 * exchange(p); return (0);
		 */
		return negPDUlengthRequest();
	}
}
