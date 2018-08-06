package com.ismartautocare;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.BoringLayout;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;
import com.starmicronics.starioextension.ICommandBuilder;
import com.starmicronics.starioextension.StarIoExt;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MiniPrinterFunctions {
	enum BarcodeWidth {
		_125, _250, _375, _500, _625, _750, _875, _1_0
	};

	enum BarcodeType {
		code39, ITF, code93, code128
	};

	private static StarIOPort portForMoreThanOneFunction = null;

	public static void AddRange(ArrayList<byte[]> array, byte[] newData) {
		for (int index = 0; index < newData.length; index++) {
			array.add(newData);
		}
	}

	/**
	 * This function is not supported by portable(ESC/POS) printers.
	 * 
	 * @param context
	 *     Activity for displaying messages
	 * @param portName
	 *     Port name to use for communication
	 * @param portSettings
	 *     The port settings to use
	 */
	public static void OpenCashDrawer(Context context, String portName, String portSettings) {
		Builder dialog = new Builder(context);
		dialog.setNegativeButton("Ok", null);
		AlertDialog alert = dialog.create();
		alert.setTitle("Feature Not Available");
		alert.setMessage("Cash drawer functionality is supported only on POS printer models");
		alert.setCancelable(false);
		alert.show();
	}

	/**
	 * This function shows how to get the firmware information of a printer
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress> or BT:<DeviceName> for bluetooth)
	 * @param portSettings
	 *     Should be portable;escpos, the port settings portable;escpos is used for portable(ESC/POS) printers
	 */
	public static void CheckFirmwareVersion(Context context, String portName, String portSettings) {
		StarIOPort port = null;
		try {
			/*
			 * using StarIOPort3.1.jar (support USB Port) Android OS Version: upper 2.2
			 */
			port = StarIOPort.getPort(portName, portSettings, 10000, context);
			/*
			 * using StarIOPort.jar Android OS Version: under 2.1 port = StarIOPort.getPort(portName, portSettings, 10000);
			 */

			// A sleep is used to get time for the socket to completely open
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			Map<String, String> firmware = port.getFirmwareInformation();

			String modelName = firmware.get("ModelName");
			String firmwareVersion = firmware.get("FirmwareVersion");

			String message = "Model Name:" + modelName;
			message += "\nFirmware Version:" + firmwareVersion;

			Builder dialog = new Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Firmware Information");
			alert.setMessage(message);
			alert.setCancelable(false);
			alert.show();

		} catch (StarIOPortException e) {
			Builder dialog = new Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage("Failed to connect to printer");
			alert.setCancelable(false);
			alert.show();
		} finally {
			if (port != null) {
				try {
					StarIOPort.releasePort(port);
				} catch (StarIOPortException e) {
				}
			}
		}
	}

	/**
	 * This function shows how to get the status of a printer
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress> or BT:<DeviceName> for bluetooth)
	 * @param portSettings
	 *     Should be portable;escpos, the port settings portable;escpos is used for portable(ESC/POS) printers
	 */
	public static void CheckStatus(Context context, String portName, String portSettings) {
		StarIOPort port = null;
		try {
			/*
			 * using StarIOPort3.1.jar (support USB Port) Android OS Version: upper 2.2
			 */
			port = StarIOPort.getPort(portName, portSettings, 10000, context);
			/*
			 * using StarIOPort.jar Android OS Version: under 2.1 port = StarIOPort.getPort(portName, portSettings, 10000);
			 */

			// A sleep is used to get time for the socket to completely open
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			StarPrinterStatus status = port.retreiveStatus();

			if (status.offline == false) {
				Builder dialog = new Builder(context);
				dialog.setNegativeButton("Ok", null);
				AlertDialog alert = dialog.create();
				alert.setTitle("Printer");
				alert.setMessage("Printer is Online");
				alert.setCancelable(false);
				alert.show();
			} else {
				String message = "Printer is offline";
				if (status.receiptPaperEmpty == true) {
					message += "\nPaper is Empty";
				}
				if (status.coverOpen == true) {
					message += "\nCover is Open";
				}
				Builder dialog = new Builder(context);
				dialog.setNegativeButton("Ok", null);
				AlertDialog alert = dialog.create();
				alert.setTitle("Printer");
				alert.setMessage(message);
				alert.setCancelable(false);
				alert.show();
			}
		} catch (StarIOPortException e) {
			Builder dialog = new Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage("Failed to connect to printer");
			alert.setCancelable(false);
			alert.show();
		} finally {
			if (port != null) {
				try {
					StarIOPort.releasePort(port);
				} catch (StarIOPortException e) {
				}
			}
		}
	}

	/**
	 * This function is used to print any of the barcodes supported by portable(ESC/POS) printers This example supports 4 barcode types code39, code93, ITF, code128. For a complete list of supported barcodes see manual (pg 35).
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress> or BT:<DeviceName> for bluetooth)
	 * @param portSettings
	 *     Should be portable;escpos, the port settings portable;escpos is used for portable(ESC/POS) printers
	 * @param height
	 *     The height of the barcode, max is 255
	 * @param width
	 *     Sets the width of the barcode, value of this should be 1 to 8. See pg 34 of the manual for the definitions of the values.
	 * @param type
	 *     The type of barcode to print. This program supports code39, code93, ITF, code128.
	 * @param barcodeData
	 *     The data to print. The type of characters supported varies. See pg 35 for a complete list of all support characters
	 */
	public static void PrintBarcode(Context context, String portName, String portSettings, byte height, BarcodeWidth width, BarcodeType type, byte[] barcodeData) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		byte[] height_Commands = new byte[] { 0x1d, 0x68, 0x00 };
		height_Commands[2] = height;
		commands.add(height_Commands);

		byte[] width_Commands = new byte[] { 0x1d, 0x77, 0x00 };
		switch (width) {
		case _125:
			width_Commands[2] = 1;
			break;
		case _250:
			width_Commands[2] = 2;
			break;
		case _375:
			width_Commands[2] = 3;
			break;
		case _500:
			width_Commands[2] = 4;
			break;
		case _625:
			width_Commands[2] = 5;
			break;
		case _750:
			width_Commands[2] = 6;
			break;
		case _875:
			width_Commands[2] = 7;
			break;
		case _1_0:
			width_Commands[2] = 8;
			break;
		}
		commands.add(width_Commands);

		byte[] print_Barcode = new byte[4 + barcodeData.length + 1];
		print_Barcode[0] = 0x1d;
		print_Barcode[1] = 0x6b;
		switch (type) {
		case code39:
			print_Barcode[2] = 69;
			break;
		case ITF:
			print_Barcode[2] = 70;
			break;
		case code93:
			print_Barcode[2] = 72;
			break;
		case code128:
			print_Barcode[2] = 73;
			break;
		}
		print_Barcode[3] = (byte) barcodeData.length;
		System.arraycopy(barcodeData, 0, print_Barcode, 4, barcodeData.length);

		commands.add(print_Barcode);

		commands.add(new byte[] { 0x0a, 0x0a, 0x0a, 0x0a });

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * The function is used to print a QRCode for portable(ESC/POS) printers
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress> or BT:<DeviceName> for bluetooth)
	 * @param portSettings
	 *     Should be portable;escpos, the port settings portable;escpos is used for portable(ESC/POS) printers
	 * @param correctionLevel
	 *     The correction level for the QRCode. This value should be 0x4C, 0x4D, 0x51, or 0x48. See pg 41 for for definition of values
	 * @param sizeByECLevel
	 *     This specifies the symbol version. This value should be 1 to 40. See pg 41 for the definition of the level
	 * @param moduleSize
	 *     The module size of the QRCode. This value should be 1 to 8.
	 * @param barcodeData
	 *     The characters to print in the QRCode
	 */
