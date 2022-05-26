package fr.ul.miage;

import java.util.ArrayList;

import org.bson.Document;
import org.slf4j.*;
import com.jayway.jsonpath.JsonPath;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ApiLastFM {
	
	private static String key = "6bec022c061e92dd4f13be1c90cdcfcd";
	private static HTTPTools http = new HTTPTools();
	
	public static void getTags(String s) {
		String res = http.sendGet("http://ws.audioscrobbler.com/2.0/?method=tag.getinfo&tag=" + s + "&api_key=" + key + "&format=json");
		System.out.println(Document.parse(res));
		//ajoutBDD(Document.parse(res), "GGJSC_tags");
	}
	
	public static void getArtist(String s) {
		String res = http.sendGet("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" + s +"&api_key="+ key +"&format=json");
		String bicycleColor = JsonPath.read(res, "$.artist.name");
		System.out.println(bicycleColor);
		//ajoutBDD(Document.parse(res), "GGJSC_artistes");
	}
	
	public static void getAlbum(String art, String alb) {
		String res = http.sendGet("http://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key= "+ key + "&artist=" + art + "&album=" + alb + "&format=json");
		ajoutBDD(Document.parse(res), "GGJSC_albums");
	}
	
	public static void getTopArtists() {
		String res = http.sendGet("http://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&api_key=" + key + "&format=json");
		ajoutBDD(Document.parse(res), "GGJSC_topArtistes");
	}
	
	public static void getTopTags() {
		String res = http.sendGet("https://ws.audioscrobbler.com/2.0/?method=chart.gettoptags&api_key=" + key + "&format=json");
		ajoutBDD(Document.parse(res), "GGJSC_topTags");
	}
	
	public static void getTopTracks() {
		String res = http.sendGet("http://ws.audioscrobbler.com/2.0/?method=chart.gettoptracks&api_key=" + key + "&format=json");
		ajoutBDD(Document.parse(res), "GGJSC_topTracks");
	}
	
	public static void getTopTracksPays(String p) {
		String res = http.sendGet("http://ws.audioscrobbler.com/2.0/?method=geo.gettoptracks&country=" + p + "&api_key=" + key + "&format=json");
		ajoutBDD(Document.parse(res), "GGJSC_topTrack"+p);
	}
	
	public static void getTopArtistsPays(String p) {
		String res = http.sendGet("http://ws.audioscrobbler.com/2.0/?method=geo.gettopartists&country=" + p + "&api_key=" + key + "&format=json");
		ajoutBDD(Document.parse(res), "GGJSC_topArtists"+p);
	}
	
	public static void ajoutBDD(Document d, String nTable) {
		String uri = "mongodb://localhost:27017";
		try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase db = mongoClient.getDatabase("SD2022_projet");
            if(!db.listCollectionNames().into(new ArrayList<String>()).contains(nTable)) {
            	db.createCollection(nTable);
            }
            MongoCollection<Document> collection = db.getCollection(nTable);
            collection.insertOne(d);
 
            } catch (MongoException me) {
                System.err.println("An error occurred while attempting to run a command: " + me);
            }
	}

}
