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

###Niveaux (pas de scénario spécifique) :

Niveaux de base : Level0 et Level1 (les unités sont différentes)

Ajouts : 2 niveaux supplémentaires (Level3 et Level4) qui ont le même background que Level1.
- Level2 : pour tester les unités Rocket
- Level3 : pour tester les unités Medic