package miniprojetjava;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FenettreStats extends JFrame {

    private JLabel lblTotalVisiteurs;
    private JLabel lblChiffreAffaires;
    private JTable tablePass;
    private DefaultTableModel modelTable;

    public FenettreStats() {
        super("Statistiques du Parc d'Attractions");
        initComponents();
        chargerStatistiques();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(480, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // --- 1. En-tête (Titre) ---
        JLabel lblTitre = new JLabel("Tableau de bord - Statistiques", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitre.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));
        add(lblTitre, BorderLayout.NORTH);

        // --- 2. Centre (Indicateurs clés + Tableau par type de pass) ---
        JPanel panelCentre = new JPanel();
        panelCentre.setLayout(new BoxLayout(panelCentre, BoxLayout.Y_AXIS));
        panelCentre.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Bloc des cartes / indicateurs chiffrés
        JPanel panelCards = new JPanel(new GridLayout(2, 1, 10, 10));
        panelCards.setBorder(BorderFactory.createTitledBorder("Indicateurs Globaux"));

        lblTotalVisiteurs = new JLabel("Nombre total d'entrées : Chargement...", SwingConstants.LEFT);
        lblTotalVisiteurs.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        lblChiffreAffaires = new JLabel("Chiffre d'affaires total : Chargement...", SwingConstants.LEFT);
        lblChiffreAffaires.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblChiffreAffaires.setForeground(new Color(24, 120, 48));

        panelCards.add(lblTotalVisiteurs);
        panelCards.add(lblChiffreAffaires);
        panelCentre.add(panelCards);

        panelCentre.add(Box.createRigidArea(new Dimension(0, 15)));

        // Bloc répartition par pass
        JLabel lblSousTitre = new JLabel("Répartition par type de pass :");
        lblSousTitre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSousTitre.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentre.add(lblSousTitre);
        panelCentre.add(Box.createRigidArea(new Dimension(0, 5)));

        modelTable = new DefaultTableModel(new String[]{"Type de Pass", "Nombre de Billets"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre le tableau en lecture seule
            }
        };
        tablePass = new JTable(modelTable);
        tablePass.setRowHeight(24);
        JScrollPane scrollTable = new JScrollPane(tablePass);
        scrollTable.setPreferredSize(new Dimension(420, 130));
        scrollTable.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentre.add(scrollTable);

        add(panelCentre, BorderLayout.CENTER);

        // --- 3. Bas (Bouton Actualiser & Fermer) ---
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton btnActualiser = new JButton("Actualiser");
        btnActualiser.addActionListener(e -> chargerStatistiques());

        JButton btnFermer = new JButton("Fermer");
        btnFermer.addActionListener(e -> dispose());

        panelBoutons.add(btnActualiser);
        panelBoutons.add(btnFermer);
        add(panelBoutons, BorderLayout.SOUTH);
    }

    /**
     * Exécute les requêtes SQL d'agrégation et alimente l'interface
     */
    public void chargerStatistiques() {
        Connection con = ConnexionBDD.getConnection();
        if (con == null) {
            JOptionPane.showMessageDialog(this,
                    "Impossible de se connecter à la base pour charger les statistiques.",
                    "Erreur BDD", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 1. Nombre total de visiteurs
            String sqlCount = "SELECT COUNT(*) AS total FROM entree_parc";
            try (PreparedStatement pst = con.prepareStatement(sqlCount);
                 ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    lblTotalVisiteurs.setText("Nombre total d'entrées : " + total + " visiteur(s)");
                }
            }

            // 2. Chiffre d'affaires total
            String sqlSum = "SELECT COALESCE(SUM(prix), 0) AS ca_total FROM entree_parc";
            try (PreparedStatement pst = con.prepareStatement(sqlSum);
                 ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    double ca = rs.getDouble("ca_total");
                    lblChiffreAffaires.setText(String.format("Chiffre d'affaires total : %.2f €", ca));
                }
            }

            // 3. Répartition par pass
            modelTable.setRowCount(0); // Réinitialiser le tableau
            String sqlGroup = "SELECT type_pass, COUNT(*) AS nb FROM entree_parc GROUP BY type_pass";
            try (PreparedStatement pst = con.prepareStatement(sqlGroup);
                 ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String pass = rs.getString("type_pass");
                    int nb = rs.getInt("nb");
                    modelTable.addRow(new Object[]{pass, nb});
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du calcul des statistiques : " + ex.getMessage(),
                    "Erreur SQL", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                con.close();
            } catch (SQLException ignored) {}
        }
    }

    // Méthode main optionnelle pour tester ta fenêtre seule
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FenettreStats().setVisible(true);
        });
    }
}