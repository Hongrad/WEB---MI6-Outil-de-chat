package serveur;

import corbaInterface.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import serveur_bd.ServiceBd;

/**
 * 
 */
public class ServiceServeurImpl extends ServiceServeurPOA {
    // Liste des utilisateurs connectés
    private ArrayList<Utilisateur> utilisateurs;
    
    // Le service de la BD
    private ServiceBd serviceBd;

    public ServiceServeurImpl(ServiceBd serviceBd) {
        this.utilisateurs = new ArrayList<>();
        this.serviceBd = serviceBd;
    }
    
    /**
     * Envoie un message à tous les utilisateurs sauf celui passé en paramètre
     * 
     * @param message
     * @param utilisateur 
     */
    private void sendMessageToAll(String message, Utilisateur utilisateur) {        
        synchronized (this.utilisateurs){
            Iterator<Utilisateur> i = this.utilisateurs.iterator();
            while (i.hasNext()) {
                Utilisateur u = i.next();
                        
                if (!u.equals(utilisateur)) {
                    try {
                        u.afficher(message);
                    } catch (Exception e) {
                        // Utilisateur non joingnable : le supprimer
                        i.remove();
                    }
                }
            }
        }
    }
    
    /**
     * L'utilisateur tante de créer un compte
     * 
     * @param utilisateur
     * @return 
     */
    @Override
    public boolean createAccount(Utilisateur utilisateur) {
        try {
            if (this.serviceBd.createNewUser(utilisateur)) {
                synchronized (this.utilisateurs){
                    this.utilisateurs.add(utilisateur);
                }
                this.sendMessageToAll(utilisateur.getName() + " vien de se connecter !", utilisateur);
                return true;
            } else {
                return false;
            }
        } catch (RemoteException ex) {
            return false;
        }
    }
    
    /**
     * L'utilisateur tante de s'authentifier
     * 
     * @param utilisateur
     * @return 
     */
    @Override
    public boolean authenticate(Utilisateur utilisateur) {
        try {
            if (this.serviceBd.authenticate(utilisateur)) {
                synchronized (this.utilisateurs){
                    this.utilisateurs.add(utilisateur);
                }
                this.sendMessageToAll(utilisateur.getName() + " vien de se connecter !", utilisateur);
                return true;
            } else {
                return false;
            }
        } catch (RemoteException ex) {
            return false;
        }
    }
    
    /**
     * L'utilisateur tante de créer une nouvelle salle de chat
     * 
     * @param roomName
     * @param utilisateur
     * @return 
     */
    @Override
    public boolean createRoom(String roomName, Utilisateur utilisateur) {
        return false;
    }
    
    /**
     * L'utilisateur tante de rejoindre une salle de chat
     * 
     * @param roomName
     * @param utilisateur
     * @return 
     */
    @Override
    public boolean joinRoom(String roomName, Utilisateur utilisateur) {
        return false;
    }
    
    /**
     * L'utilisateur envoie un message au serveur
     * 
     * @param message
     * @param utilisateur 
     */
    @Override
    public void sendMessage(String message, Utilisateur utilisateur) {
        this.sendMessageToAll(utilisateur.getName() + " : " + message, utilisateur);
    }
    
    /**
     * L'utilisateur veux récupérer l'historique des conversations
     * 
     * @param utilisateur 
     * @return  
     */
    @Override
    public String getHistory(Utilisateur utilisateur) {
        return "";
    }
    
    /**
     * L'utilisateur se déconnecte
     * 
     * @param utilisateur 
     */
    @Override
    public void quit(Utilisateur utilisateur) {
        synchronized (this.utilisateurs){
            this.utilisateurs.remove(utilisateur);
        }
        this.sendMessageToAll(utilisateur.getName() + " s'est déconnecté !", utilisateur);
    }
}
