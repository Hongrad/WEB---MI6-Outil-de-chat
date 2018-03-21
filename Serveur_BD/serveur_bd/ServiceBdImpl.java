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

/**
 * @author
 */
public class ServiceBdImpl implements ServiceBd {
    
    // Liste des données utilisateur
    private ArrayList<UtilisateurData> utilisateursData = null;

    public ServiceBdImpl() {
        utilisateursData = new ArrayList<>();
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
	    this.utilisateursData = (ArrayList<UtilisateurData>) ois.readObject();
	    ois.close();
	}catch (EOFException ex) {
            // Si le fichier est vide : ne rien faire
        }catch (Exception ex) {
            // Fichier introuvable ou corrompu
            System.out.println("Erreur de lecture du fichier de BD !");
            System.out.println(ex.toString());
            return false;
	}

	if (this.utilisateursData == null){
	    this.utilisateursData = new ArrayList<>();
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
            oos.writeObject(this.utilisateursData);
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
        return false;
    }

    @Override
    public boolean createNewUser(Utilisateur utilisateur) throws RemoteException {
        return false;
    }

    @Override
    public boolean updateUser(Utilisateur utilisateur) throws RemoteException {
        return false;
    }
}
