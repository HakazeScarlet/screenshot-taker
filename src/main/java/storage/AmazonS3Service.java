package storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

// TODO in the future: create endpoint for removing all files from S3
public class AmazonS3Service {

    private static final String BUCKET_NAME = "screenshot-taker-bucket";
    private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(AmazonS3Service.class);
    private final AmazonS3 s3client;

    public AmazonS3Service() {
        AWSCredentials credentials = new BasicAWSCredentials(
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

    public List<S3ObjectSummary> retrieveObjects() {
        ListObjectsV2Result listObjects = s3client.listObjectsV2(BUCKET_NAME);
        return listObjects.getObjectSummaries();
    }

    public void deleteAll() {
        List<DeleteObjectsRequest.KeyVersion> keys = retrieveObjects().stream()
            .map(obj -> new DeleteObjectsRequest.KeyVersion(obj.getKey()))
            .toList();

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(BUCKET_NAME)
            .withKeys(keys);
        s3client.deleteObjects(deleteObjectsRequest);

        logger.info("Files in " + BUCKET_NAME + " deleted successfully");
    }

    private static final class BucketCreateException extends RuntimeException {

        public BucketCreateException(String message) {
            super(message);
        }
    }
}
