package projetPocketBudget.model;

public class Account {
    private String nom;
    private double solde;

    public Account(String nom, double solde) {
        this.nom = nom;
        this.solde = solde;
    }

    public String getNom() {
        return nom;
    }

    public double getSolde() {
        return solde;
    }

    public double updateSolde(double montant) {
        return this.solde += montant;
    }

    public void afficherCompte() {
        System.out.println("Compte : " + nom + " | Solde : " + solde + " €");
    }
}
