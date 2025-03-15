package utils.cdn;

public class GlobalRegion {
	private int id;
	private String name;
	
	public GlobalRegion(int id, String name) {
		this.setId(id);
		this.setName(name);
	}
	
	public GlobalRegion(String serializable) {
		String[] data = serializable.split(" ");
		this.setId(Integer.parseInt(data[0]));
		this.setName(data[1]);
	}
	
	public void debug() {
		System.out.println("");
		System.out.println("Debuging Global Region Object");
		System.out.println("id: " + this.getId());
		System.out.println("name: " + this.getName());
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

	
}
