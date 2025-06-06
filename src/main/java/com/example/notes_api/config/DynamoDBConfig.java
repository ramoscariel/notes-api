package com.example.notes_api.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.example.notes_api.repository")
public class DynamoDBConfig {

    @Value("${amazon.dynamodb.region}")
    private String amazonDynamoDBRegion;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        System.out.println("Amazon DynamoDB Region="+amazonDynamoDBRegion);
        // For AWS DynamoDB, don't specify endpoint - just region and credentials
        return AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(amazonDynamoDBRegion)
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
    }
}