import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.PriorityQueue;

//where simulation occurs
public class ExcelFed {
	int canceledAttempts;
	int invalidAttempts;
	double currentTime;
	double totalTrainingTime;
	double totalMassageTime;
	double totalPhysioTime;
	double totalWaitingTimeForTraining;
	double totalWaitingTimeForPhysio;
	double totalWaitingTimeForMassage;
	
	PriorityQueue<Event> timeQueue;//stores events
	PriorityQueue<Player> trainingQueue;//stores players to be trained
	PriorityQueue<Player> physioQueue;//stores players waiting physiotherapy
	PriorityQueue<Player> massageQueue;//stores players waiting massage
	
	ArrayList<DoneTraining> listDoneTrainings;//list of done training events
	ArrayList<DoneMassage> listDoneMassages;//list of done massage events 
	ArrayList<DonePhysiotheraphy> listDonePhysiotheraphies;//list of done physiotherapies
	
	ArrayList<Player> players;//list of players 
	ArrayList<Player> playersWith3Massage;//players completed three massage services
	Hashtable<Integer,Player> playerTable;//hash table of players
	ArrayList<Physiotherapist> physiotherapists;//list of physiotherapists
	
	int noOfCoaches;//suitable coaches number
	int noOfMasseurs;//suitable masseur number
	int maxSizeOfTrainingQueue;
	int maxSizeOfMassageQueue;
	int maxSizeOfPhysioQueue;
	int currentSizeOfTrainingQueue;
	int currentSizeOfMassageQueue;
	int currentSizeOfPhysioQueue;
	
	
	
	public ExcelFed() {
		currentTime=0;canceledAttempts=0;
		timeQueue= new PriorityQueue<Event>(new EventSortForTime());
		trainingQueue= new PriorityQueue<Player>(new PlayerSortForTraining());
		massageQueue= new PriorityQueue<Player>(new PlayerSortForMassage());
		physioQueue= new PriorityQueue<Player>(new PlayerSortForPhysio());
		listDoneTrainings= new ArrayList<DoneTraining>();
		listDoneMassages=new ArrayList<DoneMassage>();
		listDonePhysiotheraphies=new ArrayList<DonePhysiotheraphy>();
		playersWith3Massage= new ArrayList<Player>();
		players=new ArrayList<Player>();
		playerTable=new Hashtable<Integer,Player>();
		physiotherapists= new ArrayList<Physiotherapist>();
		
		maxSizeOfMassageQueue=0;
		maxSizeOfTrainingQueue=0;
		maxSizeOfPhysioQueue=0;
		currentSizeOfTrainingQueue=0;
		currentSizeOfMassageQueue=0;
		currentSizeOfPhysioQueue=0;
		totalTrainingTime=0;
		totalPhysioTime=0;
		totalMassageTime=0;
	}
	
	
	
	
	//dequeues the time queue and puts player into related queue
	public void dequeueTimeQueue() {
		Event event= timeQueue.poll();
		currentTime=event.time;
		Player player=playerTable.get(event.playerId);
		
		if(event instanceof Massage) {
			goingMassageQueue(player,event);			
		}
		
		if(event instanceof Training) {
			goingTrainingQueue(player,event);
		}
		if(event instanceof DoneTraining) {
			trainingDone(player,event);
		}
		if(event instanceof DoneMassage) {
			massageDone(player);
		}
		if(event instanceof DonePhysiotheraphy) {
			physioDone(player);
			player.event=event;
		}
		
	}
	//players arrives for training
	public void arrivalToTraining(Player player, double time, double serviceTime ) {
		
		Training train= new Training(player.getId(),time,serviceTime);
		player.event=train;
		timeQueue.add(train);
		
	}
	
	//players arrives for massage
	public void arrivalToMassage(Player player, double time, double serviceTime) {
		
		Massage massage= new Massage(player.getId(),time,serviceTime);
		player.event=massage;
		timeQueue.add(massage);
	}
	

	//adds player to training queue
	public void goingTrainingQueue(Player player,Event event) {
		//check if it is cancelled
		if(player.busy) {
			canceledAttempts++;
			return;
		}
		player.event=event;
		player.enterTrainingQueueTime=currentTime;	
		player.busy=true;
		trainingQueue.add(player);
		
		
	} 

	//adds player to massage queue
	public void goingMassageQueue(Player player,Event event ) {
		//check invalidity
		if(player.noOfMassage==3) {
			invalidAttempts++;
			return;
		}
		//check if it is cancelled
		if(player.busy) {
			canceledAttempts++;
			return;
		}
		player.noOfMassage++;
		player.enterMassageQueueTime=currentTime;
		player.event=event;
		player.busy=true;
		massageQueue.add(player);

	}
	//adds player physio queue
	public void goingPhysioQueue(Player player ) {
		
		player.enterPhysioQueueTime=currentTime;
		physioQueue.add(player);
	}
	
