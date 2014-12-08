import java.util.ArrayList;


public class CouplePages {
	private ArrayList<String> motsCommuns; // mots communs à toutes les pages du chapitre de page1 et page2
	
	private int page1;
	private int page2;
	
	//CONSTRUCTEUR
	//Complexité O(1)
	public CouplePages(int page1, int page2) {
		super();
		this.page1 = page1;
		this.page2 = page2;
		this.motsCommuns = new ArrayList<String>();
	}
	
	/**
	 * Renvoie si ce couple contient  bien p1 et p2
	 * Compexité O(1)
	 */
	public boolean is(int p1, int p2){
		return ((page1==p1 && page2==p2)||(page1==p2 && page2==p1));
	}
	
	/**
	 * Ajoute un mot à la liste de mots que ces deux pages ont en commun
	 * @param mot
	 * Complexité O(motsCommuns.size())
	 */
	public void ajouteMot(String mot){
		motsCommuns.add(mot);
	}
	
	/**
	 * Determine si ces deux pages sont dans le meme chapitre
	 * @param k le nombre de mots en commun requis pour dire que deux pages sont dans le même chapitre
	 * Complexité O(1)
	 */
	public boolean checkMemeChapitre(int k){
		return (motsCommuns.size()>=k);
	}

	//GETTER
	//Complexité O(1)
	public ArrayList<String> getMotsCommuns() { return motsCommuns; }	
}

