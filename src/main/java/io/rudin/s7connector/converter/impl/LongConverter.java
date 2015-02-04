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
package io.rudin.s7connector.converter.impl;

import io.rudin.s7connector.converter.base.S7Serializable;
import io.rudin.s7connector.impl.utils.S7Type;

public class LongConverter implements S7Serializable
{


	@Override
	public void insert(Object javaType, byte[] buffer, int byteOffset, int bitOffset, int size)
	{
		Long value = (Long)javaType;
		byte b1 = (byte)( ( value >> 0 ) & 0xFF );
		byte b2 = (byte)( ( value  >> 8 ) & 0xFF );
		byte b3 = (byte)( ( value  >> 16 ) & 0xFF );
		byte b4 = (byte)( ( value  >> 24 ) & 0xFF );
		buffer[byteOffset+0] = b1;
		buffer[byteOffset+1] = b2;
		buffer[byteOffset+2] = b3;
		buffer[byteOffset+3] = b4;
	}

	@Override
	public <T> T extract(Class<T> targetClass, byte[] buffer, int byteOffset, int bitOffset)
	{
		byte b1 = buffer[byteOffset+0];
		byte b2 = buffer[byteOffset+1];
		byte b3 = buffer[byteOffset+2];
		byte b4 = buffer[byteOffset+3];
		
		Integer i = 
				(int)( (b1 << 0) & 0x000000FF) | 
				(int)( (b2 << 8) & 0x0000FF00) | 
				(int)( (b3 << 16) & 0x00FF0000) | 
				(int)( (b4 << 24) & 0xFF000000);
		
		return targetClass.cast(i);
	}

	@Override
	public int getSizeInBytes()
	{
		return 4;
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
