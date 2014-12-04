import java.util.ArrayList;


public class Chapitre {
	private ArrayList<Page> pagesduchapitre;
	private int idChapitre;
	
	public Chapitre(Page p1){
		pagesduchapitre= new ArrayList<Page>();
		pagesduchapitre.add(p1);
		idChapitre = p1.getIdChapitre();
		
	}
	
	public void ajoutePage(Page nouvPage){
		pagesduchapitre.add(nouvPage);
	}
	
	public ArrayList<Page> getPagesDuChapitre(){
		return pagesduchapitre;
	}

	@Override	
	public String toString() {
	String texte="Chapitre "+ idChapitre+ " -Mots: "+pagesduchapitre.get(0).getListeMotsChapitre()+ " -Pages: ";
	
		for (Page pageAnalysee : pagesduchapitre){
			texte+=pageAnalysee.getIdPage()+", ";
		}
		return texte;
	}
	
	
}