	// sends player to training service
	public void goingTrainingService(Player player) {
		
		noOfCoaches--;
		DoneTraining doneTraining= new DoneTraining(player.getId(),player.event.duration+currentTime,player.event.duration);
		
		listDoneTrainings.add(doneTraining);
		totalWaitingTimeForTraining+=currentTime-player.enterTrainingQueueTime;
		player.event=doneTraining;
		timeQueue.add(doneTraining);
		
		
	}
	//sends player to massage service
	public void goingMassageService(Player player) {
		
		totalWaitingTimeForMassage+=(currentTime-player.enterMassageQueueTime);
		player.totalMassageWaiting+=(currentTime-player.enterMassageQueueTime);
	
		DoneMassage doneMassage= new DoneMassage(player.getId(),player.event.duration+currentTime,player.event.duration);
		listDoneMassages.add(doneMassage);
		player.event=doneMassage;
		noOfMasseurs--;
		timeQueue.add(doneMassage);

	}
	//sends player to physiotherapy service
	public void goingPhysioService(Player player,Physiotherapist physiotherapist) {
		player.physiotherapist=physiotherapist;
		physiotherapist.busy=true;
		
		DonePhysiotheraphy donePhysiotheraphy= new DonePhysiotheraphy(player.getId(),player.physiotherapist.serviceSpeed+currentTime,player.physiotherapist.serviceSpeed);
		player.event=donePhysiotheraphy;
		listDonePhysiotheraphies.add(donePhysiotheraphy);
		
		totalWaitingTimeForPhysio+=(currentTime-player.enterPhysioQueueTime);
		player.totalPhysioWaiting+=(currentTime-player.enterPhysioQueueTime);
		
		timeQueue.add(donePhysiotheraphy);

	}
	
	
	//player polled from time queue, end its training service
	public void trainingDone(Player player,Event event) {
		currentTime=player.event.time;
		noOfCoaches++;
		
		player.event=event;
		player.trainingTime=event.duration;
		player.enterPhysioQueueTime=currentTime;
		player.event=new Physiotherapy(player.getId());
		goingPhysioQueue(player);//player goes to physiotherapy queue immediately
		
		
	}
	//player polled from time queue, end its massage service
	public void massageDone(Player player) {
		currentTime=player.event.time;
		noOfMasseurs++; 
		player.busy=false;
		totalMassageTime+=player.event.duration;
		player.event=null;
		
	}
	//player polled from time queue, end its physiotherapy service
	public void physioDone(Player player) {
		currentTime=player.event.time;
		player.physiotherapist.busy=false;	
		player.busy=false;
		totalPhysioTime+=player.event.duration;
		player.event=null;	
	}
	//check if a player can leave training queue
	public void checkTrainingQueue() {
		while(!trainingQueue.isEmpty()&&noOfCoaches>0) {
			Player player = trainingQueue.poll();
			goingTrainingService(player);
		}
		if(trainingQueue.size()>maxSizeOfTrainingQueue) {
			maxSizeOfTrainingQueue=trainingQueue.size();
		}
		 
	}
	//check if a player can leave massage queue
	public void checkMassageQueue() {
		while(!massageQueue.isEmpty()&&noOfMasseurs>0) {
			Player player =massageQueue.poll();
			goingMassageService(player);
		}
		if(massageQueue.size()>maxSizeOfMassageQueue) {
			maxSizeOfMassageQueue=massageQueue.size();
		}
	}
	//check if a player can leave physiotherapy queue
	public void checkPhysioQueue() {		
		Collections.sort(physiotherapists, new PhysiotherapistSort());
		for(Physiotherapist phy: physiotherapists) {
			if(physioQueue.isEmpty()) {
				return;
			}
			if(phy.busy==false) {
				goingPhysioService(physioQueue.poll(),phy);
			}
		}
		if (physioQueue.size()>maxSizeOfPhysioQueue) { 
			maxSizeOfPhysioQueue=physioQueue.size();
			
		}

	}
	//calculates average training time
	public double averageTrainingTime() {
		double sum=0;
		for(DoneTraining dT:listDoneTrainings) {
			sum+=dT.duration;
		}
		
		
		return sum/listDoneTrainings.size(); 
	}
	//calculates average physiotherapy time
	public double averagePhysiotherapyTime() {
		double sum=0;
		for(DonePhysiotheraphy dP:listDonePhysiotheraphies) {
			sum+=dP.duration;
		}
				
		return sum/listDonePhysiotheraphies.size(); 
	}
	//calculates average physiotherapy time
	public double averageMassageTime() {
		
		double sum=0;
		for(DoneMassage dM:listDoneMassages) {
			sum+=dM.duration;
		}
		
		return sum/listDoneMassages.size();
	}
	//calculates average waiting time in training queue
	public double averageWaitingTrainingQueue() {
		
		return totalWaitingTimeForTraining/listDoneTrainings.size();
	}

	//calculates average waiting time in physiotherapy queue
	public double averageWaitingPhysioQeueue() {
		
		return totalWaitingTimeForPhysio/listDonePhysiotheraphies.size();
	}

	//calculates average waiting time in massage queue
	public double averageWaitingMassageQeueue() {
		
		return totalWaitingTimeForMassage/listDoneMassages.size();
	}
	//calculates average turnaround time
	public double averageTurnaround() {
		
		return averageTrainingTime()+averageWaitingTrainingQueue()+averagePhysiotherapyTime()+averageWaitingPhysioQeueue();
	}
	//find players who have done massages three times
	public int findPlayersWith3Massage() {
		for(Player player:players) {
			if(player.noOfMassage==3) {
				playersWith3Massage.add(player);
			}
			
		}
		if (playersWith3Massage.isEmpty()) {
			return -1;
		}
		else {
			return 0;
		}
	}
	
	
}
