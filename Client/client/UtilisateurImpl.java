package client;

import corbaInterface.UtilisateurPOA;

/**
 *
 */
public class UtilisateurImpl extends UtilisateurPOA implements Utilisateur {
	private String name;
	private String password;

        public UtilisateurImpl(String name, String password) {
            this.name = name;
            this.password = password;
        }
        
        @Override
	public String getName() {
		return name;
	}
        @Override
	public void setName(String name) {
		this.name = name;
	}
        @Override
	public String getPassword() {
		return password;
	}
        @Override
	public void setPassword(String password) {
		this.password = password;
	}
	
        @Override
	public void afficher(String message) {
            System.out.println(message);
            System.out.println(this.getName());
        }
}
