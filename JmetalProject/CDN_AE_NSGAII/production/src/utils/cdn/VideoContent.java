package utils.cdn;

public class VideoContent {
	private int id;
	private double size;
	private int providerid;
	
	public VideoContent(int id, double size, int providerid) {
		this.setId(id);
		this.setSize(size);
		this.setProviderid(providerid);
	}
	
	public VideoContent(String serializable) {
		String[] data = serializable.split(" ");
		this.setId(Integer.parseInt(data[0]));
		this.setSize(Double.parseDouble(data[1]));
		this.setProviderid(Integer.parseInt(data[2]));
	}
	
	public void debug() {
		System.out.println("");
		System.out.println("Debuging Video Content Object");
		System.out.println("id: " + this.getId());
		System.out.println("size: " + this.getSize());
		System.out.println("provider id: " + this.getProviderid());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public int getProviderid() {
		return providerid;
	}

	public void setProviderid(int providerid) {
		this.providerid = providerid;
	}
	
	
}
