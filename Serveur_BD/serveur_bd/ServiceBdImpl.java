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
    
    private ArrayList<Utilisateur> utilisateurs = null;

    /**
     * 
     */
    public boolean loadAllUsersFromFile(){
        ObjectInputStream ois = null;
	try {
	    ois = new ObjectInputStream(new FileInputStream(new File(this.getClass().getResource("DB").toURI())));
	    this.utilisateurs = (ArrayList<Utilisateur>) ois.readObject();
	    ois.close();
	}catch (EOFException ex) {
            return true;
        }catch (Exception ex) {
            System.out.println("Erreur de lecture du fichier de BD !");
            System.out.println(ex.toString());
            return false;
	}
	
	if (this.utilisateurs == null){
	    this.utilisateurs = new ArrayList<>();
	}
        
        System.out.println("Liste des utilisateurs chargé !");
        return true;
    }
    
    /**
     * 
     */
    public boolean saveAllUsersToFile(){
        ObjectOutputStream oos;
        try{
            oos = new ObjectOutputStream(new FileOutputStream(new File(this.getClass().getResource("DB").toURI())));
            oos.writeObject(this.utilisateurs);
            oos.close();
        }catch (Exception ex){
            System.out.println(ex.toString());
            return false;
        }
        
        System.out.println("Liste des utilisateurs sauvegardé !");
        return true;
    }
}
