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
package ch.ri.s7.impl.nodave;

public enum DaveArea
{
	SYSINFO(3), // System info of 200 family
	SYSTEMFLAGS(5), // System flags of 200 family
	ANALOGINPUTS200(6), // analog inputs of 200 family
	ANALOGOUTPUTS200(7), // analog outputs of 200 family
	P(0x80), // Peripheral I/O
	INPUTS(0x81), OUTPUTS(0x82), FLAGS(0x83), DB(0x84), // data blocks
	DI(0x85), // instance data blocks
	LOCAL(0x86), // not tested
	V(0x87), // local of caller
	COUNTER(28), // S7 counters
	TIMER(29), // S7 timers
	COUNTER200(30), // IEC counters (200 family)
	TIMER200(31); // IEC timers (200 family)

	DaveArea(int code)
	{
		this.code = code;
	}

	int code;

	public int getCode()
	{
		return code;
	}
}
