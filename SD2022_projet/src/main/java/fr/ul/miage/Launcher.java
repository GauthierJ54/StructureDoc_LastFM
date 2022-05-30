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
	
		System.out.println("*** Projet Structuration de Documents ***\r\n"
				+ "** Collecte de statistiques et de recommandation de musique **\r\n"
				+ "* Master I MIAGE 2022 Gauthier JACQUES - Sofiane CHELH * \r\n");
		
		System.out.println("Souhaitez vous vous connecter ou vous inscrire ?");
		int x;
		while (!co) {
				System.out.println("(1) Connexion");
				System.out.println("(2) Inscription");
				x = sc.nextInt();
				switch (x) {
				case 1:

					System.out.println("** CONNEXION **");
					System.out.println("Veuillez saisir votre nom d'utilisateur :");
					sc.nextLine();
					user = sc.nextLine();
					System.out.println("Veuillez saisir votre mot de passe :");
					String mdp = sc.nextLine();
					co = CI.connexion(user, mdp);
					break;
					
				case 2:
					System.out.println("** INSCRIPTION **");
					System.out.println("Veuillez saisir un nom d'utilisateur disponible :");
					sc.nextLine();
					user = sc.nextLine();
					System.out.println("Veuillez saisir un mot de passe :");
					String passInscr = sc.nextLine();
					co = CI.inscription(user, passInscr);
					break;
				default:
					co = true;
					break;
				}			
		}
		
		
		
		System.out.println("Bienvenue " + user);
		String l;
		String loc;
		Document docu;
		Document d;
		
		while (bool) {
				System.out.println("Que voulez vous faire ?");
				System.out.println("(1) Rechercher des informations sur un tag");
				System.out.println("(2) Rechercher des informations sur un album");
				System.out.println("(3) Rechercher des informations sur un artiste");
				System.out.println("(4) Rechercher les tendances concernant les artistes (Monde)");
				System.out.println("(5) Rechercher les tendances concernant les tracks (Monde)");
				System.out.println("(6) Tendance Tags (Monde)");
				System.out.println("(7) Tendance Artiste (Pays)");
				System.out.println("(8) Tendance Tracks (Pays)");
				System.out.println("(9) Album ou chansons similaires a un artiste");
				System.out.println("(10) Laisser un avis");
				System.out.println("(11) Afficher les dernières opérations");
				System.out.println("(12) Quitter l'application");
				int c = sc.nextInt();
				
				switch (c) {
				case 1:
					System.out.println("Entrez le nom du tag recherché :");
					sc.nextLine();
					l = sc.nextLine();
					loc = null;
					if(!db.listCollectionNames().into(new ArrayList<String>()).contains("GGJSC_tags")) {
			        	db.createCollection("GGJSC_tags");
			        }
					collection = db.getCollection("GGJSC_tags");
					docu = collection.find(new Document("name", l)).first();
					if (docu == null) {
						api.getTags(l);
						docu = collection.find(new Document("name", l)).first();
						loc = "distance";
					}else {
						loc = "local";
					}
					System.out.println("Informations sur le tag - " + l.toUpperCase() + " - :");
					System.out.println(docu.getInteger("total"));
					System.out.println(docu.getInteger("reach"));
					System.out.println("Description : " + docu.getString("summary") + "\n");
		            d = new Document()
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
					System.out.println("Entrez le nom de l'artiste recherché :");
					sc.nextLine();
					l = sc.nextLine();
					loc = null;
					if(!db.listCollectionNames().into(new ArrayList<String>()).contains("GGJSC_artistes")) {
			        	db.createCollection("GGJSC_artistes");
			        }
					collection = db.getCollection("GGJSC_artistes");
					docu = collection.find(new Document("name", l)).first();
					if (docu == null) {
						api.getArtist(l);
						docu = collection.find(new Document("name", l)).first();
						loc = "distance";
					}else {
						loc = "local";
					}
					if (docu != null) {
						System.out.println("Informations sur l'artiste - " + l.toUpperCase() + " - :");
						System.out.println(docu.getString("url"));
						System.out.println(docu.getString("listeners"));
						System.out.println(docu.getString("playcount"));
						System.out.print("Description : " + docu.getString("summary") + "\n");
						ArrayList<String> al = (ArrayList<String>) docu.get("similar");
						for (String s : al) {
							System.out.print("* " + s + " *");
						}
						System.out.println("");
						al = (ArrayList<String>) docu.get("tags");
						for (String s : al) {
							System.out.print("* " + s + " *");
						}
						System.out.println("");
						d = new Document()
						        .append("_id", new ObjectId())
						        .append("requete", "Infos Artiste - " + l)
						        .append("date", dtf4.format(LocalDateTime.now()))
						        .append("user", user)
						        .append("type", loc);
			            if(!db.listCollectionNames().into(new ArrayList<String>()).contains("GGJSC_operation")) {
				        	db.createCollection("GGJSC_operation");
				        }
			            collection = db.getCollection("GGJSC_operation");
			            collection.insertOne(d);
					}else {
						System.out.println("L'artiste n'existe pas !!");
					}
					
					break;
				case 4:
					break;
				case 5:
					api.getTopTracks();
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
