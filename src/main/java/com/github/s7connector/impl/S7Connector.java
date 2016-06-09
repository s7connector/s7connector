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
package com.github.s7connector.impl;

import java.io.Closeable;

import com.github.s7connector.impl.nodave.DaveArea;
import com.github.s7connector.impl.utils.S7Type;

public interface S7Connector extends Closeable {
	/**
	 * Reads an area
	 * 
	 * @param area
	 * @param areaNumber
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public byte[] read(DaveArea area, int areaNumber, int bytes, int offset);

	/**
	 * Reads a block
	 * 
	 * @param blockNumber
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public byte[] readBlock(int blockNumber, int bytes, int offset);

	/**
	 * Reads an Object
	 * 
	 * @param type
	 * @param area
	 * @param areaNumber
	 * @param offset
	 * @return
	 */
	public <T> T readObject(Class<T> javaType, S7Type s7type, DaveArea area, int areaNumber, int byteOffset,
			int bitOffset);

	/**
	 * Writes an area
	 * 
	 * @param area
	 * @param areaNumber
	 * @param offset
	 * @param buffer
	 */
	public void write(DaveArea area, int areaNumber, int offset, byte[] buffer);

	/**
	 * Writes a block
	 * 
	 * @param blockNumber
	 * @param offset
	 * @param buffer
	 */
	public void writeBlock(int blockNumber, int offset, byte[] buffer);
}
