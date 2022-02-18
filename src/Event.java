
//abstract class for other events
public abstract class Event {
	
	public int playerId;
	public double time;
	public double duration;
	
	public Event( int playerId,double time,double duration) {
		
		this.playerId=playerId;
		this.time=time;
		this.duration=duration;
	}
	public Event(int playerId) {
		this.playerId=playerId;
	}
	

	
}
