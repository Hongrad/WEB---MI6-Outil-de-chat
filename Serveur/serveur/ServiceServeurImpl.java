package serveur;

import corbaInterface.*;
import java.util.ArrayList;

/**
 * 
 */
public class ServiceServeurImpl extends ServiceServeurPOA {
    // Liste des utilisateurs
    private ArrayList<Utilisateur> utilisateurs = null;

    public ServiceServeurImpl() {
        utilisateurs = new ArrayList<>();
    }

    @Override
    public void sendMessage(MessageType messageType ,String message, Utilisateur utilisateur) {
        if (messageType.equals(MessageType.authenticate)) {
            // Authentification de l'utilisateur
            
            // Todo : voir avec la BD
            
            utilisateurs.add(utilisateur);
            
        } else if (messageType.equals(MessageType.message)) {
            // L'utilisateur envoie un message sur le chat
            
            for(Utilisateur u : utilisateurs) {
                if (!u.equals(utilisateur)) {
                    try {
                        u.afficher(utilisateur.getName() + " : " + message);
                    } catch (Exception e) {
                        // Utilisateur non joingnable : le supprime
                        utilisateurs.remove(u);
                    }
                }
            }
        }
    }
    
    @Override
    public boolean authenticate(String nom, String motDePasse, Utilisateur utilisateur) {
        // Todo
        return false;
    }
}
