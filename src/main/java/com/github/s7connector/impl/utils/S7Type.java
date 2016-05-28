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
package com.github.s7connector.impl.utils;

import com.github.s7connector.converter.base.S7Serializable;
import com.github.s7connector.converter.impl.BitConverter;
import com.github.s7connector.converter.impl.ByteConverter;
import com.github.s7connector.converter.impl.DateAndTimeConverter;
import com.github.s7connector.converter.impl.DateConverter;
import com.github.s7connector.converter.impl.IntegerConverter;
import com.github.s7connector.converter.impl.LongConverter;
import com.github.s7connector.converter.impl.RealConverter;
import com.github.s7connector.converter.impl.StringConverter;
import com.github.s7connector.converter.impl.StructConverter;
import com.github.s7connector.converter.impl.TimeConverter;

/**
 * Type of the Address
 * @author Thomas Rudin
 * Libnodave: http://libnodave.sourceforge.net/
 */
public enum S7Type
{
	/**
	 * Boolean type
	 */
	BOOL			(BitConverter.class, 0, 1),
	
	/**
	 * Byte type
	 */
	BYTE			(ByteConverter.class, 1, 0),
	
	/**
	 * A Word-type (same as int-type)
	 */
	WORD			(IntegerConverter.class, 2, 0),
	
	/**
	 * Simple Date with 2 bytes in length
	 */
	DATE			(DateConverter.class, 2, 0),

	/**
	 * Double word
	 */
	DWORD			(LongConverter.class, 4, 0),
	
	/**
	 * Real-type, corresponds to float or double
	 */
	REAL			(RealConverter.class, 4, 0),
	
	/**
	 * Time-type, 4 bytes in length, number of millis
	 */
	TIME			(TimeConverter.class, 4, 0),
	
	/**
	 * Full Date and time format with precision in milliseconds
	 */
	DATE_AND_TIME	(DateAndTimeConverter.class, 8, 0),
	
	/**
	 * String type, size must be specified manually
	 */
	STRING			(StringConverter.class, 2, 0),
	
	/**
	 * Structure type
	 */
	STRUCT			(StructConverter.class, 0, 0);
	
	/**
	 * Enum Constructor
	 * @param serializer
	 * @param byteSize
	 * @param bitSize
	 */
	S7Type(Class<? extends S7Serializable> serializer, int byteSize, int bitSize)
	{
		this.serializer = serializer;
		this.bitSize = bitSize;
		this.byteSize = byteSize;
	}
	
	private int byteSize, bitSize;
	
	private Class<? extends S7Serializable> serializer;

	public int getByteSize()
	{
		return byteSize;
	}

	public int getBitSize()
	{
		return bitSize;
	}

	public Class<? extends S7Serializable> getSerializer()
	{
		return serializer;
	}
}
