import java.util.ArrayList;

public class ArbreBinDico {
	
	//VARIABLES
	private ArbreBinDico sag; //sous-arbre gauche ou null
	private ArbreBinDico sad; //sous-arbre droit ou null
	private String elem; //étiquette du noeud
	private ArrayList<Page> pgWith; //liste des pages contenant l'étiquette comme mot clé
	//On aurait pu utiliser un AVL ou ARN pour stocker l'ensemble des pages, ce qui aurait diminué la complexité des opérations
	//d'ajouts et de recherche de O(n) à O(log(n)), où n = pgWith.size(), le nombre de pages associées aux mots clés.

	/**
	 * Crée un arbre sans fils contenant un seul élément
	 * @param e l'élément de l'arbre
	 * Complexité : O(1)
	 */
	public ArbreBinDico(String e){
		elem = e;
		sag = null;
		sad = null;
		pgWith = new ArrayList<Page>();
	}

	/**
	 * Crée l'arbre binaire d'une arrayList de T (s'ils sont ordonnés, c'est un ABR).
	 * @param l liste de T pour construire l'arbre binaire
	 * Complexité O(l.size()) où l.size() = nombre de mots du dictionnaire
	 */
	public ArbreBinDico(ArrayList<String> l) {
		pgWith = new ArrayList<Page>(); 
		
		if(l.size() == 1) { //O(1)
			elem = l.get(0);
			sag = null;
			sad =  null;
		}

		else if(l.size() == 2) { //O(1) 
			elem =  l.get(1);
			sag = new ArbreBinDico(l.get(0));
			sad = null;
		}

		else { //O(l.size())
			//Choix de l'élément : élément central de la liste
			elem = l.get(l.size()/2); 

			//Construction du sag avec la moitié des mots à gauche de la liste
			//O(l.size()/2)
			sag = new ArbreBinDico(new ArrayList<String>(l.subList(0,(l.size()/2))));

			//Construction du sag avec la moitié des mots à droite de la liste
			//O(l.size()/2)
			sad = new ArbreBinDico(new ArrayList<String>(l.subList((l.size()/2)+1, l.size())));
		}
	}

	//PROFONDEUR ARBRE
	//Complexité : O(n) où n = nombre de sous-noeuds total
	public int profondeurArbre() {		
		return Math.max(sag==null ? 0 : 1 + sag.profondeurArbre(), sad==null ? 0 : 1 + sad.profondeurArbre());
	}
	
	

	/**
	 * Renvoie une représentation de l'arbre
	 * @param i profondeur de l'arbre
	 * @return représentation en String de l'arbre
	 * Complexité : O(n) où n = nombre de sous-noeuds total
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

	//Complexité : O(1)
	public boolean estFeuille() { return sag==null && sag==null; }

	//AJOUTER PAGE
	//Complexité : O(pgWith.size()) (nombre de pages du noeud courant)
	public void addPageContenant(Page p){ pgWith.add(p); }

	//GETTERS
	//Complexités : O(1)
	public ArrayList<Page> getPgWith() { return pgWith; }
	public String getElem() { return elem; }

	/**
	 * Teste si un mot appartient au dictionnaire et, si oui, ajoute le numéro de la page le contenant au noeud du mot
	 * @param mot Le mot à tester
	 * @param page La page contenant le mot
	 * @return ArbreBin (la branche) du mot s'il appartient au dictionnaire et auquel il vient donc d'être ajouté. Renvoie null si le mot n'appartient pas au dictionnaire ou a déjà été trouvé pour cette page
	 * Complexité : O(nbsousnoeuds + nbpages) où nbsousnoeuds = nombre total de sous noeuds de l'arbre courant,
	 * et nbpages le nombre de pages totales
	 */
	public ArbreBinDico ajoutePage(String mot, Page page) {
		
		int comparaison = mot.compareTo(elem); //On compare le mot du noeud à celui cherché
		//On simplifie la complexité en O(1), en supposant que la taille des mots ne sera jamais significative
		
		if(comparaison == 0) { //si l'étiquette du noeud courant est le mot recherché, O(pgWith.size())

			if(pgWith.contains(page)) return null; //Si la page était déjà dans la liste, on retourne null. O(pgWith.size())

			//Sinon, on ajoute la page au noeud et on renvoie le noeud courant
			this.addPageContenant(page); // O(pgWith.size())
			System.err.println("Le mot " + mot + " est présent dans la page " + page.getIdPage()); //log de l'opération
			return this;

		} else if (estFeuille()) { //Si le noeud courant n'est pas le mot cherché et qu'on est au bout de l'arbre
			return null; //le noeud n'a pas été trouvé
		} else if(comparaison < 0 && sag != null) { //Le mot cherché est "plus petit" que le courant
			return sag.ajoutePage(mot, page); //O(nbsousnoeudsgauches + nbpages) 
		} else if(comparaison > 0 && sad != null) { //Le mot cherché est "plus grand" que le courant
			return sad.ajoutePage(mot, page); //O(nbsousnoeudsdroits + nbpages)
		}
		return null;
	}
}

