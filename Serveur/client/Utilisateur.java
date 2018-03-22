/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 */
public interface Utilisateur extends Remote {
    public String getName() throws RemoteException;
    public void setName(String name) throws RemoteException;
    public String getPassword() throws RemoteException;
    public void setPassword(String password) throws RemoteException;
    public void afficher(String message) throws RemoteException;
}
