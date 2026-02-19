package projetPocketBudget.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import projetPocketBudget.model.Account;
import projetPocketBudget.model.Categorie;
import projetPocketBudget.model.CategorieType;
import projetPocketBudget.model.Transaction;

public class TransactionCsvStorage {
    // chemin par défaut pour le fichier CSV
    // je met cette variable en constante pour éviter de devoir la changer à
    // plusieurs endroits dans le code si jamais on veut changer le nom du fichier
    // ou son emplacement
    // et j'utilise le static pour éviter de créer un path pour chaque objet
    // comme sa du coup pour toutes les transactions on aura le même path
    // le static permet de dire que cette variable appartient à la classe et pas aux
    // objets
    private static final String PATH = "transactions.csv";

    // méthode pour sauvegarder les transactions dans un fichier CSV
    // donc la méthode prend en paramètre une liste de transactions et le chemin du
    // fichier où on veut sauvegarder les transactions
    // sa permet de pouvoir changer le nom du fichier ou son emplacement sans avoir
    // à changer le code de la méthode
    public void save(List<Transaction> transactions, String path) {
        // si le path est null ou vide, on utilise le chemin par défaut
        // sinon on utilise le path passé en paramètre
        String finalPath = (path == null || path.isEmpty()) ? PATH : path;
        // ici j'essaie d'ouvrir un BufferedWriter pour écrire dans le fichier CSV
        // j'utilise un FileWriter pour écrire dans le fichier et je lui passe le nom du
        // fichier et le mode d'ouverture (false pour écraser le fichier à chaque fois)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(finalPath, false))) {
            // ici j'écris la première ligne du fichier CSV
            // c'est le header qui contient les noms des colonnes
            writer.write("id;montant;description;date;type;nomCategorie");
            // je fais un retour à la ligne
            writer.newLine();
            // ici je parcours chaque transaction de la liste de transactions
            for (Transaction transaction : transactions) {
                // je vais construire une ligne
                // avec son id, son montant, sa description, sa date, son type et le nom de sa
                // catégorie
                // en les séparant par des points virgules
                String line = transaction.getId() + ";" +
                        transaction.getMontant() + ";" +
                        transaction.getDescription() + ";" +
                        transaction.getDate() + ";" +
                        transaction.getCategorie().getCategorie() + ";" +
                        transaction.getCategorie().getNom();
                // ensuite j'écris cette ligne dans le fichier CSV
                writer.write(line);
                // puis je fais un retour à la ligne pour passer à la transaction suivante
                writer.newLine();
            }
        } catch (IOException erreur) {
            System.out.println("Erreur lors de la sauvegarde des transactions : " + erreur.getMessage());
        }
    }

    // méthode pour lire un fichier CSV
    // cette méthode va permettre quand lorsqu'on arrête le programme et qu'on le
    // relance de récupérer les transactions qui étaient sauvegardées dans le
    // fichier CSV
    // sauf que dans le fichier CSV je n'ai pas stocké les comptes et les catégories
    // des transactions
    // donc je vais devoir les passer en paramètre de la méthode pour pouvoir les
    // associer aux transactions que je vais lire du fichier CSV
    public List<Transaction> load(String path, Account compte1, Categorie categorieDepense,
            Categorie categorieRecette) {
        // je créer une liste de transactions vide pour stocker les transactions lues du
        // fichier CSV
        List<Transaction> transactions = new ArrayList<>();
        String finalPath = (path == null || path.isEmpty()) ? PATH : path;
        // ici je transforme le texte du path passé en paramètre qui est un String
        // en un objet "chemin" que Java sait utilisé pour lire les
        // fichiers
        Path p = Paths.get(finalPath);
        // ici je vérifie si le fichier existe ou pas
        if (!Files.exists(p)) {
            // je retourne la liste de transactions vide
            return transactions;
        }
        // BufferedReader est une classe qui permet de lire un fichier ligne par ligne
        // Files.newBufferedReader(p) est une méthode qui permet d'ouvrir le fichier
        try (BufferedReader lecture = Files.newBufferedReader(p)) {
            // je lis la première ligne du fichier qui est le header et je ne fais rien avec
            lecture.readLine();
            // cette variable va contenir la ligne en cours (donc celle que je suis en train
            // de lire)
            String line;
            // tant que la ligne n'est pas null (c'est à dire tant que je n'ai pas atteint
            // la fin du fichier)
            // readLine() est une méthode qui lit une ligne du fichier et retourne null si
            // on a atteint la fin du fichier
            while ((line = lecture.readLine()) != null) {
                // si la ligne est vide ou ne contient que des espaces, je passe à la ligne
                // suivante
                if (line.isBlank()) {
                    continue;
                }
                // ici je découpe la ligne en plusieurs parties en utilisant le point virgule
                // comme séparateur
                String[] parts = line.split(";");
                if (parts.length < 5) {
                    continue;
                }
                // ici je récupère l'id de la transaction qui est la première partie de la ligne
                // (index 0), j'utilise trim() pour enlever les espaces avant et après l'id
                String id = parts[0].trim();
                // ensuite je récupère le montant que je convertis en double (car dans le
                // fichier CSV c'est du texte) et je le stocke
                double montant = Double.parseDouble(parts[1].trim());
                // ensuite la description
                String description = parts[2].trim();
                // ensuite la date que je convertis en LocalDate
                LocalDate date = LocalDate.parse(parts[3].trim());
                // ensuite je récupère le type de la catégorie (DEPENSE ou RECETTE) que je
                // convertis en CategorieType
                CategorieType type = CategorieType.valueOf(parts[4].trim());
                // pour vérifier la ligne je l'affiche dans la console
                System.out.println(line);
                // ensuite je choisi la bonne catégorie en fonction du type
                // si le type est DEPENSE, j'utilise la catégorie de dépense passée en paramètre
                // sinon j'utilise la catégorie de recette passée en paramètre
                Categorie categorie = type == CategorieType.DEPENSE ? categorieDepense : categorieRecette;
                // puis je reconstruis une transaction avec les informations que j'ai récupérées
                // du fichier CSV
                Transaction transaction = new Transaction(id, montant, description, date, compte1, categorie);
                // enfin j'ajoute cette transaction à la liste de transactions que je vais
                // retourner à la fin de la méthode
                transactions.add(transaction);
            }
        } catch (IOException erreur) {
            erreur.printStackTrace();
        }
        // et je renvoie donc toutes les transactions que j'ai lues du fichier CSV
        return transactions;
    }

}
