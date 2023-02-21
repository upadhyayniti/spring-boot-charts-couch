package com.example.demo.runners;

import com.couchbase.client.core.error.*;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryResult;
import com.example.demo.configs.DBProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * This class run after the application startup. It automatically sets up all needed indexes
 */
@Component
public class DBSetupRunner implements CommandLineRunner {

    @Autowired
    private Bucket bucket;
    @Autowired
    private Cluster cluster;
    @Autowired
    private DBProperties props;

    @Override
    public void run(String... args) {
        try {
            cluster.queryIndexes().createPrimaryIndex(props.getBucketName());
            System.out.println("Created primary index" + props.getBucketName());
        } catch (Exception e) {
            System.out.println("Primary index already exists on bucket "+props.getBucketName());
        }

     /*    CollectionManager collectionManager = bucket.collections();
        try {
            CollectionSpec spec = CollectionSpec.create(CollectionNames.PROFILE, bucket.defaultScope().name());
            collectionManager.createCollection(spec);
            System.out.println("Created collection '" + spec.name() + "' in scope '" + spec.scopeName() + "' of bucket '" + bucket.name() + "'");
            Thread.sleep(1000);
        } catch (CollectionExistsException e){
            System.out.println(String.format("Collection <%s> already exists", CollectionNames.PROFILE));
        } catch (Exception e) {
            System.out.println(String.format("Generic error <%s>",e.getMessage()));
        }*/

        try {
            final String query = "CREATE PRIMARY INDEX `adv_id_default_prim` ON `cb-bucket`._default._default";
            System.out.println(String.format("Creating adv_id_default_prim: <%s>", query));
            final QueryResult result = cluster.query(query);
            for (JsonObject row : result.rowsAsObject()){
                System.out.println(String.format("Index Creation Status %s",row.getObject("meta").getString("status")));
            }
            System.out.println("Created primary index on collection cb-bucket");
            Thread.sleep(1000);
        } catch (IndexExistsException e){
            System.out.println(String.format("Collection's primary index already exists"));
        } catch (Exception e){
            System.out.println(String.format("General error <%s> when trying to create index ",e.getMessage()));
        }
        
     /*   try {
          final QueryResult result = cluster.query("CREATE INDEX default_profile_firstName_index ON " + props.getBucketName() + "._default." + CollectionNames.PROFILE + "(firstName)");
          Thread.sleep(1000);
        } catch (Exception e) {
          System.out.println(String.format("Failed to create secondary index on profile.firstName: %s", e.getMessage()));
        }*/

        System.out.println("Application is ready.");
    }
}
