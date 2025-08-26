package com.scm.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfThumbnailService {

	public byte[] generateThumbnail(MultipartFile file) throws IOException{
		
		byte[] pdfBytes = file.getBytes();
		
		try(PDDocument document = Loader.loadPDF(pdfBytes)){
			
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			
			BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 100);
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			ImageIO.write(bufferedImage, "png", outputStream);
			
			return outputStream.toByteArray();
			
			
		}
	}
}
