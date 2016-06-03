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
