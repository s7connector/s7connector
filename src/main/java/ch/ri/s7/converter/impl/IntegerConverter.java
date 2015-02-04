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

public class IntegerConverter implements S7Serializable
{

	
	private static final int OFFSET_HIGH_BYTE = 0;
	private static final int OFFSET_LOW_BYTE = 1;

	@Override
	public void insert(Object javaType, byte[] buffer, int byteOffset, int bitOffset, int size)
	{
		Integer value = (Integer)javaType;
		byte lower = (byte)( ( value >> 0 ) & 0xFF );
		byte higher = (byte)( ( value  >> 8 ) & 0xFF );
		buffer[byteOffset+OFFSET_LOW_BYTE] = lower;
		buffer[byteOffset+OFFSET_HIGH_BYTE] = higher;
	}

	@Override
	public <T> T extract(Class<T> targetClass, byte[] buffer, int byteOffset, int bitOffset)
	{
		byte lower = buffer[byteOffset+OFFSET_LOW_BYTE];
		byte higher = buffer[byteOffset+OFFSET_HIGH_BYTE];
		
		Integer i = (int)(lower & 0xFF) | (int)( (higher << 8) & 0xFF00);
		
		return targetClass.cast(i);
	}

	@Override
	public int getSizeInBytes()
	{
		return 2;
	}

	@Override
	public int getSizeInBits()
	{
		return 0;
	}

	@Override
	public S7Type getS7Type()
	{
		return S7Type.WORD;
	}

}
