MuchasSolucionesGreedy <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/M2/greedyC/TodosGreedysCost.csv' , sep='	',header = FALSE)
MuchasSolucionesQoS <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/M2/greedyQoS/TodosGreedysQoS.csv' , sep='	',header = FALSE)
MuchasSolucionesRoundRobin <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/M2/robin/TodosROBIN.csv' , sep='	',header = FALSE)
ParetoGeneral <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/M2/eval_M2M05C0001G1000P200/paretoGeneral.txt' , sep='	',header = FALSE)


plot(MuchasSolucionesGreedy ,xlab="Costo", ylab="RTT (ms)", pch=19,col="red",xlim=range(0,17.57134378815291), ylim=range(0,3281565.0430223895))
points(MuchasSolucionesRoundRobin,col="blue", pch=19)
points(MuchasSolucionesQoS,col="green", pch=19)
points(ParetoGeneral,col="grey", pch=19)
legend(
  "topright", 
  pch=c(19,19,19,19), 
  col=c("red", "green", "blue", "grey"), 
  legend = c("Greedy costo", "Greedy Qos", "Round robin", "AE")
)
