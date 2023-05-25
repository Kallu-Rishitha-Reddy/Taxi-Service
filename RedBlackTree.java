import java.util.ArrayList;

public class RedBlackTree {

  public Node root, dummy;

  //Constructor for initializing a new node with both left and right children as null and node color as black
  public RedBlackTree() {
    dummy = new Node();
    dummy.left = null;
    dummy.right = null;
    dummy.color = 0;
    root = dummy;
  }

  //Function to get dummy leaf nodes
  public Node getLeafNode() {
    return dummy;
  }

  //Function to perform left rotation on the tree
  public void leftRotate(Node leftNode) {
    Node tempNode = leftNode.right;
    leftNode.right = tempNode.left;
    if (tempNode.left != dummy) {
      tempNode.left.parent = leftNode;
    }
    tempNode.parent = leftNode.parent;
    if (leftNode.parent == null) {
      root = tempNode;
    } 
    else if (leftNode == leftNode.parent.left) {
      leftNode.parent.left = tempNode;
    } 
    else {
      leftNode.parent.right = tempNode;
    }
    tempNode.left = leftNode;
    leftNode.parent = tempNode;
  }

  //Funciton to perform right rotation on the tree
  public void rightRotate(Node rightNode) {
    Node tempNode = rightNode.left;
    rightNode.left = tempNode.right;
    if (tempNode.right != dummy) {
      tempNode.right.parent = rightNode;
    }
    tempNode.parent = rightNode.parent;
    if (rightNode.parent == null) {
      root = tempNode;
    }
    else if (rightNode == rightNode.parent.right) {
      rightNode.parent.right = tempNode;
    }
    else {
      rightNode.parent.left = tempNode;
    }
    tempNode.right = rightNode;
    rightNode.parent = tempNode;
  }

  //Helper function to assign node elements to their values
  public Node assignNodeElements(int num, int cost, int dur) {
    Node node = new Node();
    node.rideNum = num;
    node.left = dummy;
    node.right = dummy;
    node.rideCost = cost;
    node.rideDur = dur;
    node.color = 1;
    node.parent = null;
    return node;
  }

  //Function to insert a ride into the tree
  public Node insertRide(int num, int cost, int dur) {
    Node n = assignNodeElements(num, cost, dur), temp1 = root, temp2 = null, balanceTemp1 = n, balanceTemp2;

    for (; temp1 != dummy;) {
      temp2 = temp1;
      temp1 = n.rideNum < temp1.rideNum ? temp1.left : temp1.right;
    }

    n.parent = temp2;
    if (temp2 == null) {
      root = n;
    }
    else if (n.rideNum < temp2.rideNum) {
      temp2.left = n;
    }
    else {
      temp2.right = n;
    }

    //Returning the node if the node's parent is null
    if (n.parent == null) {
      n.color = 0;
      return n;
    }

    //Returning the node if the node's grandparent is null
    if (n.parent.parent == null) {
      return n;
    }

    //Balance the tree after the insert operation is performed
    while (balanceTemp1.parent.color == 1) {
      if (balanceTemp1.parent == balanceTemp1.parent.parent.right) {
        balanceTemp2 = balanceTemp1.parent.parent.left;
        if (balanceTemp2.color == 1) {
          balanceTemp2.color = 0; //Setting the color to black
          balanceTemp1.parent.color = 0; //Setting the color to black
          balanceTemp1.parent.parent.color = 1; //Setting the color to red
          balanceTemp1 = balanceTemp1.parent.parent;
        } 
        else {
          if (balanceTemp1 == balanceTemp1.parent.left) {
            balanceTemp1 = balanceTemp1.parent;
            //Performing right rotation to balance the tree
            rightRotate(balanceTemp1);
          }
          balanceTemp1.parent.color = 0; //Setting the color to black
          balanceTemp1.parent.parent.color = 1; //Setting the color to red
          //Performing left rotation to balance the tree
          leftRotate(balanceTemp1.parent.parent);
        }
      }
      else {
        balanceTemp2 = balanceTemp1.parent.parent.right;

        if (balanceTemp2.color == 1) {
          balanceTemp2.color = 0; //Setting the color to black
          balanceTemp1.parent.color = 0; //Setting the color to black
          balanceTemp1.parent.parent.color = 1; //Setting the color to red
          balanceTemp1 = balanceTemp1.parent.parent;
        } 
        else {
          if (balanceTemp1 == balanceTemp1.parent.right) {
            balanceTemp1 = balanceTemp1.parent;
            leftRotate(balanceTemp1);
          }
          balanceTemp1.parent.color = 0; //Setting the color to black
          balanceTemp1.parent.parent.color = 1; //Setting the color to red
          rightRotate(balanceTemp1.parent.parent);
        }
      }
      if (balanceTemp1 == root) {
        break;
      }
    }
    root.color = 0; //Setting the root color to black
    return n;
  }

  //Function to replaces temp1 node with the temp2 node in the tree
  public void replace(Node temp1, Node temp2) {
    if (temp1.parent == null) {
      root = temp2;
      temp2.parent = null;
    } 
    else if (temp1 == temp1.parent.left) {
      temp1.parent.left = temp2;
    }
    else {
      temp1.parent.right = temp2;
    }
    temp2.parent = temp1.parent;
  }

