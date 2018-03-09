package serveur_bd;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * 
 */
public class ServeurBd {
    public static void main(final String[] args) {
        // ---------------------------------------------------------------------
        // Mise en ligne du service (RMI)
        // ---------------------------------------------------------------------
        ServiceBdImpl service = new ServiceBdImpl();
        
        ServiceBd stub = null;
        try {
            stub = (ServiceBd) UnicastRemoteObject.exportObject(service, 0);
        } catch (RemoteException ex) {
            System.out.println("Erreur : La création du stub pour le service BD !");
            return;
        }
        
        try {
            Registry registry = LocateRegistry.createRegistry(2001);
            registry.bind("ServiceBd", stub);
        } catch (Exception ex) {
            System.out.println("Erreur : La création du registre pour le service 'ServiceBd' a échoué !");
            return;
        }
        // ---------------------------------------------------------------------
        // Fin mise en ligne du service
        // ---------------------------------------------------------------------
    }
}
