//package project4;

import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;



import java.util.Random;


public class maze /*implements Iterable <maze.box>*/ {
    Box[] bx;
    int m;		//rows
    int n;		// cols
    int e;		//walls	e= ( m(n-1)+n(m-1)    = 2mn-m-n )
    
    
    public class Box
    {
        int name;
        public List<Wall> adj;
        
        public Box(int number)
        {
            
            name = number;
            adj = new LinkedList<Wall>();
        }
        @Override
        public String toString()
        {
            return Integer.toString(this.name);
        }
        
    }
    
    public class Wall
    {
        int name;
        Box from;
        Box to;
        boolean visited;
        boolean horizontal;
        
        public Wall(Box u, Box v, int n, boolean visit,boolean h) {
            from = u;
            to = v;
            name = n;
            visited= visit;
            horizontal=h;
        }
        
        @Override
        public String toString()
        {
            return Integer.toString(this.name);
        }
        
        @Override
        public boolean equals(Object other)
        {
            if(other == null) {
                return false;
            }
            Wall otherEdge = (Wall) other;
            return this.name == otherEdge.name && this.from.equals(otherEdge.from) && this.to.equals(otherEdge.to);
        }
        
        
        
        public Box otherEnd(Box u)
        {
            assert from.equals(u) || to.equals(u);
            // if the vertex u is the head of the arc, then return the tail else return the head
            if (from.equals(u)) {
                return to;
            } else {
                return from;
            }
        }
        
        
    }
    
    
    
    public maze (int a , int b)
    {
        m = a;
        n = b;
        //System.out.println(m*n);
        bx = new Box[m*n];
        // create an array of BOX objects
        for (int i = 0; i < m*n; i++)
        {
            bx[i] = new Box(i);
            
        }
        
    }
    
    public Box getBox(int n)
    {
        return bx[n-1];
    }
    
    
    
    
    public Wall getWall(int n)
    {
        return wallList.get(n);
    }
    
    ArrayList<Wall> wallList = new ArrayList<>();
    
    
    
    public Wall addWall(Box from, Box to, int name, boolean visited, boolean h)
    {
        Wall e = new Wall(from, to, name,visited,h);
        
        from.adj.add(e);
        to.adj.add(e);
        wallList.add(e);
        
        
        return e;
    }
    
    
    public int generateRandomEdge()
    {
        int size= wallList.size();
        Random r = new Random();
        int Low = 0;
        int High = size-1;
        int Result = r.nextInt(High-Low) + Low;
        //System.out.println("-->" +Result);
        
        
        if(getWall(Result).visited)
        {
            Result= generateRandomEdge();
        }
        
        return Result;
    }
    
    
    
    
    public void generatemaze()
    
    {
        DisjSets ds = new DisjSets(m*n);
        for(int d=0;d < (m*n)-1;d++)
        {
            int selectEdge= generateRandomEdge();
            //		 System.out.println("selectEdge: " + selectEdge);
            getWall(selectEdge).visited= true;
            
            //		 	System.out.println("vertex are");
            //			System.out.println(getWall(selectEdge).from.name);
            //			System.out.println(getWall(selectEdge).to.name);
            
            
            
            int set1, set2;
            
            Box v3=getWall(selectEdge).from;
            Box v4=getWall(selectEdge).to ;
            
            int indexv3=-1;
            int indexv4=-1;
            
            for(int i=0;i<m*n;i++)
            {
                if(bx[i]==v3)
                {
                    indexv3=i;
                    //            		 System.out.println("indexv3");
                    //            		 System.out.println(indexv3);
                }
                if(bx[i]==v4)
                {
                    indexv4=i;
                    //            		 System.out.println("index4");
                    //            		 System.out.println(indexv4);
                }
            }
            //
            set1 = ds.find(indexv3);
            set2 = ds.find(indexv4);
            //         System.out.println("sets...........");
            //    		 System.out.println(set1);
            //    		 System.out.println(set2);
            
            
            if(set1 == set2)
            {
                //            	 System.out.println("SAME SET");
                
                //
                //            // System.out.println(d);
                //		 System.out.println("sets...........");
                //		 System.out.println(set1);
                //		 System.out.println(set2);
                d=d-1;
                getWall(selectEdge).visited= false;
            }
            //
            else
            {
                ds.union( set1, set2 );
                //		 System.out.println("UNION sets..........." + d);
                //		 System.out.println(ds.find(set1));
                //		 System.out.println(ds.find(set2));
            }
            
        }
        
        
        
    }
    
