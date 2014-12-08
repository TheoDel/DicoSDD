//Classe séparant les mots clés du dictionnaire
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

class Dictionary {

	//VARIABLES
	private File f_; //fichier du dictionnaire
	private ArrayList<String> keyWords_; //liste triée des mots clés
	private ArbreBinDico keyWordsABR_; //ABR contenant les mots clés

	//CONSTRUCTEUR
	//Complexité: O(n*log(n)) où n = nombre de mots du dictionnaire
	public Dictionary (String fileName) {
		f_ = new File(fileName);
		keyWords_ = new ArrayList<String>();
		readKeyWords(); //O(n*log(n))
		keyWordsABR_ = new ArbreBinDico(keyWords_); //O(n)
	}

	//SEPARER LES MOTS CLES
	//Lance une exception quand le fichier n'existe pas
	//Complexité : O(n*log(n)) où n = nombre de mots du dictionnaire
	private void readKeyWords() {
		try {
			Scanner sc = new Scanner(f_);
			while (sc.hasNext()) //tant qu'on peut lire le fichier
				keyWords_.add(sc.next()); //ajout du mot clé (O(n))
			sc.close();
			Collections.sort(keyWords_); //trie la liste par ordre croissant en O(n*log(n))
		} catch (FileNotFoundException e) {
			System.out.println("Erreur : Fichier inexistant");
		}
	}

	//GETTERS
	//Complexités: O(1)
	public File getFile() {return f_;}
	public ArrayList<String> getKeyWords() {return keyWords_;}
	public ArbreBinDico getKeyWordsABR() {return keyWordsABR_;}

	//AFFICHER LES MOTS CLES
	//Complexité : O(n) où n = nombre de mots du dictionnaire
	public void printKeyWords() {
		for (String s : keyWords_)
			System.err.print(s + ";");
	}
	//Complexité : O(n)
	public void printKeyABR() {
		System.err.println(keyWordsABR_.print(0));
	}

	/**
	 * Teste si un mot appartient au dictionnaire et, si oui, ajoute le numéro de la page le contenant au noeud du mot
	 * @param mot Le mot à tester
	 * @param page La page contenant le mot
	 * @return ArbreBin du mot s'il appartient au dictionnaire et auquel il vient donc d'être ajouté. Renvoie null si le mot n'appartient pas au dictionnaire ou a déjà été trouvé pour cette page
	 * Complexités : O(n + nbpages) où n = nombre total de mots du dictionnaire, et nbpages le nombre de pages totales
	 */
	public ArbreBinDico ajoutePage(String mot, Page page){
		return keyWordsABR_.ajoutePage(mot, page);
	}
}
