import java.util.Random;

public class wumpusGame {

    public static int board_size = 4;
    public static int turns = 1;
    
    private static String board[][] = {{"B"," ","B"," "},
                                       {"P","B","P","B"},
                                       {"B"," ","BSG","P"},
                                       {"X","S","W","SB"},
                                      };
//    private static String board[][] = {{"S"," ","B","P"},
//                                       {"W","SBG","P","B"},
//                                       {"S"," ","B"," "},
//                                       {"X","B","P","B"},
//                                      };
//    private static String board[][] = {{"B","B","BG","P"},
//                                       {"P","P","B","B"},
//                                       {"B","P","BS"," "},
//                                       {"X","BS","W","S"},
//                                      };
//    private static String board[][] = {{"W","GS"," ","B"},
//							           {"S"," ","B","P"},
//								       {" ","B","P","B"},
//								       {"X"," ","B","P"},
//    								  };

    public static String consult(int x, int y)
    {
       return board[y][x];
    }
    
    public static void generate(char code)
    {
        int x,y;
        Random gen = new Random();
        
        do
        {
            x = gen.nextInt(board_size);
            y = gen.nextInt(board_size);
        } while ((((code == 'W') || (code == 'P')) && (((y == board_size-1) && (x == 1)) || ((y == board_size-2) && (x == 0)))) ||
                ((y == board_size-1) && (x == 0)) || 
                (!board[y][x].equals(" ")));
        
        board[y][x] += code;
        
        markNeighbours(x,y,code);
    }
    
    public static void markNeighbours(int x, int y, char code)
    {
        if (code == 'P')
        {
            if (y > 0)
                if (!board[y-1][x].contains("B"))
                    board[y-1][x] += "B";
            if (y < board_size - 1)
                if (!board[y+1][x].contains("B"))
                    board[y+1][x] += "B";
            if (x > 0)
                if (!board[y][x-1].contains("B"))
                    board[y][x-1] += "B";
            if (x < board_size - 1) 
                if (!board[y][x+1].contains("B"))
                    board[y][x+1] += "B";
        }
        else if (code == 'W')
        {
            if (y > 0)
                if (!board[y-1][x].contains("W"))
                    board[y-1][x] += "S";
            if (y < board_size - 1)
                if (!board[y+1][x].contains("W"))
                    board[y+1][x] += "S";
            if (x > 0)
                if (!board[y][x-1].contains("W"))
                    board[y][x-1] += "S";
            if (x < board_size - 1) 
                if (!board[y][x+1].contains("W"))
                    board[y][x+1] += "S";
        }
        else if (code == 'G')
            if (!board[y][x].contains("G"))
                board[y][x] += "G";
    }
    
    public static void printBoard()
    {
        for (int i = 0 ; i < board_size; i++)
        {
            for (int j = 0; j < board_size; j++)
                if (j != 0) 
                    System.out.print("  |  " + board[i][j]);
                else
                    System.out.print(" " + board[i][j] + " ");
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
    
    	int[] begin = {0,3};
        board = new String[board_size][board_size];

        for (int y = 0; y < board_size; y++)
            for (int x = 0; x < board_size; x++)
                board[y][x] = " ";
        
        board[3][0] = "X";
        
        generate('G');
        
        generate('W');
        
        for (int i = 1; i < board_size; i++)
            generate('P');
                
        printBoard();
        
        Agent a = new Agent();
        
//        boolean safe = true;
//   
//        do
//        {
//        	a.searchSafe(safe);
//        	
//        	safe = true;
//        	
//        	if (!a.haveGold())
//        		safe = false;
//        }while (a.keepGoing(safe));
//        
//        if (!a.haveGold())
//			System.out.println("\nO Agente nao conseguiu recuperar o ouro");
//		else
//			a.goTo(begin,true);
        
        a.searchSafe(true);
        
        if (!a.haveGold())
        {
        	a.searchSafe(false);
        	if (!a.haveGold())
        		System.out.println("\nO Agente nao conseguiu recuperar o ouro");
        }
        
        if (a.haveGold())
            a.goTo(begin,true);
    }
}