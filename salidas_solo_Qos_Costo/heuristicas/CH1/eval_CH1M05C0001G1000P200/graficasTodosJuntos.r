MuchasSolucionesGreedy <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/CH1/greedyC/TodosGreedysCost.csv' , sep='	',header = FALSE)
MuchasSolucionesQoS <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/CH1/greedyQoS/TodosGreedysQoS.csv' , sep='	',header = FALSE)
MuchasSolucionesRoundRobin <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/CH1/robin/TodosROBIN.csv' , sep='	',header = FALSE)
ParetoGeneral <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/CH1/eval_CH1M05C0001G1000P200/paretoGeneral.txt' , sep='	',header = FALSE)


plot(MuchasSolucionesGreedy ,xlab="Costo", ylab="RTT (ms)", pch=19,col="red",xlim=range(0,22.450832199447813), ylim=range(0,2222879.4506255863))
points(MuchasSolucionesRoundRobin,col="blue", pch=19)
points(MuchasSolucionesQoS,col="green", pch=19)
points(ParetoGeneral,col="grey", pch=19)
legend(
  "topright", 
  pch=c(19,19,19,19), 
  col=c("red", "green", "blue", "grey"), 
  legend = c("Greedy costo", "Greedy Qos", "Round robin", "AE")
)
