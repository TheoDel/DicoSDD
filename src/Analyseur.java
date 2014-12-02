import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Analyseur {
	private Dictionary dic;
	
	/** La liste des pages à analyser*/
	private ArrayList<Page> pages;
	
	/** Matrice donnant le nombre de mots en commun entre 2 pages*/
	private int[][] motsCommuns;
	
	/** Nombre de mots en commun minimum nécessaire pour dire que 2 mots appartiennent au même chapitre */
	private int k;

	public Analyseur(Dictionary d, ArrayList<Page> p, int k){
		pages = p;
		dic = d;
		motsCommuns = new int[pages.size()][pages.size()];
		this.k = k;
	}

	public void analyserPages(){
		//Boucle sur toutes les pages :
		for(Page pageAnalysee : pages) {
			int numPageActu = pageAnalysee.getIdPage();
			try {
				Scanner sc = new Scanner(pageAnalysee.getFichier());
				//TODO :  séparateur a revoir (renvoie des mots vides)
				sc.useDelimiter("[.,;:?!\n\t ']");
				//Décompose mot par mot
				String motCourant="";
				while (sc.hasNext()){//tant qu'on peut lire le fichier
					motCourant=sc.next().toLowerCase();
					//System.out.println("--->\""+motCourant+"\"<---");
					ArbreBin motDic = dic.ajoutePage(motCourant, pageAnalysee); // On passe au mot suivant. On cherche si le mot appartient au dictionnaire
					
					if (motDic != null){ // Dans le cas où le mot courant appartient au dictionnaire (et n'a pas encore été relevé sur cette page)
						for (Page pageWith : motDic.getPgWith()) { //récupère les pages contenant aussi déjà ce mot
							int numPageCommune = pageWith.getIdPage();
							if (numPageCommune != numPageActu){
								++motsCommuns[(numPageActu-1)][(numPageCommune-1)]; //incrémente le nombre de mots en communs de ces 2 pages
								System.out.println("\tLes pages "+numPageActu+" et "+numPageCommune+" ont "+motsCommuns[numPageActu-1][numPageCommune-1]+" mots en commun.");

								/* TODO : Si k est atteint pour ces deux pages, ils sont du même 
								 * chapitre,faire une union des classes (via la classe Page)
								 */
								if (motsCommuns[numPageActu-1][numPageCommune-1] >= k){
									System.out.println("\t\tLes pages "+numPageActu+" et "+numPageCommune+" sont dans le même chapitre.");
									pageAnalysee.setParent(pageWith);
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
		String texte = "";
		int chapCourant = 0;
		int numChapitre = 0;
		for(Page pageAnalysee : pages) {
			if (pageAnalysee.getIdParentRacine()!= chapCourant){
				numChapitre++;
				System.out.println(texte);
				chapCourant = pageAnalysee.getIdPage();
				texte="Chapitre "+numChapitre+ ": les pages "+chapCourant;
			} else {
				texte += ", "+pageAnalysee.getIdPage();
			}
		}
		System.out.println(texte);
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
