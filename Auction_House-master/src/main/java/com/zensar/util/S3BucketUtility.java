package com.zensar.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3BucketUtility {

	// AWS S3 bucket configuration
	private AmazonS3 s3client;
	private String endpointUrl = "https://s3.ap-south-1.amazonaws.com";
	private String secretKey = "";
	private String accessKey = "";
	private String bucketName = "demoforauction/images";

	// Initializing AWS
	private void initializeAmazon() {
		System.out.println("inside init.....");
		s3client = (AmazonS3Client) AmazonS3ClientBuilder.standard().withRegion("ap-south-1")
				.withCredentials(
						new AWSStaticCredentialsProvider(new BasicAWSCredentials(this.accessKey, this.secretKey)))
				.withPathStyleAccessEnabled(true).build();
	}

	// Method to upload image
	public String uploadFile(MultipartFile multipartFile) throws Exception {
		initializeAmazon();
		System.out.println("Inside Upload file...");
		String bucketUrl = "https://demoforauction.s3.ap-south-1.amazonaws.com/images/";
		String fileName = "";
		try {
			System.out.println("Inside try of s3");
			File file = convertMultiPartToFile(multipartFile);
			fileName = generateFileName(multipartFile);
			boolean isImageExtensionValid = isImageExtensionValid(multipartFile);
			if (isImageExtensionValid) {
				System.out.println("Inside try of s3    if block");
				System.out.println("\n\n\n Props Values : endpoint: " + endpointUrl + " bucketName : " + bucketName
						+ "accesskey : " + accessKey);
				System.out.println("\n\n\n Props Values : endpoint: " + endpointUrl + " filename : " + fileName
						+ "file : " + file);
				uploadFileTos3bucket(fileName, file);
				file.delete();
				return bucketUrl + fileName;
			} else {
				return "Extension of image is not correct";
			}
		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
			System.out.println("Caught an AmazonServiceException from GET requests, rejected reasons:");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());

		} catch (AmazonClientException ace) {
			ace.printStackTrace();
			System.out.println("Caught an AmazonClientException: ");
			System.out.println("Error Message: " + ace.getMessage());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("IOE Error Message: " + ioe.getMessage());

		}
		return bucketUrl + fileName;
	}

	// Method to delete image from AWS S3 bucket
	public String deleteFileFromS3Bucket(String fileUrl) {
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
		return "Successfully deleted";
	}

	// Method to upload image on the AWS S3 bucket
	private void uploadFileTos3bucket(String fileName, File file) {
		System.out.println("File Name : " + fileName + " file : " + file);
		s3client.putObject(new PutObjectRequest(bucketName, fileName, file));

		System.out.println("End of putObj");
	}

	// Method to generate image name to be stored in bucket
	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	// Method to convert MultiPartToFile
	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	// Method to check whether file extension is correct or not
	public static boolean isImageExtensionValid(MultipartFile image) {
		System.out.println("Inside extension");
		String extension = "";
		String imageName = image.getOriginalFilename();
		int imagePathLastIndex = imageName.length() - 1;
		int imagePathFourthLastIndex = imageName.length() - 5;
		int check = 0;
		for (int i = imagePathFourthLastIndex; i <= imagePathLastIndex; i++) {
			if (imageName.charAt(i) == '.') {
				check = 1;
			}
			if (check == 1) {
				extension = extension.concat(imageName.substring(i, i + 1));
			}
		}
		if (extension.equals(".jpeg") || extension.equals(".png") || extension.equals(".jpg"))
			return true;
		return false;
	}

}
