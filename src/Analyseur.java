import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Analyseur {
	private Dictionary dic;
	private ArrayList<Page> pages;
	private int[][] motsCommuns;
	/** Nombre de mots en commun minimum nécessaire pour dire que 2 mots appartiennent au même chapitre */
	private int k;

	public Analyseur(Dictionary d, ArrayList<Page> p, int k){
		pages = p;
		dic = d;
		motsCommuns = new int[pages.size()][pages.size()];
		this.k = k;
	}
	/* TODO : On travaille ici avec des entiers pour désigner les pages, peut-être serait-il plus 
	 * propre de travailler avec un classe Page contenant son File et son identifiant ? Cela pourrait
	 * aussi servir pour les classes-union, au moment de rassembler les pages en chapitre.
	 */

	public void analyserPages(){
		//Boucle sur toutes les pages :
		for(Page f : pages) {
			int numPageActu = f.getIdPage();
			try {
				Scanner sc = new Scanner(f.getFichier());
				sc.useDelimiter("[.,;:?! ']");
				//Décompose mot par mot
				while (sc.hasNext()){//tant qu'on peut lire le fichier
					ArbreBin motDic = dic.ajoutePage(sc.next(), f); // On passe au mot suivant. On cherche si le mot appartient au dictionnaire

					if (motDic != null){ // Dans le cas où le mot courant appartient au dictionnaire (et n'a pas encore été relevé sur cette page)
						for (Page pageWith : motDic.getPgWith()) { //récupère les identifiants des pages contenant aussi déjà ce mot
							int numPageCommune = pageWith.getIdPage();
							if (numPageCommune != numPageActu){
								++motsCommuns[(numPageActu-1)][(numPageCommune-1)]; //incrémente le nombre de mots en communs de ces 2 pages
								System.out.println("\tLes pages "+numPageActu+" et "+numPageCommune+" ont "+motsCommuns[numPageActu-1][numPageCommune-1]+" mots en commun.");

								/* TODO : Si k est atteint pour ces deux pages, ils sont du même 
								 * chapitre,faire une union des classes (via la classe Page)
								 */
								if (motsCommuns[numPageActu-1][numPageCommune-1] >= k){
									System.out.println("\t\tLes pages "+numPageActu+" et "+numPageCommune+" sont dans le même chapitre.");
								}
							}
						}
					}
				}
				sc.close();
			} catch (FileNotFoundException e) {
				System.out.println("Erreur : Fichier inexistant");
			}
		}
		affichageMatriceMotsPartages();
		//On a ainsi dégagé un ensemble de chapitres qu'on va renvoyer d'une façon ou d'une autre
	}

	/**
	 * Affiche la matrice du nombre de mots partagés entre les pages
	 */
	public void affichageMatriceMotsPartages(){
		String ligneHaut1 = "   ";
		for (int j=0; j<pages.size(); j++){
			ligneHaut1 = ligneHaut1 + (j+1) + " ";
		}
		System.out.println(ligneHaut1);
		for (int i=0; i<pages.size(); i++){
			String ligne = (i+1)+"||";
			for (int j=0; j<pages.size(); j++){
				ligne = ligne + (motsCommuns[i][j])+"|";
			}
			System.out.println(ligne);
		}
	}
}
