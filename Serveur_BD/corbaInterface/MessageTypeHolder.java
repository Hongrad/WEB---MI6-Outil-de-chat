package corbaInterface;

/**
* corbaInterface/MessageTypeHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ./serviceServeur.idl
* mercredi 21 mars 2018 10 h 47 CET
*/

public final class MessageTypeHolder implements org.omg.CORBA.portable.Streamable
{
  public corbaInterface.MessageType value = null;

  public MessageTypeHolder ()
  {
  }

  public MessageTypeHolder (corbaInterface.MessageType initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = corbaInterface.MessageTypeHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    corbaInterface.MessageTypeHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return corbaInterface.MessageTypeHelper.type ();
  }

}