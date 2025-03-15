package utils.cdn;

import java.util.ArrayList;

import utils.CdnInstancia;

public class DataCenter {
	private int id;
	private String name;
	private int globalregionid;
	private double storage_base_price; //este precio es por GB
	private double transfer_base_price; //este precio es por GB
	private float on_demand_vm_hourly_price;
	private float reserved_vm_price; //este precio es por ano
	private ArrayList<QoS> qosRegionesUsrs ;


	public DataCenter(int id, String name, int globalregionid, double storage_base_price, double transfer_base_price, float on_demand_vm_hourly_price, float reserved_vm_price) {
		this.setId(id);
		this.setName(name);
		this.setGlobalregionid(globalregionid);
		this.setStorage_base_price(storage_base_price);
		this.setTransfer_base_price(transfer_base_price);
		this.setOn_demand_vm_hourly_price(on_demand_vm_hourly_price);
		this.setReserved_vm_price(reserved_vm_price);
		
	}

	public DataCenter(String serializable) {
		String[] data = serializable.split(" ");
		this.qosRegionesUsrs = new ArrayList<>();
		this.setId(Integer.parseInt(data[0]));
		this.setName(data[1]);
		//this.setGlobalregionid(Integer.parseInt(data[2]));
		this.setStorage_base_price(Double.parseDouble(data[2]));
		this.setTransfer_base_price(Double.parseDouble(data[3]));
		this.setOn_demand_vm_hourly_price(Float.parseFloat(data[4]));
		this.setReserved_vm_price(Float.parseFloat(data[5]));
	}


	public double getNetCost() {
		return (1f/8)*this.getStorage_base_price()
				+ (1f/8)*this.getTransfer_base_price()
				+ (2f/4)*this.getOn_demand_vm_hourly_price()
				+ (1f/4)*this.getReserved_vm_price();
	}

	public double getCostroBruto() {
		return this.getStorage_base_price()
				+ this.getTransfer_base_price()
				+ this.getOn_demand_vm_hourly_price()
				+ this.getReserved_vm_price();
	}

	public double getQoSRegion(int regionUsuario) {
		return this.qosRegionesUsrs.get(regionUsuario).getMetric();
	}

	public void agregarRegionUsuarioQoS(int regionUsuario,QoS qos) {
		qosRegionesUsrs.add(regionUsuario,qos);
	}


	public void debug() {
		System.out.println("");
		System.out.println("Debuging DataCenter Object");
		System.out.println("id: " + this.getId());
		System.out.println("name: " + this.getName());
		System.out.println("region id: " + this.getGlobalregionid());
		System.out.println("storage base price: " + this.getStorage_base_price());
		System.out.println("tranfer base price: " + this.getTransfer_base_price());
		System.out.println("on demand vm hourly price: " + this.getOn_demand_vm_hourly_price());
		System.out.println("reserved vm price: " + this.getReserved_vm_price());
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGlobalregionid() {
		return globalregionid;
	}
	public void setGlobalregionid(int globalregionid) {
		this.globalregionid = globalregionid;
	}
	/**
	 * devuelve el precio prorrateado a bytes
	 * @return precio en bytes
	 */
	public double getStorage_base_price() {
		return storage_base_price/1000/1000/1000;
	}
	public void setStorage_base_price(double storage_base_price) {
		this.storage_base_price = storage_base_price;
	}
	/**
	 * devuelve el precio prorrateado a bytes
	 * @return precio en bytes
	 */
	public double getTransfer_base_price() {
		return transfer_base_price/1000/1000/1000;
	}
	public void setTransfer_base_price(double transfer_base_price) {
		this.transfer_base_price = transfer_base_price;
	}
	public float getOn_demand_vm_hourly_price() {
		return on_demand_vm_hourly_price;
	}
	public void setOn_demand_vm_hourly_price(float on_demand_vm_hourly_price) {
		this.on_demand_vm_hourly_price = on_demand_vm_hourly_price;
	}

	public float getReserved_vm_price() {
		return reserved_vm_price;
	}

	public void setReserved_vm_price(float reserved_vm_price) {
		this.reserved_vm_price = reserved_vm_price;
	}
	
	public float getReserved_vm_priceProrrated(int time_in_secs) {
		return (this.reserved_vm_price/(365f*24*60*60))*time_in_secs;
	}


}
