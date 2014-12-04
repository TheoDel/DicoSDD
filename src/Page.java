import java.io.File;
import java.util.ArrayList;


public class Page {

	private int idPage;
	private int idChapitre;

	private File fichier;
	private Page parent;
	private ArrayList<String> listeMotsClesChapitre;

	public Page(int id, File f){
		idPage = id;
		fichier = f;
		parent = null;
		listeMotsClesChapitre = new ArrayList<String>();
	}
	
	/**
	 * Ajoute tous les mots-clés de nouvListe qu'elle ne contient pas déjà
	 * @param nouvListe
	 */
	public void fusionSansDoublonListeMot(ArrayList<String> nouvListe) {
		for (String motCourant : nouvListe){
			if (!listeMotsClesChapitre.contains(motCourant)){
				listeMotsClesChapitre.add(motCourant);
			}
		}
	}
	
	/**
	 * Fusionne la liste de mots-clés de la page avec nouvListe
	 * @param nouvListe
	 */
	public void fusionListeMot(ArrayList<String> nouvListe) {
		listeMotsClesChapitre.addAll(nouvListe);
	}
	
	public boolean isParent(){
		return (parent==null);
	}
	
	public void setIdChapitre(int idChapitre) {
		this.idChapitre = idChapitre;
	}
	
	public void setParent(Page parent) {
		this.parent = parent;
		this.parent.fusionSansDoublonListeMot(listeMotsClesChapitre);
	}
	
	public Page getParentRacine(){
		if (isParent())
			return this;
		else
			return parent.getParentRacine();
	}

	public int getIdPage() {
		return idPage;
	}

	public int getIdRepresentant(){
		return getParentRacine().idPage;
	}

	public File getFichier() {
		return fichier;
	}
	
	public ArrayList<String> getListeMotsChapitre(){
		return listeMotsClesChapitre;
	}	
	
	public int getIdChapitre() {
		if (isParent())
			return this.idChapitre;
		else
			return parent.getIdChapitre();
	}
}
