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

import com.github.s7connector.api.DaveArea;
import com.github.s7connector.api.S7Connector;
import com.github.s7connector.bean.parser.BeanEntry;
import com.github.s7connector.bean.parser.BeanParseResult;
import com.github.s7connector.bean.parser.BeanParser;
import com.github.s7connector.exception.S7Exception;

/**
 * The Class S7Serializer is responsible for serializing S7 TCP Connection
 */
public final class S7Serializer {

	/** Local Logger. */
	private static final Logger logger = LoggerFactory.getLogger(S7Serializer.class);

	/**
	 * Extracts bytes from a buffer.
	 *
	 * @param <T>
	 *            the generic type
	 * @param beanClass
	 *            the bean class
	 * @param buffer
	 *            the buffer
	 * @param byteOffset
	 *            the byte offset
	 * @return the t
	 */
	public static <T> T extractBytes(final Class<T> beanClass, final byte[] buffer, final int byteOffset) {
		try {
			final T obj = beanClass.newInstance();

			final BeanParseResult result = BeanParser.parse(beanClass);

			for (final BeanEntry entry : result.entries) {
				Object value = null;

				if (entry.isArray) {
					value = Array.newInstance(entry.type, entry.arraySize);
					for (int i = 0; i < entry.arraySize; i++) {
						final Object component = entry.serializer.extract(entry.type, buffer,
								entry.byteOffset + byteOffset + (i * entry.s7type.getByteSize()),
								entry.bitOffset + (i * entry.s7type.getBitSize()));
						Array.set(value, i, component);
					}
				} else {
					value = entry.serializer.extract(entry.type, buffer, entry.byteOffset + byteOffset,
							entry.bitOffset);
				}

				entry.field.set(obj, value);
			}

			return obj;
		} catch (final Exception e) {
			throw new S7Exception("extractBytes", e);
		}
	}

	/**
	 * Inserts the bytes to the buffer.
	 *
	 * @param bean
	 *            the bean
	 * @param buffer
	 *            the buffer
	 * @param byteOffset
	 *            the byte offset
	 */
	public static void insertBytes(final Object bean, final byte[] buffer, final int byteOffset) {
		try {
			final BeanParseResult result = BeanParser.parse(bean);

			for (final BeanEntry entry : result.entries) {
				final Object fieldValue = entry.field.get(bean);

				if (fieldValue != null) {
					if (entry.isArray) {
						for (int i = 0; i < entry.arraySize; i++) {
							final Object arrayItem = Array.get(fieldValue, i);

							if (arrayItem != null) {
								entry.serializer.insert(arrayItem, buffer,
										entry.byteOffset + byteOffset + (i * entry.s7type.getByteSize()),
										entry.bitOffset + (i * entry.s7type.getBitSize()), entry.size);
							}
						}
					} else {
						entry.serializer.insert(fieldValue, buffer, entry.byteOffset + byteOffset, entry.bitOffset,
								entry.size);
					}
				}
			}
		} catch (final Exception e) {
			throw new S7Exception("insertBytes", e);
		}
	}

	/** The Connector. */
	private final S7Connector connector;

	/**
	 * Instantiates a new s7 serializer.
	 *
	 * @param connector
	 *            the connector
	 */
	public S7Serializer(final S7Connector connector) {
		this.connector = connector;
	}

	/**
	 * Dispenses an Object from the mapping of the Datablock.
	 *
	 * @param <T>
	 *            the generic type
	 * @param beanClass
	 *            the bean class
	 * @param dbNum
	 *            the db num
	 * @param byteOffset
	 *            the byte offset
	 * @return the t
	 * @throws S7Exception
	 *             the s7 exception
	 */
	public synchronized <T> T dispense(final Class<T> beanClass, final int dbNum, final int byteOffset)
			throws S7Exception {
		try {
			final T obj = beanClass.newInstance(); // TODO: not needed!
			final BeanParseResult result = BeanParser.parse(obj);
			final byte[] buffer = this.connector.read(DaveArea.DB, dbNum, result.blockSize, byteOffset);
			return extractBytes(beanClass, buffer, 0);
		} catch (final Exception e) {
			throw new S7Exception("dispense", e);
		}
	}

	/**
	 * Dispense.
	 *
	 * @param <T>
	 *            the generic type
	 * @param beanClass
	 *            the bean class
	 * @param dbNum
	 *            the db num
	 * @param byteOffset
	 *            the byte offset
	 * @param blockSize
	 *            the block size
	 * @return the t
	 * @throws S7Exception
	 *             the s7 exception
	 */
	public synchronized <T> T dispense(final Class<T> beanClass, final int dbNum, final int byteOffset,
			final int blockSize) throws S7Exception {
		try {
			final byte[] buffer = this.connector.read(DaveArea.DB, dbNum, blockSize, byteOffset);
			return extractBytes(beanClass, buffer, 0);
		} catch (final Exception e) {
			throw new S7Exception(
					"dispense dbnum(" + dbNum + ") byteoffset(" + byteOffset + ") blocksize(" + blockSize + ")", e);
		}
	}

	/**
	 * Stores an Object to the Datablock.
	 *
	 * @param bean
	 *            the bean
	 * @param dbNum
	 *            the db num
	 * @param byteOffset
	 *            the byte offset
	 */
	public synchronized void store(final Object bean, final int dbNum, final int byteOffset) {
		try {
			final BeanParseResult result = BeanParser.parse(bean);

			final byte[] buffer = new byte[result.blockSize];
			logger.trace("store-buffer-size: " + buffer.length);

			insertBytes(bean, buffer, 0);

			this.connector.write(DaveArea.DB, dbNum, byteOffset, buffer);
		} catch (final Exception e) {
			throw new S7Exception("store", e);
		}
	}

	/**
	 * Update.
	 *
	 * @param <T>
	 *            the generic type
	 * @param obj
	 *            the obj
	 * @param dbNum
	 *            the db num
	 * @param byteOffset
	 *            the byte offset
	 * @throws S7Exception
	 *             the s7 exception
	 */
	public synchronized <T> void update(final T obj, final int dbNum, final int byteOffset) throws S7Exception {
		// TODO
	}

}
