/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.chart;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.PlotClientAreaSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * Sheet for plot settings
 * 
 */
public class ChartPlotSheetImpl extends SubtaskSheetImpl implements
		Listener,
		SelectionListener
{

	private Combo cmbIncludingVisible;
	
	private Combo cmbWithinVisible;
	
	private FillChooserComposite cmbBlockColor;

	private FillChooserComposite cmbClientAreaColor;

	public void createControl( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.SUBTASK_PLOT );

		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( 2, false );
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		Composite cmpBasic = new Composite( cmpContent, SWT.NONE );
		{
			cmpBasic.setLayout( new GridLayout( 2, false ) );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cmpBasic.setLayoutData( gd );
		}

		Label lblIncludingAxes = new Label( cmpBasic, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalSpan = 2;
			lblIncludingAxes.setLayoutData( gd );
			lblIncludingAxes.setFont( JFaceResources.getBannerFont( ) );
			lblIncludingAxes.setText( getChart( ) instanceof ChartWithAxes ? Messages.getString( "ChartPlotSheetImpl.Label.AreaIncludingAxes" ) : Messages.getString( "ChartPlotSheetImpl.Label.PlotArea" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		}

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartPlotSheetImpl.Label.Background" ) ); //$NON-NLS-1$

		int fillStyles = FillChooserComposite.ENABLE_AUTO
				| FillChooserComposite.ENABLE_GRADIENT
				| FillChooserComposite.ENABLE_IMAGE
				| FillChooserComposite.ENABLE_TRANSPARENT
				| FillChooserComposite.ENABLE_TRANSPARENT_SLIDER;
		cmbBlockColor = new FillChooserComposite( cmpBasic,
				SWT.DROP_DOWN | SWT.READ_ONLY,
				fillStyles,
				getContext( ),
				getChart( ).getPlot( ).getBackground( ) );
		{
			GridData gd = new GridData( );
			gd.widthHint = 200;
			cmbBlockColor.setLayoutData( gd );
			cmbBlockColor.addListener( this );
		}

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartPlotSheetImpl.Label.Outline" ) ); //$NON-NLS-1$

		cmbIncludingVisible = ChartUIExtensionUtil.createCombo( cmpBasic,
				ChartUIExtensionUtil.getShowHideComboItems( ) );
		cmbIncludingVisible.select( getChart( ).getPlot( )
				.getOutline( )
				.isSetVisible( ) ? ( getChart( ).getPlot( )
				.getOutline( )
				.isVisible( ) ? 1 : 2 ) : 0 );
		cmbIncludingVisible.addSelectionListener( this );

		Label lblWithinAxes = new Label( cmpBasic, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalSpan = 2;
			lblWithinAxes.setLayoutData( gd );
			lblWithinAxes.setFont( JFaceResources.getBannerFont( ) );
			lblWithinAxes.setText( getChart( ) instanceof ChartWithAxes ? Messages.getString( "ChartPlotSheetImpl.Label.AreaWithinAxes" ) : Messages.getString( "ChartPlotSheetImpl.Label.ClientArea" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// WithinAxes area is not supported in 3D
		if ( !ChartUIUtil.is3DType( getChart( ) ) )
		{
			new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartPlotSheetImpl.Label.Background2" ) ); //$NON-NLS-1$

			cmbClientAreaColor = new FillChooserComposite( cmpBasic,
					SWT.DROP_DOWN | SWT.READ_ONLY,
					fillStyles,
					getContext( ),
					getChart( ).getPlot( ).getClientArea( ).getBackground( ) );
			{
				GridData gridData = new GridData( );
				gridData.widthHint = 200;
				cmbClientAreaColor.setLayoutData( gridData );
				cmbClientAreaColor.addListener( this );
			}
		}

		// Following settings only work in some criteria
		boolean is3DWallFloorSet = ChartUIUtil.is3DWallFloorSet( getChart( ) );
		Label lblVisibleWithin = new Label( cmpBasic, SWT.NONE );
		{
			lblVisibleWithin.setText( Messages.getString( "ChartPlotSheetImpl.Label.Outline" ) ); //$NON-NLS-1$
			lblVisibleWithin.setEnabled( is3DWallFloorSet );
		}

		cmbWithinVisible = ChartUIExtensionUtil.createCombo( cmpBasic,
				ChartUIExtensionUtil.getShowHideComboItems( ) );
		cmbWithinVisible.select( getChart( ).getPlot( )
				.getClientArea( )
				.getOutline( )
				.isSetVisible( ) ? ( getChart( ).getPlot( )
				.getClientArea( )
				.getOutline( )
				.isVisible( ) ? 1 : 2 ) : 0 );
		cmbWithinVisible.setEnabled( is3DWallFloorSet );
		if ( !cmbWithinVisible.getEnabled( ) )
		{
			cmbWithinVisible.select( 2 ); // Hide for 3D
		}
		cmbWithinVisible.addSelectionListener( this );

		// This control is only for testing chart engine and not exposed in UI
		final Button btnCV = new Button( cmpBasic, SWT.CHECK );
		btnCV.setText( "Plot Visible" ); //$NON-NLS-1$
		btnCV.setSelection( getChart( ).getPlot( ).getClientArea( ).isVisible( ) );
		btnCV.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected( SelectionEvent e )
			{
				getChart( ).getPlot( )
						.getClientArea( )
						.setVisible( btnCV.getSelection( ) );

			}
		} );
		btnCV.setVisible( false );

		createButtonGroup( cmpContent );
	}

	private void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		ITaskPopupSheet popup = new PlotClientAreaSheet( Messages.getString( "ChartPlotSheetImpl.Label.AreaFormat" ), //$NON-NLS-1$
				getContext( ) );
		Button btnArea = createToggleButton( cmp,
				BUTTON_AREA_FORMAT,
				Messages.getString( "ChartPlotSheetImpl.Label.AreaFormat&" ), popup ); //$NON-NLS-1$
		btnArea.addSelectionListener( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( cmbBlockColor ) )
		{
			if ( event.type == FillChooserComposite.FILL_CHANGED_EVENT )
			{
				getChart( ).getPlot( ).setBackground( (Fill) event.data );
			}
		}
		else if ( event.widget.equals( cmbClientAreaColor ) )
		{
			if ( event.type == FillChooserComposite.FILL_CHANGED_EVENT )
			{
				getChart( ).getPlot( )
						.getClientArea( )
						.setBackground( (Fill) event.data );
			}
		}
	}

	public void widgetSelected( SelectionEvent e )
	{
		// Detach popup dialog if there's selected popup button.
		if ( detachPopup( e.widget ) )
		{
			return;
		}

		if ( isRegistered( e.widget ) )
		{
			attachPopup( ( (Button) e.widget ).getData( ).toString( ) );
		}

		if ( e.widget.equals( cmbIncludingVisible ) )
		{
			if ( cmbIncludingVisible.getSelectionIndex( ) == 0 )
			{
				getChart( ).getPlot( ).getOutline( ).unsetVisible( );
			}
			else
			{
				getChart( ).getPlot( )
						.getOutline( )
						.setVisible( ( (Combo) e.widget ).getSelectionIndex( ) == 1 );
			}
			refreshPopupSheet( );
		}
		else if ( e.widget.equals( cmbWithinVisible ) )
		{
			if ( cmbWithinVisible.getSelectionIndex( ) == 0 )
			{
				getChart( ).getPlot( )
						.getClientArea( )
						.getOutline( )
						.unsetVisible( );
			}
			else
			{
				getChart( ).getPlot( )
						.getClientArea( )
						.getOutline( )
						.setVisible( ( (Combo) e.widget ).getSelectionIndex( ) == 1 );
			}
			refreshPopupSheet( );
		}

	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub
	}

}