package serveur;

import corbaInterface.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
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
     * Récupère un utilisateur connecté au stub RMI
     * Permet de faire le lien entre l'interface corba et rmi
     * 
     * @param utilisateurCorba
     * @return utilisateurRmi
     */
    private client.Utilisateur getRmiUtilisateur(Utilisateur utilisateurCorba) {
        client.Utilisateur utilisateurRmi = new client.Utilisateur() {
            @Override
            public String getName() throws RemoteException {
                return utilisateurCorba.getName();
            }

            @Override
            public void setName(String name) throws RemoteException {
                utilisateurCorba.setName(name);
            }

            @Override
            public String getPassword() throws RemoteException {
                return utilisateurCorba.getPassword();
            }

            @Override
            public void setPassword(String password) throws RemoteException {
                utilisateurCorba.setPassword(password);
            }

            @Override
            public void afficher(String message) throws RemoteException {
                utilisateurCorba.afficher(message);
            }
        };
        
        try {
            client.Utilisateur stub = (client.Utilisateur) UnicastRemoteObject.exportObject(utilisateurRmi, 0);
        } catch (RemoteException ex) {
            System.out.println("Erreur : La création du stub RMI a échoué pour l'utilisateur " + utilisateurCorba.getName());
        }
        
        return utilisateurRmi;
    }
    
    /**
     * Envoie un message à tous les utilisateurs sauf celui passé en paramètre
     * 
     * @param message
     * @param utilisateur 
     */
    private void sendMessageToAll(String message, Utilisateur utilisateur) {        
        synchronized (this.utilisateurs) {
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
            if (this.serviceBd.createNewUser(this.getRmiUtilisateur(utilisateur))) {
                synchronized (this.utilisateurs){
                    this.utilisateurs.add(utilisateur);
                }
                this.sendMessageToAll(utilisateur.getName() + " vien de se connecter !", utilisateur);
                return true;
            } else {
                return false;
            }
        } catch (RemoteException ex) {
            System.out.println(ex.toString());
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
            if (this.serviceBd.authenticate(this.getRmiUtilisateur(utilisateur))) {
                synchronized (this.utilisateurs) {
                    this.utilisateurs.add(utilisateur);
                }
                this.sendMessageToAll(utilisateur.getName() + " vien de se connecter !", utilisateur);
                return true;
            } else {
                return false;
            }
        } catch (RemoteException ex) {
            System.out.println(ex.toString());
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
