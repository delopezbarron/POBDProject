package config;

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 1/25/16.
 */
public class TwitterConfiguration {
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;

    public TwitterConfiguration(){}
    public TwitterConfiguration(String consumerKey, String consumerSecret,
                                String accessToken, String accessTokenSecret){

        this.consumerKey= consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
    }
    public TwitterConfiguration(String fileDir) throws IOException{
        getConfigFromFile(fileDir);
    }


    public Configuration getConfiguration(){
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.setDebugEnabled(true)
                .setOAuthConsumerKey(this.consumerKey)
                .setOAuthConsumerSecret(this.consumerSecret)
                .setOAuthAccessToken(this.accessToken)
                .setOAuthAccessTokenSecret(this.accessTokenSecret);

        config.setJSONStoreEnabled(true);
        return config.build();
    }

    public List<TwitterBatchConfiguration> getConfigurations(
            String fileDir, String queryStr, int tweetsInQuery) throws IOException, TwitterException {

        ArrayList<TwitterBatchConfiguration> result;
        BufferedReader buffer;
        int configs;

        ConfigurationBuilder tempConfig;
        TwitterBatchConfiguration newBatchConfig;
        Twitter twitterInterface;
        Query twitterQuery;

        result = new ArrayList<>();
        buffer = new BufferedReader(new FileReader(fileDir));

        configs = Integer.parseInt(buffer.readLine().trim());
        buffer.readLine();
        for(int i = 0; i < configs; i++){
            String consumerKey = buffer.readLine();
            String consumerSecret = buffer.readLine();
            String accessToken = buffer.readLine();
            String accessTokenSecret = buffer.readLine();

            // Read the ## and do nothing
            buffer.readLine();

            // Create the config for each connection
            tempConfig = new ConfigurationBuilder();

            twitterInterface = new TwitterFactory(
            tempConfig.setDebugEnabled(true)
                    .setOAuthConsumerKey(consumerKey)
                    .setOAuthConsumerSecret(consumerSecret)
                    .setOAuthAccessToken(accessToken)
                    .setOAuthAccessTokenSecret(accessTokenSecret)
                    .setJSONStoreEnabled(true)
                    .build()
            ).getInstance();

            newBatchConfig = new TwitterBatchConfiguration(twitterInterface, queryStr, tweetsInQuery);
            result.add(newBatchConfig);
        }

        return result;
    }


    private void getConfigFromFile(String fileDir) throws IOException {
        BufferedReader buffer = new BufferedReader(new FileReader(fileDir));

        consumerKey = buffer.readLine();
        consumerSecret = buffer.readLine();
        accessToken = buffer.readLine();
        accessTokenSecret = buffer.readLine();

        buffer.close();
    }


}