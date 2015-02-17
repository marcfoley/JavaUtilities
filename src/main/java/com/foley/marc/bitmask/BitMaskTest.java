package com.foley.marc.bitmask;

import org.junit.Test;

public class BitMaskTest {

	
	@Test
	public void testShiftLeft(){
		int bitmask = 0x000E;
		int applicationCode = 1;
		int downStream = 1;
		int errorCode = 1000;
		int finalCode = bitmask << 20 | applicationCode << 12 | downStream << 8 | errorCode ;
		System.out.println(finalCode);
	}
}
