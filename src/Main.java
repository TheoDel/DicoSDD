import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

class Main {

	public static void main(String [] args) {
		if(args.length < 3) //pas assez d'arguments
			System.err.println("Entrez les arguments dans l'ordre : [k] [dictionnaire] [fichiers]");
		else {
			Dictionary dic = new Dictionary(args[1]); //crée le dictionnaire depuis le fichier
			System.err.println("------CONSTRUCTION DU DICTIONNAIRE-----------");
			dic.printKeyABR();
			
			//Récupère les pages
			ArrayList<Page> pages = new ArrayList<Page>();
			int idPage = 0;
			for (int i=2; i < args.length; i++){
				File f = new File(args[i]);
				if (f.isFile()){
					try {
						Scanner sc = new Scanner(f); //scanner pour récupérer le numéro dans la page
						pages.add(new Page(sc.nextInt(),idPage,f));
						sc.close();
					} catch (FileNotFoundException e) {
						System.out.println("Erreur : Fichier inexistant");
					}
				}
				++idPage;
			}
			
			try {
				//Récupère k
				Analyseur a = new Analyseur(dic,pages,Integer.parseInt(args[0]));
				
				System.err.println("\n----------ANALYSE DES PAGES-----------------");
				
				// Complexité totale de l’algorithme:
				// O((nombre de pages)³ * nombre de mots du dictionnaire + nombre de pages * (nombre de mots du dictionnaire)²)
				
				a.analyserPages(); // O((nombre total de pages)³ * nombre de mots du dictionnaire + nombre total de pages * (nombre de mots du dictionnaire)²)
				a.retraitMotsCommuns(); // O(nombre de chapitres * (nombre de mots du dictionnaire)²)
				a.afficherChapitres(); // O(nombre de chapitres * nombre de mots du dictionnaire)
			}
			catch (Exception e) {
				System.out.println("L'analyseur a levé une erreur. Vérifiez que k est bien un entier.");
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
}
