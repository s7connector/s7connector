package com.github.s7connector.api;

import com.github.s7connector.exception.S7Exception;

public interface S7Serializer {

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
	<T> T dispense(Class<T> beanClass, int dbNum, int byteOffset) throws S7Exception;

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
	<T> T dispense(Class<T> beanClass, int dbNum, int byteOffset, int blockSize) throws S7Exception;

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
	void store(Object bean, int dbNum, int byteOffset);

}