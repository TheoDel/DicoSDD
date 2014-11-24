import java.io.File;


public class Page {

	private int idPage;
	private File fichier;
	private Page parent;

	public Page(int id, File f){
		idPage = id;
		fichier = f;
		parent = null;
	}

	public Page getParent() {
		return parent;
	}

	public Page getParentRacine(){
		if (parent == null)
			return this;
		else
			return parent.getParentRacine();
	}

	public void setParent(Page parent) {
		this.parent = parent;
	}

	public int getIdPage() {
		return idPage;
	}

	public int getIdRepresentant(){
		return getParentRacine().idPage;
	}

	public File getFichier() {
		return fichier;
	}
}
