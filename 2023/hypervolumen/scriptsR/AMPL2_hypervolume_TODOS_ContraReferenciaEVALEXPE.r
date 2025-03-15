#correr este script para distintas instancias y acumula los valores en los archivos correspondientes en la carpteta /home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/
library(emoa)
library(nortest)
GreedyCosto <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/AMPL2_pareto_greedy_costo.pr' , sep='	',header = FALSE)
GreedyQoS <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/AMPL2_pareto_greedy_Qos.pr' , sep='	',header = FALSE)
RoundRobin <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/AMPL2_pareto_RR.pr' , sep='	',header = FALSE)
AE <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/AMPL2_paretoSoloAE.pr' , sep='	',header = FALSE)
CPLEX <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/AMPL2_pareto_CPLEX.pr' , sep='	',header = FALSE)
ParetoGeneral <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/AMPL2_paretoGeneralTodasLasTecnicasMasAE.pr' , sep='	',header = FALSE)


matrizzz = as.matrix(ParetoGeneral)
traspuesta<-t(matrizzz)
maximaX<-81155.06794919996
maximaY<-3.5574809751802827
hypervolumenReferencia<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))


matrizzz = as.matrix(GreedyCosto)
traspuesta<-t(matrizzz)
hypervolumenGreedyCosto<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoGreedyCosto<-hypervolumenGreedyCosto/hypervolumenReferencia
escribir <- paste(c("GreedyCosto", hypervolumenRelativoGreedyCosto), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoEEVALEXPE.csv',ncolumns = 1,append = FALSE, sep ='	')


matrizzz = as.matrix(GreedyQoS)
traspuesta<-t(matrizzz)
hypervolumenGreedyQoS<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoGreedyQoS<-hypervolumenGreedyQoS/hypervolumenReferencia
escribir <- paste(c("GreedyQoS", hypervolumenRelativoGreedyQoS), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoEEVALEXPE.csv',ncolumns = 1,append = TRUE, sep ='	')


matrizzz = as.matrix(RoundRobin)
traspuesta<-t(matrizzz)
hypervolumenRoundRobin<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoRoundRobin<-hypervolumenRoundRobin/hypervolumenReferencia
escribir <- paste(c("RoundRobin", hypervolumenRelativoRoundRobin), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoEEVALEXPE.csv',ncolumns = 1,append = TRUE, sep ='	')


matrizzz = as.matrix(AE)
traspuesta<-t(matrizzz)
hypervolumenAE<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAE<-hypervolumenAE/hypervolumenReferencia
escribir <- paste(c("AE", hypervolumenRelativoAE), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoEEVALEXPE.csv',ncolumns = 1,append = TRUE, sep ='	')


matrizzz = as.matrix(CPLEX)
traspuesta<-t(matrizzz)
hypervolumenCPLEX<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoCPLEX<-hypervolumenCPLEX/hypervolumenReferencia
escribir <- paste(c("CPLEX", hypervolumenRelativoCPLEX), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoEEVALEXPE.csv',ncolumns = 1,append = TRUE, sep ='	')










AMPL2_paretoAE_I41 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I41.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I41)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I41<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I41<-hypervolumenAMPL2_paretoAE_I41/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I41", hypervolumenRelativoAMPL2_paretoAE_I41), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I40 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I40.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I40)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I40<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I40<-hypervolumenAMPL2_paretoAE_I40/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I40", hypervolumenRelativoAMPL2_paretoAE_I40), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I39 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I39.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I39)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I39<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I39<-hypervolumenAMPL2_paretoAE_I39/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I39", hypervolumenRelativoAMPL2_paretoAE_I39), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I38 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I38.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I38)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I38<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I38<-hypervolumenAMPL2_paretoAE_I38/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I38", hypervolumenRelativoAMPL2_paretoAE_I38), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I37 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I37.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I37)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I37<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I37<-hypervolumenAMPL2_paretoAE_I37/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I37", hypervolumenRelativoAMPL2_paretoAE_I37), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I36 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I36.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I36)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I36<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I36<-hypervolumenAMPL2_paretoAE_I36/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I36", hypervolumenRelativoAMPL2_paretoAE_I36), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I35 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I35.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I35)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I35<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I35<-hypervolumenAMPL2_paretoAE_I35/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I35", hypervolumenRelativoAMPL2_paretoAE_I35), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I34 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I34.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I34)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I34<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I34<-hypervolumenAMPL2_paretoAE_I34/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I34", hypervolumenRelativoAMPL2_paretoAE_I34), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I33 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I33.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I33)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I33<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I33<-hypervolumenAMPL2_paretoAE_I33/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I33", hypervolumenRelativoAMPL2_paretoAE_I33), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I32 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I32.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I32)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I32<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I32<-hypervolumenAMPL2_paretoAE_I32/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I32", hypervolumenRelativoAMPL2_paretoAE_I32), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I31 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I31.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I31)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I31<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I31<-hypervolumenAMPL2_paretoAE_I31/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I31", hypervolumenRelativoAMPL2_paretoAE_I31), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I49 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I49.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I49)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I49<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I49<-hypervolumenAMPL2_paretoAE_I49/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I49", hypervolumenRelativoAMPL2_paretoAE_I49), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I48 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I48.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I48)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I48<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I48<-hypervolumenAMPL2_paretoAE_I48/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I48", hypervolumenRelativoAMPL2_paretoAE_I48), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I47 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I47.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I47)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I47<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I47<-hypervolumenAMPL2_paretoAE_I47/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I47", hypervolumenRelativoAMPL2_paretoAE_I47), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I46 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I46.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I46)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I46<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I46<-hypervolumenAMPL2_paretoAE_I46/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I46", hypervolumenRelativoAMPL2_paretoAE_I46), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I45 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I45.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I45)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I45<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I45<-hypervolumenAMPL2_paretoAE_I45/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I45", hypervolumenRelativoAMPL2_paretoAE_I45), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I44 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I44.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I44)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I44<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I44<-hypervolumenAMPL2_paretoAE_I44/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I44", hypervolumenRelativoAMPL2_paretoAE_I44), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I43 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I43.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I43)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I43<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I43<-hypervolumenAMPL2_paretoAE_I43/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I43", hypervolumenRelativoAMPL2_paretoAE_I43), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I42 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I42.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I42)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I42<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I42<-hypervolumenAMPL2_paretoAE_I42/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I42", hypervolumenRelativoAMPL2_paretoAE_I42), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I3 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I3.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I3)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I3<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I3<-hypervolumenAMPL2_paretoAE_I3/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I3", hypervolumenRelativoAMPL2_paretoAE_I3), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I2 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I2.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I2)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I2<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I2<-hypervolumenAMPL2_paretoAE_I2/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I2", hypervolumenRelativoAMPL2_paretoAE_I2), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I5 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I5.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I5)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I5<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I5<-hypervolumenAMPL2_paretoAE_I5/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I5", hypervolumenRelativoAMPL2_paretoAE_I5), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I4 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I4.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I4)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I4<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I4<-hypervolumenAMPL2_paretoAE_I4/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I4", hypervolumenRelativoAMPL2_paretoAE_I4), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I7 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I7.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I7)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I7<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I7<-hypervolumenAMPL2_paretoAE_I7/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I7", hypervolumenRelativoAMPL2_paretoAE_I7), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I6 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I6.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I6)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I6<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I6<-hypervolumenAMPL2_paretoAE_I6/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I6", hypervolumenRelativoAMPL2_paretoAE_I6), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I9 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I9.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I9)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I9<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I9<-hypervolumenAMPL2_paretoAE_I9/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I9", hypervolumenRelativoAMPL2_paretoAE_I9), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I8 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I8.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I8)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I8<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I8<-hypervolumenAMPL2_paretoAE_I8/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I8", hypervolumenRelativoAMPL2_paretoAE_I8), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I19 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I19.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I19)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I19<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I19<-hypervolumenAMPL2_paretoAE_I19/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I19", hypervolumenRelativoAMPL2_paretoAE_I19), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I18 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I18.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I18)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I18<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I18<-hypervolumenAMPL2_paretoAE_I18/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I18", hypervolumenRelativoAMPL2_paretoAE_I18), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I17 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I17.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I17)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I17<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I17<-hypervolumenAMPL2_paretoAE_I17/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I17", hypervolumenRelativoAMPL2_paretoAE_I17), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I16 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I16.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I16)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I16<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I16<-hypervolumenAMPL2_paretoAE_I16/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I16", hypervolumenRelativoAMPL2_paretoAE_I16), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I15 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I15.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I15)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I15<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I15<-hypervolumenAMPL2_paretoAE_I15/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I15", hypervolumenRelativoAMPL2_paretoAE_I15), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I14 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I14.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I14)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I14<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I14<-hypervolumenAMPL2_paretoAE_I14/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I14", hypervolumenRelativoAMPL2_paretoAE_I14), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I13 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I13.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I13)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I13<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I13<-hypervolumenAMPL2_paretoAE_I13/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I13", hypervolumenRelativoAMPL2_paretoAE_I13), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I12 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I12.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I12)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I12<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I12<-hypervolumenAMPL2_paretoAE_I12/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I12", hypervolumenRelativoAMPL2_paretoAE_I12), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I11 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I11.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I11)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I11<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I11<-hypervolumenAMPL2_paretoAE_I11/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I11", hypervolumenRelativoAMPL2_paretoAE_I11), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I1 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I1.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I1)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I1<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I1<-hypervolumenAMPL2_paretoAE_I1/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I1", hypervolumenRelativoAMPL2_paretoAE_I1), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I10 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I10.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I10)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I10<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I10<-hypervolumenAMPL2_paretoAE_I10/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I10", hypervolumenRelativoAMPL2_paretoAE_I10), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I0 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I0.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I0)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I0<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I0<-hypervolumenAMPL2_paretoAE_I0/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I0", hypervolumenRelativoAMPL2_paretoAE_I0), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I30 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I30.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I30)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I30<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I30<-hypervolumenAMPL2_paretoAE_I30/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I30", hypervolumenRelativoAMPL2_paretoAE_I30), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I29 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I29.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I29)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I29<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I29<-hypervolumenAMPL2_paretoAE_I29/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I29", hypervolumenRelativoAMPL2_paretoAE_I29), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I28 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I28.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I28)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I28<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I28<-hypervolumenAMPL2_paretoAE_I28/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I28", hypervolumenRelativoAMPL2_paretoAE_I28), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I27 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I27.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I27)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I27<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I27<-hypervolumenAMPL2_paretoAE_I27/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I27", hypervolumenRelativoAMPL2_paretoAE_I27), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I26 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I26.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I26)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I26<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I26<-hypervolumenAMPL2_paretoAE_I26/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I26", hypervolumenRelativoAMPL2_paretoAE_I26), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I25 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I25.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I25)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I25<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I25<-hypervolumenAMPL2_paretoAE_I25/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I25", hypervolumenRelativoAMPL2_paretoAE_I25), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I24 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I24.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I24)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I24<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I24<-hypervolumenAMPL2_paretoAE_I24/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I24", hypervolumenRelativoAMPL2_paretoAE_I24), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I23 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I23.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I23)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I23<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I23<-hypervolumenAMPL2_paretoAE_I23/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I23", hypervolumenRelativoAMPL2_paretoAE_I23), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I22 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I22.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I22)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I22<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I22<-hypervolumenAMPL2_paretoAE_I22/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I22", hypervolumenRelativoAMPL2_paretoAE_I22), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I21 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I21.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I21)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I21<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I21<-hypervolumenAMPL2_paretoAE_I21/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I21", hypervolumenRelativoAMPL2_paretoAE_I21), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')


