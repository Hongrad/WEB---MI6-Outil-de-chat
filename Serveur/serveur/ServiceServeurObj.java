package serveur;

import serviceServeur.*;
import serviceServeur.ServiceServeurPOA;

/**
 * 
 */
public class ServiceServeurObj extends ServiceServeurPOA {

    @Override
    public void sendMessage(String message, Utilisateur utilisateur) {
        System.out.println("Message : " + message);
        System.out.println("Nom : " + utilisateur.getName());
        utilisateur.setName("gg");
        System.out.println("Nom : " + utilisateur.getName());
        utilisateur.afficher("test affichage");
    }
    
    @Override
    public boolean authenticate(String nom, String motDePasse, UtilisateurHolder utilisateur) {
        return false;
    }
}
