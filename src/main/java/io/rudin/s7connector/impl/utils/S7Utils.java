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
package io.rudin.s7connector.impl.utils;

/**
 * S7-Utils
 * 
 * @author Thomas Rudin
 * Libnodave: http://libnodave.sourceforge.net/
 *
 */
public class S7Utils
{
	/**
	 * Converts a byte to 8 bits
	 * @param buffer The Input-Byte
	 * @return The 8 bits
	 */
	public static boolean[] getBits(int buffer)
	{		
		if (buffer < 0)
			buffer += 256;
	
		String binString = Integer.toBinaryString(buffer);
		/*
		 * String-Pos: 	0 	1 	2 	3 	4 	5 	6 	7
		 * Bit:			128	64	32	16	8	4	2	1
		 */
		
		boolean[] ret = new boolean[8];
		for (int i=binString.length()-1; i>=0; i--)
		{
			//Check for the '1'-Char and mirror-set the result
			int mirrorPos = ( binString.length() - 1 ) - i;
			if (binString.charAt(i) == '1')
				ret[mirrorPos] = true;
		}
		return ret;
	}
}
