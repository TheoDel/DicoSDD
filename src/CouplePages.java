import java.util.ArrayList;


public class CouplePages {
	private ArrayList<String> motsCommuns;
	private int page1;
	private int page2;
	
	
	public CouplePages(int page1, int page2) {
		super();
		this.page1 = page1;
		this.page2 = page2;
		this.motsCommuns = new ArrayList<String>();
	}
	
	/**
	 * Renvoie si ce couple contient  bien p1 et p2
	 * @param p1
	 * @param p2
	 * @return
	 */
	public boolean is(int p1, int p2){
		return ((page1==p1 && page2==p2)||(page1==p2 && page2==p1));
	}
	
	/**
	 * Ajoute un mot à la liste de mots que ces deux pages ont en commun
	 * @param mot
	 */
	public void ajouteMot(String mot){
		motsCommuns.add(mot);
	}
	
	/**
	 * Determine si ces deux pages sont dans le meme chapitre
	 * @param k le nombre de mots en commun requis pour dire que deux pages sont dans le même chapitre
	 * @return
	 */
	public boolean checkMemeChapitre(int k){
		return (motsCommuns.size()>=k);
	}

	public ArrayList<String> getMotsCommuns() {
		return motsCommuns;
	}
	
	
}
