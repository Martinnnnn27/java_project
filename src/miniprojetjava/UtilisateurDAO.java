/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package miniprojetjava;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurDAO {

    public boolean authentifier(String login, String motDePasse) {
        String sql = "SELECT * FROM utilisateur WHERE login = ? AND mot_de_passe = ?";
        
        try (Connection con = ConnexionBDD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, login);
            pst.setString(2, motDePasse);
            
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next(); // Retourne true si un utilisateur correspond
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification : " + e.getMessage());
            return false;
        }
    }
}