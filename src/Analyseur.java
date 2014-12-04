import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Analyseur {
	private Dictionary dic;
	
	/** La liste des pages à analyser*/
	private ArrayList<Page> pages;
	
	/** Liste des chapitres dégagés*/
	private ArrayList<Chapitre> chapitres;
	
	/** Nombre de mots en commun minimum nécessaire pour dire que 2 mots appartiennent au même chapitre */
	private int k;
	
	/** Indique si les pages ont été analysées*/
	private boolean analyseFaite;

	public Analyseur(Dictionary d, ArrayList<Page> p, int k){
		pages = p;
		dic = d;
		this.k = k;
		analyseFaite = false;
		chapitres = new ArrayList<Chapitre>();
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
					motCourant=sc.next().toLowerCase(); // On passe au mot suivant (qu'on met en minuscules, pour ignorer la casse)
					//System.out.println("--->\""+motCourant+"\"<---"); //Ligne de debug Pour voir tous les mots parsés
					
					ArbreBinDico feuilleDico = dic.ajoutePage(motCourant, pageAnalysee); // On cherche si le mot appartient au dictionnaire
					
					if (feuilleDico != null){ // Dans le cas où le mot courant appartient au dictionnaire... (et n'a pas encore été relevé sur cette page)
						
						for (Page pageWith : feuilleDico.getPgWith()) { //On récupère les pages contenant aussi déjà ce mot (enregistrees sur la feuille du mot)
							int numPageCommune = pageWith.getIdPage();
							if (numPageCommune != numPageActu){
								//Ajoute le mot que ces pages ont en commun dans leur couple
								couplesPages.get(numPageCommune-1).ajouteMot(feuilleDico.getElem());
								
								// Si le couple de ces deux pages contient au moins k mots, ils sont du même chapitre,
								if (couplesPages.get(numPageCommune-1).checkMemeChapitre(k)){
									System.out.println("\t\tLes pages "+numPageActu+" et "+numPageCommune+" sont dans le même chapitre.");
									
									//On fait donc une union de classe entre ces deux pages
									pageAnalysee.setParent(pageWith.getParentRacine());
									
									//Et on ajoute les mots du chapitre à la page courante
									//pageAnalysee.fusionListeMot(couplesPages.get(numPageCommune-1).getMotsCommuns());
									//System.out.println("\t\t"+pageAnalysee.getListeMotsChapitre());
									
									/* On pourrait aussi directement fusionner les mots à la page représentante de la classe 
									 * (la page représentante du chapitre, donc) en supprimant les doublons, 
									 * Mais on a préféré le faire après pour ne pas augmenter la complexité 
									 * de cette boucle car l'opération est couteuse. Pour tester, il faut décomenter la
									 * ligne suivante et commenter les deux opérations précédentes.
									 */
									pageAnalysee.getParentRacine().fusionSansDoublonListeMot(couplesPages.get(numPageCommune-1).getMotsCommuns());
									
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
		
		//Fusion des mots-clés des chapitres et identification des pages par chapitre
		int numChapitre=0;
		
		for(Page pageAnalysee : pages) {
			if (pageAnalysee.isParent()){ //Page représentante du chapitre
				numChapitre++;
				pageAnalysee.setIdChapitre(numChapitre);
				chapitres.add(new Chapitre(pageAnalysee));
			}
			else { //Page non représentante du chapitre
				pageAnalysee.getParentRacine().fusionSansDoublonListeMot(pageAnalysee.getListeMotsChapitre()); //Fusion des mots clés
				chapitres.get(pageAnalysee.getIdChapitre()-1).ajoutePage(pageAnalysee);
			}
		}
		
		analyseFaite = true;
	}
	
	public void afficherChapitres(){
		if (!analyseFaite){
			System.out.println("Les pages n'ont pas été analysées");
			return;
		}

		for(Chapitre chapitreAnalyse : chapitres){
			System.out.println(chapitreAnalyse);
		}
	}
	
}
