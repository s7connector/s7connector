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
package ch.ri.s7.converter.impl;

import ch.ri.s7.converter.base.S7Serializable;
import ch.ri.s7.impl.utils.S7Type;

public class StringConverter implements S7Serializable
{

	private static final int OFFSET_OVERALL_LENGTH = 0;
	private static final int OFFSET_CURRENT_LENGTH = 1;
	private static final int OFFSET_START = 2;
	
	@Override
	public void insert(Object javaType, byte[] buffer, int byteOffset, int bitOffset, int size)
	{
		String value = (String)javaType;
		
		int len = value.length();
		
		if (len > size)
			throw new IllegalArgumentException("String to big: " + len);
		
		buffer[byteOffset+OFFSET_OVERALL_LENGTH] = (byte)size;
		buffer[byteOffset+OFFSET_CURRENT_LENGTH] = (byte)len;
		
		byte[] strBytes = value.getBytes();
		for (int i=0; i<len; i++)
			buffer[byteOffset+OFFSET_START+i] = (byte)(strBytes[i] & 0xFF);
	}

	@Override
	public <T> T extract(Class<T> targetClass, byte[] buffer, int byteOffset, int bitOffset)
	{
		int len = buffer[byteOffset+OFFSET_CURRENT_LENGTH];
		
		byte[] bytes = new byte[len];
		
		for (int i=0; i<len; i++)
			bytes[i] = buffer[byteOffset+OFFSET_START+i];
		
		return targetClass.cast(new String(bytes));
	}

	@Override
	public int getSizeInBytes()
	{
		//Not static
		return 2; //2 bytes overhead
	}

	@Override
	public int getSizeInBits()
	{
		//Not static
		return 0;
	}

	@Override
	public S7Type getS7Type()
	{
		return S7Type.STRING;
	}

}
