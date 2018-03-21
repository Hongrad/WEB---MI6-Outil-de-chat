package serveur_bd;

import corbaInterface.Utilisateur;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 */
public interface ServiceBd extends Remote {
    
    /**
     * Authentifie et charge les données de l'utilisateur
     * 
     * @param utilisateur
     * @return vrai si l'utilisateur a bien été authentifié
     * @throws RemoteException 
     */
    public boolean authenticate(Utilisateur utilisateur) throws RemoteException;
    
    /**
     * Creer un nouvelle utilisateur en BD avec les infos de l'utilisateur donné
     * 
     * @param utilisateur
     * @return vrai si réussi
     * @throws RemoteException 
     */
    public boolean createNewUser(Utilisateur utilisateur) throws RemoteException;
}
