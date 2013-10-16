package com.foley.marc.integer;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class IntegerTest {

	private static final Logger m_log = Logger.getLogger(IntegerTest.class);

	@Test
	public void testIntegerMinValue() {
		int _min = Integer.MIN_VALUE;
		int _absMin = Math.abs(Integer.MIN_VALUE);
		m_log.info("min: " + _min);
		m_log.info("Absolute Value min: " + _absMin);
		// this doesnt make a lot of sense since the absolute value should have changed it to a positive number
		Assert.assertEquals(_min, _absMin);
		int _absMinPlusOne = _absMin + 1;
		m_log.info("Absolute Value min plus one: " + _absMinPlusOne);
		m_log.info("Absolute Value min plus one (abs again): " + Math.abs(_absMinPlusOne));
	}
}
