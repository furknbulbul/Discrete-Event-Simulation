
//class for player
public class Player  {
	double enterTrainingQueueTime; double enterPhysioQueueTime; double enterMassageQueueTime;double trainingTime;double physioTime;double massageTime;
	double leaveTrainingQueueTime; double leavePhysioQueueTime; double leaveMassageQueueTime;double totalMassageWaiting; double totalPhysioWaiting;
	
	private int skillLevel; 
	private int id; 
	
	boolean busy;//if player is in process
	int noOfMassage;//number of massage player did
	Physiotherapist physiotherapist;
	Event event ;
	
	 
	public Player(int id, int skillLevel) {
		this.id=id;
		this.skillLevel=skillLevel;
		noOfMassage=0;
		busy=false;
		totalMassageWaiting=0;
		totalPhysioWaiting=0;
	
	}
	public int getSkillLevel() {
		return skillLevel;
	}

	public int getId() {
		return id;
	}
	
	
		
}
