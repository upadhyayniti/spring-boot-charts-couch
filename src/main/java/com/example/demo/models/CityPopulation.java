package com.example.demo.models;


import org.springframework.data.couchbase.core.mapping.Document;

@Document
public class CityPopulation {

    private String city;
    private String population;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }
}