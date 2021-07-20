/*******************************************************************************
 * Copyright (c) 2017 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.util;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Util package test
 */

public class AllUtilTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		test.addTestSuite(IOUtilTest.class);

		// add all test classes here

		return test;
	}

}