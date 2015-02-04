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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PLCinterface
{
	int localMPI; // the adapter's MPI address
	String name;
	int protocol; // The kind of transport used on this interface.

	OutputStream out;
	InputStream in;
	int wp, rp;

	public PLCinterface(OutputStream out, InputStream in, String name,
			int localMPI, int protocol)
	{
		init(out, in, name, localMPI, protocol);
	}

	public void init(OutputStream oStream, InputStream iStream, String name,
			int localMPI, int protocol)
	{
		this.out = oStream;
		this.in = iStream;
		this.name = name;
		this.localMPI = localMPI;
		this.protocol = protocol;
	}

	public void write(byte[] b, int start, int len)
	{
		try
		{
			out.write(b, start, len);
		} catch (IOException e)
		{
			System.err.println("Interface.write: " + e);
		}
	}

	public int read(byte[] b, int start, int len)
	{
		int res;
		try
		{
			int retry = 0;
			while ((in.available() <= 0) && (retry < 500))
			{
				try
				{
					if (retry > 0)
					{
						Thread.sleep(1);
					}
					retry++;
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			res = 0;
			while ((in.available() > 0) && (len > 0))
			{
				res = in.read(b, start, len);
				start += res;
				len -= res;
			}
			return res;
		} catch (IOException e)
		{
			e.printStackTrace();
			return 0;
		}
	}

}
