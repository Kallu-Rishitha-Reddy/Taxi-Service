public class MinHeap {
	Node heap_array[]= null;
	int heap_capacity;
	int heap_limit;

	//constructor function for the class
	MinHeap(int limit) {
		heap_capacity = 0;
		heap_limit = limit;
		heap_array = new Node[limit];
	}

	//helper function which swaps two nodes
	void swap(Node a, Node b) {
		int dummy = a.indexOfHeap;
		a.indexOfHeap = b.indexOfHeap;
		b.indexOfHeap = dummy;
		heap_array[a.indexOfHeap] = a;
		heap_array[b.indexOfHeap] = b;
	}

	//Checking if the min heap conditions are statisfied or not
	boolean checkHeapCondition(int i, int j) {
		if (((heap_array[i].rideCost == heap_array[j].rideCost) && (heap_array[i].rideDur > heap_array[j].rideDur)) || (heap_array[i].rideCost > heap_array[j].rideCost))
			return true;
		else
			return false;
	}

	//Function performing heapify operation for adjusting the heap
	void heapifyAboveRoot(int index) {
		while (checkHeapCondition(((index - 1) / 2), index) && index != 0) {
			swap(heap_array[((index - 1) / 2)], heap_array[index]);
			index = (index - 1) / 2;
		}
	}

	//Function to insert node into the heap
	boolean insertNode(Node node) {
		if (heap_capacity == heap_limit) {
			return false;
		}
		//Inserting the new node at the end of the heap
		heap_capacity++;
		int index = heap_capacity - 1;
		node.indexOfHeap = index;
		heap_array[index] = node; 
		//Checking if the heapify poperty is changed and performing heapify function
		heapifyAboveRoot(index);
		return true;
	}

	//Function to heapify elements from the node using recursion
	void heapifyFromRoot(Node node) {
		int i = node.indexOfHeap;
		int l = (2 * i + 1);
		int r = (2 * i + 2);
		int smallest = i;
		if (l < heap_capacity && !checkHeapCondition(l, i))
			smallest = l;
		if (r < heap_capacity && !checkHeapCondition(r, smallest))
			smallest = r;
		if (smallest != i) {
			swap(heap_array[i], heap_array[smallest]);
			heapifyFromRoot(heap_array[smallest]);
		}
	}

	//Function to delete ride at given index
	void deleteRide(int index) {
		heap_array[index] = heap_array[heap_capacity - 1];
		heap_array[index].indexOfHeap = index;
		heap_capacity--;
		heapifyFromRoot(heap_array[0]);
	}

	//Function to eliminate the minimum element from the heap which is root node always in a min heap
	int getNextRide() {
		//Checking if there are no elements in the heap
		if (heap_capacity <= 0)
			return -1;
		Node root = heap_array[0];
		if (heap_capacity == 1) {
			heap_capacity--;
			return root.rideNum;
		}
		heap_array[0] = heap_array[heap_capacity - 1];
		heap_array[0].indexOfHeap = 0;
		heap_capacity--;
		heapifyFromRoot(heap_array[0]);
		//Returning the rideNumber of the root
		return root.rideNum;
	}
}
