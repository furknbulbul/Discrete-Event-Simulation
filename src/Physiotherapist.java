
//class for physiotherapist
public class Physiotherapist extends Staff implements Comparable<Physiotherapist>{
	public double serviceSpeed;
	public boolean busy;
	
	public Physiotherapist(int id ,double serviceTime) {
		super(id);
		this.serviceSpeed=serviceTime;
		busy =false;
	}
	public int compareTo(Physiotherapist other) {
		return this.id-other.id;
	}

}
