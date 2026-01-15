package projetPocketBudget.model;

public class Transaction {
    private String id;
    private double montant;
    private String description;
    private java.time.LocalDate date;
    private Account compte;
    private Categorie categorie;

    public Transaction(String id, double montant, String description, java.time.LocalDate date, Account compte,
            Categorie categorie) {
        this.id = id;
        this.montant = montant;
        this.description = description;
        this.date = date;
        this.compte = compte;
        this.categorie = categorie;
    }

    public String getId() {
        return id;
    }

    public double getMontant() {
        return montant;
    }

    public String getDescription() {
        return description;
    }

    public java.time.LocalDate getDate() {
        return date;
    }

    public Account getCompte() {
        return compte;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void afficherTransaction() {
        System.out.println("[" + id + "] " + date + " | " + categorie.getNom() + " | "
                + categorie.getCategorie() + " | " + montant + " € | Compte : "
                + compte.getNom()
                + " | " + description);
    }
}
