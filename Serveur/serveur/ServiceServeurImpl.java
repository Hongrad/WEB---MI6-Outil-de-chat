package serveur;

import corbaInterface.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import serveur_bd.ServiceBd;

/**
 * 
 */
public class ServiceServeurImpl extends ServiceServeurPOA {
    // Liste des salles de chat
    private HashMap<String, ArrayList<Utilisateur>> chatRooms;
    
    // Liste des utilisateur et de la salle dans laquelle il est
    private HashMap<Utilisateur, String> utilisateurs;
    
    // Le service de la BD
    private ServiceBd serviceBd;

    public ServiceServeurImpl(ServiceBd serviceBd) {
        this.chatRooms = new HashMap<>();
        this.utilisateurs = new HashMap<>();
        this.serviceBd = serviceBd;
    }
    
    /**
     * Retourne un utilisateur connecté au stub RMI
     * Permet de faire le lien entre l'interface corba et rmi
     * 
     * @param utilisateurCorba
     * @return utilisateurRmi
     */
    private client.Utilisateur getRmiUtilisateur(Utilisateur utilisateurCorba) {
        client.Utilisateur utilisateurRmi = new client.Utilisateur() {
            @Override
            public String getName() throws RemoteException {
                return utilisateurCorba.getName();
            }

            @Override
            public void setName(String name) throws RemoteException {
                utilisateurCorba.setName(name);
            }

            @Override
            public String getPassword() throws RemoteException {
                return utilisateurCorba.getPassword();
            }

            @Override
            public void setPassword(String password) throws RemoteException {
                utilisateurCorba.setPassword(password);
            }

            @Override
            public void afficher(String message) throws RemoteException {
                utilisateurCorba.afficher(message);
            }
        };
        
        try {
            client.Utilisateur stub = (client.Utilisateur) UnicastRemoteObject.exportObject(utilisateurRmi, 0);
        } catch (RemoteException ex) {
            System.out.println("Erreur : La création du stub RMI a échoué pour l'utilisateur " + utilisateurCorba.getName());
        }
        
        return utilisateurRmi;
    }
    
    /**
     * Envoie un message à tous les utilisateurs de la salle de l'utilisateur passé en paramètre
     * 
     * @param message
     * @param utilisateur 
     */
    private void sendMessageToRoom(String message, Utilisateur utilisateur) {
        synchronized (this.utilisateurs) {
            String chatRoomName = this.utilisateurs.get(utilisateur);
        
            if (chatRoomName != null) {
                synchronized (this.chatRooms) {
                    ArrayList<Utilisateur> lostUtilisateurs = new ArrayList<>();
                    
                    for (Utilisateur u : this.chatRooms.get(chatRoomName)) {
                        if (!u.equals(utilisateur)) {
                            try {
                                u.afficher(message);
                            } catch (Exception e) {
                                // Utilisateur non joingnable : le supprimer
                                lostUtilisateurs.add(u);
                            }
                        }
                    }
                    
                    for (Utilisateur u : lostUtilisateurs) {
                        this.disconnectUtilisateurFromServeur(u);
                    }
                }
            }
        }
    }
    
    /**
     * Permet de déconnecter un utilisateur de la salle de chat
     * 
     * @param utilisateur 
     */
    private void disconnectUtilisateurFromChatRoom(Utilisateur utilisateur) {
        synchronized (this.utilisateurs) {
            String chatRoomName = this.utilisateurs.get(utilisateur);
            
            if (chatRoomName != null) {
                synchronized (this.chatRooms) {
                    this.chatRooms.get(chatRoomName).remove(utilisateur);
                    
                    try {
                        String name = utilisateur.getName();
                        this.sendMessageToRoom(name + " s'est déconnecté !", utilisateur);
                    } catch (Exception ex) {
                        this.sendMessageToRoom("Un utilisateur n'est plus joignable !", utilisateur);
                    }
                                        
                    // Si il n'y a plus d'utilisateur dans la salle, on la supprime 
                    if (this.chatRooms.get(chatRoomName).size() == 0) {
                        this.chatRooms.remove(chatRoomName);
                    }
                }
            }
        }
    }
    
    /**
     * Permet de déconnecter un utilisateur
     * 
     * @param utilisateur 
     */
    private void disconnectUtilisateurFromServeur(Utilisateur utilisateur) {
        synchronized (this.utilisateurs) {
            this.disconnectUtilisateurFromChatRoom(utilisateur);
            
            this.utilisateurs.remove(utilisateur);
        }
    }
    