//	public static void PrintQrcode(Context context, String portName, String portSettings, PrinterFunctions.CorrectionLevelOption correctionLevel, byte sizeByECLevel, byte moduleSize, byte[] barcodeData) {
//		ArrayList<byte[]> commands = new ArrayList<byte[]>();
//
//		// The printer supports 3 2d bar code types, this one selects qrcode
//		commands.add(new byte[] { 0x1d, 0x5a, 0x02 });
//
//		// This builds the qrcommand
//		byte[] print2dbarcode = new byte[7 + barcodeData.length];
//		print2dbarcode[0] = 0x1b;
//		print2dbarcode[1] = 0x5a;
//		print2dbarcode[2] = sizeByECLevel;
//		switch (correctionLevel) {
//		case Low:
//			print2dbarcode[3] = 'L';
//			break;
//		case Middle:
//			print2dbarcode[3] = 'M';
//			break;
//		case Q:
//			print2dbarcode[3] = 'Q';
//			break;
//		case High:
//			print2dbarcode[3] = 'H';
//			break;
//		}
//		print2dbarcode[4] = moduleSize;
//		print2dbarcode[5] = (byte) (barcodeData.length % 256);
//		print2dbarcode[6] = (byte) (barcodeData.length / 256);
//		System.arraycopy(barcodeData, 0, print2dbarcode, 7, barcodeData.length);
//		commands.add(print2dbarcode);
//
//		commands.add(new byte[] { 0x0a, 0x0a, 0x0a, 0x0a });
//
//		sendCommand(context, portName, portSettings, commands);
//	}

	/**
	 * This function prints PDF417 barcodes for portable(ESC/POS) printers
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress> or BT:<DeviceName> for Bluetooth)
	 * @param portSettings
	 *     Should be portable;escpos, the port settings portable;escpos is used for portable(ESC/POS) printers
	 * @param width
	 *     This is the width of the PDF417 barcode to print. This is the same width used by the 1D barcodes. See pg 34 of the command manual.
	 * @param columnNumber
	 *     This is the column number of the PDF417 barcode to print. The value of this should be between 1 and 30.
	 * @param securityLevel
	 *     The represents how well the barcode can be restored if damaged. The value should be between 0 and 8.
	 * @param ratio
	 *     The value representing the horizontal and vertical ratio of the barcode. This value should between 2 and 5.
	 * @param barcodeData
	 *     The characters that will be in the barcode
	 */
	public static void PrintPDF417(Context context, String portName, String portSettings, BarcodeWidth width, byte columnNumber, byte securityLevel, byte ratio, byte[] barcodeData) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		byte[] barcodeWidthCommand = new byte[] { 0x1d, 'w', 0x00 };
		switch (width) {
		case _125:
			barcodeWidthCommand[2] = 1;
			break;
		case _250:
			barcodeWidthCommand[2] = 2;
			break;
		case _375:
			barcodeWidthCommand[2] = 3;
			break;
		case _500:
			barcodeWidthCommand[2] = 4;
			break;
		case _625:
			barcodeWidthCommand[2] = 5;
			break;
		case _750:
			barcodeWidthCommand[2] = 6;
			break;
		case _875:
			barcodeWidthCommand[2] = 7;
			break;
		case _1_0:
			barcodeWidthCommand[2] = 8;
			break;
		}

		commands.add(barcodeWidthCommand);

		commands.add(new byte[] { 0x1d, 0x5a, 0x00 });

		byte[] barcodeCommand = new byte[7 + barcodeData.length];
		barcodeCommand[0] = 0x1b;
		barcodeCommand[1] = 0x5a;
		barcodeCommand[2] = columnNumber;
		barcodeCommand[3] = securityLevel;
		barcodeCommand[4] = ratio;
		barcodeCommand[5] = (byte) (barcodeData.length % 256);
		barcodeCommand[6] = (byte) (barcodeData.length / 256);

		System.arraycopy(barcodeData, 0, barcodeCommand, 7, barcodeData.length);
		commands.add(barcodeCommand);

		commands.add(new byte[] { 0x0a, 0x0a, 0x0a, 0x0a });

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * Cut is not supported on portable(ESC/POS) printers
	 * 
	 * @param context
	 *     Activity to send the message that cut is not supported to the user
	 */
//	public static void performCut(Context context) {
//		Builder dialog = new Builder(context);
//		dialog.setNegativeButton("Ok", null);
//		AlertDialog alert = dialog.create();
//		alert.setTitle("Feature Not Available");
//		alert.setMessage("Cut functionality is supported only on POS printer models");
//		alert.setCancelable(false);
//		alert.show();
//	}
//
//	/**
//	 * This function is used to print a java bitmap directly to a portable(ESC/POS) printer.
//	 *
//	 * @param context
//	 *     Activity for displaying messages to the user
//	 * @param portName
//	 *     Port name to use for communication. This should be (TCP:<IPAddress> or BT:<Device pair name>)
//	 * @param portSettings
//	 *     Should be portable;escpos, the port settings portable;escpos is used for portable(ESC/POS) printers
//	 * @param source
//	 *     The bitmap to convert to Star printer data for portable(ESC/POS) printers
//	 * @param maxWidth
//	 *     The maximum width of the image to print. This is usually the page width of the printer. If the image exceeds the maximum width then the image is scaled down. The ratio is maintained.
//	 */
//
	/**
	 * This function is used to print a java bitmap directly to a portable(ESC/POS) printer.
	 *
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress> or BT:<Device pair name>)
	 * @param portSettings
	 *     Should be portable;escpos, the port settings portable;escpos is used for portable(ESC/POS) printers
	 * @param res
	 *     The resources object containing the image data
	 * @param source
	 *     The resource id of the image data
	 * @param maxWidth
	 *     The maximum width of the image to print. This is usually the page width of the printer. If the image exceeds the maximum width then the image is scaled down. The ratio is maintained.
	 */