AMPL2_paretoAE_I20 <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/paretos_por_instancia_por_iteracion_AE/AMPL2_paretoAE_I20.pr' , sep='	',header =FALSE)
matrizzz = as.matrix(AMPL2_paretoAE_I20)
traspuesta<-t(matrizzz)
hypervolumenAMPL2_paretoAE_I20<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))
hypervolumenRelativoAMPL2_paretoAE_I20<-hypervolumenAMPL2_paretoAE_I20/hypervolumenReferencia
escribir <- paste(c("AMPL2_paretoAE_I20", hypervolumenRelativoAMPL2_paretoAE_I20), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv',ncolumns = 1,append = TRUE, sep ='	')






AMPL2_hypervolumenRelativoParetitosAE <- read.csv('/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_hypervolumenRelativoParetitosAE.csv' , sep='	',header =FALSE)
AMPL2_hypervolumenRelativoParetitosAE_pvalue <- lillie.test(AMPL2_hypervolumenRelativoParetitosAE[,2])$p.value
escribir <- paste(c("AMPL2_hypervolumenRelativoParetitosAE",AMPL2_hypervolumenRelativoParetitosAE_pvalue), collapse = '	')
write(escribir, file = '/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/2023/hypervolumen/AMPL2_resultadoKS.csv',ncolumns = 1,append = TRUE, sep ='	')


