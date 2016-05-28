package com.github.s7connector.test.test.connector;

import java.io.IOException;

import com.github.s7connector.impl.S7Connector;
import com.github.s7connector.impl.nodave.DaveArea;
import com.github.s7connector.impl.utils.S7Type;

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
