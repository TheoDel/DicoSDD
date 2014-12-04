import java.io.File;
import java.util.ArrayList;


public class Page {

	private int idPage;
	private int idChapitre;

	private File fichier;
	private Page parent;
	private ArrayList<String> listeMotsChapitre;

	public Page(int id, File f){
		idPage = id;
		fichier = f;
		parent = null;
		listeMotsChapitre = new ArrayList<String>();
	}

	public Page getParentRacine(){
		if (isParent())
			return this;
		else
			return parent.getParentRacine();
	}
	
	public int getIdParentRacine(){
		if (isParent())
			return idPage;
		else
			return parent.getIdParentRacine();
	}

	public void setParent(Page parent) {
		this.parent = parent;
		this.parent.fusionSansDoublonListeMot(listeMotsChapitre);
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
	
	public void fusionSansDoublonListeMot(ArrayList<String> nouvListe) {
		for (String motCourant : nouvListe){
			if (!listeMotsChapitre.contains(motCourant)){
				listeMotsChapitre.add(motCourant);
			}
		}
	}
	
	public void fusionListeMot(ArrayList<String> nouvListe) {
		listeMotsChapitre.addAll(nouvListe);
	}
	
	public void setListeMot(ArrayList<String> nouvListe) {
		listeMotsChapitre = nouvListe;
	}
	
	public String getStringListeMotsChapitre(){
		String liste = "";
		for (String motcourant : listeMotsChapitre){
			liste += motcourant + " ";
		}
		return liste;
	}
	
	public ArrayList<String> getListeMotsChapitre(){
		return listeMotsChapitre;
	}
	
	public boolean isParent(){
		return (parent==null);
	}
	

	public int getIdChapitre() {
		if (isParent())
			return this.idChapitre;
		else
			return parent.getIdChapitre();
	}

	public void setIdChapitre(int idChapitre) {
		this.idChapitre = idChapitre;
	}
}
