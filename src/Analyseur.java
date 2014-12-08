import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Analyseur {
	private Dictionary dic; //le dictionnaire de mots clés
	private ArrayList<Page> pages; //La liste des pages à analyser
	private ArrayList<Chapitre> chapitres; //Liste des chapitres dégagés
	private int k; //Nombre de mots en commun minimum nécessaire pour dire que deux pages appartiennent au même chapitre
	private boolean analyseFaite; //Indique si les pages ont été analysées

	//CONSTRUCTEUR
	//Complexité : O(1)
	public Analyseur(Dictionary d, ArrayList<Page> p, int k){
		pages = p;
		dic = d;
		this.k = k;
		analyseFaite = false;
		chapitres = new ArrayList<Chapitre>();
	}

	//ANALYSE DES PAGES
	// O((nombre total de pages)³ * nombre de mots du dictionnaire + nombre total de pages * (nombre de mots du dictionnaire)²)
	public void analyserPages() {
	
		//Cree des couples entre la page analysee et toutes les pages deja analysees
		ArrayList<CouplePages> couplesPages = new ArrayList<CouplePages>(pages.size());
		//On réserve assez de capacité pour touts les couples à venir, et les ajouts passent ainsi de O(n) à O(1).
		
		//Boucle sur toutes les pages :
		for(Page pageAnalysee : pages) { // O((nombre total de pages)³ * nombre de mots du dictionnaire)
			int idPageActu = pageAnalysee.getIdPage(); //id de la page actuelle
			
			//On ajoute une place dans couplesPages
			couplesPages.add(null); // Comme expliqué ci-dessus, O(1)
			
			//On remplit couplesPages de façon appropriée
			for(int i = 0; i<idPageActu; i++) //O(idPageActu) 
				couplesPages.set(i, new CouplePages(i,idPageActu));
				
			try {
				Scanner sc = new Scanner(pageAnalysee.getFichier());
				sc.useDelimiter("[.,;:?!\n\t\\s']+");
				//Décompose mot par mot
				String motCourant="";
				// O((nombre total de pages)² * nombre de mots du dictionnaire)
				while (sc.hasNext()){//tant qu'on peut lire le fichier
					motCourant=sc.next().toLowerCase(); // On passe au mot suivant (qu'on met en minuscules, pour ignorer la casse)
					//System.out.println("--->\""+motCourant+"\"<---"); //Ligne de debug Pour voir tous les mots parsés
					
					ArbreBinDico feuilleDico = dic.ajoutePage(motCourant, pageAnalysee); // On cherche si le mot appartient au dictionnaire
					// O(nombre total de mots du dictionnaire + nombre total de pages)
					
					if (feuilleDico != null){ // Dans le cas où le mot courant appartient au dictionnaire... (et n'a pas encore été relevé sur cette page)
						
						//Complexité de la boucle for:
						// O(nombres de pages contenant motCourant * (nombre de mots de pageAnalysee + nombre de mots du chapitre de son couple))
						for (Page pageWith : feuilleDico.getPgWith()) { //On récupère les pages contenant déjà ce mot (enregistrees sur la feuille du mot)
							int numPageCommune = pageWith.getIdPage();
							if (numPageCommune != idPageActu){ //O(nombre d mots du chapitre + nombre de mots de pageAnalysee)
								//Ajoute le mot que ces pages ont en commun dans leur couple
								CouplePages couple = couplesPages.get(numPageCommune);
								couple.ajouteMot(feuilleDico.getElem()); // O(nombre de mots du chapitre)
								
								// Si le couple de ces deux pages contient au moins k mots, ils sont du même chapitre,
								if (couplesPages.get(numPageCommune).checkMemeChapitre(k)){ // O(nombre de mots de pageAnalysee + nombre de mots du chapitre) 
									System.err.println("\t\tLes pages " + idPageActu + " et " + numPageCommune + " sont dans le même chapitre.");
									
									//On fait donc une union de classe entre ces deux pages
									pageAnalysee.setParent(pageWith.getParentRacine());
									
									//Et on ajoute les mots du chapitre à la page courante
									pageAnalysee.fusionListeMot(couple.getMotsCommuns()); // O(nombre de mots de pageAnalysee + nombre de mots du chapitre)
									
									System.err.println("\t\t"+pageAnalysee.getListeMotsChapitre());
									
									/* On pourrait aussi directement fusionner les mots à la page représentante de la classe 
									 * (la page représentante du chapitre, donc) en supprimant les doublons, 
									 * Mais on a préféré le faire après pour ne pas augmenter la complexité 
									 * de cette boucle car l'opération est couteuse. Pour tester, il faut décomenter la
									 * ligne suivante et commenter les deux opérations précédentes.
									 */
									//pageAnalysee.getParentRacine().fusionSansDoublonListeMot(couplesPages.get(numPageCommune-1).getMotsCommuns());
									
								}
							}
						}
					}
				}
				sc.close();
			} catch (FileNotFoundException e) {
				System.err.println("Erreur : Fichier inexistant");
			}
		}
		
		//Fusion des mots-clés des chapitres et identification des pages par chapitre
		int numChapitre=0;
		
		for(Page pageAnalysee : pages) { // O(nombre de pages * nombre de chapitres + nombre de pages * (nombre de mots du dictionnaire)²)
			if (pageAnalysee.isParent()){ //Page représentante du chapitre
				numChapitre++;
				pageAnalysee.setIdChapitre(numChapitre);
				chapitres.add(new Chapitre(pageAnalysee)); // O(chapitres.size())
			}
			else { //Page non représentante du chapitre
				pageAnalysee.getParentRacine().fusionSansDoublonListeMot(pageAnalysee.getListeMotsChapitre()); //Fusion des mots clés
				// O((nombre de mots du dictionnaire)²)
				
				chapitres.get(pageAnalysee.getIdChapitre()-1).ajoutePage(pageAnalysee);
			}
		}
		
		analyseFaite = true;
	}
	
	//RETRAIT DES MOTS COMMUNS ENTRE LES CHAPITRES
	//Complexité: O(nombre de chapitres * (nombre de mots du dictionnaire)²)
	public void retraitMotsCommuns() {
		if (!analyseFaite){
			System.err.println("Les pages n'ont pas été analysées");
			return;
		}

		// Retrait des mots communs aux chapitres
		// Ce processus peut être amélioré en termes de complexité en utilisant des ARN ou des AVL.

		ArrayList<String> motsCommuns = new ArrayList<String>(dic.getKeyWords().size()); // mots trouvés au moins une fois dans des chapitres
		ArrayList<String> motsMultiples = new ArrayList<String>(dic.getKeyWords().size()); // mots trouvés au moins deux fois dans des chapitres
		// On réserve de la place dans les deux tableaux pour que les ajouts ne prennent qu’un temps constant par la suite.
		// Temps d’allocation initial: O(nombre de mots du dictionnaire)
		
		for(Chapitre chapitre : chapitres) // O(nombre de chapitres * nombre de mots du dictionnaire)
			for(String mot : chapitre.getPagesDuChapitre().get(0).getListeMotsChapitre()) // O(nombre de mots du dictionnaire)
				if(motsCommuns.contains(mot)) {
					if(!motsMultiples.contains(mot))
						motsMultiples.add(mot);
				} else
					motsCommuns.add(mot);
		
		for(Chapitre chapitre : chapitres) // O(nombre de chapitres * (nombre de mots du dictionnaire)²)
			for(String mot : motsMultiples) // O((nombre de mots du dictionnaire)²)
				chapitre.getPagesDuChapitre().get(0).getListeMotsChapitre().remove(mot); // O(nombre de mots dans la liste de mots du chapitre)
		
		analyseFaite = true;
	}
	
	// AFFICHAGE FINAL
	// Complexité: O(nombre de chapitres * nombre de mots du dictionnaire)
	public void afficherChapitres(){
		if (!analyseFaite){
			System.err.println("Les pages n'ont pas été analysées");
			return;
		}
		
		for(Chapitre chapitreAnalyse : chapitres) // O(nombre de mots du chapitre * nombre de mots du dictionnaire)
			System.out.println(chapitreAnalyse); // O(nombre de mots du dictionnaire)
	}
}
