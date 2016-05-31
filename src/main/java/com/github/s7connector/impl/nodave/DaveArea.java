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
package com.github.s7connector.impl.nodave;

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
