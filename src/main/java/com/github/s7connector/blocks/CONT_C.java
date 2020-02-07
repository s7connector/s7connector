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
package com.github.s7connector.blocks;

import com.github.s7connector.api.annotation.S7Variable;
import com.github.s7connector.api.S7Type;

/**
 * PID Control block representation
 *
 * @author Thomas Rudin (thomas@rudin-informatik.ch)
 *
 */
public class CONT_C {

	@S7Variable(type = S7Type.BOOL, byteOffset = 0, bitOffset = 6)
	public boolean D_SEL;

	@S7Variable(type = S7Type.REAL, byteOffset = 20)
	public double GAIN;

	@S7Variable(type = S7Type.BOOL, byteOffset = 0, bitOffset = 3)
	public boolean I_SEL;

	@S7Variable(type = S7Type.REAL, byteOffset = 72)
	public double LMN;

	@S7Variable(type = S7Type.BOOL, byteOffset = 0, bitOffset = 0)
	public boolean MAN_ON;

	@S7Variable(type = S7Type.BOOL, byteOffset = 0, bitOffset = 2)
	public boolean P_SEL;

	@S7Variable(type = S7Type.REAL, byteOffset = 10)
	public double PV_IN;

	@S7Variable(type = S7Type.REAL, byteOffset = 6)
	public double SP_INT;

	@S7Variable(type = S7Type.TIME, byteOffset = 24)
	public long TN;

	@S7Variable(type = S7Type.TIME, byteOffset = 28)
	public long TV;

	@Override
	public String toString() {
		return "CONT_C [MAN_ON=" + this.MAN_ON + ", P_SEL=" + this.P_SEL + ", I_SEL=" + this.I_SEL + ", D_SEL="
				+ this.D_SEL + ", SP_INT=" + this.SP_INT + ", PV_IN=" + this.PV_IN + ", GAIN=" + this.GAIN + ", TN="
				+ this.TN + ", TV=" + this.TV + ", LMN=" + this.LMN + "]";
	}

}
