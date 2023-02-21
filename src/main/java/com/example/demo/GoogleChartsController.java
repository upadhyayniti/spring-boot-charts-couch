package com.example.demo;

import java.util.Map;
import java.util.TreeMap;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;

import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;

import com.example.demo.configs.DBProperties;
import com.example.demo.models.CityPopulation;
import org.springframework.web.bind.annotation.RestController;
@Controller
@RestController
public class GoogleChartsController {

    private Cluster cluster;
    private Bucket bucket;
    private DBProperties dbProperties;

    public GoogleChartsController(Cluster cluster, Bucket bucket, DBProperties dbProperties) {
        System.out.println("Initializing googlecharts controller, cluster: " + cluster + "; bucket: " + bucket);
          this.cluster = cluster;
          this.bucket = bucket;
          this.dbProperties = dbProperties;
      }

    @GetMapping("/")
    public String getPieChart(Model model) {

        //select  distinct address.city, population from `cb-bucket`._default._default where population is not null
        String qryString = "SELECT cp.address.city, cp.population FROM `"+dbProperties.getBucketName()+"`.`_default`.`_default cp "+
                            "WHERE population is not null";
        System.out.println("Query="+qryString);
        //TBD with params: final List<Profile> profiles = cluster.query("SELECT p.* FROM `$bucketName`.`_default`.`$collectionName` p WHERE lower(p.firstName) LIKE '$search' OR lower(p.lastName) LIKE '$search' LIMIT $limit OFFSET $skip",
        final List<CityPopulation> cityPopulation = 
                cluster.query(qryString,
                    QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(CityPopulation.class);

        Map<String, Integer> graphData = new TreeMap<>();
        graphData.put("2016", 147);
        graphData.put("2017", 1256);
        graphData.put("2018", 3856);
        graphData.put("2019", 19807);
        model.addAttribute("chartData", graphData);
        return "google-charts";
    }
}
