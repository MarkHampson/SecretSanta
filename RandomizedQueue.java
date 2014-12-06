//import java.util.UnsupportedOperationException;
import java.util.NoSuchElementException;
import java.util.Iterator;
 
public class RandomizedQueue<Item> implements Iterable<Item>{ 

    private Item[] items;   //The generic array
    private int N;          //The item count

    public RandomizedQueue(){
        items = (Item[]) new Object[2]; //Initialize the array,
                                        //have to cast to generic type
                                        //because of lameness 
        N = 0;                          //Initialize array count
    }

    public boolean isEmpty(){
        return (N == 0);
    }

    public int size(){
        return N;
    }
    
    private void resize(int newSize){
        Item[] newItems = (Item[]) new Object[newSize];
        for(int i=0; i<size(); i++){ //copy from old to new array
            newItems[i] = this.items[i];
        }
        this.items = newItems;  //reassign the array reference
    }

    public void enqueue(Item item){
        if(item == null) throw new NullPointerException("Don't queue a null pointer");
        if(items.length == size()){                          //queue is full, double it
            resize(2*size());
        }
        items[N++] = item;      //stick the new item in the array,
                                //and increment the item count
    }
    
//This cannot be in the final version (extra public method):
//    public String toString(){
//      String s = new String();
//      for(int i=0; i<size(); i++) s+= items[i];
//      return s;
//  }

    //randomly select and remove an item,
    //resize by 1/2 when 1/4 full
    public Item dequeue(){
        if(isEmpty()) throw new NoSuchElementException("Queue underflow");
        int randomIndex;
        randomIndex = StdRandom.uniform(N);        
        //swap the randomly selected index with the last element in the array
        Item temp = items[N-1];
        items[N-1] = items[randomIndex];
        items[randomIndex] = temp; //trading places 
        temp = items[N-1];  //save pointer to the swapped element
        items[N-1] = null;  //no loitering!
        N--;
        if((N>0) && (N <= items.length/4)){
            resize(items.length/2);
        }
        return temp;
    }
    //randomly select and return, but don't remove item
    public Item sample(){
        if(isEmpty()) throw new NoSuchElementException("Queue underflow"); 
        int randomIndex;
        randomIndex = StdRandom.uniform(N);
        return items[randomIndex];
    }
    //implement the iterator() method:
    public Iterator<Item> iterator(){
        return new RandomIterator<Item>(this);
    }
    //implement the Iterator<Item> class:
    private class RandomIterator<Item> implements Iterator<Item>{
        private RandomizedQueue<Item> iterateMe;
        private int[] indexArray;
        private int current;

        public RandomIterator(RandomizedQueue<Item> iterateMe){
            this.iterateMe = iterateMe;  //save the pointer    
            indexArray = new int[iterateMe.size()];
            for(int i=0; i<iterateMe.size(); i++){
                indexArray[i] = i; //initialize by counting
            }
            //now randomize the indexing array
            StdRandom.shuffle(indexArray);                
            current = 0;
        }
        public boolean hasNext(){ 
            return current < iterateMe.size();
        }
        public void remove(){ 
            throw new UnsupportedOperationException(); 
        }
        public Item next(){
            if(!hasNext()) throw new NoSuchElementException();
            int index = indexArray[current]; //get the random index
            Item item = iterateMe.items[index]; //retrieve the item
            current++;
            return item;
        }
    }


    public static void main(String[] args){
        if(args.length < 2) throw new java.lang.IllegalArgumentException("More than one arg pls");        
        RandomizedQueue<String> myQ = new RandomizedQueue();
        System.out.println("Size: " + myQ.size());
        System.out.println("Empty?: " + myQ.isEmpty());
        for(int i=0; i<args.length; i++){
            myQ.enqueue(args[i]);
            for(String s: myQ) System.out.print(s);
            System.out.println("");
        }
        System.out.println("");
        System.out.println("Size: " + myQ.size());
        System.out.println("Empty?: " + myQ.isEmpty());
        int max = myQ.size();
        for(int i = 0; i < max; i++){
            myQ.dequeue();
            for(String s: myQ) System.out.print(s);
            System.out.println("");
        }
        System.out.println("Size: " + myQ.size());
        System.out.println("Empty?: " + myQ.isEmpty());
    }
}
