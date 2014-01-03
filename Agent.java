import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Agent {
    
    private Tile board[][];
    private int x, y;
    private String orientation;
    private boolean arrow, gold;

    public Agent(int x, int y, String orientation, Tile[][] board)
    {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.board = board;
        this.arrow = true;
    }
    
    public Agent() 
    {
        this.board = new Tile[wumpusGame.board_size][wumpusGame.board_size];
        this.y = 3;
        this.x = 0;
        this.arrow = true;
        this.gold = false;
        this.orientation = "Right";
        
        for (int j = 0; j < wumpusGame.board_size; j++)
            for (int i = 0; i < wumpusGame.board_size; i++)
                this.board[j][i] = new Tile();
        
        this.board[this.y][this.x].setVisited(true);
        
        this.board[this.y][this.x].setWumpus(0);
        this.board[this.y][this.x].setPit(0);
        this.board[this.y-1][this.x].setWumpus(0);
        this.board[this.y-1][this.x].setPit(0);
        this.board[this.y][this.x+1].setWumpus(0);
        this.board[this.y][this.x+1].setPit(0);
    }
    
    public boolean haveGold() {
        return gold;
    }
    
    public void printBoard()
    {
//        Stench,Breeze,Glitter,Bump,Scream
        System.out.println("   S B G b s   V P W     S B G b s   V P W     S B G b s   V P W     S B G b s   V P W");
        for (int i = 0 ; i < wumpusGame.board_size; i++)
        {
            for (int j = 0; j < wumpusGame.board_size; j++)
            {
                    System.out.print(" ");
                    if (j != 0)
                        System.out.print("| ");
                    
                    System.out.print("[ ");
                    
                    for (int k = 0; k < 5; k++)
                        if (this.board[i][j].getPercept(k))
                            System.out.print("T ");
                        else
                            System.out.print("F ");
                    
                    System.out.print("] ");
                    
                    if (this.board[i][j].isVisited())
                        System.out.print("T ");
                    else
                        System.out.print("F ");
                                        
                    System.out.print(this.board[i][j].getPit() + " " + this.board[i][j].getWumpus());
            }
            System.out.println();
        }
    }
    
    public void printAction(String action)
    {
        System.out.println();
        System.out.println("----- Turn " + Integer.toString(wumpusGame.turns) + "-----");
        System.out.println("Position: X = " + this.x + " Y = " + this.y);
        System.out.println("Orientation: " + this.orientation);
        System.out.println(action);
        System.out.println("------------");
    }
    
    public void markSafe()
    {
        if (this.y > 0)
        {
            this.board[this.y-1][this.x].setPit(0);
            this.board[this.y-1][this.x].setWumpus(0);
        }
        if (this.y < wumpusGame.board_size - 1)
        {
            this.board[this.y+1][this.x].setPit(0);
            this.board[this.y+1][this.x].setWumpus(0);
        }
        if (this.x > 0)
        {
            this.board[this.y][this.x-1].setPit(0);
            this.board[this.y][this.x-1].setWumpus(0);
        }
        if (this.x < wumpusGame.board_size - 1) 
        {
            this.board[this.y][this.x+1].setPit(0);
            this.board[this.y][this.x+1].setWumpus(0);
        }
    }
    
    public int[] safeNeighbour()
    {
    	int[] coord = new int[2];
    	
    	coord[0] =  this.x;
        coord[1] =  this.y;
    	
        if (this.x < wumpusGame.board_size - 1 && this.board[this.y][this.x+1].isSafe())
        	coord[0]++;
        else if (this.y > 0 && this.board[this.y-1][this.x].isSafe())
            coord[1]--;
        else if (this.y < wumpusGame.board_size - 1 && this.board[this.y+1][this.x].isSafe())
            coord[1]++;
        else if (this.x > 0 && this.board[this.y][this.x-1].isSafe())
            coord[0]--;
        
        return coord;
    }
    
    public int[] unvisitedNeighbour(boolean safe)
    {
    	int[] coord = new int[2];
    	
    	coord[0] =  this.x;
        coord[1] =  this.y;
    	
        if (this.x < wumpusGame.board_size - 1 && !this.board[this.y][this.x+1].isVisited() && (this.board[this.y][this.x+1].isSafe() || !safe))
        	coord[0]++;
        else if (this.y > 0 && !this.board[this.y-1][this.x].isVisited() && (this.board[this.y-1][this.x].isSafe() || !safe))
            coord[1]--;
        else if (this.y < wumpusGame.board_size - 1 && !this.board[this.y+1][this.x].isVisited() && (this.board[this.y+1][this.x].isSafe() || !safe))
            coord[1]++;
        else if (this.x > 0 && !this.board[this.y][this.x-1].isVisited() && (this.board[this.y][this.x-1].isSafe() || !safe))
            coord[0]--;
        else
        {
        	coord[0] = -1;
        	coord[1] = -1;
        }
        
        return coord;
    }
    
    public void markNeighbours(char code)
    {
        if (code == 'B')
        {
            if (!this.board[this.y][this.x].getPercept(1))
                this.board[this.y][this.x].setPercept(1, true);
            if (this.y > 0)
                if (!this.board[this.y-1][this.x].isVisited() && (this.board[this.y-1][this.x].getPit() > 1))
                    this.board[this.y-1][this.x].setPit(2);
            if (this.y < wumpusGame.board_size - 1)
                if (!this.board[this.y+1][this.x].isVisited() && (this.board[this.y+1][this.x].getPit() > 1))
                    this.board[this.y+1][this.x].setPit(2);
            if (this.x > 0)
                if (!this.board[this.y][this.x-1].isVisited() && (this.board[this.y][this.x-1].getPit() > 1))
                    this.board[this.y][this.x-1].setPit(2);
            if (this.x < wumpusGame.board_size - 1) 
                if (!this.board[this.y][this.x+1].isVisited() && (this.board[this.y][this.x+1].getPit() > 1))
                    this.board[this.y][this.x+1].setPit(2);
        }
        else if (code == 'S')
        {
            if (!this.board[this.y][this.x].getPercept(0))
                this.board[this.y][this.x].setPercept(0, true);
            if (this.y > 0)
                if (!this.board[this.y-1][this.x].isVisited() && (this.board[this.y-1][this.x].getWumpus() > 1))
                    this.board[this.y-1][this.x].setWumpus(2);
            if (this.y < wumpusGame.board_size - 1)
                if (!this.board[this.y+1][this.x].isVisited() && (this.board[this.y+1][this.x].getWumpus() > 1))
                    this.board[this.y+1][this.x].setWumpus(2);
            if (this.x > 0)
                if (!this.board[this.y][this.x-1].isVisited() && (this.board[this.y][this.x-1].getWumpus() > 1))
                    this.board[this.y][this.x-1].setWumpus(2);
            if (this.x < wumpusGame.board_size - 1) 
                if (!this.board[this.y][this.x+1].isVisited() && (this.board[this.y][this.x+1].getWumpus() > 1))
                    this.board[this.y][this.x+1].setWumpus(2);
        }
        else if (code == 'G')
        {
            this.board[this.y][this.x].setPercept(2, true);
            this.board[this.y][this.x].setPit(0);
            this.board[this.y][this.x].setWumpus(0);
        }
        else if (code == 'T')
        {
            if (this.y > 0)
                this.board[this.y-1][this.x].setWumpus(0);
            if (this.y < wumpusGame.board_size - 1)
                this.board[this.y+1][this.x].setWumpus(0);
            if (this.x > 0)
                this.board[this.y][this.x-1].setWumpus(0);
            if (this.x < wumpusGame.board_size - 1) 
                this.board[this.y][this.x+1].setWumpus(0);
        }
        else if (code == 'R')
        {
            if (this.y > 0)
                this.board[this.y-1][this.x].setPit(0);
            if (this.y < wumpusGame.board_size - 1)
                this.board[this.y+1][this.x].setPit(0);
            if (this.x > 0)
                this.board[this.y][this.x-1].setPit(0);
            if (this.x < wumpusGame.board_size - 1) 
                this.board[this.y][this.x+1].setPit(0);
        }
    }
    
    public void killedWumpus(boolean safe)
    {
    	boolean flag = false;
    	
    	if (!safe)
    	{
    		int i = 0;
    		String content = "";
    		
    		switch (this.orientation)
            {
                case "Up":
                            i = this.y;
                            while (!flag && (i > 0))
                            {    
                            	content = wumpusGame.consult(this.x, i);
                                if (content.contains("W"))
                                    flag = true;
                                i--;
                            }
                            break;
                case "Down":
                            i = this.y;
                            while (!flag && (i < wumpusGame.board_size))
                            {
                                content = wumpusGame.consult(this.x, i);
                                if (content.contains("W"))
                                    flag = true;
                                i++;
                            }
                            break;
                case "Left":
                            i = this.x;
                            while (!flag && (i > 0))
                            {
                            	content = wumpusGame.consult(i,this.y);
                            	if (content.contains("W"))
                                    flag = true;
                                i--;
                            }
                            break;
                case "Right":
                            i = this.x;
                            while (!flag && (i < wumpusGame.board_size))
                            {
                            	content = wumpusGame.consult(i,this.y);
                            	if (content.contains("W"))
                                    flag = true;
                                i++;
                            }
                            break;
            }
    	}
    	
    	if (safe || (!safe && flag))
    	{
	    	for (int i = 0; i < wumpusGame.board_size; i++)
	            for (int j = 0; j < wumpusGame.board_size; j++)
	            {
	                this.board[i][j].setPercept(4, true);
	                this.board[i][j].setWumpus(0);
	            }
	        this.arrow = false;
    	}
    }
    
    public void updateWumpus()
    {
        int k = 0,a = 0,b = 0;
        
        for (int i = 0; i < wumpusGame.board_size; i++)
            for (int j = 0; j < wumpusGame.board_size; j++)
                if (this.board[i][j].getWumpus() == 2)
                {
                    k++;
                    a = j;
                    b = i;
                }
        
        if (k == 1)
        {
            this.board[b][a].setWumpus(1);
            this.board[b][a].setPit(0);
            
            System.out.println();
            System.out.println(" Updating .....");
            System.out.println();
            printBoard();
        }
    }
        
    public void updatePit()
    {
    	int a = 0, b = 0, count = 0;
    	boolean matrix[][] = new boolean[4][4];
    	
    	for (int i = 0; i < wumpusGame.board_size; i++)
            for (int j = 0; j < wumpusGame.board_size; j++)
            {
            	matrix[b][a] = false;
            	
            	a = i; 
            	b = j;
            	
		    	if (i > 0 && this.board[j][i-1].getPit() > 0)
		    	{
		    		count++;
		    		a--;
		    	}
		    	if (i < wumpusGame.board_size - 1 && this.board[j][i+1].getPit() > 0)
		    	{
		    		count++;
		    		a++;
		    	}
		    	if (j > 0 && this.board[j-1][i].getPit() > 0)
				{
		    		count++;
		    		b--;
				}
		    	if (j < wumpusGame.board_size - 1 && this.board[j+1][i].getPit() > 0)
		    	{
		    		count++;
		    		b++;
		    	}
		    	
		    	if (count == 1)
		    		matrix[b][a] = true;
            }
    	
    	for (int i = 0; i < wumpusGame.board_size; i++)
            for (int j = 0; j < wumpusGame.board_size; j++)
            	if (matrix[b][a])
            		this.board[b][a].setPit(1);
    }
    
    public void rotate(String direction)
    {
        switch (this.orientation)
        {
            case "Up":
                        this.orientation = (direction.equals("Right"))? "Right" : "Left";
                        break;
            case "Down":
                        this.orientation = (direction.equals("Right"))? "Left" : "Right";
                        break;
            case "Left":
                        this.orientation = (direction.equals("Right"))? "Up" : "Down";
                        break;
            case "Right":
                        this.orientation = (direction.equals("Right"))? "Down" : "Up";
                        break;
        }
    }
    
    public boolean move(boolean safe)
    {
        switch (this.orientation)
        {
            case "Up":
                        if (this.y > 0 && (this.board[this.y-1][this.x].isSafe() || (!safe && !this.board[this.y-1][this.x].isSafe())))
                        {
                            this.y--;
                            return true;
                        }
                        else
                            this.board[this.y][this.x].setPercept(3, true);
                        break;
            case "Down":
                        if (this.y < wumpusGame.board_size - 1 && (this.board[this.y+1][this.x].isSafe() || (!safe && !this.board[this.y+1][this.x].isSafe())))
                        {
                            this.y++;
                            return true;
                        }
                        else
                            this.board[this.y][this.x].setPercept(3, true);
                        break; 
            case "Left":
                        if (this.x > 0 && (this.board[this.y][this.x-1].isSafe() || (!safe && !this.board[this.y][this.x-1].isSafe())))
                        {
                            this.x--;
                            return true;
                        }
                        else
                            this.board[this.y][this.x].setPercept(3, true);
                        break;
            case "Right":
                        if (this.x < wumpusGame.board_size - 1 && (this.board[this.y][this.x+1].isSafe() || (!safe && !this.board[this.y][this.x+1].isSafe())))
                        {
                            this.x++;
                            return true;
                        }
                        else
                            this.board[this.y][this.x].setPercept(3, true);
                        break;
        }
        
        return false;
    }
    
    public void grab()
    {
        this.gold = true;
        this.board[this.y][this.x].setPercept(2, false);
    }
    
    public boolean shoot(boolean safe)
    {
        int i;
        boolean flag = false;
        
        if (arrow)
        {
        	switch (this.orientation)
            {
                case "Up":
                            i = this.y;
                            while (!flag && (i >= 0))
                            {    
                                if (this.board[i][this.x].getWumpus() == 1)
                                    flag = true;
                                else if (!safe && this.board[i][this.x].getWumpus() == 2)
                                	flag = true;
                                i--;
                            }
                            break;
                case "Down":
                            i = this.y;
                            while (!flag && (i < wumpusGame.board_size))
                            {
                                if (this.board[i][this.x].getWumpus() == 1)
                                    flag = true;
                                else if (!safe && this.board[i][this.x].getWumpus() == 2)
                                    flag = true;
                                i++;
                            }
                            break;
                case "Left":
                            i = this.x;
                            while (!flag && (i >= 0))
                            {
                                if (this.board[this.y][i].getWumpus() == 1)
                                    flag = true;
                                else if (!safe && this.board[this.y][i].getWumpus() == 2)
                                    flag = true;
                                i--;
                            }
                            break;
                case "Right":
                            i = this.x;
                            while (!flag && (i < wumpusGame.board_size))
                            {
                                if (this.board[this.y][i].getWumpus() == 1)
                                    flag = true;
                                else if (!safe && this.board[this.y][i].getWumpus() == 2)
                                    flag = true;
                                i++;
                            }
                            break;
            }
        }
        return flag;
    }
    
    public void percept()
    {
        if (!this.board[this.y][this.x].isVisited())
            this.board[this.y][this.x].setVisited(true);
        
        if (!this.board[this.y][this.x].isSafe())
        {
            this.board[this.y][this.x].setPit(0);
            this.board[this.y][this.x].setWumpus(0);
        }
        
        String percepts;
        percepts = wumpusGame.consult(this.x, this.y);
        
        if (percepts.length() == 0)
            markSafe();
        else
        {
            int i = percepts.indexOf("B");
            
            if (i != -1)
                markNeighbours(percepts.charAt(i));
            else
                markNeighbours('R');
            
            i = percepts.indexOf("S");
            
            if (i != -1)
                markNeighbours(percepts.charAt(i));
            else
                markNeighbours('T');
            
            i = percepts.indexOf("G");
            
            if (i != -1)
                markNeighbours(percepts.charAt(i));
        }
    }
    
    public boolean takeAction(boolean safe)
    {
        int[] pos = new int[2];
        pos = unvisitedNeighbour(safe);
    
        if (this.board[this.y][this.x].getPercept(2) && !this.gold)
        {
            printAction("Grabbing Gold");
            grab();
        }
        else 
        {
        	String action = canShoot(safe);
        	
        	if (!action.isEmpty())
        	{
	        	if (action.equals("NULL"))
	        		this.shoot(safe);
	        	else
	        	{
        			while (!this.shoot(safe))
					{
						this.rotate(action);
						printAction("Turning " + action);
					}
	        	}
	        	
	        	printAction("Shooting Foward");
	            
	            killedWumpus(safe);
        	}
        	else if (pos[0] != -1 && pos[1] != -1)
        		goTo(pos, safe);
	        else
	        {
	        	pos = safeNeighbour();
	        	goTo(pos, safe);
	        }
        }
        
        if (isDead())
        {
        	printAction("Killed");
        	return false;
        }
        
        return true;
    }
    
	private boolean isDead() {
		String danger = wumpusGame.consult(this.x, this.y);
		
		return ((danger.contains("W") && this.board[this.y][this.x].getWumpus() != 0) || danger.contains("P"));
	}

	private String canShoot(boolean safe) 
	{
		boolean flag = false;
		for (int i = 0; i < wumpusGame.board_size; i++)
			if ((this.board[i][this.x].getWumpus() == 1) || (this.board[this.y][i].getWumpus() == 1))
			{
				flag = true;
				break;
			}
			else if (!safe && ((this.board[i][this.x].getWumpus() == 2) || (this.board[this.y][i].getWumpus() == 2)))
			{
				flag = true;
				break;
			}				
		
		if (flag)
		{
			Agent a = new Agent(this.x,this.y,this.orientation, this.board);
		
	    	Agent b = new Agent(this.x,this.y,this.orientation, this.board);
	    	
	    	int z = 0, k = 0;
	    	
	    	if (a.shoot(safe))
	    		return "NULL";
	    	
	    	while(!a.shoot(safe))
	    	{
	    		a.rotate("Right");
	    		z++;
	    	}
	    	
	    	while(!b.shoot(safe))
	    	{
	    		b.rotate("Left");
	    		k++;
	    	}
	    	
	    	if (z > k)
	    		return "Left";
	    	else
	    		return "Right";
	    }
		
		return "";
	}
    
    public boolean keepGoing(boolean safe)
    {
        boolean a = false,b = false;
        for (int i = 0; i < wumpusGame.board_size; i++)
            for (int j = 0; j < wumpusGame.board_size; j++)
                if (this.board[i][j].getWumpus() == 1)
                    a = true;
                else if (this.board[i][j].isSafe() && !this.board[i][j].isVisited())
                    b = true;
        
        return  !this.gold && (a || b || !safe);
    }
    
    public void searchSafe(boolean flag)
    {
    	boolean alive = true;
    	
        do
        {
        	if (!flag)
        		updatePit();
        	alive = takeAction(flag);
    		if (alive)
			{
    			percept();
	    		printBoard();
	        	if (this.arrow)
	        		updateWumpus();
			}
    		else
    			printBoard();
    		
    		if (flag == false)
    			flag = true;
    		
        }while (alive && keepGoing(flag));
    }
    
    public void generator(Node gen, ArrayList<Node> open, ArrayList<Node> closed, int[] coord, boolean safe)
    {
        Agent a = new Agent(gen.getX(),gen.getY(),gen.getOrientation(), this.board);
        Agent b = new Agent(gen.getX(),gen.getY(),gen.getOrientation(), this.board);
        Agent c = new Agent(gen.getX(),gen.getY(),gen.getOrientation(), this.board);
        
        Node n;
        
        a.rotate("Right");
        n = new Node(a.x,a.y,a.orientation);
        n.setParent(gen);
        n.setH(n.calculaH(coord[0],coord[1]));
        n.setG(gen.getG()+1);
        n.setF(n.getG() + n.getH());
        n.setAction("Turning Right");
        
        if (!open.contains(n))
            open.add(n);
        
        b.rotate("Left");
        n = new Node(b.x,b.y,b.orientation);
        n.setParent(gen);
        n.setH(n.calculaH(coord[0],coord[1]));
        n.setG(gen.getG()+1);
        n.setF(n.getG() + n.getH());
        n.setAction("Turning Left");
        if (!open.contains(n))
            open.add(n);
        
        if (c.move(safe))
        {
            n = new Node(c.x,c.y,c.orientation);
            n.setParent(gen);
            n.setH(n.calculaH(coord[0],coord[1]));
            n.setG(gen.getG()+1);
            n.setF(n.getG() + n.getH());
            n.setAction("Moving Foward");
            if (!open.contains(n))
                open.add(n);
        }
        
        if (!closed.contains(gen))
            closed.add(gen);
    }
    
    public void backtrack(Node n, Node m)
    {
        if (n.getParent() != null)
            backtrack(n.getParent(), n);
        
        this.x = n.getX();
        this.y = n.getY();
        this.orientation = n.getOrientation();
        
        if (m != null)
            printAction(m.getAction());
        else if (this.gold)
            printAction("All Done");
        
        wumpusGame.turns++;
    }
    
    public void goTo(int[] coord, boolean safe)
    {
        ArrayList<Node> open = new ArrayList<>();
        ArrayList<Node> closed = new ArrayList<>();
        Node n = new Node (this.x,this.y,this.orientation);
        
        open.add(n);
        n.setH(n.calculaH(coord[0],coord[1]));
        n.setF(n.getG() + n.getH());
        
        do 
        {
            n = open.get(0);
            open.remove(0);
            generator(n, open, closed, coord, safe);
            Collections.sort(open, new Comparator<Node>() 
            {
                @Override
                public int compare(Node a, Node b) 
                {
                    return a.getF() -  b.getF();
                }
            });   
        }while (n.getH() != 0);
        
        backtrack(n,null);
    }
}