  //Function to delete the ride
  public void deleteRide(int taxiNum) {
    Node tempNode = root, taxiTemp3 = dummy, taxiTemp1, taxiTemp2;
    while (tempNode != dummy) {
      if (tempNode.rideNum == taxiNum)
        taxiTemp3 = tempNode;
      tempNode = (tempNode.rideNum <= taxiNum) ? tempNode.right : tempNode.left;
    }

    if (taxiTemp3 == dummy) {
      return;
    }

    taxiTemp2 = taxiTemp3;
    int taxiTemp2Color = taxiTemp2.color;
    if (taxiTemp3.left == dummy) {
      taxiTemp1 = taxiTemp3.right;
      //after delete operation, replacing the element with the minimum element in the right subtree
      replace(taxiTemp3, taxiTemp3.right);
    } 
    else if (taxiTemp3.right == dummy) {
      taxiTemp1 = taxiTemp3.left;
      //after delete operation, replacing the element with the minimum element in the right subtree
      replace(taxiTemp3, taxiTemp3.left);
    } 
    else {
      Node minnode = taxiTemp3.right;
      while (minnode.left != dummy) {
        minnode = minnode.left;
      }
      taxiTemp2 = minnode;
      taxiTemp2Color = taxiTemp2.color;
      taxiTemp1 = taxiTemp2.right;
      if (taxiTemp2.parent == taxiTemp3) {
        taxiTemp1.parent = taxiTemp2;
      } 
      else {
        //after delete operation, replacing the element with the minimum element in the right subtree
        replace(taxiTemp2, taxiTemp2.right);
        taxiTemp2.right = taxiTemp3.right;
        taxiTemp2.right.parent = taxiTemp2;
      }
      //after delete operation, replacing the element with the minimum element in the right subtree
      replace(taxiTemp3, taxiTemp2);
      taxiTemp2.left = taxiTemp3.left;
      taxiTemp2.left.parent = taxiTemp2;
      taxiTemp2.color = taxiTemp3.color;
    }
    // delete taxiTemp3;
    if (taxiTemp2Color == 0) {
      // For balancing the tree after deletion
      Node balanceTemp;
      while (taxiTemp1 != root && taxiTemp1.color == 0) {
        if (taxiTemp1 == taxiTemp1.parent.left) {
          balanceTemp = taxiTemp1.parent.right;
          if (balanceTemp.color == 1) {
            balanceTemp.color = 0; 
            taxiTemp1.parent.color = 1;
            leftRotate(taxiTemp1.parent); 
            balanceTemp = taxiTemp1.parent.right;
          }

          if (balanceTemp.left.color == 0 && balanceTemp.right.color == 0) {
            balanceTemp.color = 1;
            taxiTemp1 = taxiTemp1.parent;
          } 
          else {
            if (balanceTemp.right.color == 0) {
              balanceTemp.left.color = 0;
              balanceTemp.color = 1;
              rightRotate(balanceTemp);
              balanceTemp = taxiTemp1.parent.right;
            }

            balanceTemp.color = taxiTemp1.parent.color;
            taxiTemp1.parent.color = 0;
            balanceTemp.right.color = 0;
            leftRotate(taxiTemp1.parent);
            taxiTemp1 = root;
          }
        } else {
          balanceTemp = taxiTemp1.parent.left;
          if (balanceTemp.color == 1) {
            balanceTemp.color = 0;
            taxiTemp1.parent.color = 1;
            rightRotate(taxiTemp1.parent);
            balanceTemp = taxiTemp1.parent.left;
          }

          if (balanceTemp.right.color == 0 && balanceTemp.right.color == 0) {
            balanceTemp.color = 1;
            taxiTemp1 = taxiTemp1.parent;
          } 
          else {
            if (balanceTemp.left.color == 0) {
              balanceTemp.right.color = 0;
              balanceTemp.color = 1;
              leftRotate(balanceTemp);
              balanceTemp = taxiTemp1.parent.left;
            }
            balanceTemp.color = taxiTemp1.parent.color;
            taxiTemp1.parent.color = 0;
            balanceTemp.left.color = 0;
            rightRotate(taxiTemp1.parent);
            taxiTemp1 = root;
          }
        }
      }
      taxiTemp1.color = 0;
    }
  }

  public Node lookIntoRide(Node taxiNode, int taxiNum) {
    if (taxiNum == taxiNode.rideNum || taxiNode == dummy) {
      return taxiNode;
    }
    return (taxiNum < taxiNode.rideNum) ? lookIntoRide(taxiNode.left, taxiNum) : lookIntoRide(taxiNode.right, taxiNum);
  }

  //Function to search for ride using taxiNum
  public Node lookForRide(int taxiNum) {
    return lookIntoRide(this.root, taxiNum);
  }

  public ArrayList<String> lookIntoRide(Node taxiNode, int ride1, int ride2) {
    if (taxiNode == dummy)
      return new ArrayList<>();

    ArrayList<String> rideList = new ArrayList<>();
    if (taxiNode.rideNum >= ride1)
      rideList.addAll(lookIntoRide(taxiNode.left, ride1, ride2));
    if ((ride1 <= taxiNode.rideNum) && (ride2 >= taxiNode.rideNum))
      rideList.add("(" + Integer.toString(taxiNode.rideNum) + "," + Integer.toString(taxiNode.rideCost) + "," + Integer.toString(taxiNode.rideDur)+ ")");
    if (taxiNode.rideNum <= ride2)
      rideList.addAll(lookIntoRide(taxiNode.right, ride1, ride2));

    return rideList;
  }

  //Function to search for ride using ride1 and ride2
  public ArrayList<String> lookForRide(int ride1, int ride2) {
    return lookIntoRide(this.root, ride1, ride2);
  }
}
