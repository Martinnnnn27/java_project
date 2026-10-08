/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package miniprojetjava;

import java.util.Date;

public class EntreeParc {
    private int id;
    private String nomVisiteur;
    private String typeBillet;
    private double prix;
    private Date dateEntree;

    // Constructeur vide
    public EntreeParc() {
    }

    // Constructeur avec paramètres
    public EntreeParc(String nomVisiteur, String typeBillet, double prix) {
        this.nomVisiteur = nomVisiteur;
        this.typeBillet = typeBillet;
        this.prix = prix;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomVisiteur() {
        return nomVisiteur;
    }

    public void setNomVisiteur(String nomVisiteur) {
        this.nomVisiteur = nomVisiteur;
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