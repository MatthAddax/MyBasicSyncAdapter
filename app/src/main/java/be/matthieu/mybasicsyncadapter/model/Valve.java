package be.matthieu.mybasicsyncadapter.model;

/**
 * Created by Matthieu on 05-02-16  .
 */
public class Valve {
    private long _id;
    private String titre, contenu;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }


}
