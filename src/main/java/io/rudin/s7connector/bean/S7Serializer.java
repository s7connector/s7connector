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
package io.rudin.s7connector.bean;

import io.rudin.s7connector.bean.parser.BeanEntry;
import io.rudin.s7connector.bean.parser.BeanParseResult;
import io.rudin.s7connector.bean.parser.BeanParser;
import io.rudin.s7connector.exception.S7Exception;
import io.rudin.s7connector.impl.S7Connector;
import io.rudin.s7connector.impl.nodave.DaveArea;

import java.lang.reflect.Array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class S7Serializer
{
	
	/**
	 * Local Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(S7Serializer.class);

	public S7Serializer(S7Connector connector)
	{
		this.connector = connector;
	}

	/**
	 * The Connector
	 */
	private S7Connector connector;

	/**
	 * Extracts bytes from a buffer
	 * @param beanClass
	 * @param buffer
	 * @param byteOffset
	 * @return
	 */
	public static <T> T extractBytes(Class<T> beanClass, byte[] buffer, int byteOffset)
	{
		try
		{
			T obj = beanClass.newInstance();

			BeanParseResult result = BeanParser.parse(beanClass);

			for (BeanEntry entry: result.entries)
			{
				Object value = null;

				if (entry.isArray)
				{
					value = Array.newInstance(entry.type, entry.arraySize);
					for (int i=0; i<entry.arraySize; i++)
					{
						Object component = entry.serializer.extract(
								entry.type,
								buffer,
								entry.byteOffset + byteOffset + ( i * entry.s7type.getByteSize() ),
								entry.bitOffset + ( i * entry.s7type.getBitSize() )
								);
						Array.set(value, i, component);
					}
				}
				else
				{
					value = entry.serializer.extract(
							entry.type,
							buffer,
							entry.byteOffset + byteOffset,
							entry.bitOffset
							);
				}

				entry.field.set(obj, value);
			}

			return obj;
		}
		catch (Exception e)
		{
			throw new S7Exception("extractBytes", e);
		}
	}

	/**
	 * Dispenses an Object from the mapping of the Datablock
	 * @param beanClass
	 * @param dbNum
	 * @param byteOffset
	 * @return
	 * @throws S7Exception
	 */
	public synchronized <T> T dispense(Class<T> beanClass, int dbNum, int byteOffset) throws S7Exception
	{
		try
		{
			T obj = beanClass.newInstance();
			BeanParseResult result = BeanParser.parse(obj);
			byte[] buffer = connector.read(DaveArea.DB, dbNum, result.blockSize, byteOffset);
			return extractBytes(beanClass, buffer, 0);
		}
		catch (Exception e)
		{
			throw new S7Exception("dispense", e);
		}
	}

	public synchronized <T> T dispense(Class<T> beanClass, int dbNum, int byteOffset, int blockSize) throws S7Exception
	{
		try
		{
			byte[] buffer = connector.read(DaveArea.DB, dbNum, blockSize, byteOffset);
			return extractBytes(beanClass, buffer, 0);
		}
		catch (Exception e)
		{
			throw new S7Exception("dispense dbnum("+dbNum+") byteoffset("+byteOffset+") blocksize("+blockSize+")", e);
		}
	}

	/**
	 * Inserts the bytes to the buffer
	 * @param bean
	 * @param buffer
	 * @param byteOffset
	 */
	public static void insertBytes(Object bean, byte[] buffer, int byteOffset)
	{
		try
		{
			BeanParseResult result = BeanParser.parse(bean);

			for (BeanEntry entry: result.entries)
			{
				Object fieldValue = entry.field.get(bean);

				if (fieldValue != null)
				{
					if (entry.isArray)
					{
						for (int i=0; i<entry.arraySize; i++)
						{
							Object arrayItem = Array.get(fieldValue, i);

							if (arrayItem != null)
							{
								entry.serializer.insert(
										arrayItem,
										buffer,
										entry.byteOffset + byteOffset + ( i * entry.s7type.getByteSize() ),
										entry.bitOffset + ( i * entry.s7type.getBitSize() ),
										entry.size
										);
							}
						}
					}
					else
					{
						entry.serializer.insert(
								fieldValue,
								buffer,
								entry.byteOffset + byteOffset,
								entry.bitOffset,
								entry.size
								);
					}
				}
			}
		}
		catch (Exception e)
		{
			throw new S7Exception("insertBytes", e);
		}
	}

	/**
	 * Stores an Object to the Datablock
	 * @param bean
	 * @param dbNum
	 * @param byteOffset
	 */
	public synchronized void store(Object bean, int dbNum, int byteOffset)
	{
		try
		{
			BeanParseResult result = BeanParser.parse(bean);

			byte[] buffer = new byte[result.blockSize];
			logger.trace("store-buffer-size: " + buffer.length);

			insertBytes(bean, buffer, 0);

			connector.write(DaveArea.DB, dbNum, byteOffset, buffer);
		}
		catch (Exception e)
		{
			throw new S7Exception("store", e);
		}
	}

}
