package fr.ul.miage;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.jayway.jsonpath.JsonPath;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Connexion_Inscription {
	
	private MongoDatabase db;
	MongoCollection<Document> collection = null;
	
	public Connexion_Inscription(MongoDatabase m) {
		db = m;
	}

	public boolean connexion(String user, String mdp) {
		if(!db.listCollectionNames().into(new ArrayList<String>()).contains("GGJSC_client")) {
        	db.createCollection("GGJSC_client");
        }
		collection = db.getCollection("GGJSC_client");
		Document Docuser = collection.find(new Document("user", user)).first();
		if (Docuser == null) {
			System.out.println("Ce nom d'utilisateur n'existe pas, veuillez recommencer.");
			return false;
		}else {
			if (Docuser.get("pass").equals(mdp)) {
				System.out.println("Connecté !!");
				return true;
			}else {
				System.out.println("Mot de passe incorrect");
				return false;
			}
		}
	}
	
	public boolean inscription(String user, String mdp) {
		if(!db.listCollectionNames().into(new ArrayList<String>()).contains("GGJSC_client")) {
        	db.createCollection("GGJSC_client");
        }
		collection = db.getCollection("GGJSC_client");
		Document Docuser = collection.find(new Document("user", user)).first();
		if (Docuser == null) {
			Document d = new Document()
			        .append("_id", new ObjectId())
			        .append("user", user)
			        .append("pass", mdp)
			        .append("niveau", "0");
			collection.insertOne(d);
			return true;
		}else {
			System.out.println("Ce nom est deja utilisée !!");
			return false;
		}
	}
}
