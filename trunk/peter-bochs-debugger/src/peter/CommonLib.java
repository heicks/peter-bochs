package peter;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.text.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class CommonLib {
	public CommonLib() {
		super();
	}

	public static boolean isNumeric(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!(s.charAt(i) >= '0' && s.charAt(i) <= '9')) {
				return false;
			}
		}
		return true;
	}

	public static void dumpByteArray(byte b[]) {
		for (int x = 0; x < b.length; x++) {
			System.out.print(b[x] + " ");
		}
		System.out.println();
	}

	public static void dumpByteArrayInHex(byte b[]) {
		for (int x = 0; x < b.length; x++) {
			System.out.print(String.format("0x%02x", b[x]) + " ");
		}
		System.out.println();
	}

	public static void centerDialog(JFrame dialog) {
		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();

		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}

		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}

		dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}

	public static void centerDialog(JDialog dialog) {
		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();

		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}

		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}

		dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}

	public static void centerDialog(JFileChooser chooser) {
		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = chooser.getSize();

		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}

		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}

		chooser.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}

	private static char numberOfFile_runningChar[] = { '-', '\\', '|', '/' };

	public static int numberOfFile(File node, boolean isSlient) {
		int x = 0;
		for (File file : node.listFiles()) {
			if (!isSlient) {
				System.out.print("\r" + numberOfFile_runningChar[x % numberOfFile_runningChar.length]);
			}
			if (file.isDirectory()) {
				x += numberOfFile(file, isSlient);
			} else {
				x++;
			}
		}
		System.out.print("\r \r");
		return x;
	}

	public static int getNumberOfFileAndDirectory(String file) {
		return getNumberOfFileAndDirectory(new File(file));
	}

	public static int getNumberOfFileAndDirectory(File file) {
		if (file.isFile()) {
			return -1;
		} else {
			int num = file.listFiles().length;
			for (int x = 0; x < file.listFiles().length; x++) {
				if (file.listFiles()[x].isDirectory()) {
					num += getNumberOfFileAndDirectory(file.listFiles()[x]);
				}
			}
			return num;
		}
	}

	public static int getNumberOfFile(String file) {
		return getNumberOfFile(new File(file));
	}

	public static int getNumberOfFile(File file) {
		if (file.isFile()) {
			return -1;
		} else {
			int num = 0;
			for (int x = 0; x < file.listFiles().length; x++) {
				if (file.listFiles()[x].isDirectory()) {
					num += getNumberOfFile(file.listFiles()[x]);
				} else if (file.listFiles()[x].isFile()) {
					num++;
				}
			}
			return num;
		}
	}

	public static int getNumberOfDirectory(String file) {
		return getNumberOfDirectory(new File(file));
	}

	public static int getNumberOfDirectory(File file) {
		if (file.isFile()) {
			return -1;
		} else {
			int num = 0;
			for (int x = 0; x < file.listFiles().length; x++) {
				if (file.listFiles()[x].isDirectory()) {
					num += getNumberOfDirectory(file.listFiles()[x]);
					num++;
				}
			}
			return num;
		}
	}

	public static byte readFileByte(File file, long offset) {
		return readFileByte(file.getAbsolutePath(), offset);
	}

	public static byte readFileByte(String filepath, long offset) {
		try {
			RandomAccessFile br = new RandomAccessFile(filepath, "r");
			br.seek(offset);
			byte b = br.readByte();
			br.close();
			return b;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public static boolean deleteDirectory(String path) {
		if (new File(path).exists()) {
			File[] files = new File(path).listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i].getPath());
				} else {
					files[i].delete();
				}
			}
		}
		return (new File(path).delete());
	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i].getPath());
				} else {
					files[i].delete();
				}
			}
		}
		return path.delete();
	}

	public static byte[] readFile(File file, long offset, int size) {
		return readFile(file.getAbsolutePath(), offset, size);
	}

	public static byte[] readFile(String filepath, long offset, int size) {
		try {
			RandomAccessFile br = new RandomAccessFile(filepath, "r");
			byte data[] = new byte[size];
			br.seek(offset);
			br.read(data);
			br.close();
			return data;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static int[] readFileUnsigned(File file, long offset, int size) {
		return readFileUnsigned(file.getAbsolutePath(), offset, size);
	}

	public static int[] readFileUnsigned(String filepath, long offset, int size) {
		try {
			RandomAccessFile br = new RandomAccessFile(filepath, "r");
			int data[] = new int[size];
			br.seek(offset);
			for (int x = 0; x < size; x++) {
				data[x] = br.readUnsignedByte();
			}
			br.close();
			return data;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Object[] reverseArray(Object objects[]) {
		try {
			List l = Arrays.asList(objects);
			java.util.Collections.reverse(l);
			/** and if you must back to the array again */
			objects = l.toArray();
			return objects;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static long convertFilesize(String filesize) {
		if (filesize.length() == 0) {
			return 0;
		} else if (filesize.length() == 1) {
			return Long.parseLong(filesize);
		} else if (filesize.substring(filesize.length() - 1).toLowerCase().equals("t")) {
			try {
				return Long.parseLong(filesize.substring(0, filesize.length() - 1)) * 1024 * 1024 * 1024 * 1024;
			} catch (Exception ex) {
				return -1;
			}
		} else if (filesize.substring(filesize.length() - 2).toLowerCase().equals("tb")) {
			try {
				return Long.parseLong(filesize.substring(0, filesize.length() - 2)) * 1024 * 1024 * 1024 * 1024;
			} catch (Exception ex) {
				return -1;
			}
		} else if (filesize.substring(filesize.length() - 1).toLowerCase().equals("g")) {
			try {
				return Long.parseLong(filesize.substring(0, filesize.length() - 1)) * 1024 * 1024 * 1024;
			} catch (Exception ex) {
				return -1;
			}
		} else if (filesize.substring(filesize.length() - 2).toLowerCase().equals("gb")) {
			try {
				return Long.parseLong(filesize.substring(0, filesize.length() - 2)) * 1024 * 1024 * 1024;
			} catch (Exception ex) {
				return -1;
			}
		} else if (filesize.substring(filesize.length() - 1).toLowerCase().equals("m")) {
			try {
				return Long.parseLong(filesize.substring(0, filesize.length() - 1)) * 1024 * 1024;
			} catch (Exception ex) {
				return -1;
			}
		} else if (filesize.substring(filesize.length() - 2).toLowerCase().equals("mb")) {
			try {
				return Long.parseLong(filesize.substring(0, filesize.length() - 2)) * 1024 * 1024;
			} catch (Exception ex) {
				return -1;
			}
		} else if (filesize.substring(filesize.length() - 1).toLowerCase().equals("k")) {
			try {
				return Long.parseLong(filesize.substring(0, filesize.length() - 1)) * 1024;
			} catch (Exception ex) {
				return -1;
			}
		} else if (filesize.substring(filesize.length() - 2).toLowerCase().equals("kb")) {
			try {
				return Long.parseLong(filesize.substring(0, filesize.length() - 2)) * 1024;
			} catch (Exception ex) {
				return -1;
			}
		} else {
			try {
				return Long.parseLong(filesize);
			} catch (Exception ex) {
				return -1;
			}
		}
	}

	public static String convertFilesize(long filesize) {
		if (filesize < 1024) {
			return filesize + " bytes";
		} else if (filesize >= 1024 && filesize < 1024 * 1024) {
			return filesize / 1024 + " KB";
		} else {
			return filesize / 1024 / 1024 + " MB";
		}
	}

	public static void printSecond(long second) {
		if (second > 60 * 60 * 24 * 365) {
			System.out.println(second / (60 * 60 * 24 * 365) + " years, " + second + " seconds");
		} else if (second > 60 * 60 * 24) {
			System.out.println(second / (60 * 60 * 24) + " days, " + second + " seconds");
		} else if (second > 60 * 60) {
			System.out.println(second / (60 * 60) + " hours, " + second + " seconds");
		} else if (second > 60) {
			System.out.println(second / (60) + " minutes, " + second + " seconds");
		} else {
			System.out.println(second + " seconds");
		}
	}

	public static String getRelativePath(File file) {
		return getRelativePath(file.getAbsolutePath());
	}

	public static String getRelativePath(String file) {
		return file.substring(System.getProperty("user.dir").length());
	}

	public static void highlightUsingRegularExpression(JTextComponent textComp, String regularExpression, Color color) {
		removeHighlights(textComp);
		Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(color);

		try {
			Highlighter hilite = textComp.getHighlighter();
			Document doc = textComp.getDocument();
			String text = doc.getText(0, doc.getLength());
			int pos = 0;

			String inputStr = textComp.getText();
			Pattern pattern = Pattern.compile(regularExpression);
			Matcher matcher = pattern.matcher(inputStr);
			boolean matchFound = matcher.find();
			while (matchFound) {
				// System.out.println(matcher.start() + "-" +
				// matcher.end());
				for (int i = 0; i <= matcher.groupCount(); i++) {
					String groupStr = matcher.group(i);
					// System.out.println(i + ":" +
					// groupStr);
				}
				hilite.addHighlight(matcher.start(), matcher.end(), myHighlightPainter);
				if (matcher.end() + 1 <= inputStr.length()) {
					matchFound = matcher.find(matcher.end());
				} else {
					break;
				}
			}
		} catch (BadLocationException e) {
		}
	}

	public static void highlight(JTextComponent textComp, String pattern, Color color) {
		// First remove all old highlights
		removeHighlights(textComp);
		Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(color);

		try {
			Highlighter hilite = textComp.getHighlighter();
			Document doc = textComp.getDocument();
			String text = doc.getText(0, doc.getLength());
			int pos = 0;

			// Search for pattern
			while ((pos = text.indexOf(pattern, pos)) >= 0) {
				// Create highlighter using private painter and
				// apply around
				// pattern
				hilite.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
				pos += pattern.length();
			}
		} catch (BadLocationException e) {
		}
	}

	// Removes only our private highlights
	public static void removeHighlights(JTextComponent textComp) {
		Highlighter hilite = textComp.getHighlighter();
		Highlighter.Highlight[] hilites = hilite.getHighlights();

		for (int i = 0; i < hilites.length; i++) {
			if (hilites[i].getPainter() instanceof MyHighlightPainter) {
				hilite.removeHighlight(hilites[i]);
			}
		}
	}

	public static void printFilesize(long fileOffset, long filesize) {
		System.out.print("\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r");
		if (fileOffset < 1024) {
			System.out.print(fileOffset + " /" + filesize + " bytes, " + fileOffset * 100 / filesize + "%            ");
		} else if (fileOffset >= 1024 & fileOffset < 1024 * 1024) {
			System.out.print(fileOffset / 1024 + " /" + filesize / 1024 + " KB, " + fileOffset * 100 / filesize + "%          ");
		} else if (fileOffset >= 1024 * 1024) {
			System.out.print(fileOffset / 1024 / 1024 + "MB (" + fileOffset / 1024 + " KB) / " + filesize / 1024 / 1024 + " MB, " + fileOffset * 100 / filesize + "%         ");
		}
	}

	public static String trimByteZero(String str) {
		if (str.indexOf(0) == -1) {
			return str;
		} else {
			return str.substring(0, str.indexOf(0));
		}
	}

	static class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
		public MyHighlightPainter(Color color) {
			super(color);
		}
	}

	public static void writeFile(String filepath, String content) {
		try {

			FileWriter fstream = new FileWriter(filepath);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void appendFile(String filepath, String content) {
		try {
			FileOutputStream file = new FileOutputStream(filepath, true);
			DataOutputStream out = new DataOutputStream(file);
			out.writeBytes(content);
			out.flush();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String empty(Object str) {
		if (str == null) {
			return "";
		} else {
			return str.toString();
		}
	}

	public static Long hex2decimal(String s) {
		s = s.trim().toLowerCase();
		if (s.startsWith("0x")) {
			return Long.parseLong(s.substring(2), 16);
		} else {
			return Long.parseLong(s, 16);
		}
	}

	public static Long string2decimal(String s) {
		s = s.trim().toLowerCase();
		if (s.startsWith("0x")) {
			return Long.parseLong(s.substring(2), 16);
		} else {
			return Long.parseLong(s, 10);
		}
	}

	public static byte[] hexStringToByteArray(String s) {
		if (s.length() % 2 == 1) {
			s = "0" + s;
		}
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static byte[] integerStringToByteArray(String s) {
		if (s.length() % 2 == 1) {
			s = "0" + s;
		}
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 10) * 10) + Character.digit(s.charAt(i + 1), 10));
		}
		return data;
	}

	public static byte[] stringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len];
		for (int i = 0; i < len; i++) {
			data[i] = (byte) s.charAt(i);
		}
		return data;
	}

	public static void putShort(byte b[], short s, int index) {
		b[index] = (byte) (s >> 0);
		b[index + 1] = (byte) (s >> 8);
	}

	public static short getShort(byte b0, byte b1) {
		return (short) ((b0 << 0) | (b1 & 0xff) << 8);
	}

	public static short getShort(byte[] bb, int index) {
		return (short) (((bb[index + 0] & 0xff) << 0) | ((bb[index + 1] & 0xff) << 8));
	}

	public static short getShort(int[] bb, int index) {
		return (short) (((bb[index + 0] & 0xff) << 0) | ((bb[index + 1] & 0xff) << 8));
	}

	// ///////////////////////////////////////////////////////
	public static void putInt(byte[] bb, int x, int index) {
		bb[index + 0] = (byte) (x >> 0);
		bb[index + 1] = (byte) (x >> 8);
		bb[index + 2] = (byte) (x >> 16);
		bb[index + 3] = (byte) (x >> 24);
	}

	public static int getInt(byte[] bb, int index) {
		return (int) ((((bb[index + 0] & 0xff) << 0) | ((bb[index + 1] & 0xff) << 8) | ((bb[index + 2] & 0xff) << 16) | ((bb[index + 3] & 0xff) << 24)));
	}

	public static int getInt(int[] bb, int index) {
		return (int) ((((bb[index + 0] & 0xff) << 0) | ((bb[index + 1] & 0xff) << 8) | ((bb[index + 2] & 0xff) << 16) | ((bb[index + 3] & 0xff) << 24)));
	}

	public static int getInt(byte b0, byte b1, byte b2, byte b3) {
		return (int) ((((b0 & 0xff) << 0) | ((b1 & 0xff) << 8) | ((b2 & 0xff) << 16) | ((b3 & 0xff) << 24)));
	}

	public static void putLong(byte[] bb, long x, int index) {
		bb[index + 0] = (byte) (x >> 0);
		bb[index + 1] = (byte) (x >> 8);
		bb[index + 2] = (byte) (x >> 16);
		bb[index + 3] = (byte) (x >> 24);
		bb[index + 4] = (byte) (x >> 32);
		bb[index + 5] = (byte) (x >> 40);
		bb[index + 6] = (byte) (x >> 48);
		bb[index + 7] = (byte) (x >> 56);
	}

	public static long getLong(byte[] bb, int index) {
		return ((((long) bb[index + 0] & 0xff) << 0) | (((long) bb[index + 1] & 0xff) << 8) | (((long) bb[index + 2] & 0xff) << 16) | (((long) bb[index + 3] & 0xff) << 24)
				| (((long) bb[index + 4] & 0xff) << 32) | (((long) bb[index + 5] & 0xff) << 40) | (((long) bb[index + 6] & 0xff) << 48) | (((long) bb[index + 7] & 0xff) << 56));
	}

	public static long getLong(int[] bb, int index) {
		return ((((long) bb[index + 0] & 0xff) << 0) | (((long) bb[index + 1] & 0xff) << 8) | (((long) bb[index + 2] & 0xff) << 16) | (((long) bb[index + 3] & 0xff) << 24)
				| (((long) bb[index + 4] & 0xff) << 32) | (((long) bb[index + 5] & 0xff) << 40) | (((long) bb[index + 6] & 0xff) << 48) | (((long) bb[index + 7] & 0xff) << 56));
	}

	public static long getLong(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7) {
		return ((((long) b0 & 0xff) << 0) | (((long) b1 & 0xff) << 8) | (((long) b2 & 0xff) << 16) | (((long) b3 & 0xff) << 24) | (((long) b4 & 0xff) << 32) | (((long) b5 & 0xff) << 40)
				| (((long) b6 & 0xff) << 48) | (((long) b7 & 0xff) << 56));
	}

	public static long getLong(int b0, int b1, int b2, int b3, int b4, int b5, int b6, int b7) {
		return ((((long) b0 & 0xff) << 0) | (((long) b1 & 0xff) << 8) | (((long) b2 & 0xff) << 16) | (((long) b3 & 0xff) << 24) | (((long) b4 & 0xff) << 32) | (((long) b5 & 0xff) << 40)
				| (((long) b6 & 0xff) << 48) | (((long) b7 & 0xff) << 56));
	}

	public static long getLong(long b0, long b1, long b2, long b3, long b4, long b5, long b6, long b7) {
		return ((((long) b0 & 0xff) << 0) | (((long) b1 & 0xff) << 8) | (((long) b2 & 0xff) << 16) | (((long) b3 & 0xff) << 24) | (((long) b4 & 0xff) << 32) | (((long) b5 & 0xff) << 40)
				| (((long) b6 & 0xff) << 48) | (((long) b7 & 0xff) << 56));

	}

	public static long getBit(long value, int bitNo) {
		return value >> bitNo & 1;
	}

	public static boolean saveImage(Container container, File file) {
		final BufferedImage image = new BufferedImage(container.getWidth(), container.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics gr = image.getGraphics();
		container.printAll(gr);
		gr.dispose();
		try {
			ImageIO.write(image, "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void exportRegisterHistory(File file) {
		Workbook wb = new HSSFWorkbook();
		exportRegisterHistory(file, wb);
		// Write the output to a file
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(file);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void exportRegisterHistory(File file, Workbook wb) {
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = wb.createSheet("Registers");

		// Create a row and put some cells in it. Rows are 0 based.
		String columnNames[] = { "time", "cs", "eip", "ds", "es", "fs", "gs", "ss", "eflags", "eax", "ebx", "ecx", "edx", "esi", "edi", "ebp", "esp", "cr0", "cr2", "cr3", "cr4", "gdtr", "ldtr",
				"idtr", "tr" };
		Vector data[] = { AllRegisters.time, AllRegisters.cs, AllRegisters.eip, AllRegisters.ds, AllRegisters.es, AllRegisters.fs, AllRegisters.gs, AllRegisters.ss, AllRegisters.eflags,
				AllRegisters.eax, AllRegisters.ebx, AllRegisters.ecx, AllRegisters.edx, AllRegisters.esi, AllRegisters.edi, AllRegisters.ebp, AllRegisters.esp, AllRegisters.cr0, AllRegisters.cr2,
				AllRegisters.cr3, AllRegisters.cr4, AllRegisters.gdtr, AllRegisters.ldtr, AllRegisters.idtr, AllRegisters.tr };
		Row row = sheet.createRow(0);

		Cell cell;
		for (int x = 0; x < columnNames.length; x++) {
			cell = row.createCell(x);
			cell.setCellValue(columnNames[x]);
		}

		// data
		for (int x = 0; x < AllRegisters.time.size(); x++) {
			row = sheet.createRow(x + 1);
			for (int y = 0; y < data.length; y++) {
				cell = row.createCell(y);
				if (data[y].get(x).getClass() == Long.class) {
					cell.setCellValue("0x" + Long.toHexString((Long) data[y].get(x)));
				} else {
					CellStyle cellStyle = wb.createCellStyle();
					cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yy/m/d h:mm:ss"));
					cell.setCellValue(data[y].get(x).toString());
					cell.setCellStyle(cellStyle);
				}
			}
		}
	}

	public static void exportTableModelToExcel(File file, TableModel model, String sheetName) {
		Workbook wb = new HSSFWorkbook();// Write the output to a file
		exportTableModelToExcel(file, model, sheetName, wb);
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(file);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void exportTableModelToExcel(File file, TableModel model, String sheetName, Workbook wb) {
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = wb.createSheet(sheetName);

		// column header
		Row row = sheet.createRow(0);
		Cell cell;
		for (int x = 0; x < model.getColumnCount(); x++) {
			cell = row.createCell(x);
			cell.setCellValue(model.getColumnName(x));
		}

		// data
		for (int y = 0; y < model.getRowCount(); y++) {
			row = sheet.createRow(y + 1);
			for (int x = 0; x < model.getColumnCount(); x++) {
				cell = row.createCell(x);
				cell.setCellValue(model.getValueAt(y, x).toString());
			}
		}
	}

	public static long getValue(long l, int startBit, int endBit) {
		// System.out.println(l);
		l = l >> startBit;
		// System.out.println(l);
		// System.out.println(new Double(Math.pow(2, (endBit - startBit + 1)) -
		// 1).longValue());
		return l & new Double(Math.pow(2, (endBit - startBit + 1)) - 1).longValue();
	}

	public static long getPhysicalAddress(long cr3, long linearAddress) {
		long pdNo = getValue(linearAddress, 22, 31);
		long ptNo = getValue(linearAddress, 12, 21);
		long offset = getValue(linearAddress, 0, 11);
		System.out.println("pd=" + pdNo);
		System.out.println("pt=" + ptNo);

		long pdAddr = cr3 + pdNo;
		System.out.println("pdAddr=" + Long.toHexString(pdAddr));
		Application.commandReceiver.clearBuffer();
		Application.sendCommand("xp /8bx " + pdAddr);
		String result = Application.commandReceiver.getCommandResult(String.format("%08x", pdAddr));
		byte bytes[] = new byte[8];
		String[] b = result.replaceFirst("^.*:", "").split("\t");
		for (int y = 1; y <= 8; y++) {
			bytes[y - 1] = (byte) (long) CommonLib.string2decimal(b[y]);
		}
		Long pde = getValue(CommonLib.getLong(bytes, 0), 12, 31) << 12;
		System.out.println("pde=" + Long.toHexString(pde));

		long ptAddr = pde << 10 + ptNo;
		System.out.println("ptAddr=" + Long.toHexString(ptAddr));
		Application.commandReceiver.clearBuffer();
		Application.sendCommand("xp /8bx " + ptAddr);
		result = Application.commandReceiver.getCommandResult(String.format("%08x", ptAddr));
		bytes = new byte[8];
		b = result.replaceFirst("^.*:", "").split("\t");
		for (int y = 1; y <= 8; y++) {
			bytes[y - 1] = (byte) (long) CommonLib.string2decimal(b[y]);
		}
		Long pageAddr = getValue(CommonLib.getLong(bytes, 0), 12, 31) << 12;
		System.out.println("pageAddr=" + Long.toHexString(pageAddr));

		return pageAddr + offset;
	}

	public static long getLongFromBochs(long address) {
		Application.commandReceiver.clearBuffer();
		Application.sendCommand("xp /8bx " + address);
		String result = Application.commandReceiver.getCommandResult(String.format("%08x", address));
		byte bytes[] = new byte[8];
		String[] b = result.replaceFirst("^.*:", "").split("\t");
		for (int y = 1; y <= 8; y++) {
			bytes[y - 1] = (byte) (long) CommonLib.string2decimal(b[y]);
		}
		return CommonLib.getLong(bytes, 0);
	}

	public static byte[] getMemoryFromBochs(long address, int totalByte) {
		byte bytes[] = new byte[totalByte];

		Application.commandReceiver.clearBuffer();
		Application.sendCommand("xp /" + totalByte + "bx " + address);

		if (totalByte > 0) {
			float totalByte2 = totalByte - 1;
			totalByte2 = totalByte2 / 8;
			int totalByte3 = (int) Math.floor(totalByte2);
			String realEndAddressStr;
			String realStartAddressStr;
			long realStartAddress = address;
			realStartAddressStr = String.format("%08x", realStartAddress);
			long realEndAddress = realStartAddress + totalByte3 * 8;
			realEndAddressStr = String.format("%08x", realEndAddress);
			// System.out.println(realStartAddressStr);
			// System.out.println(realEndAddressStr);
			String result = Application.commandReceiver.getCommandResult(realStartAddressStr, realEndAddressStr);
			// System.out.println(result);
			String[] lines = result.split("\n");

			int offset = 0;
			// System.out.println(result);

			for (int y = 0; y < lines.length; y++) {
				String[] b = lines[y].replaceFirst("^.*:", "").split("\t");
				// System.out.println(lines[y]);
				for (int x = 1; x < b.length && x < 200; x++) {
					// System.out.print(offset + " ");
					bytes[offset] = (byte) Long.parseLong(b[x].substring(2).trim(), 16);
					offset++;
				}

			}

		}
		return bytes;
	}
}