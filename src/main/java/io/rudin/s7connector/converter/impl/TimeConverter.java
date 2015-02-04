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

import io.rudin.s7connector.impl.utils.S7Type;

public class TimeConverter extends ByteConverter
{

	
	@Override
	public void insert(Object javaType, byte[] buffer, int byteOffset, int bitOffset, int size)
	{
		long l = (Long)javaType;
		
		byte b1 = (byte)( (byte)( l >> 0 ) & 0xFF);
		byte b2 = (byte)( (byte)( l >> 8 ) & 0xFF);
		byte b3 = (byte)( (byte)( l >> 16 ) & 0xFF);
		byte b4 = (byte)( (byte)( l >> 24 ) & 0xFF);
		
		super.insert(b1, buffer, byteOffset+3, bitOffset, 1);
		super.insert(b2, buffer, byteOffset+2, bitOffset, 1);
		super.insert(b3, buffer, byteOffset+1, bitOffset, 1);
		super.insert(b4, buffer, byteOffset+0, bitOffset, 1);
	}

	@Override
	public <T> T extract(Class<T> targetClass, byte[] buffer, int byteOffset, int bitOffset)
	{
		byte b1 = super.extract(Byte.class, buffer, byteOffset+3, bitOffset);
		byte b2 = super.extract(Byte.class, buffer, byteOffset+2, bitOffset);
		byte b3 = super.extract(Byte.class, buffer, byteOffset+1, bitOffset);
		byte b4 = super.extract(Byte.class, buffer, byteOffset+0, bitOffset);

		long l =
				(long)( ((long)b1 & 0xFF) << 0 ) |
				(long)( ((long)b2 & 0xFF) << 8 ) |
				(long)( ((long)b3 & 0xFF) << 16 ) |
				(long)( ((long)b4 & 0xFF) << 24 );
		
		return targetClass.cast(l);
	}

	@Override
	public int getSizeInBytes()
	{
		return 4;
	}

	@Override
	public S7Type getS7Type()
	{
		return S7Type.TIME;
	}

}
