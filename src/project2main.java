import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class project2main {
	public static void main(String[] args) {
		//reading input
        String fileName = args[0];
        ArrayList<String> inputLines = new ArrayList<String>();
        try (FileReader reader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader((reader))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                inputLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //parse lines       
    	ExcelFed excelFed= new ExcelFed();
        
    	//creating players
    	Iterator<String> iterator= inputLines.iterator(); 
    	
    	int numberOfPlayers=Integer.parseInt(iterator.next());
    	for(int i=0;i<numberOfPlayers;i++) {
        	String[] tokens1= iterator.next().split(" ");
        	int id = Integer.parseInt(tokens1[0]);
        	int skill=Integer.parseInt(tokens1[1]);
        	Player player = new Player(id,skill);
        	excelFed.playerTable.put(id,player);
        	        
        }
        
        //arrivals are read
    	int numberOfArrivals=Integer.parseInt(iterator.next());
        for(int i=0;i<numberOfArrivals;i++) {
        	String[] tokens2=iterator.next().split(" ");
        	String type=tokens2[0];
        	int pId=Integer.parseInt(tokens2[1]);
        	double  arrivalTime=Double.parseDouble(tokens2[2]);
        	double serviceDuration=Double.parseDouble(tokens2[3]);
        	Player player=excelFed.playerTable.get(pId);
        	excelFed.players.add(player);
        	if(type.equals("t")) {
        		excelFed.arrivalToTraining(player,arrivalTime,serviceDuration);
        	}
        	if(type.equals("m")) {
        		excelFed.arrivalToMassage(player,arrivalTime,serviceDuration);
        	}
        }
        
        //physiotherapists are read 
        String[] tokens3= iterator.next().split(" ");
        int numberOfPhysiotherapist=Integer.parseInt(tokens3[0]);
        for(int i =0; i<numberOfPhysiotherapist;i++) {
        	Physiotherapist phy= new Physiotherapist(i,Double.parseDouble(tokens3[i+1]));
        	excelFed.physiotherapists.add(phy);
        }
        //coaches and masseurs are read
        String[] tokens4=iterator.next().split(" ");
        excelFed.noOfCoaches=Integer.parseInt(tokens4[0]);
        excelFed.noOfMasseurs=Integer.parseInt(tokens4[1]);
        
        //simulating the system
        while(!excelFed.timeQueue.isEmpty()) {
        	excelFed.dequeueTimeQueue();
        	excelFed.checkTrainingQueue();
        	excelFed.checkMassageQueue();
        	excelFed.checkPhysioQueue();
        	
        }
        //output file
        Locale.setDefault(new Locale("en","US"));
        try { 
        	File outputFile=new File(args[1]);
        	if(!outputFile.exists()) {
        		outputFile.createNewFile();
        	}
        	PrintStream output = new PrintStream(args[1]);
        	
        	//writing statics
        	//1
        	output.println(excelFed.maxSizeOfTrainingQueue);
        	//2
        	output.println(excelFed.maxSizeOfPhysioQueue); 
            //3
        	output.println(excelFed.maxSizeOfMassageQueue);
            //4            
            output.printf("%.3f",excelFed.averageWaitingTrainingQueue());
            output.println();
            //5
            output.printf("%.3f",excelFed.averageWaitingPhysioQeueue());       
            output.println();
            //6
            output.printf("%.3f",excelFed.averageWaitingMassageQeueue());
            output.println();
            //7	
            output.printf("%.3f",excelFed.averageTrainingTime());
            output.println();
            //8
            output.printf("%.3f",excelFed.averagePhysiotherapyTime());
            output.println();
            //9
            output.printf("%.3f",excelFed.averageMassageTime());
            output.println();
            //10
            output.printf("%.3f",excelFed.averageTurnaround());
            output.println();
            //11
            Collections.sort(excelFed.players,new MostPhysioWaiting());
            output.print(excelFed.players.get(0).getId()+" ");output.printf("%.3f",excelFed.players.get(0).totalPhysioWaiting);output.println();
            //12
            int tmp=excelFed.findPlayersWith3Massage();
            if(tmp==0) {
            	Collections.sort(excelFed.playersWith3Massage, new LeastMassageWaiting());
            	Player playerLeastMassage=excelFed.playersWith3Massage.get(0);
            	output.print(playerLeastMassage.getId()+" "); output.printf("%.3f",playerLeastMassage.totalMassageWaiting);output.println();
            }else if(tmp==-1) {
            	output.println(-1+" "+-1);
            }
            //13
            output.println(excelFed.invalidAttempts);
            //14
            output.println(excelFed.canceledAttempts);
            //15
            output.printf("%.3f",excelFed.currentTime);
            
        	output.close();
            
            
        }
        catch(Exception e) {
            e.getStackTrace();
        }
      
	}
}
