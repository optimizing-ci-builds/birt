/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Actuate Corporation
 * 
 */
public class LineCanvas extends Canvas implements PaintListener, FocusListener
{

	int iLineStyle = SWT.LINE_SOLID;

	int iLineWidth = 1;

	boolean isFocusIn = false;

	public LineCanvas( Composite parent, int iStyle, int iLineStyle,
			int iLineWidth )
	{
		super( parent, iStyle );
		this.iLineStyle = iLineStyle;
		this.iLineWidth = iLineWidth;
		this.addPaintListener( this );
		this.addFocusListener( this );
	}

	public int getLineStyle( )
	{
		return this.iLineStyle;
	}

	public void setLineStyle( int iLineStyle )
	{
		this.iLineStyle = iLineStyle;
	}

	public int getLineWidth( )
	{
		return this.iLineWidth;
	}

	public void setLineWidth( int iLineWidth )
	{
		this.iLineWidth = iLineWidth;
	}

	public void paintControl( PaintEvent pe )
	{
		if ( isEnabled( ) && isFocusControl( ) )
		{
			isFocusIn = true;
		}

		Color cForeground = null;
		Color cBackground = null;
		if ( this.isEnabled( ) )
		{
			cForeground = getDisplay( ).getSystemColor( SWT.COLOR_LIST_FOREGROUND );
			cBackground = getDisplay( ).getSystemColor( SWT.COLOR_LIST_BACKGROUND );
		}
		else
		{
			cForeground = getDisplay( ).getSystemColor( SWT.COLOR_DARK_GRAY );
			cBackground = getDisplay( ).getSystemColor( SWT.COLOR_WIDGET_BACKGROUND );
		}

		GC gc = pe.gc;
		if ( isFocusIn )
		{
			gc.setBackground( getDisplay( ).getSystemColor( SWT.COLOR_LIST_SELECTION ) );
			gc.setForeground( getDisplay( ).getSystemColor( SWT.COLOR_LIST_SELECTION_TEXT ) );
		}
		else
		{
			gc.setBackground( cBackground );
			gc.setForeground( cForeground );
		}

		gc.fillRectangle( 0, 0, this.getSize( ).x, this.getSize( ).y );
		int lineStyle = iLineStyle;
		int lineWidth = iLineWidth;
		boolean isAuto = iLineStyle == 0 || iLineWidth == 0;
		if ( iLineStyle == 0 )
		{
			lineStyle = SWT.LINE_SOLID;
		}
		if ( iLineWidth == 0 )
		{
			lineWidth = 1;
		}
		if ( isAuto )
		{
			gc.drawText( ChartUIExtensionUtil.getAutoMessage( ), 10, 2 );
		}
		else
		{
			gc.setLineStyle( lineStyle );
			gc.setLineWidth( lineWidth );
			gc.drawLine( 10,
					this.getSize( ).y / 2,
					this.getSize( ).x - 10,
					this.getSize( ).y / 2 );
		}
	}

	public void setEnabled( boolean bState )
	{
		super.setEnabled( bState );
		redraw( );
	}

	public void focusGained( FocusEvent e )
	{
		isFocusIn = true;

	}

	public void focusLost( FocusEvent e )
	{
		isFocusIn = false;
	}

	public Point computeSize( int wHint, int hHint, boolean changed )
	{
		Point size = new Point( 100, 20 );
		if ( wHint != SWT.DEFAULT )
			size.x = wHint;
		if ( hHint != SWT.DEFAULT )
			size.y = hHint;
		Rectangle trim = computeTrim( 0, 0, size.x, size.y );
		return new Point( trim.width, trim.height );
	}
}