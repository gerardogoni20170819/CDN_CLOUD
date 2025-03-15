package utils.cdn;

public class UserRegion {
	private int id;
	private String name;
	private int globalregionid;
	
	public UserRegion(int id, String name, int globalregoinid) {
		this.setId(id);
		this.setName(name);
		this.setGlobalregionid(globalregoinid);
	}

	public UserRegion(String serializable) {
		String[] data = serializable.split(" ");
		this.setId(Integer.parseInt(data[0]));
		this.setName(data[1]);
		//this.setGlobalregionid(Integer.parseInt(data[2]));
	}
	
	public void debug() {
		System.out.println("");
		System.out.println("Debuging User Region Object");
		System.out.println("id: " + this.getId());
		System.out.println("name: " + this.getName());
		System.out.println("global region id: " + this.getGlobalregionid());
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

	
}
