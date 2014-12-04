import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Analyseur {
	private Dictionary dic;
	
	/** La liste des pages à analyser*/
	private ArrayList<Page> pages;
	
	
	/** Nombre de mots en commun minimum nécessaire pour dire que 2 mots appartiennent au même chapitre */
	private int k;

	public Analyseur(Dictionary d, ArrayList<Page> p, int k){
		pages = p;
		dic = d;
		this.k = k;
	}

	public void analyserPages(){
		//Boucle sur toutes les pages :
		for(Page pageAnalysee : pages) {
			int numPageActu = pageAnalysee.getIdPage();
			
			//Cree des couples entre la page analysee eet toutes les pages deja analysees
			ArrayList<CouplePages> couplesPages = new ArrayList<CouplePages>();
			for(int i = 1; i<numPageActu; i++){
				couplesPages.add(new CouplePages(i, numPageActu));
			}
			
			try {
				Scanner sc = new Scanner(pageAnalysee.getFichier());
				//TODO :  séparateur a revoir (renvoie des mots vides)
				sc.useDelimiter("[.,;:?!\n\t\\s']");
				//Décompose mot par mot
				String motCourant="";
				while (sc.hasNext()){//tant qu'on peut lire le fichier
					motCourant=sc.next().toLowerCase();
					//System.out.println("--->\""+motCourant+"\"<---");
					ArbreBinDico motDic = dic.ajoutePage(motCourant, pageAnalysee); // On passe au mot suivant. On cherche si le mot appartient au dictionnaire
					
					if (motDic != null){ // Dans le cas où le mot courant appartient au dictionnaire (et n'a pas encore été relevé sur cette page)
						
						for (Page pageWith : motDic.getPgWith()) { //récupère les pages contenant aussi déjà ce mot
							int numPageCommune = pageWith.getIdPage();
							if (numPageCommune != numPageActu){
								//Ajoute le mot que ces pages ont en commun
								couplesPages.get(numPageCommune-1).ajouteMot(motDic.getElem());
								
								/* Si k est atteint pour ces deux pages, ils sont du même 
								 * chapitre,faire une union des classes (via la classe Page)
								 */
								if (couplesPages.get(numPageCommune-1).checkMemeChapitre(k)){
									System.out.println("\t\tLes pages "+numPageActu+" et "+numPageCommune+" sont dans le même chapitre.");
									pageAnalysee.setParent(pageWith.getParentRacine());
									
									pageAnalysee.addListeMot(couplesPages.get(numPageCommune-1).getMotsCommuns());
									System.out.println("\t\t"+pageAnalysee.getListeMotsChapitre());
									
									//pageAnalysee.getParentRacine().addListeMot(couplesPages.get(numPageCommune-1).getMotsCommuns());
									
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
		
		//On a ainsi dégagé un ensemble de chapitres qu'on va renvoyer d'une façon ou d'une autre
		String texte = "";
		int chapCourant = 0;
		int numChapitre = 0;
		
		//Fusion des mots-clés des chapitres
		for(Page pageAnalysee : pages) {
			pageAnalysee.getParentRacine().addListeMot(pageAnalysee.getListeMotsChapitre());
		}
		
		for(Page pageAnalysee : pages) {
			if (pageAnalysee.getIdParentRacine()!= chapCourant){
				numChapitre++;
				System.out.println(texte);
				chapCourant = pageAnalysee.getIdPage();
				texte="Chapitre "+numChapitre + " - Mots du chapitre : "+ pageAnalysee.getListeMotsChapitre() +": les pages "+chapCourant; //
			} else {
				texte += ", "+pageAnalysee.getIdPage();//+ " - Mots du chapitre : "+ pageAnalysee.getListeMotsChapitre();
				pageAnalysee.getParentRacine().addListeMot(pageAnalysee.getListeMotsChapitre());
			}
		}
		System.out.println(texte);
	}

}
