package be.matthieu.mybasicsyncadapter.model;

/**
 * Created by Matthieu on 10/02/2016.
 */
public class Horaire {
    private long _id;
    private int start_unix, end_unix;
    private String intitule, professeur, local;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) throws IllegalArgumentException{
        if(_id < 0)
            throw new IllegalArgumentException("ID must be positive");

        this._id = _id;
    }

    public int getStart_unix() {
        return start_unix;
    }

    public void setStart_unix(int start_unix) {
        this.start_unix = start_unix;
    }

    public int getEnd_unix() {
        return end_unix;
    }

    public void setEnd_unix(int end_unix) {
        this.end_unix = end_unix;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getProfesseur() {
        return professeur;
    }

    public void setProfesseur(String professeur) {
        this.professeur = professeur;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
