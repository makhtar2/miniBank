import java.util.Scanner;
public class Main {
    // --- MÉTHODE 2 : AFFICHAGE DU MENU ---
    public static void afficherMenu() {
        System.out.println("\n--- SERVICES DISPONIBLES ---");
        System.out.println("1 : Consulter le solde");
        System.out.println("2 : Effectuer un retrait");
        System.out.println("3 : Effectuer un transfert");
        System.out.println("4 : Quitter");
        System.out.print("Votre choix : ");
    }

    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        // Base de données simulée
        String[][] utilisateurs = {
                {"Makhtar", "Wade", "1234", "500000"},
                {"Fallou", "Bousso", "2345", "60000"},
                {"Awa", "Sene", "3456", "100000"},
                {"Abdou", "Diouf", "4567", "20000"},
                {"Fatou", "Ndiaye", "5678", "60000"}
        };

        int tentative = 0;
        boolean estConnecte = false;
        int indexUtilisateur = -1;

        while (tentative < 3 && !estConnecte) {
            System.out.println("\n===== GAB TOUBA ====");
            System.out.print("Donnez votre code secret : ");
            String codeSaisi = sc.next();

            for (int i = 0; i < utilisateurs.length; i++) {
                if (codeSaisi.equals(utilisateurs[i][2])) {
                    estConnecte = true;
                    indexUtilisateur = i;
                    break;
                }
            }

            if (estConnecte) {
                // MODIFICATION ICI : On récupère l'initiale du prénom (index 0 de la chaîne)
                char initiale = utilisateurs[indexUtilisateur][0].charAt(0);
                String nom = utilisateurs[indexUtilisateur][1];

                System.out.println("\nAccès autorisé ! Bienvenue " + initiale + "." + nom);
            } else {
                tentative++;
                System.out.println("Code incorrect, tentatives restantes : " + (3 - tentative));
            }
        }
        if (estConnecte) {
            boolean continuer = true;
            while (continuer) {
                float soldeActuel = Float.parseFloat(utilisateurs[indexUtilisateur][3]);

                System.out.println("\n--- SERVICES DISPONIBLES ---");
                System.out.println("1 : Consulter le solde");
                System.out.println("2 : Effectuer un retrait");
                System.out.println("3 : Effectuer un transfert");
                System.out.println("4 : Quitter");
                System.out.print("Votre choix : ");
                int choix = sc.nextInt();

                if (choix == 1) {
                    // Application du format d'initiale aussi pour la consultation du solde
                    char initiale = utilisateurs[indexUtilisateur][0].charAt(0);
                    System.out.println("\nPropriétaire : " + initiale + "." + utilisateurs[indexUtilisateur][1]);
                    System.out.println("Solde actuel : " + soldeActuel + " FCFA");
                }
                else if (choix == 2) {
                    System.out.print("\nMontant du retrait : ");
                    float montantRetrait = sc.nextFloat();

                    if (montantRetrait <= soldeActuel) {
                        soldeActuel -= montantRetrait;
                        utilisateurs[indexUtilisateur][3] = String.valueOf(soldeActuel);
                        System.out.println("Retrait réussi ! Nouveau solde : " + soldeActuel + " FCFA");
                    } else {
                        System.out.println("Solde insuffisant.");
                    }
                }
                else if (choix == 3) {
                    System.out.println("\nService de transfert vers Wave ou Orange Money en cours de développement...");
                }
                else if (choix == 4) {
                    continuer = false;
                }

                if (continuer && choix >= 1 && choix <= 3) {
                    System.out.println("\n1 : Retourner au menu");
                    System.out.println("2 : Quitter");
                    if (sc.nextInt() == 2) {
                        continuer = false;
                    }
                }
            }
        }

        System.out.println("\nMerci d'avoir utilisé GAB TOUBA. Jërëjëf !");
        sc.close();
    }
}