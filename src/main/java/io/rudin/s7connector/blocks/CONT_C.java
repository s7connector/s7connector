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
package io.rudin.s7connector.blocks;

import io.rudin.s7connector.bean.annotation.S7Variable;
import io.rudin.s7connector.impl.utils.S7Type;

/**
 * PID Control block representation
 * 
 * @author Thomas Rudin (thomas@rudin-informatik.ch)
 *
 */
public class CONT_C
{

	@S7Variable(type=S7Type.BOOL, byteOffset=0, bitOffset=0)
	public boolean MAN_ON;

	@S7Variable(type=S7Type.BOOL, byteOffset=0, bitOffset=2)
	public boolean P_SEL;

	@S7Variable(type=S7Type.BOOL, byteOffset=0, bitOffset=3)
	public boolean I_SEL;

	@S7Variable(type=S7Type.BOOL, byteOffset=0, bitOffset=6)
	public boolean D_SEL;
	
	@S7Variable(type=S7Type.REAL, byteOffset=6)
	public double SP_INT;

	@S7Variable(type=S7Type.REAL, byteOffset=10)
	public double PV_IN;

	@S7Variable(type=S7Type.REAL, byteOffset=20)
	public double GAIN;
	
	@S7Variable(type=S7Type.TIME, byteOffset=24)
	public long TN;

	@S7Variable(type=S7Type.TIME, byteOffset=28)
	public long TV;

	@S7Variable(type=S7Type.REAL, byteOffset=72)
	public double LMN;

	@Override
	public String toString()
	{
		return "CONT_C [MAN_ON=" + MAN_ON + ", P_SEL=" + P_SEL + ", I_SEL="
				+ I_SEL + ", D_SEL=" + D_SEL + ", SP_INT=" + SP_INT
				+ ", PV_IN=" + PV_IN + ", GAIN=" + GAIN + ", TN=" + TN
				+ ", TV=" + TV + ", LMN=" + LMN + "]";
	}


}
