package serviceServeur;


/**
* serviceServeur/UtilisateurPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ./serviceServeur.idl
* mercredi 21 mars 2018 09 h 02 CET
*/

public abstract class UtilisateurPOA extends org.omg.PortableServer.Servant
 implements serviceServeur.UtilisateurOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("getName", new java.lang.Integer (0));
    _methods.put ("setName", new java.lang.Integer (1));
    _methods.put ("getPassword", new java.lang.Integer (2));
    _methods.put ("setPassword", new java.lang.Integer (3));
    _methods.put ("afficher", new java.lang.Integer (4));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // serviceServeur/Utilisateur/getName
       {
         String $result = null;
         $result = this.getName ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 1:  // serviceServeur/Utilisateur/setName
       {
         String name = in.read_string ();
         this.setName (name);
         out = $rh.createReply();
         break;
       }

       case 2:  // serviceServeur/Utilisateur/getPassword
       {
         String $result = null;
         $result = this.getPassword ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 3:  // serviceServeur/Utilisateur/setPassword
       {
         String password = in.read_string ();
         this.setPassword (password);
         out = $rh.createReply();
         break;
       }

       case 4:  // serviceServeur/Utilisateur/afficher
       {
         String message = in.read_string ();
         this.afficher (message);
         out = $rh.createReply();
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:serviceServeur/Utilisateur:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Utilisateur _this() 
  {
    return UtilisateurHelper.narrow(
    super._this_object());
  }

  public Utilisateur _this(org.omg.CORBA.ORB orb) 
  {
    return UtilisateurHelper.narrow(
    super._this_object(orb));
  }


} // class UtilisateurPOA
