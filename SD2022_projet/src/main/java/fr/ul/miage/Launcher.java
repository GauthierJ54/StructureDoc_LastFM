package fr.ul.miage;

import java.util.ArrayList;
import java.util.Scanner;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

public class Launcher {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String uri = "mongodb://localhost:27017";
		String user = "";
		MongoDatabase db = null;
		try{
			MongoClient mongoClient = MongoClients.create(uri);
            db = mongoClient.getDatabase("SD2022_projet");  
            Thread.sleep(1000);
            } catch (MongoException | InterruptedException me) {
                System.err.println("An error occurred while attempting to run a command: " + me);
            }
		
	
		System.out.println("Projet Structuration de Documents\r\n"
				+ "Collecte de statistiques et de recommandation de musique\r\n"
				+ "Master I MIAGE 2021-22\r\n"
				+ "Gauthier JACQUES / Sofiane CHELH");
		
		System.out.println("Souhaitez vous vous connecter ou vous inscrire ?");
		System.out.println("(1) Connexion");
		System.out.println("(2) Inscription");
		
		int x = sc.nextInt();
		switch (x) {
		case 1:
			System.out.println("Connexion :");
			System.out.println("Nom d'utilisateur :");
			user = sc.nextLine();
			System.out.println("Mot de Passe :");
			String mdp = sc.nextLine();
			Connexion_Inscription.connexion(user, mdp, null);
			break;
			
		case 2:
			System.out.println("Inscription :");
			System.out.println("Nom d'utilisateur :");
			user = sc.nextLine();
			System.out.println("Mot de Passe :");
			String passInscr = sc.nextLine();
			Connexion_Inscription.inscription(user, passInscr);
			break;
			
		default:
			ApiLastFM.getArtist("Eminem");
			break;
		}
		
		
		System.out.println("Bonjour " + user);
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
			
			break;
		case 2:
					
			break;
		case 3:
			MongoCollection<Document> collection = db.getCollection("GGJSC_artistes");
            Document student1 = collection.find(new Document("name", "Eminem")).first();
            System.out.println("Student 1: " + student1.toJson());
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
			
			break;
		case 12:
			
			break;

		default:
			break;
		}
	}

}
