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

public class BitConverter implements S7Serializable
{

	@Override
	public void insert(Object javaType, byte[] buffer, int byteOffset, int bitOffset, int size)
	{
		Boolean value = (Boolean)javaType;
		
		if (value)
		{
			byte bufValue = buffer[byteOffset];
			bufValue |= ( 0x01 << bitOffset );
			buffer[byteOffset] = bufValue;
		}
	}

	@Override
	public <T> T extract(Class<T> targetClass, byte[] buffer, int byteOffset, int bitOffset)
	{
		byte bufValue = buffer[byteOffset];
		return targetClass.cast( bufValue == ( bufValue | ( 0x01 << bitOffset ) ) );
	}

	@Override
	public int getSizeInBytes()
	{
		return 0;
	}

	@Override
	public int getSizeInBits()
	{
		return 1;
	}

	@Override
	public S7Type getS7Type()
	{
		return S7Type.BOOL;
	}


}
