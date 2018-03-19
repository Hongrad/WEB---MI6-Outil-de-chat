package serveur_bd;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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

        // ---------------------------------------------------------------------
        // Chargement de la liste des utilisateurs depuis le fichier
        // ---------------------------------------------------------------------
        while (!service.loadAllUsersFromFile()){
            System.out.println("Nouvelle essai dans 10 sec ...");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException ex) {}
        }
        // ---------------------------------------------------------------------
        // Fin chargement de la liste des utilisateurs
        // ---------------------------------------------------------------------
        
        Scanner sc = new Scanner(System.in);
        String action = "";
        while (!action.equalsIgnoreCase("quitter")) {
            System.out.print("Action (sauvegarder) : ");
            action = sc.nextLine();
            
            if (action.equalsIgnoreCase("sauvegarder")){
                // ---------------------------------------------------------------------
                // Sauvegarde des utilisateurs
                // ---------------------------------------------------------------------
                while (!service.saveAllUsersToFile()){
                    System.out.println("Nouvelle essai dans 10 sec ...");
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException ex) {}
                }
                // ---------------------------------------------------------------------
                // Fin sauvegarde des utilisateurs
                // ---------------------------------------------------------------------
            }
        }
    }
}
