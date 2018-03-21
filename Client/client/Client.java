package client;

import java.util.Properties;
import java.util.Scanner;

import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import serviceServeur.ServiceServeurHelper;
import serviceServeur.UtilisateurHelper;
import serviceServeur.UtilisateurHolder;

/**
 * 
 */
public class Client {
    static final String PREFIX_FOR_CMD = "/";
    static final String CREATE_SERVER = PREFIX_FOR_CMD + "create ";
    static final String LIST_AVAILABLE_SERVER = PREFIX_FOR_CMD + "list";
    static final String JOIN_SERVER = PREFIX_FOR_CMD + "join ";
    static final String LEAVE_SERVER = PREFIX_FOR_CMD + "leave";
    static final String RENAME = PREFIX_FOR_CMD + "rename ";
    static final String QUIT = PREFIX_FOR_CMD + "quit";
        
    public static void main(final String[] args) {
        UtilisateurImpl utilisateur = new UtilisateurImpl("nom1", "pass2");
        serviceServeur.Utilisateur utilisateurObj = null;
        
        // ---------------------------------------------------------------------
        // Connexion au serveur (Corba)
        // ---------------------------------------------------------------------
        serviceServeur.ServiceServeur serviceServeur = null;
        try {
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialPort", "2002");
            props.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
            
            ORB orb = ORB.init((String[]) null, props);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            serviceServeur = (serviceServeur.ServiceServeur) ServiceServeurHelper.narrow(ncRef.resolve_str("ServiceServeur"));
            
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            
            rootpoa.activate_object(utilisateur);

            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(utilisateur);
            utilisateurObj = UtilisateurHelper.narrow(ref);
        }catch (Exception ex) {
           System.out.println("Erreur : " + ex.toString());
           ex.printStackTrace();
           return;
        }
        System.out.println("Connecté au serveur !");
        // ---------------------------------------------------------------------
        // Fin connexion au serveur
        // ---------------------------------------------------------------------
        Scanner in = new Scanner(System.in);
        while(true) {
        	String s = in.nextLine();
        	if (s.startsWith(CREATE_SERVER)) {
                String name = s.substring(CREATE_SERVER.length());
                //connImpl.createChatRoom(token, name);
            } else if (s.startsWith(LIST_AVAILABLE_SERVER)) {
                //connImpl.listChatRooms(token);
            } else if (s.startsWith(JOIN_SERVER)) {
                String name = s.substring(JOIN_SERVER.length());
                //connImpl.joinChatRoom(token, name);
            } else if (s.startsWith(LEAVE_SERVER)) {
                //connImpl.leaveChatRoom(token);
            } else if (s.startsWith(RENAME)) {
                String name = s.substring(RENAME.length());
                //connImpl.changeName(token, name);
            } else if (s.startsWith(QUIT)) {
                System.exit(0);
            } else {                
                serviceServeur.sendMessage(s, utilisateurObj);
            }
        }
    }
}