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
package com.github.s7connector.converter.impl;

import com.github.s7connector.converter.base.S7Serializable;
import com.github.s7connector.impl.utils.S7Type;

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
