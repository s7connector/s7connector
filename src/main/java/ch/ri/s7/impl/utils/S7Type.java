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
package ch.ri.s7.impl.utils;

import ch.ri.s7.converter.base.S7Serializable;
import ch.ri.s7.converter.impl.BitConverter;
import ch.ri.s7.converter.impl.ByteConverter;
import ch.ri.s7.converter.impl.DateAndTimeConverter;
import ch.ri.s7.converter.impl.DateConverter;
import ch.ri.s7.converter.impl.IntegerConverter;
import ch.ri.s7.converter.impl.LongConverter;
import ch.ri.s7.converter.impl.RealConverter;
import ch.ri.s7.converter.impl.StringConverter;
import ch.ri.s7.converter.impl.StructConverter;
import ch.ri.s7.converter.impl.TimeConverter;

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
