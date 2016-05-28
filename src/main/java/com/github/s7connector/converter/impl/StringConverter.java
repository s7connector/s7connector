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
