/*
Copyright 2016 Thomas Rudin

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

import com.github.s7connector.converter.base.S7Serializable;
import com.github.s7connector.exception.S7Exception;
import com.github.s7connector.impl.nodave.DaveArea;
import com.github.s7connector.impl.nodave.Nodave;
import com.github.s7connector.impl.nodave.S7Connection;
import com.github.s7connector.impl.utils.S7Type;

/**
 * Base-Connection for the S7-PLC Connection
 * Libnodave: http://libnodave.sourceforge.net/
 * @author Thomas Rudin
 *
 */
public abstract class S7BaseConnection implements S7Connector
{

	/**
	 * Checks the Result
	 * @param libnodaveResult
	 * @param address
	 */
	public static void checkResult(int libnodaveResult)
	{
		if (libnodaveResult != Nodave.RESULT_OK)
		{
			String msg = Nodave.strerror(libnodaveResult);
			throw new IllegalArgumentException("Result: " + msg);
		}
	}



	protected void init(S7Connection dc)
	{
		this.dc = dc;
	}

	private S7Connection dc;

	@Override
	public synchronized byte[] read(DaveArea area, int areaNumber, int bytes, int offset)
	{
		if (bytes > MAX_SIZE)
		{
			byte[] ret = new byte[bytes];
			
			byte[] currentBuffer = this.read(area, areaNumber, MAX_SIZE, offset);
			System.arraycopy(currentBuffer, 0, ret, 0, currentBuffer.length);
			
			byte[] nextBuffer = this.read(area, areaNumber, bytes-MAX_SIZE, offset+MAX_SIZE);
			System.arraycopy(nextBuffer, 0, ret, currentBuffer.length, nextBuffer.length);
			
			
			return ret;
		}
		else
		{
			byte[] buffer = new byte[bytes];
			int ret = dc.readBytes(
					area,
					areaNumber,
					offset,
					bytes,
					buffer
					);

			checkResult(ret);
			return buffer;
		}
	}

	@Override
	public byte[] readBlock(int blockNumber, int bytes, int offset)
	{
		return read(DaveArea.DB, blockNumber, bytes, offset);
	}

	@Override
	public <T> T readObject(Class<T> javaType, S7Type s7type, DaveArea area, int areaNumber, int byteOffset, int bitOffset)
	{
		try
		{
			byte[] buffer = read(area, areaNumber, s7type.getByteSize(), byteOffset);
			S7Serializable s = s7type.getSerializer().newInstance();
			T ret = s.extract(javaType, buffer, byteOffset, bitOffset);
			return ret;
		}
		catch (Exception e)
		{
			throw new S7Exception("readObject", e);
		}
	}

	private static final int MAX_SIZE = 96;

	@Override
	public synchronized void write(DaveArea area, int areaNumber, int offset, byte[] buffer)
	{
		if (buffer.length > MAX_SIZE)
		{
			//Split buffer
			byte[] subBuffer = new byte[MAX_SIZE];
			byte[] nextBuffer = new byte[buffer.length - subBuffer.length];

			System.arraycopy(buffer, 0, subBuffer, 0, subBuffer.length);
			System.arraycopy(buffer, MAX_SIZE, nextBuffer, 0, nextBuffer.length);

			this.write(area, areaNumber, offset, subBuffer);
			this.write(area, areaNumber, offset+subBuffer.length, nextBuffer);
		}
		else
		{
			//Size fits
			int ret = dc.writeBytes(
					area,
					areaNumber,
					offset,
					buffer.length,
					buffer
					);
			//Check return-value
			checkResult(ret);
		}
	}

	@Override
	public void writeBlock(int blockNumber, int offset, byte[] buffer)
	{
		write(DaveArea.DB, blockNumber, offset, buffer);
	}

	protected static void dump(byte[] b)
	{
		for (int i=0; i<b.length; i++)
			System.out.print( Integer.toHexString(b[i] & 0xFF) + ",");
	}


	public static final String PROPERTY_AREA = "area";
	public static final String PROPERTY_AREANUMBER = "areanumber";
	public static final String PROPERTY_BYTES = "bytes";
	public static final String PROPERTY_OFFSET = "offset";

}
