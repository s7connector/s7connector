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
package ch.ri.s7.test.test.example;

import ch.ri.s7.bean.annotation.S7Variable;
import ch.ri.s7.impl.utils.S7Type;

/**
 * @author Thomas Rudin (thomas@rudin-informatik.ch)
 *
 */
public class MyDataBean
{
	@S7Variable(byteOffset=0, bitOffset=0, type=S7Type.BOOL)
	public boolean bit1;
	
	@S7Variable(byteOffset=0, bitOffset=1, type=S7Type.BOOL)
	public boolean bit2;
	
	@S7Variable(byteOffset=1, type=S7Type.BYTE)
	public byte someByte;
	
	@S7Variable(byteOffset=2, type=S7Type.WORD)
	public int myNumber;

}