    // -------------------------------------------------------------------------
    // Implémentation de l'interface
    // -------------------------------------------------------------------------
    
    /**
     * L'utilisateur tante de créer un compte
     * 
     * @param utilisateur
     * @return 
     */
    @Override
    public boolean createAccount(Utilisateur utilisateur) {
        Boolean connected = false;
        
        try {
            connected = this.serviceBd.createNewUser(this.getRmiUtilisateur(utilisateur));
        } catch (RemoteException ex) {
            System.out.println(ex.toString());
        }
        
        if (connected) {
            synchronized (this.utilisateurs) {
                this.utilisateurs.put(utilisateur, null);
            }
        }
        
        return connected;
    }
    
    /**
     * L'utilisateur tante de s'authentifier
     * 
     * @param utilisateur
     * @return 
     */
    @Override
    public boolean authenticate(Utilisateur utilisateur) {
        // Todo : tester si il n'est pas déjà connecté
        
        Boolean connected = false;
        
        try {
            connected = this.serviceBd.authenticate(this.getRmiUtilisateur(utilisateur));
        } catch (RemoteException ex) {
            System.out.println(ex.toString());
        }
        
        if (connected) {
            synchronized (this.utilisateurs) {
                this.utilisateurs.put(utilisateur, null);
            }
        }
        
        return connected;
    }
    
    /**
     * L'utilisateur veux récupérer la liste des sales de chat disponibles
     * 
     * @param utilisateur 
     * @return  
     */
    @Override
    public String getRoomList(Utilisateur utilisateur) {
        String roomList = "";
        
        synchronized (this.chatRooms) {
            for (String roomName : this.chatRooms.keySet()) {
                roomList = roomList.concat("\n"+roomName);
            }
        }
        
        return roomList;
    }
    
    /**
     * L'utilisateur tante de créer une nouvelle salle de chat
     * 
     * @param roomName
     * @param utilisateur
     * @return 
     */
    @Override
    public boolean createRoom(String roomName, Utilisateur utilisateur) {
        synchronized (this.utilisateurs) {
            synchronized (this.chatRooms) {
                if (!this.chatRooms.containsKey(roomName)) {
                    this.disconnectUtilisateurFromChatRoom(utilisateur);
                    
                    this.utilisateurs.put(utilisateur, roomName);
                    ArrayList<Utilisateur> roomUsers = new ArrayList<>();
                    roomUsers.add(utilisateur);
                    this.chatRooms.put(roomName, roomUsers);
                    return true;
                } else {
                    utilisateur.afficher("Erreur : Cette salle existe déjà !");
                    return false;
                }
            }
        }
    }
    
    /**
     * L'utilisateur tante de rejoindre une salle de chat
     * 
     * @param roomName
     * @param utilisateur
     * @return 
     */
    @Override
    public boolean joinRoom(String roomName, Utilisateur utilisateur) {
        synchronized (this.utilisateurs) {
            synchronized (this.chatRooms) {
                if (this.chatRooms.containsKey(roomName)) {
                    this.disconnectUtilisateurFromChatRoom(utilisateur);
                    
                    this.utilisateurs.put(utilisateur, roomName);
                    this.chatRooms.get(roomName).add(utilisateur);
                    this.sendMessageToRoom(utilisateur.getName() + " vien de se connecter !", utilisateur);
                    return true;
                } else {
                    utilisateur.afficher("Erreur : Cette salle n'existe pas !");
                    return false;
                }
            }
        }
    }
    
    /**
     * L'utilisateur envoie un message au autres membres de sa salle de chat
     * 
     * @param message
     * @param utilisateur 
     */
    @Override
    public void sendMessage(String message, Utilisateur utilisateur) {
        synchronized (this.utilisateurs) {            
            if (this.utilisateurs.get(utilisateur) != null) {
                this.sendMessageToRoom(utilisateur.getName() + " : " + message, utilisateur);
            }
        }
    }
    
    /**
     * L'utilisateur veux récupérer l'historique des conversations
     * 
     * @param utilisateur 
     * @return  
     */
    @Override
    public String getHistory(Utilisateur utilisateur) {
        return "(not done)";
    }
    
    /**
     * L'utilisateur se déconnecte
     * 
     * @param utilisateur 
     */
    @Override
    public void quit(Utilisateur utilisateur) {
        this.disconnectUtilisateurFromServeur(utilisateur);
    }
}
