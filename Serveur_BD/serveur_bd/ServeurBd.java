package serveur_bd;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * 
 */
public class ServeurBd {
    public static void main(final String[] args) {
        ServiceBdImpl service = new ServiceBdImpl();
        
        // ---------------------------------------------------------------------
        // Chargement de la liste des utilisateurs depuis le fichier
        // ---------------------------------------------------------------------
        if(!service.loadAllUsersDataFromFile()){
            return;
        }
        // ---------------------------------------------------------------------
        // Fin chargement de la liste des utilisateurs
        // ---------------------------------------------------------------------
        
        // ---------------------------------------------------------------------
        // Mise en ligne du service (RMI)
        // ---------------------------------------------------------------------
        ServiceBd stub = null;
        try {
            stub = (ServiceBd) UnicastRemoteObject.exportObject(service, 0);
        } catch (RemoteException ex) {
            System.out.println("Erreur : La création du stub pour le service BD !");
            return;
        }
        
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(2001);
            registry.bind("ServiceBd", stub);
        } catch (Exception ex) {
            System.out.println("Erreur : La création du registre pour le service 'ServiceBd' a échoué !");
            return;
        }
        // ---------------------------------------------------------------------
        // Fin mise en ligne du service
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
                service.saveAllUsersDataToFile();
                // ---------------------------------------------------------------------
                // Fin sauvegarde des utilisateurs
                // ---------------------------------------------------------------------
            }
        }
    }
}
