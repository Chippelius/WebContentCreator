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

class ExportController {

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

	public void run() throws Exception {
		delete(WCCModel.exportFolder);
		WCCModel.exportFolder.mkdirs();
		exportStatic("");
		exportIndexFile();
		exportPages();
		createVersionsFile();
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

}
