import java.util.ArrayList; //Import ArrayList class
import java.io.BufferedReader;
import java.io.IOException; // Import the IOException class to handle errors
//Import classes for file reading and writing
import java.io.FileReader;
import java.io.FileWriter;

public class gatorTaxi {
    //main function to perform the given operations in the input file
    public static void main(String[] args) {
        int MAX_ACTIVE_RIDES = 2000; //Assuming the number of active rides will not exceed 2000
        RedBlackTree taxiRBT = new RedBlackTree(); //Declaring a RedBlackTree object
        MinHeap taxiHeap = new MinHeap(MAX_ACTIVE_RIDES); //Declaring a MinHeap object with max size
        String inputFile = args[0]; //Taking the input file name from command line argument

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) 
        {
            String line = reader.readLine();
            //creating a new file for storing the output
            FileWriter outputFile = new FileWriter("output_file.txt");
            
            while (line != null) {
                String[] inputOperation = line.split("\\("); //Getting the operation from each line
                String action = inputOperation[0].trim().toLowerCase(); //Splitting the first segment as action
                String[] triplet = inputOperation[1].trim().replaceAll("\\)", "").split(","); //Splitting the second segment as triplet

                switch (action) {
                    //Insert operation
                    case "insert": 
                    {
                        int rideNumber = Integer.parseInt(triplet[0].trim());
                        int rideCost = Integer.parseInt(triplet[1].trim());
                        int tripDuration = Integer.parseInt(triplet[2].trim());
                        Node taxi = taxiRBT.lookForRide(rideNumber);
                        if (taxi == taxiRBT.getLeafNode()) {
                            Node newTaxi = taxiRBT.insertRide(rideNumber, rideCost, tripDuration);
                            taxiHeap.insertNode(newTaxi);
                        } 
                        else {
                            //In case of same ride number being repeated, write into output file and force close it
                            outputFile.write("Duplicate RideNumber");
                            outputFile.close();
                            System.exit(0);
                        }
                        break;
                    }
                    //GetNextRide operation
                    case "getnextride": 
                    {
                        int nextRide = taxiHeap.getNextRide();
                        if (nextRide == -1) {
                            //In case there is no active ride requests
                            outputFile.write("No active ride requests");
                        } 
                        else {
                            //Search for the next ride and write it onto output and delete the ride
                            Node taxi = taxiRBT.lookForRide(nextRide);
                            outputFile.write("(" + taxi.rideNum + "," + taxi.rideCost + "," + taxi.rideDur + ")");
                            taxiRBT.deleteRide(nextRide);
                        }
                        break;
                    }
                    //Print operation
                    case "print": 
                    {
                        int rideNumber = Integer.parseInt(triplet[0].trim());
                        //Printing the triplet of the given ride number
                        if (triplet.length == 1) {
                            Node taxi = taxiRBT.lookForRide(rideNumber);
                            if (taxi == taxiRBT.getLeafNode()) {
                                //In case the dummy nodes are reached
                                outputFile.write("(0,0,0)");
                            } 
                            else {
                                outputFile.write("(" + taxi.rideNum + "," + taxi.rideCost + "," + taxi.rideDur + ")");
                            }

                        }
                        //Printing the triplets in the range of the given ride numbers
                        else if (triplet.length == 2) {
                            int ride1 = Integer.parseInt(triplet[0].trim());
                            int ride2 = Integer.parseInt(triplet[1].trim());
                            //ArrayList of String for looking into range from ride1 to ride2
                            ArrayList<String> rides = taxiRBT.lookForRide(ride1, ride2);
                            int len = rides.size(); //Getting the total number of rides which are to be printed
                            if (len != 0) {
                                for (int i = 0; i < len - 1; i++) {
                                    outputFile.write(rides.get(i) + ",");
                                }
                                outputFile.write(rides.get(len - 1));
                            } 
                            else {
                                //In case the dummy nodes are reached
                                outputFile.write("(0,0,0)");
                            }
                        }
                        break;
                    }
                    //Update operation
                    case "updatetrip": 
                    {
                        int rideNumber = Integer.parseInt(triplet[0].trim());
                        int rideID = rideNumber;
                        int tripDuration = Integer.parseInt(triplet[1].trim());
                        int newTripDur = tripDuration;
                        //Search for the ride
                        Node taxi = taxiRBT.lookForRide(rideNumber);
                        int newCost = taxi.rideCost + 10;
                        int oldtripDur = taxi.rideDur;
                        //Break if the ride number does not exist
                        if(taxi == taxiRBT.getLeafNode())
                            break;
                        //Checking if newTripDuration is less than or equals exisitingTripDuration
                        if (newTripDur <= oldtripDur) {
                            taxi.rideDur = newTripDur;
                            taxiHeap.heapifyAboveRoot(taxi.indexOfHeap);
                        }
                        else {
                            taxiRBT.deleteRide(rideID);
                            taxiHeap.deleteRide(taxi.indexOfHeap);
                            if (newTripDur <= 2 * oldtripDur) {
                                Node newNode = taxiRBT.insertRide(rideID, newCost, newTripDur);
                                taxiHeap.insertNode(newNode);
                            }
                        }
                        break;
                    }
                    //Cancel Operation
                    case "cancelride":
                    {
                        int rideNumber = Integer.parseInt(triplet[0].trim());
                        //Search for the given ride number and delete it
                        Node taxi = taxiRBT.lookForRide(rideNumber);
                        if (taxi != taxiRBT.getLeafNode()) {
                            taxiHeap.deleteRide(taxi.indexOfHeap);
                            taxiRBT.deleteRide(rideNumber);
                        }
                        break;
                    }
                    default:
                        break;
                }
                line = reader.readLine();
                //Writing a new line character onto the output file if the next line in the input file is not empty and the current action is either Print or GetNextRide
                if (line != null && (action.equals("print") || action.equals("getnextride")))
                    outputFile.write("\n");
            }
            //Closing the output file
            outputFile.close();
        } 
        catch (IOException e) {
            //Exception statement
            System.out.println("The input file cannot be read due to the error: " + e.getMessage());
        }
    }
}