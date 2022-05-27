package fr.ul.miage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import com.jayway.jsonpath.JsonPath;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

public class Launcher {

	private static boolean co = false;
	private static boolean bool = true;
	
	public static boolean isCo() {
		return co;
	}


	public static void setCo(boolean co) {
		Launcher.co = co;
	}


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String uri = "mongodb://localhost:27017";
		String user = "moi";
		MongoDatabase db = null;
		try{
			MongoClient mongoClient = MongoClients.create(uri);
            db = mongoClient.getDatabase("SD2022_projet");  
            Thread.sleep(1000);
            } catch (MongoException | InterruptedException me) {
                System.err.println("An error occurred while attempting to run a command: " + me);
            }
		ApiLastFM api = new ApiLastFM(db);
		Connexion_Inscription CI = new Connexion_Inscription(db);
		MongoCollection<Document> collection = null;
		DateTimeFormatter dtf4 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	
		System.out.println("Projet Structuration de Documents\r\n"
				+ "Collecte de statistiques et de recommandation de musique\r\n"
				+ "Master I MIAGE 2021-22\r\n"
				+ "Gauthier JACQUES / Sofiane CHELH");
		
		System.out.println("Souhaitez vous vous connecter ou vous inscrire ?");
		int x;
		while (!co) {
				System.out.println("(1) Connexion");
				System.out.println("(2) Inscription");
				x = sc.nextInt();
				switch (x) {
				case 1:
					System.out.println("Connexion :");
					System.out.println("Nom d'utilisateur :");
					user = sc.nextLine();
					System.out.println("Mot de Passe :");
					String mdp = sc.nextLine();
					co = CI.connexion(user, mdp, null);
					break;
					
				case 2:
					System.out.println("Inscription :");
					System.out.println("Nom d'utilisateur :");
					user = "Gauthier";
					System.out.println("Mot de Passe :");
					String passInscr = sc.nextLine();
					co = CI.inscription(user, "tes");
					break;
					
				default:
					co = true;
					api.getAlbum("Cher", "Believe");
					api.getTopArtists();
					HTTPTools http = new HTTPTools();
					http.sendGet("https://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=6bec022c061e92dd4f13be1c90cdcfcd&artist=Cher&album=Believe&format=json");
					break;
				}			
		}
		
		
		
		System.out.println("Bonjour " + user);
		
		while (bool) {
				System.out.println("Que voulez vous faire ?");
				System.out.println("(1) Informations sur un tag");
				System.out.println("(2) Informations sur un album");
				System.out.println("(3) Informations sur un artiste");
				System.out.println("(4) Tendance Artiste (Monde)");
				System.out.println("(5) Tendance Tracks (Monde)");
				System.out.println("(6) Tendance Tags (Monde)");
				System.out.println("(7) Tendance Artiste (Pays)");
				System.out.println("(8) Tendance Tracks (Pays)");
				System.out.println("(9) Album ou chansons similaires a un artiste");
				System.out.println("(10) Laisser un avis");
				System.out.println("(11) Visualiser les opérations");
				System.out.println("(12) EXIT");
				int c = sc.nextInt();
				
				switch (c) {
				case 1:
					System.out.println("Nom du tag recherché :");
					String l = sc.nextLine();
					l="rap";
					String loc = null;
					if(!db.listCollectionNames().into(new ArrayList<String>()).contains("GGJSC_tags")) {
			        	db.createCollection("GGJSC_tags");
			        }
					collection = db.getCollection("GGJSC_tags");
					Document tag = collection.find(new Document("name", l)).first();
					if (tag == null) {
						api.getTags(l);
						tag = collection.find(new Document("name", l)).first();
						loc = "disatnce";
					}else {
						loc = "local";
					}
					System.out.println("Informations sur le tag - " + l + " - :");
					System.out.println(tag.getString("name"));
		            Document d = new Document()
					        .append("_id", new ObjectId())
					        .append("requete", "Infos Tag - " + l)
					        .append("date", dtf4.format(LocalDateTime.now()))
					        .append("user", user)
					        .append("type", loc);
		            if(!db.listCollectionNames().into(new ArrayList<String>()).contains("GGJSC_operation")) {
			        	db.createCollection("GGJSC_operation");
			        }
		            collection = db.getCollection("GGJSC_operation");
		            collection.insertOne(d);
					break;
				case 2:
							
					break;
				case 3:
					collection = db.getCollection("GGJSC_artistes");
		            try {
		            	Document student1 = collection.find(new Document("name", "Eminem")).first();
		                System.out.println("Student 1: " + student1.toJson());
					} catch (Exception e) {
						
					}
					break;
				case 4:
					break;
				case 5:
					
					break;
				case 6:
					
					break;
				case 7:
					
					break;
				case 8:
					
					break;
				case 9:
					
					break;
				case 10:
					
					break;
				case 11:
					if(!db.listCollectionNames().into(new ArrayList<String>()).contains("GGJSC_operation")) {
			        	db.createCollection("GGJSC_operation");
			        }
		            collection = db.getCollection("GGJSC_operation");
					FindIterable<Document> iterDoc = collection.find().limit(10);
					int i = 1;
					System.out.println("Listes des 10 dernieres opérations :");
					for (Document doc : iterDoc) {
						System.out.println("Opérations n°" + i + ":");
						System.out.println("Requete : " + doc.getString("requete"));
						System.out.println("Date : " + doc.getString("date"));
						System.out.println("Utilisateur : " + doc.getString("user"));
						System.out.println("Type : " + doc.getString("type") + "\n");
						i++;
					}
					break;
				case 12:
					System.out.println("Au revoir !!");
					bool = false;
					break;

				default:
					break;
				}
		}
		
	}

}
