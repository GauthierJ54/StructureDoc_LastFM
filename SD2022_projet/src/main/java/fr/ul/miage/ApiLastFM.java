package fr.ul.miage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.*;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import net.minidev.json.JSONArray;


public class ApiLastFM {
	
	private static String key = "6bec022c061e92dd4f13be1c90cdcfcd";
	private static HTTPTools http = new HTTPTools();
	private static MongoDatabase db;
	
	public ApiLastFM (MongoDatabase m) {
		db = m;
	}
	
	public void getTags(String s) {
		String res = http.sendGet("http://ws.audioscrobbler.com/2.0/?method=tag.getinfo&tag=" + s + "&api_key=" + key + "&format=json");
		try {
			Document d = new Document()
			        .append("_id", new ObjectId())
			        .append("name", JsonPath.read(res, "$.tag.name"))
			        .append("total", JsonPath.read(res, "$.tag.total"))
			        .append("reach", JsonPath.read(res, "$.tag.reach"))
			        .append("summary", JsonPath.read(res, "$.tag.wiki.summary"));
			        ajoutBDD(d, "GGJSC_tags");
		} catch (PathNotFoundException e) {
			System.out.println("Le tag n'existe pas !!");
		}
	}
	
	public void getArtist(String s) {
		String res = http.sendGet("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" + s +"&api_key="+ key +"&format=json");
		try {
			Document d = new Document()
			        .append("_id", new ObjectId())
			        .append("name", JsonPath.read(res, "$.artist.name"))
			        .append("url", JsonPath.read(res, "$.artist.url"))
			        .append("listeners", JsonPath.read(res, "$.artist.stats.listeners"))
			        .append("playcount", JsonPath.read(res, "$.artist.stats.playcount"))
			        .append("summary", JsonPath.read(res, "$.artist.bio.summary"))
			        .append("similar", Arrays.asList(JsonPath.read(res, "$.artist.similar.artist[0].name"), JsonPath.read(res, "$.artist.similar.artist[1].name"),JsonPath.read(res, "$.artist.similar.artist[2].name"),JsonPath.read(res, "$.artist.similar.artist[3].name"),JsonPath.read(res, "$.artist.similar.artist[4].name")))
			        .append("tags", Arrays.asList(JsonPath.read(res, "$.artist.tags.tag[0].name"), JsonPath.read(res, "$.artist.tags.tag[1].name"),JsonPath.read(res, "$.artist.tags.tag[2].name"), JsonPath.read(res, "$.artist.tags.tag[3].name"), JsonPath.read(res, "$.artist.tags.tag[4].name")));
					ajoutBDD(d, "GGJSC_artistes");
		} catch (PathNotFoundException e) {
			System.out.println("L'artiste n'existe pas !!");
		}
		
	}
	
	public void getAlbum(String art, String alb) {
		String res = http.sendGet("https://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=" + key + "&artist=" + art + "&album=" + alb + "&format=json");
		System.out.println(res);
		JSONArray tab = JsonPath.read(res, "$.album.tracks.track");
		Document insert = new Document();
		int i = 0;
		for (Object o : tab) {
			LinkedHashMap<String, Document> t = (LinkedHashMap<String, Document>) o;
	        System.out.println(t.get("duration"));
	        int y = t.get("duration").getInteger("");
	        System.out.println(y);
		}
		System.out.println(i);
		ajoutBDD(insert, "GGJSC_albums");
	}
	
	public void getTopArtists() {
		String res = http.sendGet("http://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&api_key=" + key + "&format=json");
		ajoutBDD(Document.parse(res), "GGJSC_topArtistes");
	}
	
	public void getTopTags() {
		String res = http.sendGet("https://ws.audioscrobbler.com/2.0/?method=chart.gettoptags&api_key=" + key + "&format=json");
		ajoutBDD(Document.parse(res), "GGJSC_topTags");
	}
	
	public void getTopTracks() {
		String res = http.sendGet("http://ws.audioscrobbler.com/2.0/?method=chart.gettoptracks&api_key=" + key + "&format=json");
		ajoutBDD(Document.parse(res), "GGJSC_topTracks");
	}
	
	public void getTopTracksPays(String p) {
		String res = http.sendGet("http://ws.audioscrobbler.com/2.0/?method=geo.gettoptracks&country=" + p + "&api_key=" + key + "&format=json");
		ajoutBDD(Document.parse(res), "GGJSC_topTrack"+p);
	}
	
	public void getTopArtistsPays(String p) {
		String res = http.sendGet("http://ws.audioscrobbler.com/2.0/?method=geo.gettopartists&country=" + p + "&api_key=" + key + "&format=json");
		ajoutBDD(Document.parse(res), "GGJSC_topArtists"+p);
	}
	
	public static void ajoutBDD(Document d, String nTable) {
		if(!db.listCollectionNames().into(new ArrayList<String>()).contains(nTable)) {
        	db.createCollection(nTable);
        }
        MongoCollection<Document> collection = db.getCollection(nTable);
        collection.insertOne(d);
	}

}
