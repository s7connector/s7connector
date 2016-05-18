package io.rudin.s7connector.test.test.converter.base;

public abstract class ConverterBase
{
	
	protected static void dump(byte[] b)
	{
		for (int i=0; i<b.length; i++)
			System.out.print( Integer.toHexString(b[i] & 0xFF) + ",");
	}

	protected static void dump(Byte[] b)
	{
		for (int i=0; i<b.length; i++)
			System.out.print( Integer.toHexString(b[i] & 0xFF) + ",");
	}

}