//	public static void PrintBitmapImage(Context context, String portName, String portSettings, Resources res, int source, int maxWidth, boolean compressionEnable, PrinterFunctions.RasterCommand rasterType) {
//		ArrayList<byte[]> commands = new ArrayList<byte[]>();
//
//		RasterDocument rasterDoc = new RasterDocument(RasterDocument.RasSpeed.Medium, RasterDocument.RasPageEndMode.FeedAndFullCut, RasterDocument.RasPageEndMode.FeedAndFullCut, RasterDocument.RasTopMargin.Standard, 0, 0, 0);
//		Bitmap bm = BitmapFactory.decodeResource(res, source);
//		StarBitmap starbitmap = new StarBitmap(bm, false, maxWidth);
//
//		if (rasterType == PrinterFunctions.RasterCommand.Standard) {
//			commands.add(rasterDoc.BeginDocumentCommandData());
//
//			commands.add(starbitmap.getImageRasterDataForPrinting_Standard(compressionEnable));
//
//			commands.add(rasterDoc.EndDocumentCommandData());
//		} else {
//			commands.add(starbitmap.getImageRasterDataForPrinting_graphic(compressionEnable));
//			commands.add(new byte[] { 0x1b, 0x64, 0x02 }); // Feed to cutter position
//		}
//
//		sendCommand(context, portName, portSettings, commands);
//	}
//
	/**
	 * This function prints raw text to a Star portable(ESC/POS) printer. It shows how the text can be modified like changing its size.
	 *
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress> or BT:<Device pair name>)
	 * @param portSettings
	 *     Should be portable;escpos, the port settings portable;escpos is used for portable(ESC/POS) printers
	 * @param underline
	 *     boolean variable that tells the printer to underline the text
	 * @param emphasized
	 *     boolean variable that tells the printer to emphasize the text. This is somewhat like bold. It isn't as dark, but darker than regular characters.
//	 * @param upsideDown
	 *     boolean variable that tells the printer to print text upside down.
	 * @param invertColor
	 *     boolean variable that tells the printer to invert text. All white space will become black but the characters will be left white.
	 * @param heightExpansion
	 *     This integer tells the printer what the character height should be, ranging from 0 to 7 and representing multiples from 1 to 8.
	 * @param widthExpansion
	 *     This integer tell the printer what the character width should be, ranging from 0 to 7 and representing multiples from 1 to 8.
	 * @param leftMargin
	 *     Defines the left margin for text on Star portable(ESC/POS) printers. This number can be from 0 to 65536. However, remember how much space is available as the text can be pushed off the page.
	 * @param alignment
	 *     Defines the alignment of the text. The printers support left, right, and center justification.
	 * @param textToPrint
	 *     The text to send to the printer.
	 */
	public static void PrintText(Context context, String portName, String portSettings, boolean underline, boolean emphasized, boolean upsidedown, boolean invertColor, byte heightExpansion, byte widthExpansion, int leftMargin, PrinterFunctions.Alignment alignment, byte[] textToPrint) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		commands.add(new byte[] { 0x1b, 0x40 }); // Initialization

		byte[] underlineCommand = new byte[] { 0x1b, 0x2d, 0x00 };
		if (underline) {
			underlineCommand[2] = 49;
		} else {
			underlineCommand[2] = 48;
		}
		commands.add(underlineCommand);

		byte[] emphasizedCommand = new byte[] { 0x1b, 0x45, 0x00 };
		if (emphasized) {
			emphasizedCommand[2] = 1;
		} else {
			emphasizedCommand[2] = 0;
		}
		commands.add(emphasizedCommand);

		byte[] upsidedownCommand = new byte[] { 0x1b, 0x7b, 0x00 };
		if (upsidedown) {
			upsidedownCommand[2] = 1;
		} else {
			upsidedownCommand[2] = 0;
		}
		commands.add(upsidedownCommand);

		byte[] invertColorCommand = new byte[] { 0x1d, 0x42, 0x00 };
		if (invertColor) {
			invertColorCommand[2] = 1;
		} else {
			invertColorCommand[2] = 0;
		}
		commands.add(invertColorCommand);

		byte[] characterSizeCommand = new byte[] { 0x1d, 0x21, 0x00 };
		characterSizeCommand[2] = (byte) (heightExpansion | (widthExpansion << 4));
		commands.add(characterSizeCommand);

		byte[] leftMarginCommand = new byte[] { 0x1d, 0x4c, 0x00, 0x00 };
		leftMarginCommand[2] = (byte) (leftMargin % 256);
		leftMarginCommand[3] = (byte) (leftMargin / 256);
		commands.add(leftMarginCommand);

		byte[] justificationCommand = new byte[] { 0x1b, 0x61, 0x00 };
		switch (alignment) {
		case Left:
			justificationCommand[2] = 48;
			break;
		case Center:
			justificationCommand[2] = 49;
			break;
		case Right:
			justificationCommand[2] = 50;
			break;
		}
		commands.add(justificationCommand);

		commands.add(textToPrint);

		commands.add(new byte[] {0x0a});

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function shows how to read the MSR data(credit card) of a portable(ESC/POS) printer. The function first puts the printer into MSR read mode, then asks the user to swipe a credit card The function waits for a response from the user. The user can cancel MSR mode or have the printer read the card.
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress> or BT:<Device pair name>)
	 * @param portSettings
	 *     Should be portable;escpos, the port settings portable;escpos is used for portable(ESC/POS) printers
	 * @param strPrintArea
	 *     Printable area size, This should be ("2inch (58mm)" or "3inch (80mm)")
	 */
	public static boolean PrintSampleReceipt(Context context, String portName, String portSettings, String strPrintArea) {
		ArrayList<byte[]> list = new ArrayList<byte[]>();

		if (strPrintArea.equals("2inch (58mm)")) {
			byte[] outputByteBuffer = null;
			list.add(new byte[] { 0x1d, 0x57, (byte) 0x80, 0x31 }); // Page Area Setting <GS> <W> nL nH (nL = 128, nH = 1)

			list.add(new byte[] { 0x1b, 0x61, 0x01 }); // Center Justification <ESC> a n (0 Left, 1 Center, 2 Right)

//			 outputByteBuffer = ("[Print Stored Logo Below]\n\n").getBytes();
//			 port.writePort(outputByteBuffer, 0, outputByteBuffer.length);
//
//			 list.add(new byte[]{0x1b, 0x66, 0x00}); //Stored Logo Printing <ESC> f n (n = Store Logo # = 0 or 1 or 2 etc.)

			list.add(("Star Clothing Boutique\n" + "123 Star Road\n" + "City, State 12345\n\n").getBytes());

			list.add(new byte[] { 0x1b, 0x61, 0x00 }); // Left Alignment

			list.add(("Date: MM/DD/YYYY   Time:HH:MM PM\n" + "--------------------------------\n").getBytes());

			list.add(new byte[] { 0x1b, 0x45, 0x01 }); // Set Emphasized Printing ON

			list.add("SALE\n".getBytes());

			list.add(new byte[] { 0x1b, 0x45, 0x00 }); // Set Emphasized Printing OFF (same command as on)

			outputByteBuffer = ("300678566  PLAIN T-SHIRT  10.99\n" + "300692003  BLACK DENIM    29.99\n" + "300651148  BLUE DENIM     29.99\n" + "300642980  STRIPED DRESS  49.99\n" + "300638471  BLACK BOOTS    35.99\n\n" + "Subtotal                 156.95" + "\n" + "Tax                        0.00" + "\n" + "--------------------------------\n" + "Total ").getBytes();
			list.add(outputByteBuffer);

			list.add(new byte[] { 0x1d, 0x21, 0x11 }); // Width and Height Character Expansion <GS> ! n

			list.add("      $156.95\n".getBytes());

			list.add(new byte[] { 0x1d, 0x21, 0x00 }); // Cancel Expansion - Reference Star Portable Printer Programming Manual

			list.add(("--------------------------------\n" + "Charge\n" + "$156.95\n" + "Visa XXXX-XXXX-XXXX-0123\n").getBytes());

			list.add(new byte[] { 0x1d, 0x77, 0x02 }); // for 1D Code39 Barcode
			list.add(new byte[] { 0x1d, 0x68, 0x64 }); // for 1D Code39 Barcode
			list.add(new byte[] { 0x1d, 0x48, 0x01 }); // for 1D Code39 Barcode
			list.add(new byte[] { 0x1d, 0x6b, 0x41, 0x0b, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30 }); // for 1D Code39 Barcode

			list.add("\n".getBytes());

			list.add(new byte[] { 0x1d, 0x42, 0x01 }); // Specify White-Black Invert

			list.add("Refunds and Exchanges\n".getBytes());

			list.add(new byte[] { 0x1d, 0x42, 0x00 }); // Cancel White-Black Invert

			list.add("Within ".getBytes());

			list.add(new byte[] { 0x1b, 0x2d, 0x01 }); // Specify Underline Printing

			list.add("30 days".getBytes());

			list.add(new byte[] { 0x1b, 0x2d, 0x00 }); // Cancel Underline Printing

			outputByteBuffer = (" with receipt\n" 
								+ "And tags attached\n" 
								+ "-------------Sign Here----------\n\n\n" 
								+ "--------------------------------\n" 
								+ "Thank you for buying Star!\n" 
								+ "Scan QR code to visit our site!\n").getBytes();
			list.add(outputByteBuffer);

			list.add(new byte[] { 0x1d, 0x5a, 0x02 }); // Cancel Underline Printing

			byte[] qrcodeByteBuffer = new byte[] { 
					0x1d, 0x5a, 0x02, 0x1b, 0x5a, 0x00, 0x51, 0x04, 0x1C, 0x00, 
					0x68, 0x74, 0x74, 0x70, 0x3a, 0x2f, 0x2f, 0x77, 0x77, 0x77, 
					0x2e, 0x53, 0x74, 0x61, 0x72, 0x4d, 0x69, 0x63, 0x72, 0x6f, 
					0x6e, 0x69, 0x63, 0x73, 0x2e, 0x63, 0x6f, 0x6d };
			list.add(qrcodeByteBuffer); // QR Code (View QR 2D Barcode code for better explanation)

			list.add("\n\n\n".getBytes());

			return sendCommand(context, portName, portSettings, list);
		}

		return sendCommand(context, portName, portSettings, list);
	}

	public static void PrintBitmapImage(Context context, String portName, String portSettings, Resources res, int source, int maxWidth, boolean compressionEnable, boolean pageModeEnable) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		Bitmap bm = BitmapFactory.decodeResource(res, source);
		StarBitmap starbitmap = new StarBitmap(bm, false, maxWidth);

		try {
			commands.add(new byte[] { 0x1d, 0x21, 0x00 });
			commands.add(starbitmap.getImageEscPosDataForPrinting(compressionEnable, pageModeEnable));

			sendCommand(context, portName, portSettings, commands);
		} catch (StarIOPortException e) {
			Builder dialog = new Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage(e.getMessage());
			alert.setCancelable(false);
			alert.show();
		}
	}


	public static void PrintBitmapThai(Context context, String portName, String portSettings, String tv_final, int source, String et_plate_number, String et_province_number,
									   String get_size, String tv_model, String et_name_driver, String et_phone_driver, Resources res, int source_Top, int maxWidth, ArrayList<Item> ArratList_listview,
									   String tv_service_end, String payment_by, boolean compressionEnable, boolean pageModeEnable,String string_rno) {
		for (int j = 0 ; j<1 ; j++){

			ArrayList<byte[]> list = new ArrayList<byte[]>();

			Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/WareeSansMono-Bold.ttf");
//
//			Log.d("Tag","string_rno : "+string_rno);
//			String print_rno = "NO : #"+string_rno+"\n";
			String printdate_time = "Date:"+PrintDateCuurent()+"      "+"Time:"+PrintTimeCuurent()+"\n";
			byte[] data_rno = ("NO : #"+string_rno+"\n").getBytes();
			byte[] data_name_wash = "WashUp Ratchada\n".getBytes();
			byte[] data_tel = "Tel : 0987654321\n\n".getBytes();
			byte[] data_date_time = printdate_time.getBytes();
			byte[] data_slide = "--------------------------------".getBytes();

			String driver_name = "Driver's name: "+et_name_driver+"\n";
			byte[] data_driver = driver_name.getBytes();
			byte[] data_car_model = ("Car Model: "+tv_model+"\n").getBytes();
			byte[] data_car_size = ("Car Size: "+get_size+"\n\n").getBytes();
			byte[] data_service_price = ("Service Price (Bath)"+"\n").getBytes();
			byte[] data_driver_slide = "--------------------------------".getBytes();

			byte[] data_topic = "CODE ".getBytes();
			byte[] data_des = "DESCRIPTION".getBytes();
			byte[] data_price = "PRICE".getBytes();
			byte[] data_thai = "ราคา".getBytes();

			String data_cash_date = tv_service_end+"\n";
			byte[] data_cash_slide = "--------------------------------\n".getBytes();
			byte[] data_cash= (payment_by+"               "+tv_final+"\n").getBytes();
			byte[] data_cash_service = ("Est. Service Completion:"+"\n").getBytes();
//			byte[] data_cash_date = (tv_service_end+"\n").getBytes();
			byte[] data_cash_thank = "THANK YOU FOR USING OUR SERVICES\n\n\n".getBytes();

			StarIoExt.Emulation emulation = StarIoExt.Emulation.EscPosMobile;
			Log.d("Tag", "emulation : "+emulation);
			Log.d("Tag", "et_plate_number : "+et_plate_number);
			Log.d("Tag", "et_province_number : "+et_province_number);

			Bitmap img_name = createBitmapFromText(driver_name, 18, PrinterSetting.PAPER_SIZE_TWO_INCH, tf);
			Bitmap img_plate = createBitmapFromTextCenter(et_plate_number, 30, PrinterSetting.PAPER_SIZE_TWO_INCH, tf);
			Bitmap img_provice = createBitmapFromTextCenter(et_province_number+"\n", 30, PrinterSetting.PAPER_SIZE_TWO_INCH, tf);
			Bitmap img_title = createBitmapFromText("CODE"+"  "+"DESCRIPTION"+"  "+"         PRICE", 18, PrinterSetting.PAPER_SIZE_TWO_INCH, tf);
			Bitmap img_date = createBitmapFromText(data_cash_date, 18, PrinterSetting.PAPER_SIZE_TWO_INCH, tf);
//		Bitmap img_plate = createBitmapFromText("E1"+"    "+"เคลือบกระจกรอบคัน"+"   "+"18,600.00", 18, PrinterSetting.PAPER_SIZE_TWO_INCH, tf);

			ICommandBuilder builder = StarIoExt.createCommandBuilder(emulation);
			builder.beginDocument();
			builder.appendCodePage(ICommandBuilder.CodePageType.CP874);

			builder.appendAlignment(data_rno,ICommandBuilder.AlignmentPosition.Left);

			builder.endDocument();
			list.add(builder.getCommands());

			Bitmap bm = BitmapFactory.decodeResource(res, source);
			StarBitmap starbitmap = new StarBitmap(bm, false, maxWidth);
			try {
				list.add(new byte[] { 0x1d, 0x21, 0x00 });
				list.add(starbitmap.getImageEscPosDataForPrinting(compressionEnable, pageModeEnable));
			} catch (StarIOPortException e) {
			}

			ICommandBuilder builder_detail = StarIoExt.createCommandBuilder(emulation);
			builder_detail.beginDocument();
			builder_detail.appendCodePage(ICommandBuilder.CodePageType.CP874);

			//Shop name and detail
			builder_detail.appendAlignment(data_name_wash,ICommandBuilder.AlignmentPosition.Center);
			builder_detail.appendAlignment(data_tel,ICommandBuilder.AlignmentPosition.Center);
			builder_detail.appendAlignment(data_date_time,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendAlignment(data_slide,ICommandBuilder.AlignmentPosition.Left);

			//Plate and Provice
//			builder.appendAlignment(ICommandBuilder.AlignmentPosition.Center);
			builder_detail.appendBitmapWithAlignment(img_plate,true,ICommandBuilder.AlignmentPosition.Center);
			builder_detail.appendBitmapWithAlignment(img_provice,true,ICommandBuilder.AlignmentPosition.Center);

			//Driver
//		builder.appendAlignment(data_driver,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendBitmapWithAlignment(img_name,true,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendAlignment(data_car_model,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendAlignment(data_car_size,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendAlignment(data_service_price,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendAlignment(data_driver_slide,ICommandBuilder.AlignmentPosition.Left);

			builder_detail.appendBitmapWithAlignment(img_title,true,ICommandBuilder.AlignmentPosition.Right);

			for (int i = 0 ; i<ArratList_listview.size() ; i++){
				String des;
				if(ArratList_listview.get(i).getDesc().toString().length() > 25){
					des = ArratList_listview.get(i).getDesc().substring(0,20);
				}else{
					des = ArratList_listview.get(i).getDesc();
				}
				Bitmap img = createBitmapFromTextCenter(ArratList_listview.get(i).getCode()+"    "+des+"    "+ArratList_listview.get(i).getPrice(), 17, PrinterSetting.PAPER_SIZE_TWO_INCH, tf);
				builder_detail.appendBitmapWithAlignment(img,true,ICommandBuilder.AlignmentPosition.Right);
			}

			//Shop name and detail
			builder_detail.appendAlignment(data_cash_slide,ICommandBuilder.AlignmentPosition.Center);
			builder_detail.appendAlignment(data_cash,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendAlignment(data_cash_slide,ICommandBuilder.AlignmentPosition.Center);
			builder_detail.appendAlignment(data_cash_service,ICommandBuilder.AlignmentPosition.Left);
//			builder.appendAlignment(data_cash_date,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendBitmapWithAlignment(img_date,true,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendAlignment(data_cash_thank,ICommandBuilder.AlignmentPosition.Left);

			builder_detail.endDocument();
			list.add(builder_detail.getCommands());

		list.add(new byte[] { 0x1b, 0x64, 0x02 }); // Cut
		list.add(new byte[] { 0x07 }); // Kick cash drawer

			sendCommand(context, portName, portSettings, list);
		}

	}

	public static void PrintBitmapThaiReprint(Context context, String portName, String portSettings, String tv_final, int source, String et_plate_number, String et_province_number,
									   String get_size, String tv_model, String et_name_driver, String et_phone_driver, Resources res, int source_Top, int maxWidth, ArrayList<ItemServicePrice> ArratList_listview,
									   String tv_service_end, String payment_by, boolean compressionEnable, boolean pageModeEnable,String string_rno) {
		for (int j = 0 ; j<1 ; j++){

			ArrayList<byte[]> list = new ArrayList<byte[]>();

			Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/WareeSansMono-Bold.ttf");
//
//			Log.d("Tag","string_rno : "+string_rno);
//			String print_rno = "NO : #"+string_rno+"\n";
			String printdate_time = "Date:"+PrintDateCuurent()+"      "+"Time:"+PrintTimeCuurent()+"\n";
			byte[] data_rno = ("NO : #"+string_rno+"\n").getBytes();
			byte[] data_name_wash = "WashUp Ratchada\n".getBytes();
			byte[] data_tel = "Tel : 0987654321\n\n".getBytes();
			byte[] data_date_time = printdate_time.getBytes();
			byte[] data_slide = "--------------------------------".getBytes();

			String driver_name = "Driver's name: "+et_name_driver+"\n";
			byte[] data_driver = driver_name.getBytes();
			byte[] data_car_model = ("Car Model: "+tv_model+"\n").getBytes();
			byte[] data_car_size = ("Car Size: "+get_size+"\n\n").getBytes();
			byte[] data_service_price = ("Service Price (Bath)"+"\n").getBytes();
			byte[] data_driver_slide = "--------------------------------".getBytes();

			byte[] data_topic = "CODE ".getBytes();
			byte[] data_des = "DESCRIPTION".getBytes();
			byte[] data_price = "PRICE".getBytes();
			byte[] data_thai = "ราคา".getBytes();

			String data_cash_date = tv_service_end+"\n";
			byte[] data_cash_slide = "--------------------------------\n".getBytes();
			byte[] data_cash= (payment_by+"               "+tv_final+"\n").getBytes();
			byte[] data_cash_service = ("Est. Service Completion:"+"\n").getBytes();
//			byte[] data_cash_date = (tv_service_end+"\n").getBytes();
			byte[] data_cash_thank = "THANK YOU FOR USING OUR SERVICES\n\n\n".getBytes();

			StarIoExt.Emulation emulation = StarIoExt.Emulation.EscPosMobile;
			Log.d("Tag", "emulation : "+emulation);
			Log.d("Tag", "et_plate_number : "+et_plate_number);
			Log.d("Tag", "et_province_number : "+et_province_number);

			Bitmap img_name = createBitmapFromText(driver_name, 18, PrinterSetting.PAPER_SIZE_TWO_INCH, tf);
			Bitmap img_plate = createBitmapFromTextCenter(et_plate_number, 30, PrinterSetting.PAPER_SIZE_TWO_INCH, tf);
			Bitmap img_provice = createBitmapFromTextCenter(et_province_number+"\n", 30, PrinterSetting.PAPER_SIZE_TWO_INCH, tf);
			Bitmap img_title = createBitmapFromText("CODE"+"  "+"DESCRIPTION"+"  "+"         PRICE", 18, PrinterSetting.PAPER_SIZE_TWO_INCH, tf);
			Bitmap img_date = createBitmapFromText(data_cash_date, 18, PrinterSetting.PAPER_SIZE_TWO_INCH, tf);
//		Bitmap img_plate = createBitmapFromText("E1"+"    "+"เคลือบกระจกรอบคัน"+"   "+"18,600.00", 18, PrinterSetting.PAPER_SIZE_TWO_INCH, tf);

			ICommandBuilder builder = StarIoExt.createCommandBuilder(emulation);
			builder.beginDocument();
			builder.appendCodePage(ICommandBuilder.CodePageType.CP874);

			builder.appendAlignment(data_rno,ICommandBuilder.AlignmentPosition.Left);

			builder.endDocument();
			list.add(builder.getCommands());

			Bitmap bm = BitmapFactory.decodeResource(res, source);
			StarBitmap starbitmap = new StarBitmap(bm, false, maxWidth);
			try {
				list.add(new byte[] { 0x1d, 0x21, 0x00 });
				list.add(starbitmap.getImageEscPosDataForPrinting(compressionEnable, pageModeEnable));
			} catch (StarIOPortException e) {
			}

			ICommandBuilder builder_detail = StarIoExt.createCommandBuilder(emulation);
			builder_detail.beginDocument();
			builder_detail.appendCodePage(ICommandBuilder.CodePageType.CP874);

			//Shop name and detail
			builder_detail.appendAlignment(data_name_wash,ICommandBuilder.AlignmentPosition.Center);
			builder_detail.appendAlignment(data_tel,ICommandBuilder.AlignmentPosition.Center);
			builder_detail.appendAlignment(data_date_time,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendAlignment(data_slide,ICommandBuilder.AlignmentPosition.Left);

			//Plate and Provice
//			builder.appendAlignment(ICommandBuilder.AlignmentPosition.Center);
			builder_detail.appendBitmapWithAlignment(img_plate,true,ICommandBuilder.AlignmentPosition.Center);
			builder_detail.appendBitmapWithAlignment(img_provice,true,ICommandBuilder.AlignmentPosition.Center);

			//Driver
//		builder.appendAlignment(data_driver,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendBitmapWithAlignment(img_name,true,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendAlignment(data_car_model,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendAlignment(data_car_size,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendAlignment(data_service_price,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendAlignment(data_driver_slide,ICommandBuilder.AlignmentPosition.Left);

			builder_detail.appendBitmapWithAlignment(img_title,true,ICommandBuilder.AlignmentPosition.Right);

			for (int i = 0 ; i<ArratList_listview.size() ; i++){
				String des,price = "";
				if(ArratList_listview.get(i).getDesc().toString().length() > 25){
					des = ArratList_listview.get(i).getDesc().substring(0,20);
				}else{
					des = ArratList_listview.get(i).getDesc();
				}

				try {
					JSONObject object = new JSONObject(ArratList_listview.get(i).getPrice().toString());
					price = object.getString(get_size);
				} catch (JSONException e) {
					e.printStackTrace();
				}


				Bitmap img = createBitmapFromTextCenter(ArratList_listview.get(i).getCode()+"    "+des+"    "+price, 17, PrinterSetting.PAPER_SIZE_TWO_INCH, tf);
				builder_detail.appendBitmapWithAlignment(img,true,ICommandBuilder.AlignmentPosition.Right);
			}

			//Shop name and detail
			builder_detail.appendAlignment(data_cash_slide,ICommandBuilder.AlignmentPosition.Center);
			builder_detail.appendAlignment(data_cash,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendAlignment(data_cash_slide,ICommandBuilder.AlignmentPosition.Center);
			builder_detail.appendAlignment(data_cash_service,ICommandBuilder.AlignmentPosition.Left);
//			builder.appendAlignment(data_cash_date,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendBitmapWithAlignment(img_date,true,ICommandBuilder.AlignmentPosition.Left);
			builder_detail.appendAlignment(data_cash_thank,ICommandBuilder.AlignmentPosition.Left);

			builder_detail.endDocument();
			list.add(builder_detail.getCommands());

			list.add(new byte[] { 0x1b, 0x64, 0x02 }); // Cut
			list.add(new byte[] { 0x07 }); // Kick cash drawer

			sendCommand(context, portName, portSettings, list);
		}

	}

	static public String PrintDateCuurent(){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		String datetime = dateformat.format(c.getTime());
		return datetime;
	}

	static public String PrintTimeCuurent(){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm");
		String datetime = dateformat.format(c.getTime());
		return datetime;
	}

	static public Bitmap createBitmapFromText(String printText, int textSize, int printWidth, Typeface typeface) {
		Paint paint = new Paint();
		Bitmap bitmap;
		Canvas canvas;

		paint.setTextSize(textSize);
		paint.setTypeface(typeface);

		paint.getTextBounds(printText, 0, printText.length(), new Rect());

		TextPaint textPaint = new TextPaint(paint);
		android.text.StaticLayout staticLayout = new StaticLayout(printText, textPaint, printWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);

		// Create bitmap
		bitmap = Bitmap.createBitmap(staticLayout.getWidth(), staticLayout.getHeight(), Bitmap.Config.ARGB_8888);

		// Create canvas
		canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);
		canvas.translate(0, 0);
		staticLayout.draw(canvas);

		return bitmap;
	}

	static public Bitmap createBitmapFromTextCenter(String printText, int textSize, int printWidth, Typeface typeface) {
		Paint paint = new Paint();
		Bitmap bitmap;
		Canvas canvas;

		paint.setTextSize(textSize);
		paint.setTypeface(typeface);

		paint.getTextBounds(printText, 0, printText.length(), new Rect());

		TextPaint textPaint = new TextPaint(paint);
		android.text.StaticLayout staticLayout = new StaticLayout(printText, textPaint, printWidth, Layout.Alignment.ALIGN_CENTER, 1, 0, false);

		// Create bitmap
		bitmap = Bitmap.createBitmap(staticLayout.getWidth(), staticLayout.getHeight(), Bitmap.Config.ARGB_8888);

		// Create canvas
		canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);
		canvas.translate(0, 0);
		staticLayout.draw(canvas);

		return bitmap;
	}

//	static public Bitmap createBitmapFromTextCenter(String printText, int textSize, int printWidth, Typeface typeface) {
//		Paint paint = new Paint();
//		Bitmap bitmap;
//		Canvas canvas;
//
//		paint.setTextSize(textSize);
//		paint.setTypeface(typeface);
////		paint.setTextAlign(Paint.Align.CENTER);
////
////		paint.getTextBounds(printText, 0, printText.length(), new Rect());
//
//		TextPaint textPaint = new TextPaint(paint);
//		android.text.StaticLayout staticLayout = new StaticLayout(printText, textPaint, printWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0, true);
//
//		// Create bitmap
//		bitmap = Bitmap.createBitmap(staticLayout.getWidth(), staticLayout.getHeight(), Bitmap.Config.ARGB_8888);
//
//		// Create canvas
//		canvas = new Canvas(bitmap);
//		canvas.drawColor(Color.WHITE);
////		canvas.translate(0, 0);
//		canvas.translate((canvas.getWidth() / 2) - (staticLayout.getWidth() / 2), (canvas.getHeight() / 2) - ((staticLayout.getHeight() / 2)));
//		int xPos = (canvas.getWidth() / 2);
//		int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
//		//((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.
//
//		canvas.drawText(printText, xPos, yPos, textPaint);
//		staticLayout.draw(canvas);
//
//		return bitmap;
//	}

	public static void drawCenterText(String text, RectF rectF, Canvas canvas, Paint paint) {
		Paint.Align align = paint.getTextAlign();
		float x;
		float y;
		//x
		if (align == Paint.Align.LEFT) {
			x = rectF.centerX() - paint.measureText(text) / 2;
		} else if (align == Paint.Align.CENTER) {
			x = rectF.centerX();
		} else {
			x = rectF.centerX() + paint.measureText(text) / 2;
		}
		//y
		Paint.FontMetrics metrics = paint.getFontMetrics();
		float acent = Math.abs(metrics.ascent);
		float descent = Math.abs(metrics.descent);
		y = rectF.centerY() + (acent - descent) / 2f;
		canvas.drawText(text, x, y, paint);

		Log.e("ghui", "top:" + metrics.top + ",ascent:" + metrics.ascent
				+ ",dscent:" + metrics.descent + ",leading:" + metrics.leading + ",bottom" + metrics.bottom);
	}


	public static byte[] createData(StarIoExt.Emulation emulation) {
		byte[] data8 = new byte[] {(byte) 0x80, (byte)0x81, (byte)0x82, (byte)0x83, (byte)0x84, (byte)0x85, (byte)0x86,(byte) 0x87,(byte) 0x88, (byte)0x89, (byte)0x8a, (byte)0x8b, (byte)0x8c,(byte) 0x8d, (byte)0x8e,
				(byte)0x8f, 0x0a};

		ICommandBuilder builder = StarIoExt.createCommandBuilder(emulation);
		builder.beginDocument();
		builder.appendCodePage(ICommandBuilder.CodePageType.CP874);
		builder.append(data8);
		builder.appendCutPaper(ICommandBuilder.CutPaperAction.PartialCutWithFeed);
		builder.endDocument();
		return builder.getCommands(); }


	public static void PrintBitmapPlate(Context context, String portName, String portSettings, Bitmap source,Bitmap source_price,String final_price, int maxWidth, boolean compressionEnable, boolean pageModeEnable) {
		ArrayList<byte[]> list = new ArrayList<byte[]>();

//		Bitmap source_logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_title_print);
//		StarBitmap starbitmap_logo = new StarBitmap(source_logo, false, maxWidth);
		StarBitmap starbitmap = new StarBitmap(source, false, maxWidth);
		StarBitmap starbitmap_price = new StarBitmap(source_price, false, maxWidth);

		try {

//			//Logo
//			list.add(starbitmap_logo.getImageEscPosDataForPrinting(compressionEnable, pageModeEnable));

			//Branch
			list.add(new byte[] { 0x1c, 0x43, 0x00 }); // Disable Kanji mode

			list.add(new byte[] { 0x1b, 0x74, 0x11 }); // Code Page #1252 (Windows Latin-1)

			list.add(new byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22, 0x00 }); // Set horizontal tab

			list.add(new byte[] { 0x1b, 0x61, 0x01 }); // Alignment (center)

			list.add(new byte[] { 0x1d, 0x21, 0x00 });

			list.add(createCp1252("WashUp Ratchada\r\n"));

			list.add(createCp1252("Tel : 0987654321\r\n\r\n"));

			list.add(new byte[] { 0x1b, 0x69, 0x00, 0x00 });

			list.add(createCp1252("Date:16/02/2018"+"      "+"Time:17:50"+"\r\n"));

			list.add(createCp1252("--------------------------------\r\n"));

			//Plate
			list.add(new byte[] { 0x1b, 0x61, 0x01 }); // Alignment (center)
			list.add(starbitmap.getImageEscPosDataForPrinting(compressionEnable, pageModeEnable));

			//Info driver
			list.add(new byte[] { 0x1c, 0x43, 0x00 }); // Disable Kanji mode

			list.add(new byte[] { 0x1b, 0x74, 0x11 }); // Code Page #1252 (Windows Latin-1)

			list.add(new byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22, 0x00 }); // Set horizontal tab

			list.add(new byte[] { 0x1d, 0x21, 0x00 });

			list.add(new byte[] { 0x1b, 0x69, 0x00, 0x00 });

			list.add(createCp1252("Driver's name: plam\r\n"));
			list.add(createCp1252("Car Model: Cube\r\n"));
			list.add(createCp1252("Car Size: L\r\n\n"));

			list.add(createCp1252("Service Price (Bath)\r\n"));
			list.add(createCp1252("--------------------------------\r\n"));

			//Price
			list.add(starbitmap_price.getImageEscPosDataForPrinting(compressionEnable, pageModeEnable));

			//Info contact
			list.add(new byte[] { 0x1c, 0x43, 0x00 }); // Disable Kanji mode

			list.add(new byte[] { 0x1b, 0x74, 0x11 }); // Code Page #1252 (Windows Latin-1)

			list.add(new byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22, 0x00 }); // Set horizontal tab

			list.add(new byte[] { 0x1d, 0x21, 0x00 });

			list.add(new byte[] { 0x1b, 0x69, 0x00, 0x00 });

//			list.add(createCp1252("--------------------------------\r\n"));

			list.add(createCp1252("CASH                 "+final_price+"\r\n"));
			list.add(createCp1252("--------------------------------\r\n"));
			list.add(createCp1252("Est. Service Completion:\r\n"));

			list.add(createCp1252("Fri, 16 Feb 2018 19:50\r\n"));
			list.add(createCp1252("THANK YOU FOR USING OUR SERVICES\r\n"));

			list.add(new byte[] { 0x1b, 0x64, 0x02 }); // Cut
			list.add(new byte[] { 0x07 }); // Kick cash drawer

			sendCommand(context, portName, portSettings, list);

		} catch (StarIOPortException e) {
			Builder dialog = new Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage(e.getMessage());
			alert.setCancelable(false);
			alert.show();
		}
	}

	public static void PrintBitmapPrice(Context context, String portName, String portSettings, Bitmap source, int maxWidth, boolean compressionEnable, boolean pageModeEnable) {
		ArrayList<byte[]> list = new ArrayList<byte[]>();

		StarBitmap starbitmap = new StarBitmap(source, false, maxWidth);

		try {

			list.add(new byte[] { 0x1b, 0x61, 0x01 }); // Alignment (center)
			list.add(starbitmap.getImageEscPosDataForPrinting(compressionEnable, pageModeEnable));

			sendCommand(context, portName, portSettings, list);
		} catch (StarIOPortException e) {
			Builder dialog = new Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage(e.getMessage());
			alert.setCancelable(false);
			alert.show();
		}
	}

	public static boolean PrintSampleReceipt_Company_driver(Context context, String portName, String portSettings, String strPrintArea) {
		ArrayList<byte[]> list = new ArrayList<byte[]>();

		if (strPrintArea.equals("2inch (58mm)")) {

			list.add(new byte[] { 0x1c, 0x43, 0x00 }); // Disable Kanji mode

			list.add(new byte[] { 0x1b, 0x74, 0x11 }); // Code Page #1252 (Windows Latin-1)

			list.add(new byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22, 0x00 }); // Set horizontal tab

			list.add(new byte[] { 0x1d, 0x21, 0x00 });

			list.add(new byte[] { 0x1b, 0x69, 0x00, 0x00 });
//			list.add(createCp1252("--------------------------------\r\n"));

			list.add(createCp1252("Driver's name: plam\r\n"));
			list.add(createCp1252("Car Model: Cube\r\n"));
			list.add(createCp1252("Car Size: L\r\n\n"));

			list.add(createCp1252("Service Price (Bath)\r\n"));
			list.add(createCp1252("--------------------------------\r\n"));

		}

		return sendCommand(context, portName, portSettings, list);
	}

	public static boolean PrintSampleReceipt_Company_detail(Context context, String portName, String portSettings, String strPrintArea) {
		ArrayList<byte[]> list = new ArrayList<byte[]>();

		if (strPrintArea.equals("2inch (58mm)")) {

//			list.add(new byte[] { 0x1b, 0x1d, 0x74, 0x20 }); // Code Page #1252 (Windows Latin-1)
//
//			list.add(new byte[] { 0x1b, 0x1d, 0x61, 0x01 }); // Alignment (center)
//
//			list.add(new byte[] { 0x1b, 0x1d, 0x61, 0x00 }); // Alignment
//
//			list.add(new byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22, 0x00 }); // Set horizontal tab
//
//			list.add(createCp1252("---------------------------\r\n"));
//			// Character expansion
//			list.add(new byte[] { 0x1b, 0x69, 0x00, 0x01 });
//
//			list.add(createCp1252("(CASH)"+"             "+" 32,400.00\r\n"));
//
//			list.add(new byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22, 0x00 }); // Set horizontal tab
//
//			list.add(createCp1252("--------------------------------\r\n"));
//
//			list.add(createCp1252("Est. Service Completion:\r\n"));
//
//			list.add(createCp1252("Fri, 16 Feb 2018 19:50\r\n"));
//
//			list.add(createCp1252("THANK YOU FOR USING OUR SERVICES\r\n"));

			list.add(new byte[] { 0x1b, 0x1d, 0x74, 0x20 }); // Code Page #1252 (Windows Latin-1)

			list.add(new byte[] { 0x1b, 0x1d, 0x61, 0x01 }); // Alignment (center)

			list.add(new byte[] { 0x1b, 0x1d, 0x61, 0x00 }); // Alignment

			list.add(new byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22, 0x00 }); // Set horizontal tab

//			list.add(createCp1252("--------------------------------\r\n"));

			list.add(createCp1252("CASH           "+" 32,400.00\r\n"));
			list.add(createCp1252("--------------------------------\r\n"));
			list.add(createCp1252("Est. Service Completion:\r\n"));

			list.add(createCp1252("Fri, 16 Feb 2018 19:50\r\n"));
			list.add(createCp1252("THANK YOU FOR USING OUR SERVICES\r\n"));

			list.add(new byte[] { 0x1b, 0x64, 0x02 }); // Cut
			list.add(new byte[] { 0x07 }); // Kick cash drawer
		}

		return sendCommand(context, portName, portSettings, list);
	}

	/**
	 * This function shows how to read the MSR data(credit card) of a portable(ESC/POS) printer. The function first puts the printer into MSR read mode, then asks the user to swipe a credit card The function waits for a response from the user. The user can cancel MSR mode or have the printer read the card.
	 *
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress> or BT:<Device pair name>)
	 * @param portSettings
	 *     Should be portable;escpos, the port settings portable;escpos is used for portable(ESC/POS) printers
	 * @param strPrintArea
	 *     Printable area size, This should be ("2inch (58mm)" or "3inch (80mm)")
	 */
	public static boolean PrintSampleReceipt_Spanish(Context context, String portName, String portSettings, String strPrintArea) {
		ArrayList<byte[]> list = new ArrayList<byte[]>();

		if (strPrintArea.equals("2inch (58mm)")) {

			list.add(new byte[] { 0x1c, 0x43, 0x00 }); // Disable Kanji mode

			list.add(new byte[] { 0x1b, 0x74, 0x11 }); // Code Page #1252 (Windows Latin-1)

			list.add(new byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22, 0x00 }); // Set horizontal tab

			list.add(new byte[] { 0x1b, 0x61, 0x01 }); // Alignment (center)

			// list.add("[If loaded.. Logo1 goes here]\r\n".getBytes());

			// list.add(new byte[]{0x1b, 0x1c, 0x70, 0x01, 0x00, '\r', '\n'}); //Stored Logo Printing

			// Notice that we use a unicode representation because that is
			// how Java expresses these bytes as double byte unicode

			// Character expansion
			list.add(new byte[] { 0x1d, 0x21, 0x11 });

			list.add(createCp1252("BAR RESTAURANT\r\n"));

			list.add(createCp1252("EL POZO\r\n"));

			list.add(new byte[] { 0x1d, 0x21, 0x00 });

			list.add(createCp1252("C/.ROCAFORT 187\r\n"));

			list.add(createCp1252("08029 BARCELONA\r\n\r\n"));

			list.add(createCp1252("NIF :X-3856907Z\r\n"));

			list.add(createCp1252("TEL :934199465\r\n"));

			list.add(new byte[] { 0x1b, 0x61, 0x00 }); // Alignment

			list.add(createCp1252("--------------------------------\r\n"));

			list.add(createCp1252("MESA: 100 P: - FECHA: YYYY-MM-DD\r\n"));
			list.add(createCp1252("CAN P/U DESCRIPCION  SUMA\r\n"));

			list.add(createCp1252("--------------------------------\r\n"));

			list.add(createCp1252(" 4  3,00  JARRA  CERVESA   12,00\r\n"));

			list.add(createCp1252(" 1  1,60  COPA DE CERVESA   1,60\r\n"));

			list.add(createCp1252("--------------------------------\r\n"));

			list.add(createCp1252("               SUB TOTAL : 13,60\r\n"));

			list.add(new byte[] { 0x1d, 0x21, 0x01 });

			list.add(new byte[] { 0x1b, 0x61, 0x02 }); // Alignment

			list.add(createCp1252("TOTAL:     13,60 EUROS\r\n"));

			list.add(new byte[] { 0x1b, 0x61, 0x00 }); // Alignment

			list.add(new byte[] { 0x1d, 0x21, 0x00 });

			list.add(createCp1252("NO: 000018851     IVA INCLUIDO\r\n"));

			list.add(createCp1252("--------------------------------\r\n"));

			list.add(new byte[] { 0x1b, 0x61, 0x01 }); // Alignment (center)

			list.add(createCp1252("**** GRACIAS POR SU VISITA! ****\r\n"));

			list.add(new byte[] { 0x1b, 0x61, 0x00 }); // Alignment

			byte[] qrcodeByteBuffer = new byte[] {
					0x1d, 0x5a, 0x02, 0x1b, 0x5a, 0x00, 0x51, 0x04, 0x1C, 0x00,
					0x68, 0x74, 0x74, 0x70, 0x3a, 0x2f, 0x2f, 0x77, 0x77, 0x77,
					0x2e, 0x53, 0x74, 0x61, 0x72, 0x4d, 0x69, 0x63, 0x72, 0x6f,
					0x6e, 0x69, 0x63, 0x73, 0x2e, 0x63, 0x6f, 0x6d };
			list.add(qrcodeByteBuffer); // QR Code (View QR 2D Barcode code for better explanation)

			list.add("\n\n\n\n".getBytes());

		} else if (strPrintArea.equals("3inch (80mm)")) {
			list.add(new byte[] { 0x1c, 0x43, 0x00 }); // Disable Kanji mode

			list.add(new byte[] { 0x1b, 0x74, 0x11 }); // Code Page #1252 (Windows Latin-1)

			list.add(new byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22, 0x00 }); // Set horizontal tab

			list.add(new byte[] { 0x1b, 0x61, 0x01 }); // Alignment (center)

			// list.add("[If loaded.. Logo1 goes here]\r\n".getBytes());

			// list.add(new byte[]{0x1b, 0x1c, 0x70, 0x01, 0x00, '\r', '\n'}); //Stored Logo Printing

			// Notice that we use a unicode representation because that is
			// how Java expresses these bytes as double byte unicode

			// Character expansion
			list.add(new byte[] { 0x1d, 0x21, 0x11 });

			list.add(createCp1252("BAR RESTAURANT EL POZO\r\n"));

			list.add(new byte[] { 0x1d, 0x21, 0x00 });

			list.add(createCp1252("C/.ROCAFORT 187 08029 BARCELONA\r\n"));

			list.add(createCp1252("NIF :X-3856907Z  TEL :934199465\r\n"));

			list.add(createCp1252("------------------------------------------------\r\n"));

			list.add(createCp1252("MESA: 100 P: - FECHA: YYYY-MM-DD\r\n"));

			list.add(createCp1252("CAN P/U DESCRIPCION  SUMA\r\n"));

			list.add(new byte[] { 0x1b, 0x61, 0x00 }); // Alignment

			list.add(createCp1252("------------------------------------------------\r\n"));

			list.add(createCp1252(" 4    3,00      JARRA  CERVESA             12,00\r\n"));

			list.add(createCp1252(" 1    1,60      COPA DE CERVESA             1,60\r\n"));

			list.add(createCp1252("------------------------------------------------\r\n"));

			list.add(new byte[] { 0x1b, 0x61, 0x02 }); // Alignment

			list.add(createCp1252("SUB TOTAL : 13,60\r\n"));

			list.add(new byte[] { 0x1d, 0x21, 0x01 });

			list.add(createCp1252("TOTAL:     13,60 EUROS\r\n"));

			list.add(new byte[] { 0x1b, 0x61, 0x00 }); // Alignment

			list.add(new byte[] { 0x1d, 0x21, 0x00 });

			list.add(createCp1252("NO: 000018851     IVA INCLUIDO\r\n"));

			list.add(createCp1252("------------------------------------------------\r\n"));

			list.add(new byte[] { 0x1b, 0x61, 0x01 }); // Alignment (center)

			list.add(createCp1252("**** GRACIAS POR SU VISITA! ****\r\n"));

			list.add(new byte[] { 0x1b, 0x61, 0x00 }); // Alignment

			byte[] qrcodeByteBuffer = new byte[] {
					0x1d, 0x5a, 0x02, 0x1b, 0x5a, 0x00, 0x51, 0x04, 0x1C, 0x00,
					0x68, 0x74, 0x74, 0x70, 0x3a, 0x2f, 0x2f, 0x77, 0x77, 0x77,
					0x2e, 0x53, 0x74, 0x61, 0x72, 0x4d, 0x69, 0x63, 0x72, 0x6f,
					0x6e, 0x69, 0x63, 0x73, 0x2e, 0x63, 0x6f, 0x6d };
			list.add(qrcodeByteBuffer); // QR Code (View QR 2D Barcode code for better explanation)

			list.add("\n\n\n\n".getBytes());

		} else if (strPrintArea.equals("4inch (112mm)")) {
			list.add(new byte[] { 0x1c, 0x43, 0x00 }); // Disable Kanji mode

			list.add(new byte[] { 0x1b, 0x74, 0x11 }); // Code Page #1252 (Windows Latin-1)

			list.add(new byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22, 0x00 }); // Set horizontal tab

			list.add(new byte[] { 0x1b, 0x61, 0x01 }); // Alignment (center)

			// list.add("[If loaded.. Logo1 goes here]\r\n".getBytes());

			// list.add(new byte[]{0x1b, 0x1c, 0x70, 0x01, 0x00, '\r', '\n'}); //Stored Logo Printing

			// Notice that we use a unicode representation because that is
			// how Java expresses these bytes as double byte unicode

			// Character expansion
			list.add(new byte[] { 0x1d, 0x21, 0x11 });

			list.add(createCp1252("BAR RESTAURANT EL POZO\r\n"));

			list.add(new byte[] { 0x1d, 0x21, 0x00 });

			list.add(createCp1252("C/.ROCAFORT 187 08029 BARCELONA\r\n"));

			list.add(createCp1252("NIF :X-3856907Z  TEL :934199465\r\n"));

			list.add(createCp1252("---------------------------------------------------------------------\r\n"));

			list.add(createCp1252("MESA: 100 P: - FECHA: YYYY-MM-DD\r\n"));

			list.add(createCp1252("CAN P/U DESCRIPCION  SUMA\r\n"));

			list.add(new byte[] { 0x1b, 0x61, 0x00 }); // Alignment

			list.add(createCp1252("---------------------------------------------------------------------\r\n"));

			list.add(createCp1252(" 4        3,00          JARRA  CERVESA                          12,00\r\n"));

			list.add(createCp1252(" 1        1,60          COPA DE CERVESA                          1,60\r\n"));

			list.add(createCp1252("---------------------------------------------------------------------\r\n"));

			list.add(new byte[] { 0x1b, 0x61, 0x02 }); // Alignment

			list.add(createCp1252("SUB TOTAL         :     13,60\r\n"));

			list.add(new byte[] { 0x1d, 0x21, 0x01 });

			list.add(createCp1252("TOTAL    :    13,60 EUROS\r\n"));

			list.add(new byte[] { 0x1b, 0x61, 0x00 }); // Alignment

			list.add(new byte[] { 0x1d, 0x21, 0x00 });

			list.add(createCp1252("NO: 000018851                 IVA INCLUIDO\r\n"));

			list.add(createCp1252("---------------------------------------------------------------------\r\n"));

			list.add(new byte[] { 0x1b, 0x61, 0x01 }); // Alignment (center)

			list.add(createCp1252("**** GRACIAS POR SU VISITA! ****\r\n"));

			list.add(new byte[] { 0x1b, 0x61, 0x00 }); // Alignment

			byte[] qrcodeByteBuffer = new byte[] {
					0x1d, 0x5a, 0x02, 0x1b, 0x5a, 0x00, 0x51, 0x04, 0x1C, 0x00,
					0x68, 0x74, 0x74, 0x70, 0x3a, 0x2f, 0x2f, 0x77, 0x77, 0x77,
					0x2e, 0x53, 0x74, 0x61, 0x72, 0x4d, 0x69, 0x63, 0x72, 0x6f,
					0x6e, 0x69, 0x63, 0x73, 0x2e, 0x63, 0x6f, 0x6d };
			list.add(qrcodeByteBuffer); // QR Code (View QR 2D Barcode code for better explanation)

			list.add("\n\n\n\n".getBytes());

		}

		return sendCommand(context, portName, portSettings, list);
	}

	private static byte[] createShiftJIS(String inputText) {
		byte[] byteBuffer = null;

		try {
			byteBuffer = inputText.getBytes("Shift_JIS");
		} catch (UnsupportedEncodingException e) {
			byteBuffer = inputText.getBytes();
		}

		return byteBuffer;
	}

	private static byte[] createBIG5(String inputText) {
		byte[] byteBuffer = null;

		try {
			byteBuffer = inputText.getBytes("Big5");
		} catch (UnsupportedEncodingException e) {
			byteBuffer = inputText.getBytes();
		}

		return byteBuffer;
	}

	private static byte[] create874(String inputText) {
		byte[] byteBuffer = null;

		try {
			byteBuffer = inputText.getBytes("x-windows-874");
		} catch (UnsupportedEncodingException e) {
			byteBuffer = inputText.getBytes();
		}

		return byteBuffer;
	}
	
	private static byte[] createCp1252(String inputText) {
		byte[] byteBuffer = null;
		
		try {
			byteBuffer = inputText.getBytes("Windows-1252");
		} catch (UnsupportedEncodingException e) {
			byteBuffer = inputText.getBytes();
		}
		
		return byteBuffer;
	}

	private static byte[] convertFromListByteArrayTobyteArray(List<byte[]> ByteArray) {
		int dataLength = 0;
		for (int i = 0; i < ByteArray.size(); i++) {
			dataLength += ByteArray.get(i).length;
		}

		int distPosition = 0;
		byte[] byteArray = new byte[dataLength];
		for (int i = 0; i < ByteArray.size(); i++) {
			System.arraycopy(ByteArray.get(i), 0, byteArray, distPosition, ByteArray.get(i).length);
			distPosition += ByteArray.get(i).length;
		}

		return byteArray;
	}

	/*
	 * private static void checkPrinterSendToComplete(StarIOPort port) throws StarIOPortException { int timeout = 20000; long timeCount = 0; int readSize = 0; byte[] statusCommand = new byte[] { 0x1b, 0x76 }; byte[] statusReadByte = new byte[] { 0x00 };
	 * try { port.writePort(statusCommand, 0, statusCommand.length);
	 * StarPrinterStatus status = port.retreiveStatus();
	 * if (status.coverOpen) { throw new StarIOPortException("printer is cover open"); } if (status.receiptPaperEmpty) { throw new StarIOPortException("paper is empty"); } if (status.offline) { throw new StarIOPortException("printer is offline"); }
	 * long timeStart = System.currentTimeMillis();
	 * while (timeCount < timeout) { readSize = port.readPort(statusReadByte, 0, 1);
	 * if (readSize == 1) { break; }
	 * timeCount = System.currentTimeMillis() - timeStart; } } catch (StarIOPortException e) { try { try { Thread.sleep(500); } catch(InterruptedException ie) {}
	 * StarPrinterStatus status = port.retreiveStatus(); if (status.coverOpen) { throw new StarIOPortException("printer is cover open"); } if (status.receiptPaperEmpty) { throw new StarIOPortException("paper is empty"); } if (status.offline) { throw new StarIOPortException("printer is offline"); }
	 * long timeStart = System.currentTimeMillis();
	 * while (timeCount < timeout) { readSize = port.readPort(statusReadByte, 0, 1);
	 * if (readSize == 1) { break; }
	 * timeCount = System.currentTimeMillis() - timeStart; } } catch (StarIOPortException ex) { throw new StarIOPortException(ex.getMessage()); } } }
	 */

	private static boolean sendCommand(Context context, String portName, String portSettings, ArrayList<byte[]> byteList) {
		boolean result = true;
		StarIOPort port = null;
		try {
			/*
			 * using StarIOPort3.1.jar (support USB Port) Android OS Version: upper 2.2
			 */
			port = StarIOPort.getPort(portName, portSettings, 20000, context);
			/*
			 * using StarIOPort.jar Android OS Version: under 2.1 port = StarIOPort.getPort(portName, portSettings, 10000);
			 */
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			/*
			 * portable(ESC/POS) Printer Firmware Version 2.4 later, SM-S220i(Firmware Version 2.0 later)
			 * Using Begin / End Checked Block method for preventing "data detective".
			 * When sending large amounts of raster data, use Begin / End Checked Block method and adjust the value in the timeout in the "StarIOPort.getPort" in order to prevent "timeout" of the "endCheckedBlock method" while a printing.
			 * If receipt print is success but timeout error occurs(Show message which is "There was no response of the printer within the timeout period."), need to change value of timeout more longer in "StarIOPort.getPort" method. (e.g.) 10000 -> 30000When use "Begin / End Checked Block Sample Code", do comment out "query commands Sample code".
			 */

			/* Start of Begin / End Checked Block Sample code */
			StarPrinterStatus status = port.beginCheckedBlock();

			if (true == status.offline) {
				throw new StarIOPortException("A printer is offline");
			}

			byte[] commandToSendToPrinter = convertFromListByteArrayTobyteArray(byteList);
			port.writePort(commandToSendToPrinter, 0, commandToSendToPrinter.length);

			port.setEndCheckedBlockTimeoutMillis(30000);// Change the timeout time of endCheckedBlock method.
			status = port.endCheckedBlock();

			if (true == status.coverOpen) {
				throw new StarIOPortException("Printer cover is open");
			} else if (true == status.receiptPaperEmpty) {
				throw new StarIOPortException("Receipt paper is empty");
			} else if (true == status.offline) {
				throw new StarIOPortException("Printer is offline");
			}
			/* End of Begin / End Checked Block Sample code */

			/*
			 * portable(ESC/POS) Printer Firmware Version 2.3 earlier
			 * Using query commands for preventing "data detective".
			 * When sending large amounts of raster data, send query commands after writePort data for confirming the end of printing and adjust the value in the timeout in the "checkPrinterSendToComplete" method in order to prevent "timeout" of the "sending query commands" while a printing.
			 * If receipt print is success but timeout error occurs(Show message which is "There was no response of the printer within the timeout period."), need to change value of timeout more longer in "checkPrinterSendToComplete" method. (e.g.) 10000 -> 30000When use "query commands Sample code", do comment out "Begin / End Checked Block Sample Code".
			 */

			/* Start of query commands Sample code */
//			 byte[] commandToSendToPrinter = convertFromListByteArrayTobyteArray(byteList);
//			 port.writePort(commandToSendToPrinter, 0, commandToSendToPrinter.length);
//
//			 checkPrinterSendToComplete(port);
			/* End of query commands Sample code */
		} catch (StarIOPortException e) {
			result = false;
			Builder dialog = new Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage(e.getMessage());
			alert.setCancelable(false);
			alert.show();
		} finally {
			if (port != null) {
				try {
					StarIOPort.releasePort(port);
				} catch (StarIOPortException e) {
				}
			}
		}
		
		return result;
	}
}
