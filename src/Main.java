import java.util.ArrayList;
import java.io.File;

class Main {
	public static void main(String [] args) {
		if (args.length < 3)//pas assez d'arguments
			System.out.println("Entrez les arguments dans l'ordre : [k] [dictionnaire] [fichiers]");
		else {
			Dictionary dic = new Dictionary(args[1]);//crée le dictionnaire depuis le fichier
			dic.printKeyABR();

			//Récupère les pages
			ArrayList<Page> pages = new ArrayList<Page>();
			int numPage = 0;
			for (int i=2; i < args.length; i++){
				File f = new File(args[i]);
				if (f.isFile()){
					++numPage;
					pages.add(new Page(numPage, f));
				}
			}

			try {
				//Récupère k
				Analyseur a = new Analyseur(dic,pages,Integer.parseInt(args[0]));
				a.analyserPages();
			}
			catch (Exception e) {
				System.out.println("Erreur : Le k rentré n'est pas un entier");
				System.exit(0);
			}
		}
	}
}
