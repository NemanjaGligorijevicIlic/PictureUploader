package org.example;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


public class S3ClientCreator {
    private final String accessKey = "AKIAV3OF5WXD7UVJF7PH";
    private final String secretKey = "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";
    
    public S3Client createS3Client(){
        Region region = Region.US_EAST_1;
        S3Client s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
        
        return s3Client;
    }
}
