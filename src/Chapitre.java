import java.util.ArrayList;

public class Chapitre {
	private ArrayList<Page> pagesduchapitre;
	private int idChapitre;
	
	// CONSTRUCTEUR
	// Complexité : O(1)
	public Chapitre(Page p1){
		pagesduchapitre= new ArrayList<Page>();
		pagesduchapitre.add(p1);
		idChapitre = p1.getIdChapitre();
		
	}
	
	// Ajout d’une page au chapitre
	// Complexité : O(pagesduchapitre.size())
	public void ajoutePage(Page nouvPage){
		pagesduchapitre.add(nouvPage);
	}
	
	// GETTER
	// Complexité : O(1)
	public ArrayList<Page> getPagesDuChapitre(){ return pagesduchapitre; }

	// Génération de la sortie finale
	// Complexité : O(pagesduchapitre.size() + nombre de mots du dictionnaire)
	@Override
	public String toString() {
		String texte = "Chapitre " + idChapitre + " -Mots: " + pagesduchapitre.get(0).getListeMotsChapitre() + " -Pages: ";
		for (Page pageAnalysee : pagesduchapitre)
			texte += pageAnalysee.getNumPage() + ", ";
		return texte;
	}
	
	
}
