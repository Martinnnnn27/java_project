package miniprojetjava;

/**
 * Fenetre principale de gestion du parc d'attractions.
 */
public class FenettrePrincipale extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FenettrePrincipale.class.getName());

    public FenettrePrincipale() {
        // 1. Initialiser les composants graphiques
        initComponents();

        btnEnregistrer.setText("Enregistrer");

        // 2. Barre de menus
        javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();

        // Menu Fichier
        javax.swing.JMenu menuFichier = new javax.swing.JMenu("Fichier");

        javax.swing.JMenuItem itemActualiser = new javax.swing.JMenuItem("Actualiser");
        // Recharge les données depuis la base pour refléter les changements récents.
        itemActualiser.addActionListener(e -> chargerTableau());

        javax.swing.JMenuItem itemRecherche = new javax.swing.JMenuItem("Rechercher un visiteur");
        itemRecherche.addActionListener(e -> {
            // Demande un nom puis sélectionne la première correspondance dans le tableau.
            String nom = javax.swing.JOptionPane.showInputDialog(this, "Entrez le nom du visiteur à surligner :");
            if (nom != null && !nom.trim().isEmpty()) {
                boolean trouve = false;
                for (int i = 0; i < tableEntrees.getRowCount(); i++) {
                    Object val = tableEntrees.getValueAt(i, 1);
                    if (val != null && val.toString().equalsIgnoreCase(nom.trim())) {
                        tableEntrees.setRowSelectionInterval(i, i);
                        trouve = true;
                        break;
                    }
                }
                if (!trouve) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Aucun visiteur trouvé avec ce nom.");
                }
            }
        });

        javax.swing.JMenuItem itemExport = new javax.swing.JMenuItem("Exporter en CSV...");
        itemExport.addActionListener(e -> {
            // Laisse l'utilisateur choisir où enregistrer le fichier CSV.
            javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
            chooser.setDialogTitle("Enregistrer les entrées");
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Fichiers CSV (*.csv)", "csv"));

            if (chooser.showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
                java.io.File file = chooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".csv")) {
                    file = new java.io.File(file.getParentFile(), file.getName() + ".csv");
                }

                try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(file))) {
                    // Écrit l'en-tête puis les lignes actuellement affichées dans le tableau.
                    pw.println("ID;Nom;Prenom;TypePass;Zone;Prix");
                    for (int i = 0; i < tableEntrees.getRowCount(); i++) {
                        pw.println(
                                tableEntrees.getValueAt(i, 0) + ";" +
                                        tableEntrees.getValueAt(i, 1) + ";" +
                                        tableEntrees.getValueAt(i, 2) + ";" +
                                        tableEntrees.getValueAt(i, 3) + ";" +
                                        tableEntrees.getValueAt(i, 4) + ";" +
                                        tableEntrees.getValueAt(i, 5)
                        );
                    }
                    javax.swing.JOptionPane.showMessageDialog(this, "Export réussi dans : " + file.getName());
                } catch (java.io.IOException ex) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Erreur lors de l'export : " + ex.getMessage(), "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        javax.swing.JMenuItem itemQuitter = new javax.swing.JMenuItem("Quitter");
        // Ferme immédiatement l'application.
        itemQuitter.addActionListener(e -> System.exit(0));

        menuFichier.add(itemActualiser);
        menuFichier.add(itemRecherche);
        menuFichier.add(itemExport);
        menuFichier.addSeparator();
        menuFichier.add(itemQuitter);

        // Menu Affichage
        javax.swing.JMenu menuAffichage = new javax.swing.JMenu("Affichage");

        javax.swing.JCheckBoxMenuItem chkMode = new javax.swing.JCheckBoxMenuItem("Mode compact");
        chkMode.addActionListener(e -> {
            // Ajuste la hauteur et la police des lignes selon le mode choisi.
            boolean compact = chkMode.isSelected();
            if (compact) {
                tableEntrees.setRowHeight(18);
                tableEntrees.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
            } else {
                tableEntrees.setRowHeight(24);
                tableEntrees.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
            }
            tableEntrees.revalidate();
            tableEntrees.repaint();
        });

        javax.swing.JMenuItem itemCouleur = new javax.swing.JMenuItem("Couleur des encadrés...");
        itemCouleur.addActionListener(e -> {
            // Applique la couleur choisie aux libellés de l'interface.
            java.awt.Color nouvelleCouleur = javax.swing.JColorChooser.showDialog(
                    this,
                    "Choisir la couleur des encadrés",
                    jLabel1.getBackground()
            );
            if (nouvelleCouleur != null) {
                for (javax.swing.JLabel lbl : new javax.swing.JLabel[]{jLabel1, jLabel2, jLabel3, jLabel4}) {
                    lbl.setBackground(nouvelleCouleur);
                }
            }
        });

        menuAffichage.add(chkMode);
        menuAffichage.add(itemCouleur);

        menuBar.add(menuFichier);
        menuBar.add(menuAffichage);
        setJMenuBar(menuBar);

        // 3. Charge le fond d'écran et place les composants de la fenêtre au-dessus.
        BackgroundPanel bg = new BackgroundPanel("/miniprojetjava/fond_parc.jpg");
        bg.setLayout(new java.awt.BorderLayout());

        java.awt.Container mainContent = getContentPane();
        if (mainContent instanceof javax.swing.JComponent) {
            ((javax.swing.JComponent) mainContent).setOpaque(false);
        }

        bg.add(mainContent, java.awt.BorderLayout.CENTER);
        setContentPane(bg);

        // 4. Relie les clics dans le tableau et le bouton Modifier à leurs traitements.
        tableEntrees.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableEntreesMouseClicked(evt);
            }
        });

        btnModifier.addActionListener(this::btnModifierActionPerformed);

        // 5. Met à jour les tarifs proposés en fonction du type de billet sélectionné.
        comboTypeBillet.addActionListener(e -> {
            String selection = (String) comboTypeBillet.getSelectedItem();
            comboPrix.removeAllItems();
            if ("Enfant".equals(selection)) {
                comboPrix.addItem("12.5");
                comboPrix.addItem("15.0");
                comboPrix.addItem("18.0");
                comboPrix.addItem("20.0");
            } else if ("Adulte".equals(selection) || "Pass Journée".equals(selection)) {
                comboPrix.addItem("25.0");
                comboPrix.addItem("30.0");
                comboPrix.addItem("35.0");
                comboPrix.addItem("40.0");
            } else if ("VIP".equals(selection)) {
                comboPrix.addItem("45.0");
                comboPrix.addItem("55.0");
                comboPrix.addItem("70.0");
                comboPrix.addItem("85.0");
            } else {
                comboPrix.addItem("25.0");
            }
        });

        styliserBoutons();
        chargerTableau();
    }

    private void tableEntreesMouseClicked(java.awt.event.MouseEvent evt) {
        // Recopie les données de la ligne sélectionnée dans le formulaire de modification.
        int ligne = tableEntrees.getSelectedRow();
        if (ligne != -1) {
            txtNomVisiteur.setText(tableEntrees.getValueAt(ligne, 1).toString());
            comboTypeBillet.setSelectedItem(tableEntrees.getValueAt(ligne, 3).toString());

            Object valPrix = tableEntrees.getValueAt(ligne, 5);
            if (valPrix != null) {
                String prixStr = valPrix.toString();
                // Ajoute l'élément s'il n'est pas déjà présent dans la liste
                boolean existe = false;
                for (int i = 0; i < comboPrix.getItemCount(); i++) {
                    if (comboPrix.getItemAt(i).equals(prixStr)) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    comboPrix.addItem(prixStr);
                }
                comboPrix.setSelectedItem(prixStr);
            }
        }
    }

    private void btnModifierActionPerformed(java.awt.event.ActionEvent evt) {
        // Vérifie les champs puis enregistre en base les changements de l'entrée sélectionnée.
        int ligne = tableEntrees.getSelectedRow();
        if (ligne == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne dans le tableau à modifier.");
            return;
        }

        int id = Integer.parseInt(tableEntrees.getValueAt(ligne, 0).toString());
        String nom = txtNomVisiteur.getText().trim();
        String type = comboTypeBillet.getSelectedItem().toString();
        String prixText = comboPrix.getSelectedItem() != null ? comboPrix.getSelectedItem().toString().replace(",", ".") : "0";

        if (nom.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Veuillez renseigner le nom du visiteur.");
            return;
        }

        try {
            double prix = Double.parseDouble(prixText);
            String sql = "UPDATE entree_parc SET nom_visiteur = ?, type_pass = ?, prix = ? WHERE id = ?";

            // La requête préparée associe les valeurs au bon enregistrement sans concaténer les saisies.
            try (java.sql.Connection conn = ConnexionBDD.getConnection();
                 java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, nom);
                stmt.setString(2, type);
                stmt.setDouble(3, prix);
                stmt.setInt(4, id);

                int lignes = stmt.executeUpdate();
                if (lignes > 0) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Entrée modifiée avec succès !");
                    txtNomVisiteur.setText("");
                    tableEntrees.clearSelection();
                    chargerTableau();
                }

            } catch (java.sql.SQLException e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Erreur BDD : " + e.getMessage(), "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Prix invalide.", "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtNomVisiteur = new javax.swing.JTextPane();
        comboTypeBillet = new javax.swing.JComboBox<>();
        comboPrix = new javax.swing.JComboBox<>();
        btnEnregistrer = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableEntrees = new javax.swing.JTable();
        btnsupprimr = new javax.swing.JButton();
        btnStats = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnModifier = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String [] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        jScrollPane3.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFont(new java.awt.Font("Agency FB", 1, 18));

        jScrollPane1.setViewportView(txtNomVisiteur);

        comboTypeBillet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Adulte", "Pass Journée", "Enfant", "VIP" }));

        comboPrix.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "25.0", "30.0", "35.0" }));

        btnEnregistrer.addActionListener(this::btnEnregistrerActionPerformed);

        tableEntrees.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String [] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        jScrollPane4.setViewportView(tableEntrees);

        btnsupprimr.setText("Supprimer");
        btnsupprimr.addActionListener(this::btnsupprimrActionPerformed);

        btnStats.setText("Statistiques");
        btnStats.addActionListener(this::btnStatsActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Gestion de la fête foraine");

        jLabel2.setText("Nom du visiteur");

        jLabel3.setText("Type de billets");

        jLabel4.setText("Prix (€)");

        btnModifier.setText("Modifier");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGap(21, 21, 21)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(comboTypeBillet, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(comboPrix, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(btnEnregistrer, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(61, 61, 61)
                                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 703, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGap(301, 301, 301)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(btnStats)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnModifier, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnsupprimr)))
                                .addContainerGap(207, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(14, 14, 14)
                                                .addComponent(jLabel2)
                                                .addGap(4, 4, 4)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(36, 36, 36)
                                                .addComponent(jLabel3)
                                                .addGap(4, 4, 4)
                                                .addComponent(comboTypeBillet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(35, 35, 35)
                                                .addComponent(jLabel4)
                                                .addGap(4, 4, 4)
                                                .addComponent(comboPrix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(50, 50, 50)
                                                .addComponent(btnEnregistrer, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(40, 40, 40)
                                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnModifier)
                                        .addComponent(btnsupprimr)
                                        .addComponent(btnStats))
                                .addGap(38, 38, 38))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnregistrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnregistrerActionPerformed
        // Valide les champs, crée l'entrée et délègue son insertion au DAO.
        String nom = txtNomVisiteur.getText().trim();
        String type = comboTypeBillet.getSelectedItem().toString();
        String prixText = comboPrix.getSelectedItem() != null ? comboPrix.getSelectedItem().toString().replace(",", ".") : "0";

        if (nom.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Veuillez renseigner le nom du visiteur.");
            return;
        }

        try {
            double prix = Double.parseDouble(prixText);
            EntreeParc entree = new EntreeParc(nom, type, prix);
            EntreeParcDAO dao = new EntreeParcDAO();

            if (dao.ajouter(entree)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Entrée enregistrée avec succès !");
                txtNomVisiteur.setText("");
                chargerTableau();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement.", "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Prix invalide.", "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEnregistrerActionPerformed

    private void btnsupprimrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsupprimrActionPerformed
        // Récupère l'identifiant sélectionné et demande une confirmation avant suppression.
        int ligneSelectionnee = tableEntrees.getSelectedRow();
        if (ligneSelectionnee == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne dans le tableau.");
            return;
        }

        int id = Integer.parseInt(tableEntrees.getValueAt(ligneSelectionnee, 0).toString());

        int confirmation = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment supprimer cette entrée ?",
                "Confirmation",
                javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirmation == javax.swing.JOptionPane.YES_OPTION) {
            EntreeParcDAO dao = new EntreeParcDAO();
            if (dao.supprimer(id)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Entrée supprimée !");
                chargerTableau();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.", "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnsupprimrActionPerformed

    private void btnStatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStatsActionPerformed
        // Ouvre la fenêtre qui présente les statistiques du parc.
        new FenettreStats().setVisible(true);
    }//GEN-LAST:event_btnStatsActionPerformed

    public static void main(String args[]) {
        // Sélectionne Nimbus si disponible, puis crée la fenêtre sur le thread Swing.
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new FenettrePrincipale().setVisible(true));
    }

    public void chargerTableau() {
        // Prépare un modèle vide qui sera rempli avec les résultats de la requête.
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
                new String[]{"ID", "Nom", "Prénom", "Type Pass", "Zone", "Prix"}, 0
        );

        // Lit les entrées du parc et actualise le tableau avec les lignes retournées.
        String sql = "SELECT * FROM entree_parc";

        try (java.sql.Connection conn = ConnexionBDD.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nom_visiteur"),
                        rs.getString("prenom_visiteur"),
                        rs.getString("type_pass"),
                        rs.getString("zone_attraction"),
                        rs.getDouble("prix")
                });
            }
            tableEntrees.setModel(model);

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    private void styliserBoutons() {
        // Uniformise l'apparence des libellés et donne une couleur distincte à chaque action.
        java.awt.Color fondEncadre = new java.awt.Color(33, 37, 41);
        javax.swing.border.Border bordure = javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220), 1),
                javax.swing.BorderFactory.createEmptyBorder(3, 8, 3, 8)
        );

        for (javax.swing.JLabel lbl : new javax.swing.JLabel[]{jLabel1, jLabel2, jLabel3, jLabel4}) {
            lbl.setOpaque(true);
            lbl.setBackground(fondEncadre);
            lbl.setForeground(java.awt.Color.WHITE);
            lbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, lbl.getFont().getSize()));
            lbl.setBorder(bordure);
        }

        if (btnEnregistrer != null) {
            btnEnregistrer.setBackground(new java.awt.Color(40, 167, 69));
            btnEnregistrer.setForeground(java.awt.Color.WHITE);
            btnEnregistrer.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        }
        if (btnStats != null) {
            btnStats.setBackground(new java.awt.Color(0, 123, 255));
            btnStats.setForeground(java.awt.Color.WHITE);
            btnStats.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        }
        if (btnsupprimr != null) {
            btnsupprimr.setBackground(new java.awt.Color(220, 53, 69));
            btnsupprimr.setForeground(java.awt.Color.WHITE);
            btnsupprimr.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        }
        if (btnModifier != null) {
            btnModifier.setBackground(new java.awt.Color(255, 193, 7));
            btnModifier.setForeground(java.awt.Color.BLACK);
            btnModifier.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        }
    }

    class BackgroundPanel extends javax.swing.JPanel {
        private java.awt.Image image;

        public BackgroundPanel(String path) {
            // Charge l'image depuis les ressources, avec un chemin local comme solution de secours.
            try {
                java.net.URL imgURL = getClass().getResource(path);
                if (imgURL != null) {
                    image = javax.imageio.ImageIO.read(imgURL);
                } else {
                    image = javax.imageio.ImageIO.read(new java.io.File("src/miniprojetjava/fond_parc.jpg"));
                }
            } catch (Exception e) {
                System.out.println("Erreur image : " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            // Dessine le fond à la taille actuelle du panneau avant les composants enfants.
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnregistrer;
    private javax.swing.JButton btnModifier;
    private javax.swing.JButton btnStats;
    private javax.swing.JButton btnsupprimr;
    private javax.swing.JComboBox<String> comboPrix;
    private javax.swing.JComboBox<String> comboTypeBillet;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tableEntrees;
    private javax.swing.JTextPane txtNomVisiteur;
    // End of variables declaration//GEN-END:variables
}
