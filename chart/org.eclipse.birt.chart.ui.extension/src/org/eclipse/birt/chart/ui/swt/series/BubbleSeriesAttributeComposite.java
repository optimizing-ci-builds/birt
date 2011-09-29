/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.series;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class BubbleSeriesAttributeComposite extends Composite implements
		SelectionListener,
		Listener
{
	private transient FillChooserComposite fccShadow = null;

	private transient Group grpLine = null;

	private transient LineAttributesComposite liacLine = null;

	private transient Group grpAccLine = null;

	private transient LineAttributesComposite liacAccLine = null;
	
	private transient Label lblOrientation = null;

	private transient Combo cmbOrientation;

	private transient Series series = null;
	
	private transient Label lblShadow = null;

	ChartWizardContext context;

	private Combo cmbPalette;

	private Combo cmbCurve;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.ui.extension/swt.series" ); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public BubbleSeriesAttributeComposite( Composite parent, int style,
			ChartWizardContext context, Series series )
	{
		super( parent, style );
		if ( !( series instanceof BubbleSeriesImpl ) )
		{
			try
			{
				throw new ChartException( ChartUIExtensionPlugin.ID,
						ChartException.VALIDATION,
						"BubbleSeriesAttributeComposite.Exception.IllegalArgument", new Object[]{series.getClass( ).getName( )}, Messages.getResourceBundle( ) ); //$NON-NLS-1$
			}
			catch ( ChartException e )
			{
				logger.log( e );
				e.printStackTrace( );
			}
		}
		this.series = series;
		this.context = context;
		init( );
		placeComponents( );
		ChartUIUtil.bindHelp( parent,
				ChartHelpContextIds.SUBTASK_YSERIES_BUBBLE );
	}

	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
	}

	private void placeComponents( )
	{
		// Layout for content composite
		GridLayout glContent = new GridLayout( 3, true );
		glContent.marginHeight = 2;
		glContent.marginWidth = 2;

		// Main content composite
		this.setLayout( glContent );

		grpAccLine = new Group( this, SWT.NONE );
		GridData gdGRPAccLine = new GridData( GridData.FILL_HORIZONTAL );
		GridLayout glGRPAccline = new GridLayout( 2, false );
		glGRPAccline.verticalSpacing = 0;
		grpAccLine.setLayout( glGRPAccline );
		grpAccLine.setLayoutData( gdGRPAccLine );
		grpAccLine.setText( Messages.getString( "BubbleSeriesAttributeComposite.Lbl.AccLine" ) ); //$NON-NLS-1$

		liacAccLine = new LineAttributesComposite( grpAccLine,
				SWT.NONE,
				context,
				( (BubbleSeries) series ).getAccLineAttributes( ),
				true,
				true,
				true,
				true,
				true );
		GridData gdLIACAccLine = new GridData( GridData.FILL_BOTH );
		gdLIACAccLine.horizontalSpan = 2;
		liacAccLine.setLayoutData( gdLIACAccLine );
		liacAccLine.addListener( this );
		
		Composite cmpOrientation = new Composite( grpAccLine, SWT.NONE );
		{
			GridLayout gl = new GridLayout( 2, false );
			gl.marginHeight = 0;
			gl.marginBottom = 0;
			gl.verticalSpacing = 0;
			cmpOrientation.setLayout( gl );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalSpan = 2;
			cmpOrientation.setLayoutData( gd );
		}
		
		lblOrientation = new Label( cmpOrientation, SWT.NONE );
		lblOrientation.setText( Messages.getString( "BubbleSeriesAttributeComposite.Lbl.Orientation" ) ); //$NON-NLS-1$

		cmbOrientation = new Combo( cmpOrientation, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBOrientation = new GridData( GridData.FILL_HORIZONTAL );
		cmbOrientation.setLayoutData( gdCMBOrientation );
		cmbOrientation.addSelectionListener( this );

		grpLine = new Group( this, SWT.NONE );
		GridData gdGRPLine = new GridData( GridData.FILL_HORIZONTAL );
		gdGRPLine.horizontalSpan = 2;
		grpLine.setLayoutData( gdGRPLine );
		GridLayout glLine = new GridLayout( 2, false );
		glLine.horizontalSpacing = 0;
		grpLine.setLayout( glLine );		
		grpLine.setText( Messages.getString( "BubbleSeriesAttributeComposite.Lbl.Line" ) ); //$NON-NLS-1$

		Composite cmpLine = new Composite( grpLine, SWT.NONE );
		{
			GridLayout gl = new GridLayout( 2, false );
			gl.marginHeight = 0;
			gl.marginWidth = 0;
			gl.horizontalSpacing = 0;
			gl.verticalSpacing = 0;
			cmpLine.setLayout( gl );
			cmpLine.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		}
		
		liacLine = new LineAttributesComposite( cmpLine,
				SWT.NONE,
				context,
				( (BubbleSeries) series ).getLineAttributes( ),
				true,
				true,
				true,
				true,
				true );
		GridData gdLIACLine = new GridData( GridData.FILL_HORIZONTAL );
		gdLIACLine.horizontalSpan = 2;
		liacLine.setLayoutData( gdLIACLine );
		liacLine.addListener( this );
		
		Composite cmpShadow = new Composite( cmpLine, SWT.NONE );
		{
			GridLayout gl = new GridLayout( 2, false );
			gl.marginHeight = 0;
			gl.marginBottom = 0;
			gl.verticalSpacing = 0;
			cmpShadow.setLayout( gl );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalSpan = 2;
			cmpShadow.setLayoutData( gd );
		}
		
		lblShadow = new Label( cmpShadow, SWT.NONE );
		lblShadow.setText( Messages.getString( "BubbleSeriesAttributeComposite.Lbl.ShadowColor" ) ); //$NON-NLS-1$

		int iFillOption = FillChooserComposite.DISABLE_PATTERN_FILL
				| FillChooserComposite.ENABLE_TRANSPARENT
				| FillChooserComposite.ENABLE_TRANSPARENT_SLIDER
				| FillChooserComposite.ENABLE_AUTO;

		fccShadow = new FillChooserComposite( cmpShadow,
				SWT.DROP_DOWN | SWT.READ_ONLY,
				iFillOption,
				context,
				( (BubbleSeries) series ).getShadowColor( ) );

		GridData gdFCCShadow = new GridData( GridData.FILL_HORIZONTAL );
		fccShadow.setLayoutData( gdFCCShadow );
		fccShadow.addListener( this );

		Composite cmp = new Composite( grpLine, SWT.NONE );
		cmp.setLayout( new GridLayout( 2, false ) );

		Label lbl = new Label( cmp, SWT.NONE );
		lbl.setText( Messages.getString( "BubbleSeriesAttributeComposite.Lbl.LinePalette" ) ); //$NON-NLS-1$

		cmbPalette = ChartUIExtensionUtil.createTrueFalseItemsCombo( cmp );
		{
			cmbPalette.select( ( (BubbleSeries) series ).isSetPaletteLineColor( ) ? ( ( (BubbleSeries) series ).isPaletteLineColor( ) ? 1
					: 2 )
					: 0 );
			cmbPalette.addSelectionListener( this );
		}

		lbl = new Label( cmp, SWT.NONE );
		lbl.setText( Messages.getString( "BubbleSeriesAttributeComposite.Lbl.ShowLinesAsCurves" ) ); //$NON-NLS-1$
		cmbCurve = ChartUIExtensionUtil.createTrueFalseItemsCombo( cmp );
		{
			cmbCurve.select( ( (BubbleSeries) series ).isSetCurve( ) ? ( ( (BubbleSeries) series ).isCurve( ) ? 1
					: 2 )
					: 0 );
			cmbCurve.addSelectionListener( this );
		}

		enableLineSettings( ( (BubbleSeries) series ).getLineAttributes( )
				.isVisible( ) );
		
		enableAccLineSettings( ( (BubbleSeries) series ).getAccLineAttributes( )
				.isVisible( ) );

		populateLists( );
	}

	private void populateLists( )
	{
		NameSet ns = LiteralHelper.orientationSet;
		cmbOrientation.setItems( ChartUIExtensionUtil.getItemsWithAuto( ns.getDisplayNames( ) ) );
		cmbOrientation.select( ( (BubbleSeries) series ).isSetAccOrientation( ) ? ( ns.getSafeNameIndex( ( (BubbleSeries) series ).getAccOrientation( )
				.getName( ) ) + 1 )
				: 0 );
	}

	public Point getPreferredSize( )
	{
		return new Point( 400, 200 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( cmbCurve ) )
		{
			ChartElementUtil.setEObjectAttribute( ( (BubbleSeries) series ),
					"curve", //$NON-NLS-1$
					cmbCurve.getSelectionIndex( ) == 1,
					cmbCurve.getSelectionIndex( ) == 0 );
		}
		else if ( e.getSource( ).equals( cmbPalette ) )
		{
			ChartElementUtil.setEObjectAttribute( ( (BubbleSeries) series ),
					"paletteLineColor", //$NON-NLS-1$
					cmbPalette.getSelectionIndex( ) == 1,
					cmbPalette.getSelectionIndex( ) == 0 );
		}
		else if ( e.getSource( ).equals( cmbOrientation ) )
		{
			ChartElementUtil.setEObjectAttribute( ( (BubbleSeries) series ),
					"accOrientation", //$NON-NLS-1$
					Orientation.getByName( LiteralHelper.orientationSet.getNameByDisplayName( cmbOrientation.getText( ) ) ),
					cmbOrientation.getSelectionIndex( ) == 0 );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		boolean isUnset = ( event.detail == ChartUIExtensionUtil.PROPERTY_UNSET );
		if ( event.widget.equals( liacLine ) )
		{
			if ( event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( ( (BubbleSeries) series ).getLineAttributes( ),
						"visible",//$NON-NLS-1$
						( (Boolean) event.data ).booleanValue( ),
						isUnset );
				enableLineSettings( ( (BubbleSeries) series ).getLineAttributes( )
						.isSetVisible( )
						&& ( (BubbleSeries) series ).getLineAttributes( )
								.isVisible( ) );
			}
			else if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( ( (BubbleSeries) series ).getLineAttributes( ),
						"style",//$NON-NLS-1$
						(LineStyle) event.data,
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( ( (BubbleSeries) series ).getLineAttributes( ),
						"thickness",//$NON-NLS-1$
						( (Integer) event.data ).intValue( ),
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				( (BubbleSeries) series ).getLineAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
		}
		else if ( event.widget.equals( liacAccLine ) )
		{
			if ( event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( ( (BubbleSeries) series ).getAccLineAttributes( ),
						"visible",//$NON-NLS-1$
						( (Boolean) event.data ).booleanValue( ),
						isUnset );
				enableAccLineSettings( ( (BubbleSeries) series ).getAccLineAttributes( )
						.isSetVisible( )
						&& ( (BubbleSeries) series ).getAccLineAttributes( )
								.isVisible( ) );
			}
			else if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( ( (BubbleSeries) series ).getAccLineAttributes( ),
						"style",//$NON-NLS-1$
						(LineStyle) event.data,
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( ( (BubbleSeries) series ).getAccLineAttributes( ),
						"thickness",//$NON-NLS-1$
						( (Integer) event.data ).intValue( ),
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				( (BubbleSeries) series ).getAccLineAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
		}
		else if ( event.widget.equals( fccShadow ) )
		{
			( (BubbleSeries) series ).setShadowColor( (ColorDefinition) event.data );
		}
	}

	private void enableLineSettings( boolean isEnabled )
	{
		if ( lblShadow != null )
		{
			lblShadow.setEnabled( isEnabled );
		}
		if ( fccShadow != null )
		{
			fccShadow.setEnabled( isEnabled );
		}
		if ( cmbPalette != null )
		{
			cmbPalette.setEnabled( isEnabled );
		}
		cmbCurve.setEnabled( isEnabled );
	}
	
	private void enableAccLineSettings( boolean isEnabled )
	{
		if ( cmbOrientation != null )
		{
			cmbOrientation.setEnabled( isEnabled );
		}
		if ( lblOrientation != null )
		{
			lblOrientation.setEnabled( isEnabled );
		}
		
	}

}
