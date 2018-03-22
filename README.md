# WEB---MI6-Outil-de-chat

# Membre du groupes :

# Valentin Bouchet
# Benoit Costechareyre
# Baptiste Valayer

# Pour lancer le projet :
executer la commande 'orbd -ORBInitialPort 2002' (Creer le BUS Corba)

# Vous avez le choix entre :

# 1) Créer 3 projets :

# codeSourcesProjets/Serveur_BD
# codeSourcesProjets/Serveur
# codeSourcesProjets/Client

# Puis lancer les projets dans l'ordre :

Lancer ServeurBd.java	(3ième tière : responsable de la BD)
Lancer Serveur.java	(2ième tière : responsable des salles de chat)
Lancer Client.java	(1er tière : l'interface utilisateur qui permet d'envoyer les messages)


# 2) Vous rendre dans le dossier 'jar' et éxécuter les 3 jar dans l'ordre (3 consoles différentes) :

Lancer Serveur_BD.jar	(java -cp Serveur_BD.jar serveur_bd.ServeurBd)
Lancer Serveur.jar	(java -cp Serveur.jar serveur.Serveur)
Lancer Client.jar	(java -cp Client.jar client.Client)

