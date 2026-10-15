package miniprojetjava;

import java.util.Date;

public class EntreeParc extends Personne {
    private int id;
    private String typeBillet;
    private double prix;
    private Date dateEntree;

    // Constructeur vide
    public EntreeParc() {
        super();
    }

    // Constructeur principal
    public EntreeParc(String nomVisiteur, String typeBillet, double prix) {
        super(nomVisiteur);
        this.typeBillet = typeBillet;
        this.prix = prix;
        this.dateEntree = new Date();
    }

    // Constructeur complet avec ID et Date (utile pour le DAO / affichage)
    public EntreeParc(int id, String nomVisiteur, String typeBillet, double prix, Date dateEntree) {
        super(nomVisiteur);
        this.id = id;
        this.typeBillet = typeBillet;
        this.prix = prix;
        this.dateEntree = dateEntree;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Méthodes pour compatibilité avec le code de ton collègue
    public String getNomVisiteur() {
        return this.nom;
    }

    public void setNomVisiteur(String nomVisiteur) {
        this.nom = nomVisiteur;
    }

    public String getTypeBillet() {
        return typeBillet;
    }

    public void setTypeBillet(String typeBillet) {
        this.typeBillet = typeBillet;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Date getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(Date dateEntree) {
        this.dateEntree = dateEntree;
    }
}