package serviceServeur;

/**
* serviceServeur/UtilisateurHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ./ServiceServeur.idl
* mardi 20 mars 2018 16 h 34 CET
*/

public final class UtilisateurHolder implements org.omg.CORBA.portable.Streamable
{
  public serviceServeur.Utilisateur value = null;

  public UtilisateurHolder ()
  {
  }

  public UtilisateurHolder (serviceServeur.Utilisateur initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = serviceServeur.UtilisateurHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    serviceServeur.UtilisateurHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return serviceServeur.UtilisateurHelper.type ();
  }

}
