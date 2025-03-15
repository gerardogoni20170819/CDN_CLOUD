#correr este script para distintas instancias y acumula los valores en los archivos correspondientes en la carpteta /home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/
library(emoa)
library(nortest)
GreedyCosto <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/AMPL3_pareto_greedy_costo.pr' , sep='	',header = FALSE)
GreedyQoS <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/AMPL3_pareto_greedy_Qos.pr' , sep='	',header = FALSE)
RoundRobin <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/AMPL3_pareto_RR.pr' , sep='	',header = FALSE)
AE <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/AMPL3_paretoSoloAE.pr' , sep='	',header = FALSE)
CPLEX <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/AMPL3_pareto_CPLEX.pr' , sep='	',header = FALSE)
ParetoGeneral <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/AMPL3_paretoGeneralTodasLasTecnicasMasAE.pr' , sep='	',header = FALSE)


matrizzz = as.matrix(ParetoGeneral)
traspuesta<-t(matrizzz)
maximaX<-67235.27670000003
maximaY<-3.5578892085914613
hypervolumenReferencia<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))


matrizzz = as.matrix(GreedyCosto)
traspuesta<-t(matrizzz)
hypervolumenGreedyCosto<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoGreedyCosto<-hypervolumenGreedyCosto/hypervolumenReferencia
escribir <- paste(c("GreedyCosto", hypervolumenRelativoGreedyCosto), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoEEVALEXPE.csv',ncolumns = 1,append = FALSE, sep ='	')


matrizzz = as.matrix(GreedyQoS)
traspuesta<-t(matrizzz)
hypervolumenGreedyQoS<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoGreedyQoS<-hypervolumenGreedyQoS/hypervolumenReferencia
escribir <- paste(c("GreedyQoS", hypervolumenRelativoGreedyQoS), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoEEVALEXPE.csv',ncolumns = 1,append = TRUE, sep ='	')


matrizzz = as.matrix(RoundRobin)
traspuesta<-t(matrizzz)
hypervolumenRoundRobin<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoRoundRobin<-hypervolumenRoundRobin/hypervolumenReferencia
escribir <- paste(c("RoundRobin", hypervolumenRelativoRoundRobin), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoEEVALEXPE.csv',ncolumns = 1,append = TRUE, sep ='	')


matrizzz = as.matrix(AE)
traspuesta<-t(matrizzz)
hypervolumenAE<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAE<-hypervolumenAE/hypervolumenReferencia
escribir <- paste(c("AE", hypervolumenRelativoAE), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoEEVALEXPE.csv',ncolumns = 1,append = TRUE, sep ='	')


matrizzz = as.matrix(CPLEX)
traspuesta<-t(matrizzz)
hypervolumenCPLEX<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoCPLEX<-hypervolumenCPLEX/hypervolumenReferencia
escribir <- paste(c("CPLEX", hypervolumenRelativoCPLEX), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoEEVALEXPE.csv',ncolumns = 1,append = TRUE, sep ='	')










AMPL3_paretoAE_I20 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I20.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I20)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I20<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I20<-hypervolumenAMPL3_paretoAE_I20/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I20", hypervolumenRelativoAMPL3_paretoAE_I20), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I19 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I19.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I19)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I19<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I19<-hypervolumenAMPL3_paretoAE_I19/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I19", hypervolumenRelativoAMPL3_paretoAE_I19), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I18 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I18.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I18)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I18<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I18<-hypervolumenAMPL3_paretoAE_I18/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I18", hypervolumenRelativoAMPL3_paretoAE_I18), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I15 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I15.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I15)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I15<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I15<-hypervolumenAMPL3_paretoAE_I15/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I15", hypervolumenRelativoAMPL3_paretoAE_I15), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I14 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I14.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I14)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I14<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I14<-hypervolumenAMPL3_paretoAE_I14/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I14", hypervolumenRelativoAMPL3_paretoAE_I14), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I17 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I17.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I17)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I17<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I17<-hypervolumenAMPL3_paretoAE_I17/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I17", hypervolumenRelativoAMPL3_paretoAE_I17), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I16 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I16.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I16)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I16<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I16<-hypervolumenAMPL3_paretoAE_I16/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I16", hypervolumenRelativoAMPL3_paretoAE_I16), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I11 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I11.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I11)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I11<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I11<-hypervolumenAMPL3_paretoAE_I11/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I11", hypervolumenRelativoAMPL3_paretoAE_I11), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I10 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I10.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I10)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I10<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I10<-hypervolumenAMPL3_paretoAE_I10/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I10", hypervolumenRelativoAMPL3_paretoAE_I10), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I13 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I13.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I13)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I13<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I13<-hypervolumenAMPL3_paretoAE_I13/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I13", hypervolumenRelativoAMPL3_paretoAE_I13), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I12 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I12.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I12)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I12<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I12<-hypervolumenAMPL3_paretoAE_I12/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I12", hypervolumenRelativoAMPL3_paretoAE_I12), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I48 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I48.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I48)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I48<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I48<-hypervolumenAMPL3_paretoAE_I48/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I48", hypervolumenRelativoAMPL3_paretoAE_I48), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I47 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I47.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I47)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I47<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I47<-hypervolumenAMPL3_paretoAE_I47/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I47", hypervolumenRelativoAMPL3_paretoAE_I47), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I49 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I49.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I49)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I49<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I49<-hypervolumenAMPL3_paretoAE_I49/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I49", hypervolumenRelativoAMPL3_paretoAE_I49), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I44 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I44.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I44)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I44<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I44<-hypervolumenAMPL3_paretoAE_I44/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I44", hypervolumenRelativoAMPL3_paretoAE_I44), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I43 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I43.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I43)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I43<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I43<-hypervolumenAMPL3_paretoAE_I43/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I43", hypervolumenRelativoAMPL3_paretoAE_I43), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I46 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I46.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I46)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I46<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I46<-hypervolumenAMPL3_paretoAE_I46/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I46", hypervolumenRelativoAMPL3_paretoAE_I46), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I45 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I45.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I45)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I45<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I45<-hypervolumenAMPL3_paretoAE_I45/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I45", hypervolumenRelativoAMPL3_paretoAE_I45), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I5 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I5.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I5)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I5<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I5<-hypervolumenAMPL3_paretoAE_I5/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I5", hypervolumenRelativoAMPL3_paretoAE_I5), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I40 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I40.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I40)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I40<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I40<-hypervolumenAMPL3_paretoAE_I40/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I40", hypervolumenRelativoAMPL3_paretoAE_I40), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I6 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I6.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I6)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I6<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I6<-hypervolumenAMPL3_paretoAE_I6/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I6", hypervolumenRelativoAMPL3_paretoAE_I6), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I7 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I7.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I7)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I7<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I7<-hypervolumenAMPL3_paretoAE_I7/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I7", hypervolumenRelativoAMPL3_paretoAE_I7), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I42 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I42.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I42)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I42<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I42<-hypervolumenAMPL3_paretoAE_I42/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I42", hypervolumenRelativoAMPL3_paretoAE_I42), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I8 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I8.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I8)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I8<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I8<-hypervolumenAMPL3_paretoAE_I8/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I8", hypervolumenRelativoAMPL3_paretoAE_I8), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I41 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I41.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I41)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I41<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I41<-hypervolumenAMPL3_paretoAE_I41/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I41", hypervolumenRelativoAMPL3_paretoAE_I41), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I1 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I1.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I1)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I1<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I1<-hypervolumenAMPL3_paretoAE_I1/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I1", hypervolumenRelativoAMPL3_paretoAE_I1), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I2 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I2.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I2)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I2<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I2<-hypervolumenAMPL3_paretoAE_I2/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I2", hypervolumenRelativoAMPL3_paretoAE_I2), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I3 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I3.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I3)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I3<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I3<-hypervolumenAMPL3_paretoAE_I3/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I3", hypervolumenRelativoAMPL3_paretoAE_I3), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I4 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I4.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I4)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I4<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I4<-hypervolumenAMPL3_paretoAE_I4/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I4", hypervolumenRelativoAMPL3_paretoAE_I4), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I9 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I9.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I9)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I9<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I9<-hypervolumenAMPL3_paretoAE_I9/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I9", hypervolumenRelativoAMPL3_paretoAE_I9), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I37 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I37.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I37)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I37<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I37<-hypervolumenAMPL3_paretoAE_I37/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I37", hypervolumenRelativoAMPL3_paretoAE_I37), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I36 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I36.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I36)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I36<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I36<-hypervolumenAMPL3_paretoAE_I36/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I36", hypervolumenRelativoAMPL3_paretoAE_I36), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I39 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I39.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I39)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I39<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I39<-hypervolumenAMPL3_paretoAE_I39/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I39", hypervolumenRelativoAMPL3_paretoAE_I39), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I0 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I0.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I0)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I0<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I0<-hypervolumenAMPL3_paretoAE_I0/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I0", hypervolumenRelativoAMPL3_paretoAE_I0), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I38 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I38.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I38)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I38<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I38<-hypervolumenAMPL3_paretoAE_I38/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I38", hypervolumenRelativoAMPL3_paretoAE_I38), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I33 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I33.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I33)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I33<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I33<-hypervolumenAMPL3_paretoAE_I33/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I33", hypervolumenRelativoAMPL3_paretoAE_I33), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I32 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I32.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I32)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I32<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I32<-hypervolumenAMPL3_paretoAE_I32/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I32", hypervolumenRelativoAMPL3_paretoAE_I32), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I35 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I35.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I35)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I35<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I35<-hypervolumenAMPL3_paretoAE_I35/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I35", hypervolumenRelativoAMPL3_paretoAE_I35), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I34 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I34.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I34)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I34<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I34<-hypervolumenAMPL3_paretoAE_I34/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I34", hypervolumenRelativoAMPL3_paretoAE_I34), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I31 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I31.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I31)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I31<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I31<-hypervolumenAMPL3_paretoAE_I31/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I31", hypervolumenRelativoAMPL3_paretoAE_I31), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I30 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I30.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I30)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I30<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I30<-hypervolumenAMPL3_paretoAE_I30/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I30", hypervolumenRelativoAMPL3_paretoAE_I30), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I29 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I29.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I29)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I29<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I29<-hypervolumenAMPL3_paretoAE_I29/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I29", hypervolumenRelativoAMPL3_paretoAE_I29), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I26 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I26.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I26)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I26<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I26<-hypervolumenAMPL3_paretoAE_I26/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I26", hypervolumenRelativoAMPL3_paretoAE_I26), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I25 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I25.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I25)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I25<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I25<-hypervolumenAMPL3_paretoAE_I25/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I25", hypervolumenRelativoAMPL3_paretoAE_I25), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I28 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I28.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I28)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I28<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I28<-hypervolumenAMPL3_paretoAE_I28/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I28", hypervolumenRelativoAMPL3_paretoAE_I28), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I27 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I27.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I27)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I27<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I27<-hypervolumenAMPL3_paretoAE_I27/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I27", hypervolumenRelativoAMPL3_paretoAE_I27), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I22 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I22.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I22)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I22<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I22<-hypervolumenAMPL3_paretoAE_I22/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I22", hypervolumenRelativoAMPL3_paretoAE_I22), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I21 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I21.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I21)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I21<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I21<-hypervolumenAMPL3_paretoAE_I21/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I21", hypervolumenRelativoAMPL3_paretoAE_I21), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I24 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I24.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I24)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I24<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I24<-hypervolumenAMPL3_paretoAE_I24/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I24", hypervolumenRelativoAMPL3_paretoAE_I24), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL3_paretoAE_I23 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL3_paretoAE_I23.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL3_paretoAE_I23)
traspuesta<-t(matrizzz)
hypervolumenAMPL3_paretoAE_I23<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL3_paretoAE_I23<-hypervolumenAMPL3_paretoAE_I23/hypervolumenReferencia
escribir <- paste(c("AMPL3_paretoAE_I23", hypervolumenRelativoAMPL3_paretoAE_I23), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')






AMPL3_hypervolumenRelativoParetitosAE <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_hypervolumenRelativoParetitosAE.csv' , sep='	',header =FALSE)
AMPL3_hypervolumenRelativoParetitosAE_pvalue <- lillie.test(AMPL3_hypervolumenRelativoParetitosAE[,2])$p.value
escribir <- paste(c("AMPL3_hypervolumenRelativoParetitosAE",AMPL3_hypervolumenRelativoParetitosAE_pvalue), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL3_resultadoKS.csv',ncolumns = 1,append = TRUE, sep ='	')


