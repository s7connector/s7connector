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
package com.github.s7connector.impl.nodave;

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
