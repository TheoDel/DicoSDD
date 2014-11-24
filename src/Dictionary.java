//Shirley Noel
//Classe séparant les mots clés du dictionnaire
//TODO calcul des complexités
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

class Dictionary {

	//VARIABLES
	private File f_;//fichier du dictionnaire
	private ArrayList<String> keyWords_;//liste triée des mots clés
	private ArbreBin keyWordsABR_;//ABR contenant les mots clés

	//CONSTRUCTEUR
	public Dictionary (String fileName) {
		f_ = new File(fileName);
		keyWords_ = new ArrayList<String>();
		readKeyWords();
		keyWordsABR_ = new ArbreBin(keyWords_);
	}

	//SEPARER LES MOTS CLES
	//Lance une exception quand le fichier n'existe pas
	private void readKeyWords() {
		try {
			Scanner sc = new Scanner(f_);
			while (sc.hasNext())//tant qu'on peut lire le fichier
				keyWords_.add(sc.next());//ajout du mot clé (O(n))
			sc.close();
			Collections.sort(keyWords_);//trie la liste par ordre croissant en O(nlogn)
		} catch (FileNotFoundException e) {
			System.out.println("Erreur : Fichier inexistant");
		}
	}

	//GETTERS
	public File getFile() {return f_;}
	public ArrayList<String> getKeyWords() {return keyWords_;}
	public ArbreBin getKeyWordsABR() {return keyWordsABR_;}

	//AFFICHER LES MOTS CLES
	public void printKeyWords() {
		for (String s : keyWords_)
			System.out.print(s + ";");
	}
	public void printKeyABR() {
		System.out.println(keyWordsABR_.print(0));
	}

	/**
	 * Teste si un mot appartient au dictionnaire et, si oui, ajoute le numéro de la page le contenant au noeud du mot
	 * @param mot Le mot à tester
	 * @param page La page contenant le mot
	 * @return ArbreBin du mot s'il appartient au dictionnaire et auquel il vient donc d'être ajouté. Renvoie null si le mot n'appartient pas au dictionnaire ou a déjà été trouvé pour cette page
	 */
	public ArbreBin ajoutePage(String mot, Page page){
		return keyWordsABR_.ajoutePage(mot, page);
	}
}
