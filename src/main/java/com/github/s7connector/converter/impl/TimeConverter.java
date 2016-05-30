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

import com.github.s7connector.impl.utils.S7Type;

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
