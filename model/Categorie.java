package projetPocketBudget.model;

public class Categorie {
    private String nom;
    private CategorieType type;

    public Categorie(String nom, CategorieType type) {
        this.nom = nom;
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public CategorieType getCategorie() {
        return type;
    }

    public void afficherCategorie() {
        System.out.println("Catégorie : " + nom + " | Type : " + type);
    }
}
