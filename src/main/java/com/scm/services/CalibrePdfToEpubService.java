package com.scm.services;

import java.io.File;
import java.io.IOException;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CalibrePdfToEpubService {
	
	@Value("${calibre.path}")
	String calibrePath;
	
	public File convertPdfToEpub(MultipartFile file) throws IOException , InterruptedException{
		
//		save uploaded PDF temporarily
		File tempPdf = File.createTempFile("upload-", ".pdf");
		file.transferTo(tempPdf);
		
		File epubFile = new File(tempPdf.getParent(), file.getOriginalFilename().replace(".pdf", ".epub"));
		
		String[] command = {
				calibrePath,
				tempPdf.getAbsolutePath(),
				epubFile.getAbsolutePath()
		};
		
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(true);
		Process process = pb.start();
		
		int exitCode = process.waitFor();
		
		if(exitCode != 0) {
			throw new RuntimeException("Calibre conversion failed with exit code: "+exitCode);
		}
		
		return epubFile;
		
	}
}
