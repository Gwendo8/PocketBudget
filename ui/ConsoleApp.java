package projetPocketBudget.ui;

import java.time.LocalDate;
import java.util.Scanner;

import projetPocketBudget.model.Account;
import projetPocketBudget.model.Categorie;
import projetPocketBudget.model.CategorieType;
import projetPocketBudget.model.Transaction;
import projetPocketBudget.repository.TransactionRepository;
import projetPocketBudget.service.TransactionService;

public class ConsoleApp {

    public static void main(String[] args) {
        // Création d'un compte
        Account compte1 = new Account("courant", 1200);
        // Création de catégories
        Categorie categorie1 = new Categorie("Course", CategorieType.DEPENSE);
        Categorie categorie2 = new Categorie("Action", CategorieType.RECETTE);
        // Création de transactions
        Transaction transaction1 = new Transaction("T1", 50, "Carrefour", LocalDate.of(2026, 1, 9), compte1,
                categorie1);
        Transaction transaction2 = new Transaction("T2", 1200, "Action Saint Gobain", LocalDate.of(2025, 2, 10),
                compte1,
                categorie2);
        Transaction transaction3 = new Transaction("T3", 0, "Erreur Montant", LocalDate.of(2025, 3, 15), compte1,
                categorie1);
        // Création d'un repository
        TransactionRepository repo = new TransactionRepository();
        // Création d'un service par rapport au repository
        TransactionService service = new TransactionService(repo);
        compte1.afficherCompte();
        categorie1.afficherCategorie();
        categorie2.afficherCategorie();

        // ici j'essaie d'ajouter les transactions en utilisant le service
        try {
            // si toutes les règles sont respectées elles sont ajoutées au repository
            service.ajouterTransaction(transaction1);
            service.ajouterTransaction(transaction2);
            // exemple de transaction qui ne respecte pas les règles
            service.ajouterTransaction(transaction3);
            // sinon une exception est levée
        } catch (IllegalArgumentException e) {
            // je capture l'exception et j'affiche le message d'erreur que j'ai défini dans
            // le service
            System.out.println("Erreur lors de l'ajout de la transaction : " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        int choixUtilisateur;

        do {
            System.out.println(1 + " - Ajouter une transaction");
            System.out.println(2 + " - Afficher les transactions");
            System.out.println(3 + " - Supprimer une transaction");
            System.out.println(0 + " - Quitter");
            choixUtilisateur = scanner.nextInt();
            switch (choixUtilisateur) {
                case 1:
                    System.out.println("Ajout d'une transaction");
                    System.out.println("Entrez l'id : ");
                    String id = scanner.next();
                    System.out.println("Entrez le montant : ");
                    double montant = scanner.nextDouble();
                    // pour lire une chaîne de caractères après un nextInt() ou nextDouble()
                    // il faut consommer le retour à la ligne restant
                    // sinon le nextLine() suivant est sauté
                    scanner.nextLine();
                    System.out.println("Entrez la description : ");
                    // si je fais juste scanner.next() ici je ne peux pas avoir une description avec
                    // des espaces
                    String description = scanner.nextLine();
                    System.out.println("Entrez la date (AAAA-MM-JJ) : ");
                    String dateStr = scanner.next();
                    // je convertis la chaîne de caractères en LocalDate
                    LocalDate date = LocalDate.parse(dateStr);
                    System.out.println("Choisissez une catégorie (1 pour dépense, 2 pour recette) : ");
                    int choixCategorie = scanner.nextInt();
                    // je choisis la catégorie en fonction du choix de l'utilisateur
                    Categorie categorieChoisie;
                    if (choixCategorie == 1) {
                        categorieChoisie = categorie1;
                    } else {
                        categorieChoisie = categorie2;
                    }
                    // je crée la nouvelle transaction avec les données fournies
                    Transaction nouvelleTransaction = new Transaction(id, montant, description, date, compte1,
                            categorieChoisie);
                    // j'essaie de l'ajouter via le service
                    try {
                        service.ajouterTransaction(nouvelleTransaction);
                        System.out.println("Transaction ajoutée avec succès");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erreur lors de l'ajout de la transaction : " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Affichage des transactions");
                    // ici je parcours toutes les transactions et j'affiche leurs détails
                    // en utilisant la méthode findAll() que j'ai ajoutée au repository
                    for (Transaction transaction : repo.findAll()) {
                        // et la méthode afficherTransaction() de la classe Transaction
                        transaction.afficherTransaction();
                    }
                    // puis j'affiche le solde du compte après les transactions
                    compte1.afficherCompte();
                    break;
                case 3:
                    System.out.println("Suppression d'une transaction");
                    System.out.println("Entrez l'id de la transaction à supprimer : ");
                    String choixIdUtilisateur = scanner.next();
                    try {
                        service.supprimerTransaction(choixIdUtilisateur);
                        System.out.println("Transaction supprimée avec succès");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erreur lors de la suppression de la transaction : " + e.getMessage());
                    }
                    compte1.afficherCompte();
                    break;
                case 0:
                    System.out.println("Au revoir");
                    break;
                default:
                    System.out.println("Choix invalide");
                    break;
            }
        } while (choixUtilisateur != 0);
        scanner.close();

    }
}
