package serveur;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import serveur_bd.ServiceBd;

import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import serviceServeur.ServiceServeurHelper;

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
        
        // ---------------------------------------------------------------------
        // Mise en ligne du serveur (Corba)
        // ---------------------------------------------------------------------
        ORB orb = null;      
        try{
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialPort", "2002");
            props.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
            
            orb = ORB.init((String[]) null, props);      
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // create servant and register it with the ORB
            ServiceServeurObj addobj = new ServiceServeurObj();
            addobj.setORB(orb);

            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(addobj);
            serviceServeur.ServiceServeur href = ServiceServeurHelper.narrow(ref);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            NameComponent path[] = ncRef.to_name("ServiceServeur");
            ncRef.rebind(path, href);
        }catch (Exception ex) {
            System.out.println("Erreur : " + ex.toString());
            ex.printStackTrace(System.out);
            return;
        }
        System.out.println("Mise en ligne du serveur (ServiceServeur) réussi !");
        // ---------------------------------------------------------------------
        // Fin mise en ligne du serveur (Corba)
        // ---------------------------------------------------------------------
        
        orb.run();
    }
}
