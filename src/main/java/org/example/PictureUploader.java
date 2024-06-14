package org.example;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class PictureUploader {

    private static final String BUCKET_NAME = "spcmalmobucket";
    private static final String S3_FOLDER = "images/";
    private static final String LOCAL_FOLDER_PATH = "/Users/Nemanj Gligorijevic Ilic/Desktop/sliketest";

    public static void uploade() {
        S3ClientCreator creator = new S3ClientCreator();
        S3Client s3Client = creator.createS3Client();


        try {
            uploadImagesFromFolder(s3Client, LOCAL_FOLDER_PATH, Paths.get(LOCAL_FOLDER_PATH));
        } catch (IOException e) {
            System.err.println("Error accessing folder: " + e.getMessage());
        }
    }

    public static void uploadImagesFromFolder(S3Client s3Client, String folderPath, Path rootPath) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths.filter(Files::isRegularFile)
                    .forEach(filePath -> uploadImage(s3Client, filePath, rootPath));
        } catch (IOException e) {
            System.err.println("Error walking through folder: " + e.getMessage());
            throw e;
        }
    }

    private static void uploadImage(S3Client s3Client, Path localFilePath, Path rootPath) {
        String relativePath = rootPath.relativize(localFilePath).toString().replace("\\", "/");
        String key = S3_FOLDER + relativePath;

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build();
            s3Client.putObject(request, localFilePath);
            System.out.println("Uploaded " + localFilePath.toString() + " to S3 as " + key);
        } catch (S3Exception e) {
            System.err.println("S3 upload error: " + e.awsErrorDetails().errorMessage());
        }
    }
}
