package corbaInterface;


/**
* corbaInterface/ServiceServeurPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ./corbaInterface.idl
* mercredi 21 mars 2018 11 h 54 CET
*/

public abstract class ServiceServeurPOA extends org.omg.PortableServer.Servant
 implements corbaInterface.ServiceServeurOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("createAccount", new java.lang.Integer (0));
    _methods.put ("authenticate", new java.lang.Integer (1));
    _methods.put ("createRoom", new java.lang.Integer (2));
    _methods.put ("joinRoom", new java.lang.Integer (3));
    _methods.put ("sendMessage", new java.lang.Integer (4));
    _methods.put ("getHistory", new java.lang.Integer (5));
    _methods.put ("quit", new java.lang.Integer (6));
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
       case 0:  // corbaInterface/ServiceServeur/createAccount
       {
         corbaInterface.Utilisateur utilisateur = corbaInterface.UtilisateurHelper.read (in);
         boolean $result = false;
         $result = this.createAccount (utilisateur);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 1:  // corbaInterface/ServiceServeur/authenticate
       {
         corbaInterface.Utilisateur utilisateur = corbaInterface.UtilisateurHelper.read (in);
         boolean $result = false;
         $result = this.authenticate (utilisateur);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 2:  // corbaInterface/ServiceServeur/createRoom
       {
         String roomName = in.read_string ();
         corbaInterface.Utilisateur utilisateur = corbaInterface.UtilisateurHelper.read (in);
         boolean $result = false;
         $result = this.createRoom (roomName, utilisateur);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 3:  // corbaInterface/ServiceServeur/joinRoom
       {
         String roomName = in.read_string ();
         corbaInterface.Utilisateur utilisateur = corbaInterface.UtilisateurHelper.read (in);
         boolean $result = false;
         $result = this.joinRoom (roomName, utilisateur);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 4:  // corbaInterface/ServiceServeur/sendMessage
       {
         String message = in.read_string ();
         corbaInterface.Utilisateur utilisateur = corbaInterface.UtilisateurHelper.read (in);
         this.sendMessage (message, utilisateur);
         out = $rh.createReply();
         break;
       }

       case 5:  // corbaInterface/ServiceServeur/getHistory
       {
         corbaInterface.Utilisateur utilisateur = corbaInterface.UtilisateurHelper.read (in);
         String $result = null;
         $result = this.getHistory (utilisateur);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 6:  // corbaInterface/ServiceServeur/quit
       {
         corbaInterface.Utilisateur utilisateur = corbaInterface.UtilisateurHelper.read (in);
         this.quit (utilisateur);
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
    "IDL:corbaInterface/ServiceServeur:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public ServiceServeur _this() 
  {
    return ServiceServeurHelper.narrow(
    super._this_object());
  }

  public ServiceServeur _this(org.omg.CORBA.ORB orb) 
  {
    return ServiceServeurHelper.narrow(
    super._this_object(orb));
  }


} // class ServiceServeurPOA
