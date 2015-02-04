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
package ch.ri.s7.impl;

import java.io.Closeable;

import ch.ri.s7.impl.nodave.DaveArea;
import ch.ri.s7.impl.utils.S7Type;

public interface S7Connector extends Closeable
{
	/**
	 * Reads an area
	 * @param area
	 * @param areaNumber
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public byte[] read(DaveArea area, int areaNumber, int bytes, int offset);
	
	/**
	 * Reads a block
	 * @param blockNumber
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public byte[] readBlock(int blockNumber, int bytes, int offset);
	
	/**
	 * Reads an Object
	 * @param type
	 * @param area
	 * @param areaNumber
	 * @param offset
	 * @return
	 */
	public <T> T readObject(Class<T> javaType, S7Type s7type, DaveArea area, int areaNumber, int byteOffset, int bitOffset);

	/**
	 * Writes an area
	 * @param area
	 * @param areaNumber
	 * @param offset
	 * @param buffer
	 */
	public void write(DaveArea area, int areaNumber, int offset, byte[] buffer);
	
	/**
	 * Writes a block
	 * @param blockNumber
	 * @param offset
	 * @param buffer
	 */
	public void writeBlock(int blockNumber, int offset, byte[] buffer);
}
