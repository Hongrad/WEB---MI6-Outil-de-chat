package serveur;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeUnit;
import serveur_bd.ServiceBd;

/**
 * 
 */
public class Serveur {
    public static void main(final String[] args) {
        // ---------------------------------------------------------------------
        // Connexion au service de la BD (RMI)
        // ---------------------------------------------------------------------
        Registry registry = null;
        try{
            registry = LocateRegistry.getRegistry(2001);
        } catch (RemoteException e){
            System.out.println("Erreur : Le registre du service 'ServiceBd' est introuvable !");
            return;
        }
        
        ServiceBd serviceBd = null;
        while (serviceBd == null) {
            try{
                serviceBd = (ServiceBd) registry.lookup("ServiceBd");
            } catch (Exception e){
                System.out.println("Erreur : La récupération du service 'ServiceBd' a échoué !");
                System.out.println("Nouvelle essai dans 10 sec ...");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException ex) {}
            }
        }
        
        System.out.println("Connexion au service 'ServiceBd' Réussi !");
        // ---------------------------------------------------------------------
        // Fin connexion au service de la BD
        // ---------------------------------------------------------------------
    }
}
