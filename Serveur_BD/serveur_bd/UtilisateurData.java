package serveur_bd;

import java.io.Serializable;

/**
 * Class représentant les données d'un utilisateur
 */
public class UtilisateurData implements Serializable {
    
    private String nom;
    private String motDePasse;

    public UtilisateurData(String nom, String motDePasse) {
        this.nom = nom;
        this.motDePasse = motDePasse;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return the motDePasse
     */
    public String getMotDePasse() {
        return motDePasse;
    }
}
