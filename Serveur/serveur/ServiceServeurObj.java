package serveur;

import client.Utilisateur;
import serviceServeur.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import serviceServeur.ServiceServeurPOA;

/**
 * 
 */
public class ServiceServeurObj extends ServiceServeurPOA {

    @Override
    public void sendMessage(String message, UtilisateurHolder utilisateur) {
        System.out.println("Message : " + message);
    }
    
    @Override
    public boolean authenticate(String nom, String motDePasse, UtilisateurHolder utilisateur) {
        return false;
    }
}
