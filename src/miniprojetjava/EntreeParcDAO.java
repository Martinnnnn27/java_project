package miniprojetjava;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntreeParcDAO {

    public List<EntreeParc> getAll() {
        List<EntreeParc> liste = new ArrayList<>();
        // Ajout de prenom_visiteur dans la requête SELECT
        String sql = "SELECT id, nom_visiteur, prenom_visiteur, type_pass, prix FROM entree_parc";

        try (Connection con = ConnexionBDD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                EntreeParc ep = new EntreeParc(
                        rs.getInt("id"),
                        rs.getString("nom_visiteur"),
                        rs.getString("prenom_visiteur"), // Récupère le prénom
                        rs.getString("type_pass"),
                        rs.getDouble("prix"),
                        null
                );
                liste.add(ep);
            }
        } catch (SQLException e) {
            System.err.println("Erreur getAll : " + e.getMessage());
        }
        return liste;
    }

    public boolean ajouter(EntreeParc ep) {
        String sql = "INSERT INTO entree_parc (nom_visiteur, prenom_visiteur, type_pass, zone_attraction, prix) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConnexionBDD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, ep.getNomVisiteur());
            // Utilise le prénom s'il existe, sinon met "Visiteur" par défaut
            String prenom = (ep.getPrenomVisiteur() != null && !ep.getPrenomVisiteur().trim().isEmpty()) 
                            ? ep.getPrenomVisiteur() : "Visiteur";
            pst.setString(2, prenom);
            pst.setString(3, ep.getTypeBillet());
            pst.setString(4, "Zone Principale");
            pst.setDouble(5, ep.getPrix());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur ajouter : " + e.getMessage());
            return false;
        }
    }

    public boolean modifier(EntreeParc ep) {
        String sql = "UPDATE entree_parc SET nom_visiteur = ?, prenom_visiteur = ?, type_pass = ?, prix = ? WHERE id = ?";
        try (Connection con = ConnexionBDD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, ep.getNomVisiteur());
            pst.setString(2, ep.getPrenomVisiteur() != null ? ep.getPrenomVisiteur() : "Visiteur");
            pst.setString(3, ep.getTypeBillet());
            pst.setDouble(4, ep.getPrix());
            pst.setInt(5, ep.getId());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur modifier : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM entree_parc WHERE id = ?";
        try (Connection con = ConnexionBDD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur supprimer : " + e.getMessage());
            return false;
        }
    }
}