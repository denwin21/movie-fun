package org.superbiz.moviefun.blobstore;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

public class S3Store implements BlobStore {

    private final String photoStorageBucket;
    private final AmazonS3Client s3Client;

    public S3Store(AmazonS3Client s3Client, String photoStorageBucket) {
        this.s3Client = s3Client;
        this.photoStorageBucket = photoStorageBucket;
    }

    @Override
    public void put(Blob blob) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.addUserMetadata("ContentType",blob.contentType);
        s3Client.putObject(photoStorageBucket, blob.name, blob.inputStream, metadata);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException, URISyntaxException {
        S3Object object;
        try{
            object =  s3Client.getObject(photoStorageBucket, name);
        }
        catch(Exception ex){
            object = s3Client.getObject(photoStorageBucket, "default2");
        }
        return Optional.of(new Blob(name, object.getObjectContent(), object.getObjectMetadata().getUserMetaDataOf("ContentType")));
    }

    @Override
    public void deleteAll() {
        ObjectListing objectsList = s3Client.listObjects(photoStorageBucket);
        for (S3ObjectSummary objectSummary : objectsList.getObjectSummaries()){
            s3Client.deleteObject(photoStorageBucket, objectSummary.getKey());
        }
    }
}
