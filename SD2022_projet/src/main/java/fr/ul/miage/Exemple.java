package fr.ul.miage;
import org.bson.Document;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

public class Exemple {
/**
* M�thode pour obtenir des information sur une chanson
* @param artiste (le nom de l�artiste)
* @param �
* @return Un Document JSON format� selon les besoins
*/
	
	public Document getTrackInfo(String artist) {
		// Cr�ation du JSON � retourner
		Document respDoc = new Document();
		try {
			// Pr�paration de la requ�te
			String url = "http://ws.audioscrobbler.com/�" + URLEncoder.encode(artist, "UTF-8") + "...";
			HTTPTools httpTools = new HTTPTools();
			String jsonResponse = httpTools.sendGet(url);
			Document docLastFm = Document.parse(jsonResponse);
			
			// Extraction de donn�es de docLastFm et insertion dans respDoc
			// voir l�API org.bson.Document
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return respDoc;
	}
}
