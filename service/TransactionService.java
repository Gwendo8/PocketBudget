package projetPocketBudget.service;

import projetPocketBudget.model.CategorieType;
import projetPocketBudget.model.Transaction;
import projetPocketBudget.repository.TransactionRepository;

public class TransactionService {
    private TransactionRepository repo;

    public TransactionService(TransactionRepository repo) {
        this.repo = repo;
    }

    public void ajouterTransaction(Transaction t) {
        if (t == null) {
            // IllegalArgumentException est une exception qui signifie qu'un argument passé
            // à une méthode n'est pas valide
            // on peut l'utiliser quand un paramètre est invalide
            // une valeur ne respecte pas les règles
            // l'objet est bien fourni mais mal formé
            throw new IllegalArgumentException("La transaction ne peut pas être nulle");
        } else if (t.getMontant() <= 0) {
            throw new IllegalArgumentException("Le montant de la transaction doit être supérieur à zéro");
        } else if (t.getCompte() == null) {
            throw new IllegalArgumentException("La transaction doit être associée à un compte");
        } else if (t.getCategorie() == null) {
            throw new IllegalArgumentException("La transaction doit être associée à une catégorie");
        } else if (t.getDate() == null) {
            throw new IllegalArgumentException("La transaction doit avoir une date");
        }
        double impactSurSolde = t.getMontant();
        // ici d'abord je regarde la catégorie de la transaction
        // et ensuite je regarde le type de cette catégorie
        // soit une dépense soit une recette
        // c'est pour ça que j'utilise getCategorie() deux fois
        if (t.getCategorie().getCategorie() == CategorieType.DEPENSE) {
            impactSurSolde = -impactSurSolde;
        }
        t.getCompte().updateSolde(impactSurSolde);
        repo.addTransaction(t);
    }

    // fonction pour supprimer une transaction avec son id
    public void supprimerTransaction(String id) {
        // je vérifie que l'id n'est pas nul ou vide
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("L'id de la transaction ne peut pas être nul ou vide");
        }
        // je cherche la transaction dans le repository
        Transaction transaction = repo.findById(id);
        // si la transaction n'existe pas je lève une exception
        if (transaction == null) {
            throw new IllegalArgumentException("La transaction avec l'id " + id + " n'existe pas");
        } else {
            // sinon je mets à jour le solde du compte avant de supprimer la transaction
            double impactSurSolde = transaction.getMontant();
            if (transaction.getCategorie().getCategorie() == CategorieType.DEPENSE) {
                impactSurSolde = +impactSurSolde;
            } else {
                impactSurSolde = -impactSurSolde;
            }
            transaction.getCompte().updateSolde(impactSurSolde);
            repo.deleteTransaction(id);
        }
    }
}

// NOTES :
// Un service permet de gérer la logique métier (les règles) d'un projet :
// Une transaction ne peut pas être nulle
// Le montant doit être supérieur à zéro
// compte / catégorie / date obligatoires
// Le service répond à :
// Est ce que j'ai le droit d'ajouter cette transaction ? Et selon quelles
// règles ?