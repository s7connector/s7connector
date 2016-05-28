package com.github.s7connector.test.test;

import java.util.Calendar;

import org.junit.Test;

public class DateTest
{
	
	@Test
	public void test()
	{
		Calendar c = Calendar.getInstance();
		c.clear();
		c.add(Calendar.YEAR, 20);
		c.add(Calendar.DAY_OF_YEAR, 36865);
		
		System.out.println( c.getTime() );
		
	}
	
	@Test
	public void test2()
	{
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(Calendar.YEAR, 1990);
		System.out.println( c.getTime() );
			
	}

}
