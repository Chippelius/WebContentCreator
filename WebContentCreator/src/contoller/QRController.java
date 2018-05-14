package contoller;

import java.io.File;
import java.nio.file.FileSystems;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import model.Page;
import model.WCCModel;

public class QRController {

	/**
	 * Generates *.png image files in the given size that contain QR-codes linking to the pages of the project based on a given base-URL. 
	 * 
	 * @param destination the destination folder for the image files
	 * @param baseURL the URL pointing to where the exported project will be found online
	 * @param size the size of the image files
	 * @throws Exception
	 */
	public static void generateQRCodes(File destination, String baseURL, int size) throws Exception {
		String url = baseURL+(baseURL.contains("?")?"&":"?")+"page=";

		ExportController.delete(destination);
		destination.mkdirs();

		QRCodeWriter writer = new QRCodeWriter();

		for(Page p : WCCModel.getDataStorage()) {
			BitMatrix matrix = writer.encode(url+p.getFilename(), BarcodeFormat.QR_CODE, size, size);
			MatrixToImageWriter.writeToPath(matrix, "PNG", FileSystems.getDefault().getPath(destination.getAbsolutePath()+"\\"+p.getFilename()+".png"));
		}
	}

}
