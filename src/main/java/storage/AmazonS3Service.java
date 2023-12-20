package storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

// TODO: read docs about S3
// TODO: write code
// TODO in the future: create endpoint for removing all files from S3
public class AmazonS3Service {

    private static final String BUCKET_NAME = "screenshot-taker-bucket";
    private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(AmazonS3Service.class);
    private final AWSCredentials credentials;
    private final AmazonS3 s3client;

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

        if (s3client.doesBucketExistV2(BUCKET_NAME)) {
            logger.info("Bucket already exists " + BUCKET_NAME);
        } else {
            try {
                s3client.createBucket(BUCKET_NAME);
            } catch (AmazonS3Exception e) {
                throw new BucketCreateException("Failed to create the bucket");
            }
        }

    }

    public void save(List<File> files) {
        for (File file : files) {
            s3client.putObject(BUCKET_NAME, file.getName(), file);
            logger.info("File " + file.getName() + " has been uploaded");
        }
    }

    public void getListObject(String bucketName) {
        ListObjectsV2Result listObjects = s3client.listObjectsV2(BUCKET_NAME);
        List<S3ObjectSummary> objectSummaries = listObjects.getObjectSummaries();
    }

    private static final class BucketCreateException extends RuntimeException {

        public BucketCreateException(String message) {
            super(message);
        }
    }
}
