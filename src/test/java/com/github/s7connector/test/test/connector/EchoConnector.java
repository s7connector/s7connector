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
package com.github.s7connector.test.test.connector;

import java.io.IOException;

import com.github.s7connector.impl.S7Connector;
import com.github.s7connector.impl.nodave.DaveArea;
import com.github.s7connector.impl.utils.S7Type;

/**
 * Echo connector for testing
 * 
 * returns the same buffer in read() as given in write() regardless of the byte-range or area
 * 
 * @author Thomas Rudin
 *
 */
public class EchoConnector implements S7Connector {

	@Override
	public byte[] read(DaveArea area, int areaNumber, int bytes, int offset) {
		return buffer;
	}

	public byte[] buffer;

	@Override
	public byte[] readBlock(int blockNumber, int bytes, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T readObject(Class<T> javaType, S7Type s7type, DaveArea area, int areaNumber, int byteOffset,
			int bitOffset) {
		
		throw new IllegalArgumentException("not implemented");
	}

	@Override
	public void write(DaveArea area, int areaNumber, int offset, byte[] buffer) {
		this.buffer = buffer;

		System.out.println("Size: " + buffer.length);

		for (int i = 0; i < buffer.length; i++)
			System.out.print(Integer.toHexString(buffer[i] & 0xFF) + ",");

	}

	@Override
	public void writeBlock(int blockNumber, int offset, byte[] buffer) {
		this.buffer = buffer;

		System.out.println("Size: " + buffer.length);

		for (int i = 0; i < buffer.length; i++)
			System.out.print(Integer.toHexString(buffer[i] & 0xFF) + ",");
	}

	@Override
	public void close() throws IOException {}

}
