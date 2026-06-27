# JEI Trades
JEI Trades is an addon mod for Just Enough Items (JEI) and Roughly Enough Items (REI) that adds information on trades (e.g. villagers) to them.

## Download
You can download JEI Trades from the following links:
- [Modrinth](https://modrinth.com/mod/jei-trades)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/jei-trades)

On your client you need to additionally install Just Enough Items (JEI):
- [Modrinth](https://modrinth.com/mod/jei)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/jei)

Or alternatively Roughly Enough Items (REI):
- [Modrinth](https://modrinth.com/mod/rei)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/roughly-enough-items)

If you use the Fabric version of JEI Trades, you also need to install FabricAPI:
- [Modrinth](https://modrinth.com/mod/fabric-api)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

## How does it work?
JEI Trades allows you to view trade information in JEI/REI, including those of other mods.
It reads the information directly from the server, so it is based on the actual configuration used in the save.
This makes the shown information very complete and JEI Trades able to show information for all mods that use the standard Minecraft trades system, without needing specific support for each mod.

![Villager Trades screen in the game](https://cdn.modrinth.com/data/4SbUVStJ/images/d3dd45dac223a60b795e4e97fdda982e3c2d69c0.png)

## Mod Compatibility Requirements
To be compatible with JEI Trades, a mod must:

- Register and handle its trades using the standard Minecraft trades system.

If this requirement is met, JEI Trades will be able to show the trade information for that mod without needing any specific support.

## Multiplayer and Servers
As JEI Trades reads its information directly from the game data and this is only available on the server side,
it is recommended to install JEI Trades on the server as well.

If you cannot install JEI Trades on the server, the information can not be received from the server, so the client will not be able to show any trade related information.
You can however join a singleplayer save before joining the server; in this case JEI Trades will keep the cached information from the singleplayer save and show it while you are on the server.

As JEI Trades is entirely optional, you can join servers with JEI Trades even if you don't have the mod yourself.