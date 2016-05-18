package io.rudin.s7connector.test.test.example;

import io.rudin.s7connector.bean.annotation.S7Variable;
import io.rudin.s7connector.impl.utils.S7Type;

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
