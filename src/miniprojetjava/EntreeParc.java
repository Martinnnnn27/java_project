package miniprojetjava;

import java.util.Date;

public class EntreeParc extends Personne {
    private int id;
    private String prenomVisiteur;
    private String typeBillet;
    private double prix;
    private Date dateEntree;

    // Constructeur vide
    public EntreeParc() {
        super();
    }

    // Constructeur principal pour l'ajout (avec nom, prénom, type, prix)
    public EntreeParc(String nomVisiteur, String prenomVisiteur, String typeBillet, double prix) {
        super(nomVisiteur);
        this.prenomVisiteur = prenomVisiteur;
        this.typeBillet = typeBillet;
        this.prix = prix;
        this.dateEntree = new Date();
    }

    // Constructeur pour rétrocompatibilité à 3 paramètres (définit prénom à "Visiteur")
    public EntreeParc(String nomVisiteur, String typeBillet, double prix) {
        this(nomVisiteur, "Visiteur", typeBillet, prix);
    }

    // Constructeur complet (utile pour charger depuis la BDD / DAO)
    public EntreeParc(int id, String nomVisiteur, String prenomVisiteur, String typeBillet, double prix, Date dateEntree) {
        super(nomVisiteur);
        this.id = id;
        this.prenomVisiteur = prenomVisiteur;
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

    public String getNomVisiteur() {
        return this.nom;
    }

    public void setNomVisiteur(String nomVisiteur) {
        this.nom = nomVisiteur;
    }

    public String getPrenomVisiteur() {
        return prenomVisiteur;
    }

    public void setPrenomVisiteur(String prenomVisiteur) {
        this.prenomVisiteur = prenomVisiteur;
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