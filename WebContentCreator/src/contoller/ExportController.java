package contoller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.LinkedList;

import model.Element;
import model.Page;
import model.WCCModel;
import view.WCCView;

class ExportController implements Runnable {

	private static final File staticFolder = new File(WCCModel.templateFolder.getAbsolutePath()+"\\static");
	private static final File indexFile = new File(WCCModel.templateFolder.getAbsolutePath()+"\\dynamic\\index.html");
	private static final File menuItemFile = new File(WCCModel.templateFolder.getAbsolutePath()+"\\dynamic\\menuitem.html");
	private static final File pageFile = new File(WCCModel.templateFolder.getAbsolutePath()+"\\dynamic\\page.html");
	private static final File headerFile = new File(WCCModel.templateFolder.getAbsolutePath()+"\\dynamic\\header.html");
	private static final File subheaderFile = new File(WCCModel.templateFolder.getAbsolutePath()+"\\dynamic\\subheader.html");
	private static final File textFile = new File(WCCModel.templateFolder.getAbsolutePath()+"\\dynamic\\textcontent.html");
	private static final File imageFile = new File(WCCModel.templateFolder.getAbsolutePath()+"\\dynamic\\image.html");

	private static final String menuItems = "<!--menuitems-->";
	private static final String firstFilename = "<!--firstFilename-->";
	private static final String filename = "<!--filename-->";
	private static final String name = "<!--name-->";
	private static final String elements = "<!--elements-->";
	private static final String value = "<!--value-->";
	
	private MessageDigest masterDigest;
	private MessageDigest singleDigest;
	private LinkedList<String> versionList;

	public void run() {
		try {
			delete(WCCModel.exportFolder);
			WCCModel.exportFolder.mkdirs();
			exportStatic("");
			exportIndexFile();
			exportPages();
			createVersionsFile();
			
			WCCView.showInformationMessage("Exportieren erfolgreich angeschlossen.");
		} catch(Exception e) {
			e.printStackTrace();
			WCCView.showErrorMessage("Error occurred during export: \n\n" + e.getMessage());
		} finally {
			WCCController.enableExportActions(true);
		}
	}

	public static void delete(File f) {
		if(f.exists()) {
			if(f.isDirectory()) {
				for(File file : f.listFiles()) {
					delete(file);
				}
			}
			f.delete();
		}
	}

	private void exportStatic(String arg) throws Exception {
		for(File f : new File(staticFolder.getAbsolutePath()+arg).listFiles()) {
			if(f.isDirectory()) {
				File newF = new File(WCCModel.exportFolder.getAbsolutePath()+arg+"\\"+f.getName());
				newF.mkdir();
				exportStatic(arg+"\\"+f.getName());
			} else {
				FileReader r = new FileReader(f);
				FileWriter w = new FileWriter(WCCModel.exportFolder.getAbsolutePath()+arg+"\\"+f.getName());
				for(int i=0; (i=r.read())!=-1; w.write(i));
				r.close();
				w.close();
			}
		}
	}

	private void exportIndexFile() throws Exception {
		File f = new File(WCCModel.exportFolder.getAbsolutePath()+"\\index.html");
		f.createNewFile();
		PrintWriter pw = new PrintWriter(f);
		BufferedReader br = new BufferedReader(new FileReader(indexFile));
		String line;
		while((line=br.readLine())!=null) {
			if(line.contains(menuItems)) {
				pw.print(line.substring(0, line.indexOf(menuItems)));
				String menuitemText = getMenuItemText();
				for(Page p : WCCModel.getDataStorage()) {
					pw.print(menuitemText.replace(filename, p.getFilename()+".html").replace(name, p.getName()));
				}
				pw.println(line.substring(line.indexOf(menuItems)+menuItems.length()));
			} else if(line.contains(firstFilename)) {
				pw.print(line.substring(0, line.indexOf(firstFilename)));
				pw.print(WCCModel.getDataStorage().get(0).getFilename()+".html");
				pw.println(line.substring(line.indexOf(firstFilename)+firstFilename.length()));
			} else {
				pw.println(line);
			}
		}
		br.close();
		pw.flush();
		pw.close();
	}

