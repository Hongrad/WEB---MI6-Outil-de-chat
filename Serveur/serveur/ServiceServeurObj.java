package serveur;

import corbaInterface.*;

/**
 * 
 */
public class ServiceServeurObj extends ServiceServeurPOA {

    @Override
    public void sendMessage(String message, Utilisateur utilisateur) {
        
    }
    
    @Override
    public boolean authenticate(String nom, String motDePasse, Utilisateur utilisateur) {
        return false;
    }
}
