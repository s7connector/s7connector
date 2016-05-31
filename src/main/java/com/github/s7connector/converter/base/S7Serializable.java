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
package com.github.s7connector.converter.base;

import com.github.s7connector.impl.utils.S7Type;


public interface S7Serializable
{

	/**
	 * Inserts a Java Object to the byte buffer
	 * @param javaType
	 * @param buffer
	 * @param byteOffset
	 * @param bitOffset
	 */
	public void insert(Object javaType, byte[] buffer, int byteOffset, int bitOffset, int size);

	/**
	 * Extracts a java type from a byte buffer
	 * @param buffer
	 * @param byteOffset
	 * @param bitOffset
	 * @return
	 */
	public <T> T extract(Class<T> targetClass, byte[] buffer, int byteOffset, int bitOffset);
	
	/**
	 * Returns the size of the s7 type bytes
	 * @return
	 */
	public int getSizeInBytes();
	
	/**
	 * Returns the size of the s7 type bytes
	 * @return
	 */
	public int getSizeInBits();
	
	/**
	 * Returns the S7-Type
	 * @return
	 */
	public S7Type getS7Type();
}
