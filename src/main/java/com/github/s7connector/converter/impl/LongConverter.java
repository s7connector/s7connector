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
