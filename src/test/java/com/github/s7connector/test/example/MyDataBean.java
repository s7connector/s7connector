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
package com.github.s7connector.test.example;

import com.github.s7connector.api.annotation.S7Variable;
import com.github.s7connector.impl.utils.S7Type;

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
