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

public class RealConverter implements S7Serializable
{

	
	private static final int OFFSET_POS1 = 0;
	private static final int OFFSET_POS2 = 1;
	private static final int OFFSET_POS3 = 2;
	private static final int OFFSET_POS4 = 3;

	@Override
	public void insert(Object javaType, byte[] buffer, int byteOffset, int bitOffset, int size)
	{
		float fValue = Float.parseFloat(javaType.toString());

		int iValue = Float.floatToIntBits(fValue);
		
		buffer[byteOffset + OFFSET_POS4] = (byte)( (iValue >> 0) & 0xFF );
		buffer[byteOffset + OFFSET_POS3] = (byte)( (iValue >> 8) & 0xFF );
		buffer[byteOffset + OFFSET_POS2] = (byte)( (iValue >> 16) & 0xFF );
		buffer[byteOffset + OFFSET_POS1] = (byte)( (iValue >> 24) & 0xFF );
	}

	@Override
	public <T> T extract(Class<T> targetClass, byte[] buffer, int byteOffset, int bitOffset)
	{
		int iValue = 
				(int)( ((int)buffer[byteOffset + OFFSET_POS4] & 0xFF) << 0 ) |
				(int)( ((int)buffer[byteOffset + OFFSET_POS3] & 0xFF) << 8 ) |
				(int)( ((int)buffer[byteOffset + OFFSET_POS2] & 0xFF) << 16 ) |
				(int)( ((int)buffer[byteOffset + OFFSET_POS1] & 0xFF) << 24 );

		Float fValue = Float.intBitsToFloat(iValue);
		
		Object ret = fValue;
		
		if (targetClass == Double.class)
			ret = Double.parseDouble( fValue.toString() );

		return targetClass.cast(ret);
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
		return S7Type.REAL;
	}

}
