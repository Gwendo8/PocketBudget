package projetPocketBudget.repository;

import java.util.ArrayList;
import java.util.List;

import projetPocketBudget.model.Transaction;

// ici je stocke les transactions en mémoire dans une liste
public class TransactionRepository {
    private List<Transaction> transactions;

    // contructeur
    public TransactionRepository() {
        // j'initialise la liste des transactions
        this.transactions = new ArrayList<Transaction>();
    }

    // fonction qui ajoute une transaction à la liste
    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public boolean deleteTransaction(String id) {
        for (Transaction transaction : transactions) {
            // on aurait pu utiliser == mais equals compare le conntenu des chaînes de
            // caractères
            // alors que == compare les références en mémoire
            if (transaction.getId().equals(id)) {
                transactions.remove(transaction);
                return true;
            }
        }
        return false;
    }

    // fonction qui cherche une transaction par son id
    public Transaction findById(String id) {
        for (Transaction transaction : transactions) {
            if (transaction.getId().equals(id)) {
                // on retourne la transaction trouvée
                return transaction;
            }
        }
        return null;
    }

    // fonction qui cherche toutes les transactions
    public List<Transaction> findAll() {
        // on retourne une nouvelle liste de transactions pour éviter les modifications
        // externes
        return new ArrayList<>(transactions);
    }
}

// NOTES :
// Le repository est la couche stockage d'un projet il répond à :
// Comment je sauvegarde / je récupère mes données ?
// Quand on dit que le repository sert à stocker et sauvegarder ça veut dire
// toutes les opérations CRUD :
// Create (add, save, insert)
// Read (findAll, findById)
// Update (update)
// Delete (delet)
// Donc c'est tout a fait normal qu'on ait une méthode addTransaction ici dans
// le repository