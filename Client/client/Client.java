package client;

import corbaInterface.MessageType;
import java.util.Properties;
import java.util.Scanner;

import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import corbaInterface.ServiceServeurHelper;
import corbaInterface.UtilisateurHelper;

/**
 * 
 */
public class Client {
    static final String PREFIX_FOR_CMD = "/";
    static final String HELP = "help";
    static final String CREATE_SERVER = "create";
    static final String LIST_AVAILABLE_SERVER = "list";
    static final String JOIN_SERVER = "join ";
    static final String LEAVE_SERVER = "leave";
    static final String RENAME = "rename ";
    static final String QUIT = "quit";
        
    public static void main(final String[] args) {
        UtilisateurImpl u = new UtilisateurImpl("nom1", "pass2");
        corbaInterface.Utilisateur utilisateur = null;
        
        // ---------------------------------------------------------------------
        // Connexion au serveur (Corba)
        // ---------------------------------------------------------------------
        corbaInterface.ServiceServeur serviceServeur = null;
        try {
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialPort", "2002");
            props.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
            
            ORB orb = ORB.init((String[]) null, props);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            serviceServeur = (corbaInterface.ServiceServeur) ServiceServeurHelper.narrow(ncRef.resolve_str("ServiceServeur"));
            
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            
            rootpoa.activate_object(u);

            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(u);
            utilisateur = UtilisateurHelper.narrow(ref);
        }catch (Exception ex) {
           System.out.println("Erreur : " + ex.toString());
           ex.printStackTrace();
           return;
        }
        System.out.println("Connecté au serveur !");
        // ---------------------------------------------------------------------
        // Fin connexion au serveur
        // ---------------------------------------------------------------------
                
        try {
            Scanner in = new Scanner(System.in);

            System.out.print("Nom : ");
            String s = in.nextLine();
            System.out.println("Bonjour " + s + " !");
            utilisateur.setName(s);
            
            // Notification du serveur de la connexion
            serviceServeur.sendMessage(MessageType.authenticate, "", utilisateur);
            // Todo : voir mot de passe

            System.out.println("Tapé '/help' pour la liste des commandes !");
            while(true) {
                System.out.print("\nMessage : ");
                s = in.nextLine();

                if (s.startsWith(PREFIX_FOR_CMD)) {
                    // Si c'est une commande

                    s = s.replaceFirst(PREFIX_FOR_CMD, "");

                    if (s.startsWith(HELP)) {
                        System.out.println("Liste des commandes :");
                        System.out.println(HELP);
                        System.out.println(QUIT);
                    } else if (s.startsWith(CREATE_SERVER)) {
                        // Todo
                    } else if (s.startsWith(LIST_AVAILABLE_SERVER)) {
                        // Todo
                    } else if (s.startsWith(JOIN_SERVER)) {
                        // Todo
                    } else if (s.startsWith(LEAVE_SERVER)) {
                        // Todo
                    } else if (s.startsWith(RENAME)) {
                        // Todo
                    } else if (s.startsWith(QUIT)) {
                        System.exit(0);
                    } else {
                        System.out.println("Erreur : La commande '" + s + "' n'existe pas !");
                    }
                }else {                
                    serviceServeur.sendMessage(MessageType.message, s, utilisateur);
                }
            }
        }catch (Exception e) {
            System.out.println("Une erreur c'est produite !");
            System.out.println(e.toString());
        }
    }
}