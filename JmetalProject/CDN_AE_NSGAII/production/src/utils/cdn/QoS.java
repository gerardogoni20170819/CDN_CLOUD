package utils.cdn;

import java.util.ArrayList;

public class QoS {
	private int userregionid;
	private int datacenterid;
	private double metric;
	private double inferior;
	private double superior;
	//error que se comete al comparar dos calidades de servicio
	private double errorEnComparacion;
	ArrayList<double[]> largosEinterseccionesComparacion;

	public QoS() {
	}
	public QoS(int userregionid, int datacenterid, double metric) {
		this.setUserregionid(userregionid);
		this.setDatacenterid(datacenterid);
		this.setMetric(metric);
		this.errorEnComparacion=0;
		this.largosEinterseccionesComparacion = new ArrayList<>();
	}
	
	public QoS(String serializable) {
		this.largosEinterseccionesComparacion = new ArrayList<>();
		String[] data = serializable.split(" ");
		this.setUserregionid(Integer.parseInt(data[0]));
		this.setDatacenterid(Integer.parseInt(data[1]));
		this.setMetric(Double.parseDouble(data[2]));
		if(data.length>3){
			/* quiere decir que estoy parseando el archivo de qos con intervalo de confianza
			    el formato que se guarda en el archivo es:
			    userregionid  datacenterid   median    lower    upper
			 */
			this.setInferior(Double.parseDouble(data[3]));
			this.setSuperior(Double.parseDouble(data[4]));
		}
		this.errorEnComparacion=0;
	}
	
	public void debug() {
		System.out.println("---");
		System.out.println("Debuging QoS Object");
		System.out.println("user region id: " + this.getUserregionid());
		System.out.println("data center id: " + this.getDatacenterid());
		System.out.println("metric: " + this.getMetric());


		for(double [] pareja : largosEinterseccionesComparacion){
			System.out.println("pareja[0]: " + pareja[0]);
			System.out.println("pareja[1]: " + pareja[1]);
			System.out.println("pareja[2]: " + pareja[2]);

		}
		System.out.println("-------");


	}

	public int getUserregionid() {
		return userregionid;
	}

	public void setUserregionid(int userregionid) {
		this.userregionid = userregionid;
	}

	public int getDatacenterid() {
		return datacenterid;
	}

	public void setDatacenterid(int datacenterid) {
		this.datacenterid = datacenterid;
	}

	public double getMetric() {
		return metric;
	}

	public void setMetric(double metric) {
		this.metric = metric;
	}

	public double getInferior() {
		return inferior;
	}

	public void setInferior(double inferior) {
		this.inferior = inferior;
	}

	public double getSuperior() {
		return superior;
	}

	public void setSuperior(double superior) {
		this.superior = superior;
	}

	/**
	 * compara dos QoS
	 * @return retorna la mejor QoS junto con el error de comparacion
	 */
	public QoS mejorQoS(QoS qos2){

		QoS qos1=this;

		QoS mayor;
		QoS menor;
		QoS mejor;

		//la mediana decide siempre
		if(qos1.getMetric()<qos2.getMetric()){
			mejor=qos1.copy();
		}else{
			mejor=qos2.copy();
		}


		//calculo el error

		//ordeno los intervalos segun el que tenga mayor limite superior
		if(qos1.getSuperior() >= qos2.getSuperior()){
			mayor=qos1;
			menor=qos2;
		}else{
			mayor=qos2;
			menor=qos1;
		}

		//calculo el mayor limite inferior
		double maximoDeLosMinimos =0;
		if(mayor.getInferior()>menor.getInferior()){
			maximoDeLosMinimos=mayor.getInferior();
		}else{
			maximoDeLosMinimos=menor.getInferior();
		}

		//el error es el solapamiento por ahora
		//// TODO: 03/04/2017 determinar formula para error de qos
		mejor.setErrorEnComparacion(menor.getSuperior()-maximoDeLosMinimos);

		//el menor no se solapa con el mayor
		if(mejor.getErrorEnComparacion()<0){
			mejor.setErrorEnComparacion(0);
		}

		return mejor;
	}

	/**
	 * Compara 2 QoS y calcula el error de comparacion
	 * @param qos2
	 * @return 1 si this > qos2, -1 si this < qos2 y 0 si this == qos2
	 */
	public int comparar(QoS qos2){
		int result =0;

		QoS qos1=this;

		QoS mayor;
		QoS menor;
		QoS mejor;

		//la mediana decide siempre



		if(qos1.getMetric()>qos2.getMetric()){
			result = 1;
		}else{
			if(qos1.getMetric()<qos2.getMetric()){
				result = -1;
			}else {
				result = 0;
			}
		}


		//calculo el error

		//ordeno los intervalos segun el que tenga mayor limite superior
		if(qos1.getSuperior() >= qos2.getSuperior()){
			mayor=qos1;
			menor=qos2;
		}else{
			mayor=qos2;
			menor=qos1;
		}

		//calculo el mayor limite inferior
		double maximoDeLosMinimos =0;
		if(mayor.getInferior()>menor.getInferior()){
			maximoDeLosMinimos=mayor.getInferior();
		}else{
			maximoDeLosMinimos=menor.getInferior();
		}


		double tamanioInterseccion = menor.getSuperior()-maximoDeLosMinimos;
		//el menor no se solapa con el mayor
		if(tamanioInterseccion<0){
			tamanioInterseccion=0;
		}
		double largo_intervalo_qos1 =qos1.getSuperior()-qos1.getInferior();
		double largo_intervalo_qos2 =qos2.getSuperior()-qos2.getInferior();


		// hay que setearle el error a los 2
		double [] parejaErrorPara_qos1 = new double [3];
		double [] parejaErrorPara_qos2 = new double [3];

		parejaErrorPara_qos1[0]=largo_intervalo_qos1;
		parejaErrorPara_qos1[1]=largo_intervalo_qos2;
		parejaErrorPara_qos1[2]=tamanioInterseccion;

		parejaErrorPara_qos2[0]=largo_intervalo_qos2;
		parejaErrorPara_qos2[1]=largo_intervalo_qos1;
		parejaErrorPara_qos2[2]=tamanioInterseccion;

		this.largosEinterseccionesComparacion.add(parejaErrorPara_qos1);
		qos2.agregarLargoEinterseccionComparacion(parejaErrorPara_qos2);


		return result;
	}
	public double getErrorEnComparacion() {
		return errorEnComparacion;
	}

	public void setErrorEnComparacion(double errorEnComparacion) {
		this.errorEnComparacion = errorEnComparacion;
	}

	public QoS copy(){
		QoS qosnuevo = new QoS();
		qosnuevo.userregionid=this.userregionid;
		qosnuevo.datacenterid=this.datacenterid;
		qosnuevo.metric=this.metric;
		qosnuevo.inferior=this.inferior;
		qosnuevo.superior=this.superior;
		qosnuevo.errorEnComparacion=this.errorEnComparacion;
		return qosnuevo;
	}

	public void agregarLargoEinterseccionComparacion(double [] largoIntervalo_InterseccionIntervalos){
		this.largosEinterseccionesComparacion.add(largoIntervalo_InterseccionIntervalos);
	}

	public ArrayList<double[]> getLargosEinterseccionesComparacion() {
		return largosEinterseccionesComparacion;
	}

	public void setLargosEinterseccionesComparacion(ArrayList<double[]> largosEinterseccionesComparacion) {
		this.largosEinterseccionesComparacion = largosEinterseccionesComparacion;
	}
}
