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
package ch.ri.s7.test.test.connector;

import java.io.IOException;

import ch.ri.s7.impl.S7Connector;
import ch.ri.s7.impl.nodave.DaveArea;
import ch.ri.s7.impl.utils.S7Type;

public class DummyConnector implements S7Connector
{

	@Override
	public byte[] read(DaveArea area, int areaNumber, int bytes, int offset)
	{
		return buffer;
	}
	
	public byte[] buffer;


	@Override
	public byte[] readBlock(int blockNumber, int bytes, int offset)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T readObject(Class<T> javaType, S7Type s7type, DaveArea area,
			int areaNumber, int byteOffset, int bitOffset)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(DaveArea area, int areaNumber, int offset, byte[] buffer)
	{
		this.buffer = buffer;
		
		System.out.println("Size: " + buffer.length);
		
		for (int i=0; i<buffer.length; i++)
			System.out.print( Integer.toHexString(buffer[i] & 0xFF) + ",");
		
	}

	@Override
	public void writeBlock(int blockNumber, int offset, byte[] buffer)
	{
		this.buffer = buffer;
		
		System.out.println("Size: " + buffer.length);
		
		for (int i=0; i<buffer.length; i++)
			System.out.print( Integer.toHexString(buffer[i] & 0xFF) + ",");		
	}

	@Override
	public void close() throws IOException
	{
		// TODO Auto-generated method stub
		
	}


}
