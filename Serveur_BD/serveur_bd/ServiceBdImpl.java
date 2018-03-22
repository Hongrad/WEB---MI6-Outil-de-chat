package serveur_bd;

import client.Utilisateur;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author
 */
public class ServiceBdImpl implements ServiceBd {
    
    // Liste des données utilisateur indexé par le nom de l'utilisateur
    private HashMap<String, UtilisateurData> utilisateursData;

    public ServiceBdImpl() {
        utilisateursData = new HashMap<>();
    }

    /**
     * Charge toutes les données utilisateur
     * 
     * @return 
     */
    public boolean loadAllUsersDataFromFile(){
        ObjectInputStream ois = null;
	try {
	    ois = new ObjectInputStream(new FileInputStream(new File(this.getClass().getResource("DB").toURI())));
            ArrayList<UtilisateurData> datas = (ArrayList<UtilisateurData>) ois.readObject();
            
            synchronized (this.utilisateursData) {
                for (UtilisateurData data : datas) {
                    this.utilisateursData.put(data.getNom(), data);
                }
            }
            
	    ois.close();
	}catch (EOFException ex) {
            // Si le fichier est vide : ne rien faire
        }catch (Exception ex) {
            // Fichier introuvable ou corrompu
            System.out.println("Erreur de lecture du fichier de BD !");
            System.out.println(ex.toString());
            return false;
	}

        System.out.println("Liste des données utilisateurs chargé !");
        return true;
    }
    
    /**
     * Sauvegarde toutes les données utilisateur
     * 
     * @return 
     */
    public boolean saveAllUsersDataToFile(){        
        ObjectOutputStream oos;
        try{
            oos = new ObjectOutputStream(new FileOutputStream(new File(this.getClass().getResource("DB").toURI())));
            synchronized (this.utilisateursData) {
                oos.writeObject(new ArrayList<UtilisateurData>(this.utilisateursData.values()));
            }
            oos.close();
        }catch (Exception ex){
            System.out.println(ex.toString());
            return false;
        }

        System.out.println("Liste des données utilisateurs sauvegardé !");
        return true;
    }

    @Override
    public boolean authenticate(Utilisateur utilisateur) throws RemoteException {
        UtilisateurData utilisateurData = null;
        synchronized (this.utilisateursData) {
            utilisateurData = this.utilisateursData.get(utilisateur.getName());
        }
        
        if (utilisateurData != null) {
            if (utilisateurData.getMotDePasse().equals(utilisateur.getPassword())) {
                return true;
            } else {
                utilisateur.afficher("Erreur : Mot de passe incorrect !");
                return false;
            }
        } else {
            utilisateur.afficher("Erreur : Identifiant introuvable !");
            return false;
        }
    }

    @Override
    public boolean createNewUser(Utilisateur utilisateur) throws RemoteException {
        synchronized (this.utilisateursData) {
            UtilisateurData utilisateurData = this.utilisateursData.get(utilisateur.getName());

            if (utilisateurData == null) {
                utilisateurData = new UtilisateurData(utilisateur.getName(), utilisateur.getPassword());
                this.utilisateursData.put(utilisateur.getName(), utilisateurData);
                return true;
            } else {
                utilisateur.afficher("Erreur : Ce nom est déjà utilisé !");
                return false;
            }
        }
    }
}
