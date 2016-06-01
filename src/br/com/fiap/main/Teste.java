package br.com.fiap.main;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class Teste {
	
	
	public static void main(String[] args) {
		
		
		Twitter twitter = new TwitterFactory().getInstance();

	    AccessToken accessToken = new AccessToken("Your-Access-Token", "Your-Access-Token-Secret");
	    twitter.setOAuthConsumer("Consumer-Key", "Consumer-Key-Secret");
	    twitter.setOAuthAccessToken(accessToken);

	    try {
	        Query query = new Query("#IPL");
	        QueryResult result;
	        result = twitter.search(query);
	        List<Status> tweets = result.getTweets();
	        for (Status tweet : tweets) {
	            System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
	        }
	    }
	    catch (TwitterException te) {
	        te.printStackTrace();
	        System.out.println("Failed to search tweets: " + te.getMessage());
	        System.exit(-1);
	    }
	}

}
