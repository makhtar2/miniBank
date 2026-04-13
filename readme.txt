import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    // Scanner global pour être utilisé dans toutes les méthodes
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        String[][] utilisateurs = {
                {"Makhtar", "Wade", "1234", "500000", "AC1001"},
                {"Fallou", "Bousso", "2345", "60000", "AC1002"},
                {"Awa", "Sene", "3456", "100000", "AC1003"},
                {"Abdou", "Diouf", "4567", "20000", "AC1004"},
                {"Fatou", "Ndiaye", "5678", "60000", "AC1005"}
        };

        // ÉTAPE 1 : Authentification
        int indexUser = verifierCode(utilisateurs);

        // ÉTAPE 2 : Si connecté, afficher le menu en boucle
        if (indexUser != -1) {
            boolean continuer = true;
            while (continuer) {
                afficherMenu();
                int choix = sc.nextInt();

                switch (choix) {
                    case 1 -> consulterSolde(utilisateurs[indexUser]);
                    case 2 -> effectuerRetrait(utilisateurs[indexUser]);
                    case 3 -> effectuerTransfert(utilisateurs, indexUser);
                    case 4 -> {
                        continuer = false;
                        System.out.println("Déconnexion en cours...");
                    }
                    default -> System.out.println("Choix invalide.");
                }

                if (continuer) {
                    System.out.println("\n1: Retour au menu | 2: Quitter");
                    if (sc.nextInt() == 2) continuer = false;
                }
            }
        }

        System.out.println("\nMerci d'avoir utilisé GAB TOUBA. Jërëjëf !");
        sc.close();
    }

    // --- MÉTHODE 1 : VÉRIFICATION DU CODE ---
    public static int verifierCode(String[][] db) {
        int tentatives = 0;
        while (tentatives < 3) {
            System.out.println("\n===== GAB TOUBA ====");
            System.out.print("Donnez votre code secret (4 chiffres) : ");
            String codeSaisi = sc.next();

            // Vérification du format (4 chiffres uniquement)
            if (codeSaisi.matches("\\d{4}")) {
                for (int i = 0; i < db.length; i++) {
                    if (codeSaisi.equals(db[i][2])) {
                        System.out.println("Accès autorisé ! Bienvenue " + db[i][0].charAt(0) + "." + db[i][1]);
                        return i;
                    }
                }
            }
            tentatives++;
            System.out.println("Code incorrect ou format invalide. Tentatives restantes : " + (3 - tentatives));
        }
        System.out.println("Carte bloquée après 3 tentatives.");
        return -1;
    }

    // --- MÉTHODE 2 : AFFICHAGE DU MENU ---
    public static void afficherMenu() {
        System.out.println("\n--- SERVICES DISPONIBLES ---");
        System.out.println("1 : Consulter le solde");
        System.out.println("2 : Effectuer un retrait");
        System.out.println("3 : Effectuer un transfert");
        System.out.println("4 : Quitter");
        System.out.print("Votre choix : ");
    }

    // --- MÉTHODE 3 : CONSULTATION SOLDE ---
    public static void consulterSolde(String[] user) {
        System.out.println("\nPropriétaire : " + user[0].charAt(0) + "." + user[1]);
        System.out.println("Solde actuel : " + user[3] + " FCFA");
    }

    // --- MÉTHODE 4 : RETRAIT ---
    public static void effectuerRetrait(String[] user) {
        System.out.print("\nMontant du retrait : ");
        float montant = sc.nextFloat();
        float solde = Float.parseFloat(user[3]);

        if (montant <= solde) {
            solde -= montant;
            user[3] = String.valueOf(solde);
            System.out.println("Retrait réussi ! Nouveau solde : " + solde + " FCFA");
        } else {
            System.out.println("Erreur : Solde insuffisant.");
        }
    }

    // --- MÉTHODE 5 : TRANSFERT (Selon spécifications Veille 2) ---
    public static void effectuerTransfert(String[][] db, int indexExp) {
        System.out.print("\nNuméro de compte destinataire (ex: AC1002) : ");
        String destNum = sc.next();

        // 1. Chercher le destinataire
        int indexDest = -1;
        for (int i = 0; i < db.length; i++) {
            if (destNum.equalsIgnoreCase(db[i][4])) { // Utilisation du n° de compte
                indexDest = i;
                break;
            }
        }

        if (indexDest == -1) {
            System.out.println("Erreur : Compte destinataire inexistant.");
            return;
        }

        // 2. Saisie du montant et calcul des frais
        System.out.print("Montant à transférer : ");
        float montant = sc.nextFloat();
        float frais = montant * 0.01f; // Frais de 1%
        float totalADebiter = montant + frais;
        float soldeExp = Float.parseFloat(db[indexExp][3]);

        // 3. Vérification solde
        if (soldeExp >= totalADebiter) {
            // Débiter expéditeur
            soldeExp -= totalADebiter;
            db[indexExp][3] = String.valueOf(soldeExp);

            // Créditer destinataire
            float soldeDest = Float.parseFloat(db[indexDest][3]);
            soldeDest += montant;
            db[indexDest][3] = String.valueOf(soldeDest);

            // 4. Message de confirmation avec Date/Heure
            LocalDateTime maintenant = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            System.out.println("\n--- CONFIRMATION DE TRANSFERT ---");
            System.out.println("Destinataire : " + db[indexDest][1]);
            System.out.println("Montant total débité (avec 1% frais) : " + totalADebiter + " FCFA");
            System.out.println("Solde restant : " + soldeExp + " FCFA");
            System.out.println("Date : " + maintenant.format(formatter));
        } else {
            System.out.println("Transfert impossible : Solde insuffisant pour couvrir le montant et les frais.");
        }
    }
}