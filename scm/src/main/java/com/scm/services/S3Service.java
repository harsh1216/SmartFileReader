package com.scm.services;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

	@Autowired
	S3Client s3Client;

	@Value("${aws.bucket-name}")
	private String bucketName;

//	upload file function from Multipart
	public String uploadFile(MultipartFile file) throws IOException {
		String uniqueFileKey = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(uniqueFileKey)
				.contentType(file.getContentType()).build(), RequestBody.fromBytes(file.getBytes()));

		return uniqueFileKey;
	}
//	upload file function from file
	public String uploadFile(File file) throws IOException{
		String uniqueFileName = UUID.randomUUID().toString()+"_"+file.getName();
		
		s3Client.putObject( PutObjectRequest.builder()
				.bucket(bucketName)
				.key(uniqueFileName)
				.contentType("application/epub+zip")
				.build(),
				RequestBody.fromFile(file.toPath()));
		
		return uniqueFileName;
		
	}

//	download file function
	public byte[] downloadFile(String key) {
		ResponseBytes<GetObjectResponse> objectAsBytes = s3Client
				.getObjectAsBytes(GetObjectRequest.builder().bucket(bucketName).key(key).build());
		return objectAsBytes.asByteArray();
	}

//	upload thumbnail funtion
	public String uploadThumbnail(byte[] thumbnailBytes, String originalFileName) {
		String thumbKey = "thumbnails/" + UUID.randomUUID().toString() + "_" + originalFileName + ".png";

		s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(thumbKey).contentType("image/png").build(),
				RequestBody.fromBytes(thumbnailBytes));

		return thumbKey;
	}

//	delete from AWS S3
	public boolean deleteFile(String fileKey) {

		try {
			DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(fileKey)
					.build();

			s3Client.deleteObject(deleteObjectRequest);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}
