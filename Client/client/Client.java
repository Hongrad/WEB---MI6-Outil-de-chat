package client;

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
    static final String CREATE_ROOM = "create";
    static final String JOIN_ROOM = "join";
    static final String GET_HISTORY = "hitory";
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
           System.exit(0);
        }
        System.out.println("Connecté au serveur !");
        // ---------------------------------------------------------------------
        // Fin connexion au serveur
        // ---------------------------------------------------------------------
                
        try {
            Scanner in = new Scanner(System.in);
            String s = "";
            
            Boolean connected = false;
            while (!connected) {
                System.out.print("Connexion (0) / Créer un compte (1) : ");
                String type = in.nextLine();
                if (type.equals("0") || type.equals("1")) {
                    
                    System.out.print("Nom : ");
                    s = in.nextLine();
                    utilisateur.setName(s);
                    
                    System.out.print("Mot de Passe : ");
                    s = in.nextLine();
                    utilisateur.setPassword(s);
                    
                    if (type.equals("0")) {
                        // Connexion
                        if (serviceServeur.authenticate(utilisateur)) {
                            connected = true;
                            System.out.println("Connexion réussi !");
                        }
                    } else {
                        // Création de compte
                        if (serviceServeur.createAccount(utilisateur)) {
                            connected = true;
                            System.out.println("Création du compte réussi !");
                        }
                    }
                }
            }

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
                        System.out.println(CREATE_ROOM + "'roomName' (not done)");
                        System.out.println(JOIN_ROOM + "'roomName' (not done)");
                        System.out.println(GET_HISTORY + " (not done)");
                        System.out.println(QUIT);
                        
                    } else if (s.startsWith(CREATE_ROOM)) {
                        String roomName = s.replace(CREATE_ROOM, "").trim();
                        
                        if (serviceServeur.createRoom(roomName, utilisateur)){
                            System.out.println("La salle '" + roomName + "' a été créé !");
                        }else{
                            System.out.println(" Erreur : La sale n'a pas pu être créé ...");
                        }
                        
                    } else if (s.startsWith(JOIN_ROOM)) {
                        String roomName = s.replace(JOIN_ROOM, "").trim();
                        
                        if (serviceServeur.joinRoom(roomName, utilisateur)){
                            System.out.println("Vous avez rejoin la salle '" + roomName + "' !");
                        }else{
                            System.out.println(" Erreur : La sale '" + roomName + "' n'éxiste pas ...");
                        }
                        
                    } else if (s.startsWith(GET_HISTORY)) {
                        System.out.println(serviceServeur.getHistory(utilisateur));
                        
                    } else if (s.startsWith(QUIT)) {
                        serviceServeur.quit(utilisateur);
                        System.exit(0);
                        
                    } else {
                        System.out.println("Erreur : La commande '" + s + "' n'existe pas !");
                    }
                }else if (s.trim().length() > 0) {
                    serviceServeur.sendMessage(s, utilisateur);
                }
            }
        }catch (Exception e) {
            System.out.println("Une erreur c'est produite !");
            System.out.println(e.toString());
        }
    }
}