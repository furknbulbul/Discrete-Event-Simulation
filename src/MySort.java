import java.util.Comparator;

//sort events for time
class EventSortForTime implements Comparator<Event>{
	public int compare(Event event1, Event event2) {
		if(event1.time>event2.time) {
			return 1;
			}
		else if (event1.time<event2.time) {
			return -1;
			
		}
		else {
			return event1.playerId-event2.playerId;
		}
	}
	
}
//sort players in training queue
class PlayerSortForTraining implements Comparator<Player>{
	
	public int compare(Player p1, Player p2) {
		if(p1.enterTrainingQueueTime>p2.enterTrainingQueueTime) {
			return 1;
		}
		if(p1.enterTrainingQueueTime<p2.enterTrainingQueueTime) {
			return -1;
		}
		else {
			return(p1.getId()-p2.getId());
		}
			
		}

}	 

//sort players in massage queue
class PlayerSortForMassage implements Comparator<Player>{
	
	public int compare(Player p1, Player p2) {
		if(p1.getSkillLevel()!=p2.getSkillLevel()) {
			return -p1.getSkillLevel()+p2.getSkillLevel();
		}
		else {
			if(p1.enterMassageQueueTime>p2.enterMassageQueueTime) {
				return 1;
			}
			else if(p1.enterMassageQueueTime<p2.enterMassageQueueTime) {
				return -1;
			}
			else {
				return (p1.getId()-p2.getId());
			}
		}
		
	}
}

///sort players in physiotherapy queue
class PlayerSortForPhysio implements Comparator<Player>{
	public int compare(Player p1, Player p2) {

		if(p1.trainingTime>p2.trainingTime) {
			return -1;
		}
			
		else if (p1.trainingTime<p2.trainingTime) {
			return 1;
			
		}
		else {
			if(p1.enterPhysioQueueTime>p2.enterPhysioQueueTime) {
				return 1;
			}
			else if(p1.enterPhysioQueueTime<p2.enterPhysioQueueTime) {
				return -1;
			}
			else {
				return (p1.getId()-p2.getId());
			}	
		}
	}
}
//to find player with most physio waiting time
class MostPhysioWaiting implements Comparator<Player>{
	public int compare(Player p1,Player p2) {
		if(p1.totalPhysioWaiting>p2.totalPhysioWaiting) {
			return -1;
		}
		else if(p1.totalPhysioWaiting<p2.totalPhysioWaiting){
			return 1;
		}
		else {
			return  p1.getId()-p2.getId();
		}
	}
	
}

//to find least massage waiting time
class LeastMassageWaiting implements Comparator<Player>{
	public int compare(Player p1,Player p2) {
		if(p1.totalMassageWaiting>p2.totalMassageWaiting) {
			return 1;
		}
		else if(p1.totalMassageWaiting<p2.totalMassageWaiting) {
			return -1;
			
		}
		else {
			return p1.getId()-p2.getId();
		}
	}
}
//sort physiotherapists with respect to their ids
class PhysiotherapistSort implements Comparator<Physiotherapist>{
	public int compare(Physiotherapist p1,Physiotherapist p2) {
		return p1.getId()-p2.getId();
	}
}

	












