# Esercitazione su Alchemist

Modificate il file `prova.pt` del progetto "Alchemist Example", incrementalmente,
per calcolare in ogni device le seguenti cose.

1)	il numero di device vicini

2)	il massimo numero di vicini che il device corrente ha mai avuto

3)	il massimo numero di vicini che un qualunque device della rete ha mai avuto

4)	muoversi verso il vicino che ha meno vicini

5)	muoversi lontano dal vicino che ha più vicini

6)	combinare gli ultimi due punti: ogni device è attratto dal vicino con meno vicini,
	e respinto dal vicino con più vicini.

## Suggerimenti

* Nei primi esercizi, ragionate dove usare `nbr` ("guardo i vicini") e `rep` ("guardo il passato").

* Per muovere un device, bisogna memorizzare una posizione `[x,y]` nella molecola `target`:
``env.put("target", coordinate);``
Sostituite la porzione di codice che ora si occupa del movimento con quanto richiesto.

* Si può ottenere la posizione di un device tramite `self.getCoordinates()`.
Variabili di tipo coordinata possono essere sommate e moltiplicate vettorialmente:
``
[1,3] + [2,-1] == [3,2]
[2,4] * 0.5    == [1,2]
``
