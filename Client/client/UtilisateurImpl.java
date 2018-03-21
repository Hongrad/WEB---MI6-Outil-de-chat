/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import serviceServeur.UtilisateurPOA;

/**
 *
 * @author ratchetsniper2
 */
public class UtilisateurImpl extends UtilisateurPOA {
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
