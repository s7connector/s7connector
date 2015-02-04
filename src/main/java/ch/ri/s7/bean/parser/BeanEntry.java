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
package ch.ri.s7.bean.parser;

import java.lang.reflect.Field;

import ch.ri.s7.converter.base.S7Serializable;
import ch.ri.s7.impl.utils.S7Type;

/**
 * A Bean-Entry
 * @author Thomas Rudin
 *
 */
public class BeanEntry
{
	/**
	 * The corresponding field
	 */
	public Field field;
	
	/**
	 * The Java type
	 */
	public Class<?> type;
	
	/**
	 * Offsets and size
	 */
	public int byteOffset, bitOffset, size;
	
	/**
	 * The S7 Type
	 */
	public S7Type s7type;
	
	/**
	 * The corresponding serializer
	 */
	public S7Serializable serializer;
	
	/**
	 * Array type
	 */
	public boolean isArray;
	
	/**
	 * The Array size
	 */
	public int arraySize;
}
