/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device;

/**
 * This interface defines the capability for implementors to provide a image map
 * string.
 */
public interface IImageMapEmitter
{

	/**
	 * Returns the image map string using given id. The evaluator is used to
	 * parse the URL expression. If no evaluator given, treat the URL expression
	 * as a plain string.
	 * 
	 * @param id
	 * @param evaluator
	 * @return
	 */
	String getImageMap( );

	/**
	 * Returns the MIME type of the output image.
	 * 
	 * @return
	 */
	String getMimeType( );
}
