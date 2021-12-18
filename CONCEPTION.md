#ICWars - Conception

## Nouvelles classes

### HealthBarGraphics / HealthLostBarGraphics :
Package : .icwars.actor.graphics (Choix: des graphismes lié aux ICWarsActor)

Sous-classe de ShapeGraphics

But : visualiser les points de vie d'une unité

###ICWarsGamePanel :
Package : .icwars.gui (Choix: similaire aux autres gui)

But : afficher le niveau actuel et l'argent du joueur

##Modification
###ICWarsActionsPanel :
Modifications mineures pour afficher le prix de chaque action.

##Extension
###Economie
Ajout d'un système économique. Les joueurs (que les RealPlayer) possède de l'argent qu'ils peuvent dépenser 
en effectuant des actions avec leurs unités. Les joueurs reçoivent 100$ après chaque tour. Les prix des actions sont affichés à côté
des actions lors de ACTION_SELECTION.

###Nouvelles unités :
- Medic : médecin militaire, l'unité peut réaliser l'action Heal qui soigne les alliés autour. 
Actions disponibles : (H)eal, (M)end, (W)ait
- Rocket : lance-roquettes, tire sur les ennemis en infligeant des dégats de zone aux unités autour (qui touche aussi les alliés).
Actions disponibles : (A)ttack, (M)end, (W)ait

###Nouvelles actions :
- [H] : Heal, redonne 2hp aux unités alliés aux alentours (Rayon de 1 Cell). Prix : 40$
- [M] : Mend, répare/guéris une unité en lui remettant ses hp au max. Prix : 10$ * HpPerdu