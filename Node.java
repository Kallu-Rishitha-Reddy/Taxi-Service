public class Node {
        int rideNum; //Number of the ride
        int rideCost; //cost of the ride
        int rideDur; //Duration of the ride
        Node left; //left child of the node
        Node right; //right child of the node
        int color; // black is represented by 0 and red is resprented by 1
        Node parent; //parent node of the current node
        int indexOfHeap; //index of the heap
}