    public static maze readGraph(Scanner in) {
        // read the graph related parameters
        int m = in.nextInt(); // number of vertices in the graph
        int n = in.nextInt(); // number of edges in the graph
        if(m<=1 ||n<=1)
        {
            System.out.println("please enter valid inputs where #row>1 and #columns>1");
            //System.exit(0);
            return null;
        }
        
        //			System.out.println(m);
        //			System.out.println(n);
        // create a maze instance
        // create boxes
        
        maze g = new maze(m,n);
        int l= 0;
        
        //adding edges
        //with dummy boxes
        for(int i= 0; i< m ;i++)
        {
            for(int j=0;j< n;j++)
            {
                int k= (n*i)+j+1;
                if(k%n==0 && i!=m-1)
                {
                    //System.out.println(k + " " + (k+n));
                    g.addWall(g.getBox(k), g.getBox(k+n), ++l, false, true);
                }
                
                if(k%n!=0 && i!=m-1)
                {
                    //System.out.println(k + " " + (k+n));
                    g.addWall(g.getBox(k), g.getBox(k+n), ++l, false, true);
                    //System.out.println(k + " " + (k+1));
                    g.addWall(g.getBox(k), g.getBox(k+1), ++l, false, false);
                    
                }
                
                if(i==m-1 && j!=n-1)
                {
                    //System.out.println(k + " " + (k+1));
                    g.addWall(g.getBox(k), g.getBox(k+1), ++l, false, false);
                    
                }
                
            }
            
        }
        
        return g;
        
    }
    
    public void printmaze()
    {
        
        System.out.println("printing maze");
        int i=1;
        int j=1;
        
        //		 for(int p=0;p<m+1;p++)
        //			 for(int q=0;q<n+1;q++)
        //			 {
        //				 
        //			 }
        for(int fl=0;fl<n;fl++)
        {
            System.out.print(" ");
            System.out.print("__");
            System.out.print("");	 
        }
        System.out.println();
        System.out.print(" ");
        for(Wall w: wallList)
        {
            
            
            if(i==(2*n))
            {	if(j!=m)
            {
                System.out.print("|");
            }
                //System.out.println(i);
                System.out.println();
                System.out.print("|");
                i=1;
                j=j+1;
                //System.out.println(j);
            }
            
            
            if(w.visited && w.horizontal)
            {
                System.out.print("  ");
            }
            
            else if(w.visited && !w.horizontal)
            {
                if(j==m)
                {
                    System.out.print("__");
                    System.out.print(" ");
                    //System.out.print("__");
                    
                }
                else
                {
                    System.out.print(" ");
                }
            }
            
            else if(/*!w.visited &&*/ w.horizontal) 
            {
                
                System.out.print("__");
                
            }
            
            else if(/*!w.visited && */!w.horizontal) 
            {
                
                if(j==m)
                {
                    System.out.print("__");
                    System.out.print("|");
                    
                    
                }
                else
                {
                    System.out.print("|");
                }
                
            }
            
            i=i+1;
        }
        System.out.print("__");
    }
    
    
    
    
    
    
    public static void main(String[] args)
    {
        System.out.println("please enter matrix rows and columns");
        Scanner in;
        in = new Scanner(System.in);
        maze g = maze.readGraph(in);
        if(g==null)
        {
            System.exit(0);
        }
        //System.out.println(g.wallList);
        g.generatemaze();
        g.printmaze();
        
        
        
    }
    
    
}

