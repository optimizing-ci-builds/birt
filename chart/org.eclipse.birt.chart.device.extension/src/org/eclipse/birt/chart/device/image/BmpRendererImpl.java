
package org.eclipse.birt.chart.device.image;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;

/**
 * 
 */
public final class BmpRendererImpl extends JavaxImageIOWriter
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.extension/image" ); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getFormat()
	 */
	public final String getFormat( )
	{
		return "bmp"; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.device.IImageMapEmitter#getMimeType()
	 */
	public final String getMimeType( )
	{
		return "image/bmp"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getImageType()
	 */
	public final int getImageType( )
	{
		return BufferedImage.TYPE_INT_RGB;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#after()
	 */
	public final void after( ) throws ChartException
	{
		try
		{
			super.after( );
		}
		catch ( ChartException e )
		{
			if ( isSupportedByJavaxImageIO( ) )
			{
				throw e;
			}
			else
			{
				logger.log( ILogger.INFORMATION,
						Messages.getString( "info.use.custom.image.writer", //$NON-NLS-1$
								new Object[]{
										getFormat( ), BmpWriter.class.getName( )
								}, getLocale( ) ) );

				// If not supported by JavaxImageIO, use our own.
				BmpWriter bw = null;

				if ( _oOutputIdentifier instanceof OutputStream )
				{
					bw = new BmpWriter( _img );
					try
					{
						bw.write( (OutputStream) _oOutputIdentifier );
					}
					catch ( Exception ex )
					{
						throw new ChartException( ChartDeviceExtensionPlugin.ID,
								ChartException.RENDERING,
								ex );
					}
				}
				else if ( _oOutputIdentifier instanceof String )
				{
					FileOutputStream fos = null;
					try
					{
						fos = new FileOutputStream( (String) _oOutputIdentifier );
						bw = new BmpWriter( _img );
						bw.write( fos );
						fos.close( );
					}
					catch ( Exception ex )
					{
						throw new ChartException( ChartDeviceExtensionPlugin.ID,
								ChartException.RENDERING,
								ex );
					}
				}
				else
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING,
							"exception.unable.write.output.identifier", //$NON-NLS-1$
							new Object[]{
								_oOutputIdentifier
							},
							ResourceBundle.getBundle( Messages.DEVICE_EXTENSION,
									getLocale( ) ) );
				}
			}
		}

	}

}