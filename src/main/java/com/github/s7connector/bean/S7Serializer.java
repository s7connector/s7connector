/*
Copyright 2016 S7connector members (github.com/s7connector)

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
package com.github.s7connector.bean;

import java.lang.reflect.Array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.s7connector.bean.parser.BeanEntry;
import com.github.s7connector.bean.parser.BeanParseResult;
import com.github.s7connector.bean.parser.BeanParser;
import com.github.s7connector.exception.S7Exception;
import com.github.s7connector.impl.S7Connector;
import com.github.s7connector.impl.nodave.DaveArea;

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

	public synchronized <T> void update(T obj, int dbNum, int byteOffset) throws S7Exception
	{
		//TODO
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
			T obj = beanClass.newInstance(); //TODO: not needed!
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
