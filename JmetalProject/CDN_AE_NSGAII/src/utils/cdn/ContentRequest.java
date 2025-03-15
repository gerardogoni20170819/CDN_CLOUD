package utils.cdn;

public class ContentRequest {
	int idrequest;
	private int request_arrival_time;
	private int videocontentid;
	private double transfer_size;
	private int userregionid;

	public ContentRequest(int request_arrival_time, int videocontentid, double transfer_size, int userregionid) {
		this.setRequest_arrival_time(request_arrival_time);
		this.setVideocontentid(videocontentid);
		this.setTransfer_size(transfer_size);
		this.setUserregionid(userregionid);
	}

	public ContentRequest(String serializable) {
		String[] data = serializable.split(" ");
		this.setRequest_arrival_time(Integer.parseInt(data[0]));
		this.setVideocontentid(Integer.parseInt(data[1]));
		this.setTransfer_size(Double.parseDouble(data[2]));
		this.setUserregionid(Integer.parseInt(data[3]));
	}

	public void debug() {
		System.out.println("");
		System.out.println("Debuging Request Object");
		System.out.println("request arrival time in sec: " + this.getRequest_arrival_time());
		System.out.println("video content id: " + this.getVideocontentid());
		System.out.println("transfer size: " + this.getTransfer_size());
		System.out.println("user region id: " + this.getUserregionid());
	}

	public int getRequest_arrival_time() {
		return request_arrival_time;
	}

	public void setRequest_arrival_time(int request_arrival_time) {
		this.request_arrival_time = request_arrival_time;
	}

	public int getVideocontentid() {
		return videocontentid;
	}

	public void setVideocontentid(int videocontentid) {
		this.videocontentid = videocontentid;
	}

	public double getTransfer_size() {
		return transfer_size;
	}

	public void setTransfer_size(double transfer_size) {
		this.transfer_size = transfer_size;
	}

	public int getUserregionid() {
		return userregionid;
	}

	public void setUserregionid(int userregionid) {
		this.userregionid = userregionid;
	}

	public int getIdrequest() {
		return idrequest;
	}

	public void setIdrequest(int idrequest) {
		this.idrequest = idrequest;
	}
}
