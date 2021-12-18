# MP2 - ICWars

##Lancement
Run la méthode main de Play.

##*Controles*

Flèches directionnelles: déplacement du RealPlayer

Gestion du jeu :
- [N] : Next Level. Passe au niveau suivant
- [R] : Reset. Réinitialise la partie

Actions :
- [Enter] : Selectionne une unité (state=NORMAL) ou déplace l'unité à la position du joueur (MOVE_UNIT)
- [A] : Attack, attaque l'unité selectionnée par le joueur. Prix : 50$
- [W] : Wait, ne fait rien. Prix : 0$

Extension :
- [H] : Heal, soigne les unités alliés aux alentours (Rayon de 1 Cell). Prix : 40$
- [M] : Mend, répare/guéris une unité. Prix : 10$ * HpPerdu

##Extension
###Ajouts :
- HealthBar : barre de vie affichée en dessus des unités (HealthBarGraphics). Représente graphiquement les hp.
- Economie : système d'argent, chaque action a un prix associé et le solde du joueur est déduit 
après l'utilisation d'une action (si le montant du joueur n'est pas suffisant, il ne peut pas effectuer l'action)

###Nouvelles Unités :
- Medic : médecin militaire, l'unité peut réaliser l'action (H)eal qui soigne les alliés autour
- Rocket : lance-roquettes, tire sur les ennemis en infligeant des dégats de zone (aussi sur les alliés)

###Niveaux :
Pas d'ajouts, niveaux de base : Level0 et Level1 (les unités sont différentes, une de chaque pour tester les extensions)