package client;

import java.util.Properties;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import serviceServeur.ServiceServeurHelper;

/**
 * 
 */
public class Client {
    public static void main(final String[] args) {

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
        }catch (Exception ex) {
           System.out.println("Erreur : " + ex.toString());
           ex.printStackTrace();
           return;
        }
        System.out.println("Connecté au serveur !");
        // ---------------------------------------------------------------------
        // Fin connexion au serveur
        // ---------------------------------------------------------------------
        
        // Todo : suite
    }
}