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
package io.rudin.s7connector.converter.impl;

import io.rudin.s7connector.converter.base.S7Serializable;
import io.rudin.s7connector.impl.utils.S7Type;

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