	private String getMenuItemText() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(menuItemFile));
		String res = "";
		for(String s; (s=br.readLine())!=null; res+=s+"\n");
		br.close();
		return res;
	}

	private void exportPages() throws Exception {
		File pagesFolder = new File(WCCModel.exportFolder.getAbsolutePath()+"\\pages");
		pagesFolder.mkdir();
		File imagesFolder = new File(WCCModel.exportFolder.getAbsolutePath()+"\\images");
		imagesFolder.mkdir();
		for(Page p : WCCModel.getDataStorage()) {
			File f = new File(WCCModel.exportFolder.getAbsolutePath()+"\\pages\\"+p.getFilename()+".html");
			f.createNewFile();
			PrintWriter pw = new PrintWriter(f);
			BufferedReader br = new BufferedReader(new FileReader(pageFile));
			String line;
			while((line=br.readLine())!=null) {
				if(line.contains(name)) {
					pw.print(line.substring(0, line.indexOf(name)));
					pw.println(p.getName());
					pw.println(line.substring(line.indexOf(name)+name.length()));
				} else if(line.contains(elements)) {
					pw.print(line.substring(0, line.indexOf(elements)));
					for(Element e : p) {
						if(e.isHeader()) {
							pw.print(getHeaderText().replace(value, HtmlEscape.escape(e.getValue())));
						} else if(e.isSubheader()) {
							pw.print(getSubheaderText().replace(value, HtmlEscape.escape(e.getValue())));
						} else if(e.isImage()) {
							File imageFile = new File(e.getValue());
							File newImageFile = new File(WCCModel.exportFolder.getAbsolutePath()+"\\images\\"+imageFile.getName());
							pw.print(getImageText().replace(value, imageFile.getName()));
							if(!newImageFile.exists()) {
								InputStream is = new FileInputStream(imageFile);
								OutputStream os = new FileOutputStream(newImageFile);
								byte[] buffer = new byte[1024];
								for(int i; (i = is.read(buffer)) > 0; os.write(buffer, 0, i));
								is.close();
								os.flush();
								os.close();
							}
						} else {
							pw.print(getTextText().replace(value, HtmlEscape.escape(e.getValue())));
						}
					}
					pw.println(line.substring(line.indexOf(elements)+elements.length()));
				} else {
					pw.println(line);
				}
			}
			br.close();
			pw.flush();
			pw.close();
		}
	}

	private String getHeaderText() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(headerFile));
		String res = "";
		for(String s; (s=br.readLine())!=null; res+=s+"\n");
		br.close();
		return res;
	}

	private String getSubheaderText() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(subheaderFile));
		String res = "";
		for(String s; (s=br.readLine())!=null; res+=s+"\n");
		br.close();
		return res;
	}

	private String getTextText() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(textFile));
		String res = "";
		for(String s; (s=br.readLine())!=null; res+=s+"\n");
		br.close();
		return res;
	}

	private String getImageText() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(imageFile));
		String res = "";
		for(String s; (s=br.readLine())!=null; res+=s+"\n");
		br.close();
		return res;
	}

	private void createVersionsFile() throws Exception {
		//TODO
		masterDigest = MessageDigest.getInstance("MD5");
		singleDigest = MessageDigest.getInstance("MD5");
		versionList = new LinkedList<>();
		checkFile("");
		versionList.addFirst(getChecksum(masterDigest.digest()));
		File versionsFile = new File(WCCModel.exportFolder+"\\versions.txt");
		versionsFile.createNewFile();
		PrintWriter pw = new PrintWriter(versionsFile);
		for(String s : versionList) {
			pw.println(s);
		}
		pw.close();
	}
	
	private void checkFile(String arg) throws Exception {
		File f = new File(WCCModel.exportFolder.getAbsolutePath()+"\\"+arg);
		if(f.isDirectory()) {
			for(File file : f.listFiles()) {
				checkFile(arg+"\\"+file.getName());
			}
		} else {
			versionList.add(arg+":"+getChecksum(createChecksum(f)));
		}
	}
	
	private String getChecksum(byte[] checksum) {
		String res = "";
		for(int i=0; i<checksum.length; ++i) {
			res += Integer.toString((checksum[i] & 0xff)+0x100, 16).substring(1);
		}
		return res;
	}
	
	private byte[] createChecksum(File fileToCheck) throws Exception {
		InputStream is = new FileInputStream(fileToCheck);
		byte[] buffer = new byte[1024];
		for(int i; (i = is.read(buffer)) > 0; singleDigest.update(buffer, 0, i));
		is.close();
		byte[] res = singleDigest.digest();
		masterDigest.update(res, 0, res.length-1);
		return res;
	}




	/**
	 * HtmlEscape in Java, which is compatible with utf-8
	 * @author Ulrich Jensen, http://www.htmlescape.net
	 * Feel free to get inspired, use or steal this code and use it in your
	 * own projects.
	 * License:
	 * You have the right to use this code in your own project or publish it
	 * on your own website.
	 * If you are going to use this code, please include the author lines.
	 * Use this code at your own risk. The author does not warrent or assume any
	 * legal liability or responsibility for the accuracy, completeness or usefullness of
	 * this program code.
	 */

	static class HtmlEscape {

		private static char[] hex={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

		/**
		 * Method for html escaping a String, for use in a textarea
		 * @param original The String to escape
		 * @return The escaped String
		 */
		public static String escapeTextArea(String original)
		{
			return escapeSpecial(escapeTags(original));    
		}

		/**
		 * Normal escape function, for Html escaping Strings
		 * @param original The original String
		 * @return The escape String
		 */
		public static String escape(String original)
		{
			return escapeSpecial(escapeBr(escapeTags(original)));
		}

		public static String escapeTags(String original)
		{
			if(original==null) return "";
			StringBuffer out=new StringBuffer("");
			char[] chars=original.toCharArray();
			for(int i=0;i<chars.length;i++)
			{
				boolean found=true;
				switch(chars[i])
				{
				case 60:out.append("&lt;"); break; //<
				case 62:out.append("&gt;"); break; //>
				case 34:out.append("&quot;"); break; //"
				default:found=false;break;
				}
				if(!found) out.append(chars[i]);

			}
			return out.toString();

		}

		public static String escapeBr(String original)
		{
			if(original==null) return "";
			StringBuffer out=new StringBuffer("");
			char[] chars=original.toCharArray();
			for(int i=0;i<chars.length;i++)
			{
				boolean found=true;
				switch(chars[i])
				{
				case '\n': out.append("<br/>"); break; //newline
				case '\r': break;
				default:found=false;break;
				}
				if(!found) out.append(chars[i]);

			}
			return out.toString();
		}

		public static String escapeSpecial(String original)
		{
			if(original==null) return "";
			StringBuffer out=new StringBuffer("");
			char[] chars=original.toCharArray();
			for(int i=0;i<chars.length;i++)
			{
				boolean found=true;
				switch(chars[i]) {
				case 38:out.append("&amp;"); break; //&
				case 198:out.append("&AElig;"); break; //Æ
				case 193:out.append("&Aacute;"); break; //Á
				case 194:out.append("&Acirc;"); break; //Â
				case 192:out.append("&Agrave;"); break; //À
				case 197:out.append("&Aring;"); break; //Å
				case 195:out.append("&Atilde;"); break; //Ã
				case 196:out.append("&Auml;"); break; //Ä
				case 199:out.append("&Ccedil;"); break; //Ç
				case 208:out.append("&ETH;"); break; //Ð
				case 201:out.append("&Eacute;"); break; //É
				case 202:out.append("&Ecirc;"); break; //Ê
				case 200:out.append("&Egrave;"); break; //È
				case 203:out.append("&Euml;"); break; //Ë
				case 205:out.append("&Iacute;"); break; //Í
				case 206:out.append("&Icirc;"); break; //Î
				case 204:out.append("&Igrave;"); break; //Ì
				case 207:out.append("&Iuml;"); break; //Ï
				case 209:out.append("&Ntilde;"); break; //Ñ
				case 211:out.append("&Oacute;"); break; //Ó
				case 212:out.append("&Ocirc;"); break; //Ô
				case 210:out.append("&Ograve;"); break; //Ò
				case 216:out.append("&Oslash;"); break; //Ø
				case 213:out.append("&Otilde;"); break; //Õ
				case 214:out.append("&Ouml;"); break; //Ö
				case 222:out.append("&THORN;"); break; //Þ
				case 218:out.append("&Uacute;"); break; //Ú
				case 219:out.append("&Ucirc;"); break; //Û
				case 217:out.append("&Ugrave;"); break; //Ù
				case 220:out.append("&Uuml;"); break; //Ü
				case 221:out.append("&Yacute;"); break; //Ý
				case 225:out.append("&aacute;"); break; //á
				case 226:out.append("&acirc;"); break; //â
				case 230:out.append("&aelig;"); break; //æ
				case 224:out.append("&agrave;"); break; //à
				case 229:out.append("&aring;"); break; //å
				case 227:out.append("&atilde;"); break; //ã
				case 228:out.append("&auml;"); break; //ä
				case 231:out.append("&ccedil;"); break; //ç
				case 233:out.append("&eacute;"); break; //é
				case 234:out.append("&ecirc;"); break; //ê
				case 232:out.append("&egrave;"); break; //è
				case 240:out.append("&eth;"); break; //ð
				case 235:out.append("&euml;"); break; //ë
				case 237:out.append("&iacute;"); break; //í
				case 238:out.append("&icirc;"); break; //î
				case 236:out.append("&igrave;"); break; //ì
				case 239:out.append("&iuml;"); break; //ï
				case 241:out.append("&ntilde;"); break; //ñ
				case 243:out.append("&oacute;"); break; //ó
				case 244:out.append("&ocirc;"); break; //ô
				case 242:out.append("&ograve;"); break; //ò
				case 248:out.append("&oslash;"); break; //ø
				case 245:out.append("&otilde;"); break; //õ
				case 246:out.append("&ouml;"); break; //ö
				case 223:out.append("&szlig;"); break; //ß
				case 254:out.append("&thorn;"); break; //þ
				case 250:out.append("&uacute;"); break; //ú
				case 251:out.append("&ucirc;"); break; //û
				case 249:out.append("&ugrave;"); break; //ù
				case 252:out.append("&uuml;"); break; //ü
				case 253:out.append("&yacute;"); break; //ý
				case 255:out.append("&yuml;"); break; //ÿ
				case 162:out.append("&cent;"); break; //¢
				default:
					found=false;
					break;
				}
				if(!found)
				{
					if(chars[i]>127) {
						char c=chars[i];
						int a4=c%16;
						c=(char) (c/16);
						int a3=c%16;
						c=(char) (c/16);
						int a2=c%16;
						c=(char) (c/16);
						int a1=c%16;
						out.append("&#x"+hex[a1]+hex[a2]+hex[a3]+hex[a4]+";");    
					}
					else
					{
						out.append(chars[i]);
					}
				}
			}  
			return out.toString();
		}

	} 

}
