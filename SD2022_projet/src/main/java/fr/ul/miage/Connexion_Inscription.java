package fr.ul.miage;

import java.util.ArrayList;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Connexion_Inscription {

	public static void connexion(String user, String mdp, MongoDatabase m) {
		String uri = "mongodb://localhost:27017";
		try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase db = mongoClient.getDatabase("SD2022_projet");
            if(!db.listCollectionNames().into(new ArrayList<String>()).contains("GGJSC_client")) {
            	db.createCollection("GGJSC_client");
            }
 
            } catch (MongoException me) {
                System.err.println("An error occurred while attempting to run a command: " + me);
            }
	}
	
	public static void inscription(String user, String mdp) {
		String uri = "mongodb://localhost:27017";
		try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase db = mongoClient.getDatabase("SD2022_projet");
            if(!db.listCollectionNames().into(new ArrayList<String>()).contains("GGJSC_Client")) {
            	db.createCollection("GGJSC_Client");
            }
 
            } catch (MongoException me) {
                System.err.println("An error occurred while attempting to run a command: " + me);
            }
	}
}
