import java.util.ArrayList;
import java.util.Vector;

public class ArbreBinDico {
	private ArbreBinDico sag;
	private ArbreBinDico sad;
	private String elem;
	private Vector<Page> pgWith;

	/**
	 * Crée un arbre sans fils contenant un seul élément
	 * @param e l'élément de l'arbre
	 */
	public ArbreBinDico(String e){
		elem = e;
		sag = null;
		sad = null;
		pgWith = new Vector<Page>();
	}

	/**
	 * Crée l'arbre binaire d'une arrayList de T (s'ils sont ordonnés, c'est un ABR).
	 * @param l liste de T pour construire l'arbre binaire
	 */
	public ArbreBinDico(ArrayList<String> l){
		pgWith = new Vector<Page>();
		if (l.size() == 1) {
			elem = l.get(0);
			sag = null;
			sad = null;
		}

		else if (l.size() == 2){
			elem =  l.get(1);
			sag = new ArbreBinDico(l.get(0));
			sad = null;
		}

		else{
			//Choix de l'élément : élément central de la liste
			elem = l.get(l.size()/2);

			//Construction du sag avec la moitié des mots à gauche de la liste
			sag = new ArbreBinDico(new ArrayList<String>(l.subList(0,(l.size()/2))));

			//Construction du sag avec la moitié des mots à droite de la liste
			sad = new ArbreBinDico(new ArrayList<String>(l.subList((l.size()/2)+1, l.size())));
		}
	}

	public int profondeurArbre(){
		if (sad==null && sag==null){
			return 0;
		} else if(sad==null) {
			return 1+ sag.profondeurArbre();
		} else if(sag==null) {
			return 1+ sad.profondeurArbre();
		} else {

			int profSag = sag.profondeurArbre();
			int profSad = sad.profondeurArbre();

			if (profSag > profSad){
				return 1+profSag;
			} else {
				return 1+profSad;
			}
		}
	}

	/**
	 * Renvoie une représentation de l'arbre
	 * @param i profondeur de l'arbre
	 * @return représentation en String de l'arbre
	 */
	public String print(int i) {
		String s = (String)elem+"\t";
		if (sag!=null){
			s+= "\n";
			for (int j=0; j<=i;j++)
				s+= "\t";
			s+= "|----"  + sag.print(i+1);
		}
		if (sad!=null) {
			s+= "\n";
			for (int j=0; j<=i;j++)
				s+= "\t";
			s+="|----"+ sad.print(i+1);
		}
		return s;
	}

	public boolean estFeuille() { return profondeurArbre()==0; }

	/**
	 * Ajoute une page à la liste de pages du noeud courant
	 * @param i
	 */
	public void addPageContenant(Page p){
		pgWith.add(p);
	}

	/**
	 * 
	 * @return la liste de pages contenant ce mot
	 */
	public Vector<Page> getPgWith() {
		return pgWith;
	}

	/**
	 * Teste si un mot appartient au dictionnaire et, si oui, ajoute le numéro de la page le contenant au noeud du mot
	 * @param mot Le mot à tester
	 * @param page La page contenant le mot
	 * @return ArbreBin (la branche) du mot s'il appartient au dictionnaire et auquel il vient donc d'être ajouté. Renvoie null si le mot n'appartient pas au dictionnaire ou a déjà été trouvé pour cette page
	 */
	public ArbreBinDico ajoutePage(String mot, Page page){
		int comparaison = mot.compareTo(elem); //On compare le mot du noeud à celui cherché
		
		if (comparaison == 0){
			//A moins qu'on aie déjà ajouté cette page au noeud...
			//TODO : (Cette façon de faire ne me paraît pas très élégante... ?)
			if(pgWith.contains(page))return null;

			//Si le noeud courant est le mot cherché, on ajoute la page au noeud et on renvoie vrai
			this.addPageContenant(page);
			System.out.println("Le mot "+mot+" est présent dans la page "+page.getIdPage()); //Log de test
			return this;

		} else if (estFeuille()){ //Si le noeud courant n'est pas le mot cherché et qu'on est au bout de l'arbre
			return null;

		} else {
			if (comparaison < 0){ //Le mot cherché est "plus petit" que le courant
				if (sag != null) return sag.ajoutePage(mot, page);
			}
			if (comparaison > 0){ //Le mot cherché est "plus grand" que le courant
				if (sad != null) return sad.ajoutePage(mot, page);
			}
		}
		return null;
	}

	public String getElem() {
		return elem;
	}
	
	
}
