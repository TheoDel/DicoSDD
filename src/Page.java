import java.io.File;
import java.util.ArrayList;


public class Page {

	private int idPage; //index de la page utilisé pour l'algorithme
	private int numPage; //numéro de la page écrit dans le fichier
	private int idChapitre;

	private File fichier;
	private Page parent;
	private ArrayList<String> listeMotsClesChapitre;

	//CONSTRUCTEUR
	//Complexité O(1)
	public Page(int num, int id, File f){
		idPage = id;
		numPage = num;
		fichier = f;
		parent = null;
		listeMotsClesChapitre = new ArrayList<String>();
	}
	
	/**
	 * Ajoute tous les mots-clés de nouvListe qu'elle ne contient pas déjà
	 * @param nouvListe
	 * Complexité : O(nouvListe.size()² + nouvListe.size() * listeMotsClesChapitre.size())
	 */
	public void fusionSansDoublonListeMot(ArrayList<String> nouvListe) {
		for (String motCourant : nouvListe) //O(nouvListe.size() * (nouvListe.size() + listeMotsClesChapitre.size()))
			if (!listeMotsClesChapitre.contains(motCourant)) // O(listeMotsClesChapitre.size())
				listeMotsClesChapitre.add(motCourant); // O(listeMotsClesChapitre.size())
	}
	
	/**
	 * Fusionne la liste de mots-clés de la page avec nouvListe
	 * @param nouvListe
	 * Complexité : O(nouvListe.size() + listeMotsClesChapitre.size())
	 */
	public void fusionListeMot(ArrayList<String> nouvListe) {
		listeMotsClesChapitre.addAll(nouvListe);
	}
	
	//TESTS ET SETTERS O(1)
	public boolean isParent(){ return (parent==null); }
	public void setIdChapitre(int idChapitre) { this.idChapitre = idChapitre;}
	public void setParent(Page parent) {
		this.parent = parent;
		this.parent.fusionSansDoublonListeMot(listeMotsClesChapitre);
	}
	
	public Page getParentRacine(){ return isParent() ? this : parent.getParentRacine();} //O(1)
	public int getIdChapitre() { return getParentRacine().idChapitre; } //O(1)

	//GETTERS O(1)
	public int getIdPage() { return idPage; }
	public int getNumPage() { return numPage; }
	public int getIdRepresentant(){ return getParentRacine().idPage;}
	public File getFichier() { return fichier;}
	public ArrayList<String> getListeMotsChapitre(){ return listeMotsClesChapitre;}	
}

