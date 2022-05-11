package fr.ul.miage;

import java.util.Scanner;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Launcher {

	public static void main(String[] args) {
		String apiKey = "6bec022c061e92dd4f13be1c90cdcfcd";
		HTTPTools http = new HTTPTools();
		String s = http.sendGet("https://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key="+ apiKey +"&artist=Cher&album=Believe");
		System.out.println(s);
		Scanner sc = new Scanner(System.in);
		String l = sc.nextLine();
		String sx = http.sendGet("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" + l +"&api_key="+ apiKey +"&format=json");
		System.out.println(sx);
		JSONObject json = new JSONObject(sx); 
		String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("admin");
            try {
                Bson command = new BsonDocument("ping", new BsonInt64(1));
                Document commandResult = database.runCommand(command);
                System.out.println("Connected successfully to server.");
            } catch (MongoException me) {
                System.err.println("An error occurred while attempting to run a command: " + me);
            }
        }
	}

}
