package storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

// TODO: read docs about S3
// TODO: write code
// TODO in the future: create endpoint for removing all files from S3
public class AmazonS3Service {

    private AWSCredentials credentials;
    private AmazonS3 s3client;
    private String bucketName = "screenshot-taker-bucket";

    public AmazonS3Service() {
        this.credentials = new BasicAWSCredentials(
            System.getenv("ACCESS_KEY_ID"),
            System.getenv("SECRET_ACCESS_KEY")
        );
        this.s3client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.SA_EAST_1)
            .build();

        if (s3client.doesBucketExistV2(bucketName)) {
            System.out.format("Bucket already exists. \n", bucketName);
        }
    }
}