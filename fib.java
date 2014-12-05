import java.util.*;
import java.io.*;

 

/* Fibonacci Heap Node **/

class FibonacciHeapNode

{
    FibonacciHeapNode child, left, right, p;    
    int key;
    int degree=0;
    Boolean mark=false;  
	
 
/** Constructor **/

    public FibonacciHeapNode(int key)
    {
        this.right = this;
        this.left = this;
        this.key = key;
	this.p=null;
	this.child=null;

    } 
 
}

class FibonacciHeap
{
	private int n,degree; 
	private FibonacciHeapNode root;
	private FibonacciHeap H;
	private FibonacciHeapNode min;

	 /** Constructor **/

	public FibonacciHeap()
	{
		root = null;
		n = 0;
	}
	public int size()
	{
		return n;
	} 

    /** Check if heap is empty **/

	public boolean isEmpty()
	{
		return H.min == null;
	}

 
 /** Make heap empty **/ 

	public void clear()
	{
		H.min = null;
		n = 0;
		degree=0;
	}

 // create heap
	public void create()
	{
		H=new FibonacciHeap();
		H.n=0;
		H.min=null;
	}
//insertion
	public void insert(int k) throws IllegalArgumentException	
	{
		try
		{
			FibonacciHeapNode x=new FibonacciHeapNode(k);
			x.p=null;
			x.child=null;
			x.mark=false;
			x.degree=0;
			if(H.min==null)
			{
  				H.min=x;
			}
			else
			{
				if(x.key<H.min.key)
				{
					H.min.left.right=x; 
					x.left=H.min.left;
					x.right=H.min;
					H.min.left=x;
					H.min=x;
				}
				else
				{
					x.left=H.min;
					x.right=H.min.right;
					H.min.right.left=x;
					H.min.right=x;
				}
			}
			H.n=H.n+1;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	//extract_min

	public void extract_min()
	{
		FibonacciHeapNode y,z;
		y=H.min;
		if(y!=null)
		{
			z=y.child;
			while(z!=null)
			{
				H.min.right=z.child;
				z=z.right;
				z.p=null;
			}
 	 		H.min.left.right = H.min.right;
        		H.min.right.left =H.min.left;
			if(y==y.right)
				H.min=null;
			else
				H.min=y.right;
		}
		consolidate();
		H.n=H.n-1;
	}

// consolidate
	public void consolidate()
	{
		int arraySize =H.n ;
		System.out.println("size="+arraySize);
		FibonacciHeapNode A[] = new FibonacciHeapNode[arraySize];
		for ( int i = 0; i < arraySize; i++)
		{
			A[i]=null;
		}
		List<FibonacciHeapNode> rootNodes = new LinkedList<FibonacciHeapNode>();
		FibonacciHeapNode x=H.min;
		for ( FibonacciHeapNode n =x; n!=null; n = n.right )
		{
			rootNodes.add( n );
			int d = degree;
			while ( A[d]!=null )
			{
				FibonacciHeapNode y = A[d];
		        	if ( x.key> y.key  )
		        	{
					FibonacciHeapNode tmp = x;
					x = y;
					y = tmp;
		        	}
		        	FibonacciHeapNode node=link(y,x);
		        	A[d]= null;
		        	d++;
			}
			A[d]= x;
		}
		H.min = null;
		for ( int i = 0; i < arraySize; i++)
		{
			if(A[i]!=null)
			{
				if(min==null)
				{
					min=A[i];
				}
				else
				{
					if(A[i].key<min.key)
						min=A[i];
				}
			}
		}
		
	}

	//link

	protected FibonacciHeapNode link( FibonacciHeapNode y, FibonacciHeapNode z )
	{
		// remove y from root list
		y.left.right = y.right;
        	y.right.left = y.left;
        	// make y a child of min
        	if ( z.child == null ) // no previous children?
        	{
			y.right = y;
			y.left = y;
		}
        	else
        	{
            		y.left = z.child.left;
            		y.right = z.child;
            		y.right.left = y;
            		y.left.right = y;
        	}
        	z.child = y;
        	y.p = z;
        	z.degree++;
        	y.mark = false;
        	return z;
    	}

	//decrease a key

 	public void decreaseKey(FibonacciHeapNode x,int k)
	{   
		if(k>x.key)
		{
			System.out.println("new key is greater than current key");
		}
		else
		{
			x.key=k;
			FibonacciHeapNode y=x.p;
			if (y!=null&&(x.key<y.key))
			{
				cut(x,y);
				cascading_cut(y);
			}
			if(x.key<H.min.key)
				H.min=x;
		}
	}
	public void cut(FibonacciHeapNode x,FibonacciHeapNode y)
	{
		y.left.right =y.right;
        	y.right.left =y.left;
        	if (y.right.equals(y) )
        	{
			x.child = null;
			y.degree--;
        	}
		y.right=H.min;
		y.left=H.min.left;
		H.min.left=y;
		x.p=null;
		x.mark=false;
	}
	public void cascading_cut(FibonacciHeapNode y)
	{
		FibonacciHeapNode z=y.p;
		if (z!=null)
		{
			if (y.mark==false)
				y.mark=true;
			else 
			{
				cut(y,z);
				cascading_cut(z);
			}

		}
	}

	public FibonacciHeapNode search(int key)
	{
     		FibonacciHeapNode ptr = H.min;
		ptr = ptr.right;
		while (ptr != H.min && ptr.right != null)
		{
			if(ptr.key==key)
            		{
                		return(ptr);
            		}
		}
        	return ptr;
    	}

 	public void display()
	{
		System.out.print("\nHeap = ");
		FibonacciHeapNode ptr = H.min;
		if (ptr == null)
		{
			System.out.print("Empty\n");
			return;
		}        
		do
		{
			System.out.print(ptr.key +" ");
			ptr = ptr.right;
		} 
		while (ptr != H.min && ptr.right != null);
			System.out.println();
	} 

}    





class fib
{
	public static void main(String[] args)
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("FibonacciHeap Test\n\n");        
		FibonacciHeap fh = new FibonacciHeap();
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		char ch;

        /**  Perform FibonacciHeap operations  **/

        	do    
		{
			System.out.println("1.Create FibonacciHeap \n");
			System.out.println("2. insert element ");
			System.out.println("3. check empty");            
			System.out.println("4. clear");
            		System.out.println("5. Deletion");
            		System.out.println("6.Extract min element");
            		System.out.println("7.Decreasing a key");
 			int choice = scan.nextInt();            
			switch (choice)
			{
				case 1:
					fh.create(); 
                			break; 
				case 2 : 
					System.out.println("Enter element");
					fh.insert( scan.nextInt() );                                    
					break;                          
				case 3 : 
					System.out.println("Empty status = "+ fh.isEmpty());
					break;   
				case 4 : 
					fh.clear();
					break;  
				case 5:
                    			try
                    			{
                        			System.out.println("enter the key to be deleted");
                        			int newKey=Integer.parseInt(br.readLine());
                        			FibonacciHeapNode h=fh.search(newKey);
                        			fh.decreaseKey(h,-10);
                        			fh.extract_min();
					}
                    			catch(Exception e)
                    			{
						System.out.println("IOException"+e);
						e.printStackTrace();
					}   
                   			break;  
                		case 6:      
                   			fh.extract_min();
                    			break;
                 		case 7:
                    			try
                    			{
						System.out.println("enter the key to be decreased");
						int key=Integer.parseInt(br.readLine());
                        			FibonacciHeapNode h=fh.search(key);
                        			System.out.println("enter the new key value");
                        			int newKey=Integer.parseInt(br.readLine());
                        			fh.decreaseKey( h, newKey );
                       			}
                    			catch(Exception e)
                    			{
                        			System.out.println("IOException"+e);
                        			e.printStackTrace();
                    			}   
                    			break; 
  				default : 
					System.out.println("Wrong Entry \n ");
			}           
			fh.display();
		} while (true);  

    	}
}


