package miniprojetjava;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntreeParcDAO {

    public List<EntreeParc> getAll() {
        List<EntreeParc> liste = new ArrayList<>();
        String sql = "SELECT id, nom_visiteur, type_billet, prix, date_entree FROM entree_parc";

        try (Connection con = ConnexionBDD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                EntreeParc ep = new EntreeParc(
                        rs.getInt("id"),
                        rs.getString("nom_visiteur"),
                        rs.getString("type_billet"),
                        rs.getDouble("prix"),
                        rs.getTimestamp("date_entree")
                );
                liste.add(ep);
            }
        } catch (SQLException e) {
            System.err.println("Erreur getAll : " + e.getMessage());
        }
        return liste;
    }

    public boolean ajouter(EntreeParc ep) {
        String sql = "INSERT INTO entree_parc (nom_visiteur, type_billet, prix, date_entree) VALUES (?, ?, ?, NOW())";
        try (Connection con = ConnexionBDD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, ep.getNomVisiteur());
            pst.setString(2, ep.getTypeBillet());
            pst.setDouble(3, ep.getPrix());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur ajouter : " + e.getMessage());
            return false;
        }
    }

    public boolean modifier(EntreeParc ep) {
        String sql = "UPDATE entree_parc SET nom_visiteur = ?, type_billet = ?, prix = ? WHERE id = ?";
        try (Connection con = ConnexionBDD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, ep.getNomVisiteur());
            pst.setString(2, ep.getTypeBillet());
            pst.setDouble(3, ep.getPrix());
            pst.setInt(4, ep.getId());

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