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
package ch.ri.s7.bean.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ch.ri.s7.impl.utils.S7Type;

/**
 * Defines an Offset in a DB
 * @author Thomas Rudin
 *
 */
@Target(value={ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface S7Variable
{
	/**
	 * The Byte Offset
	 * @return
	 */
	int byteOffset();
	
	/**
	 * The bit offset, if any
	 * @return
	 */
	int bitOffset() default 0;
	
	/**
	 * The specified size (for String)
	 * @return
	 */
	int size() default 0;
	
	/**
	 * The corresponding S7 Type
	 * @return
	 */
	S7Type type();
	
	/**
	 * The size of the array
	 * @return
	 */
	int arraySize() default 1;
	
}
