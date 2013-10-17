package com.foley.marc.integer;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class IntegerTest {

	private static final Logger m_log = Logger.getLogger(IntegerTest.class);

	/**
	 * apparently the absolute value of Integer.MIN_VALUE still returns a negative number. But if you put that value
	 * into a long then it works ok. The reason for this seems to be that the max value for an int is one less than the
	 * absolute value of the min value
	 */
	@Test
	public void testIntegerMinValue() {
		m_log.info("Integer MIN VALUE: " + Integer.MIN_VALUE);
		m_log.info("Integer Max VALUE: " + Integer.MAX_VALUE);
		int _min = Integer.MIN_VALUE;
		int _absMin = Math.abs(Integer.MIN_VALUE);
		m_log.info("min: " + _min);
		m_log.info("Absolute Value min: " + _absMin);
		// this doesnt make a lot of sense since the absolute value should have changed it to a positive number
		Assert.assertEquals(_min, _absMin);
		int _absMinPlusOne = _absMin + 1;
		m_log.info("Absolute Value min plus one: " + _absMinPlusOne);
		m_log.info("Absolute Value min plus one (abs again): " + Math.abs(_absMinPlusOne));

		_absMinPlusOne--;
		m_log.info("Absolute Value min minus one: " + _absMinPlusOne);
		m_log.info("Absolute Value min Minus one (abs again): " + Math.abs(_absMinPlusOne));

		// now what happens if you cast the int to a long
		long _minLong = Integer.MIN_VALUE;
		m_log.info("min value casted to long: " + _minLong);
		long _absLong = Integer.MIN_VALUE;
		_absLong = Math.abs(_absLong);
		m_log.info("abs of min value casted to long: " + _absLong);
		Assert.assertNotEquals(_absLong, _minLong);
	}
}
