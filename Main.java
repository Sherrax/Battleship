package battleship;
import java.util.EnumSet;
import java.util.Scanner;
public class Main {

    // SIZE OF BOARD
    final static int n = 10;
    final static int m = 10;

    // GAME BOARDS FOR 2 PLAYERS - game board and fogOfwar board. Start values of boards are fill by initBoardWithDefaultValues()
    static char[][] boardP1 = initBoardWithDefaultValues();
    static char[][] boardP2 = initBoardWithDefaultValues();
    static char[][] fogOfWarP1 = initBoardWithDefaultValues();
    static char[][] fogOfWarP2 = initBoardWithDefaultValues();
    // TWO  BOARDS WITCH KEEP THE VALUE OF FIELDS
    static int[][] occupationBoardP1 = new int[n][m];
    static int[][] occupationBoardP2 = new int[n][m];

    public static void main(String[] args) {
        /*
        Program structure
        1. Preparation for the game. Introducing ships.
        2. Playing in a loop until all enemy ships have been sunk.
        program structure
        1. Preparation for the game. Introducing ships.
        2. Playing in a loop until all enemy ships have been sunk.
    METHODS;
    + drawBoard - printing the game board.
    + requestCoordinates -for each player and for each ship it will call the method
            * static void shipInput it is used to correctly introduce ships to the game board. Uses methods:
                     ** shipInput - it is used to correctly introduce ships to the game board. Uses methods:
                          *** checkShipLocation - checks if the ship is within the board
                          *** checkShipLength - checks if the ship has the correct length
                          *** checkShipNeighbourhood - judge if the ships are not touching each other
                              ** drawShip - places a ship on the player's board, executes the command
                          *** addOccupation - introduces the ship to the int table in which the individual terms,
                                              the surrounding of ships and free fields are represented by individual values.
                                              The complete occupationBoard has a value of 170.
    + waitForEnter - printing a massage and processing ENTER
    + takeAShot - each player takes a shot, the correctness of the entered value is checked, Effect of the shot.
                    With fogOfwar, the values ​​of X and M are entered on the fog of war plane, miss and hit messages are issued,
                    the value of the individual occupationBards is updated and checked. Loses the player whose first ships are sunk,
                    i.e. the sum of occupationBord elements will be 170.

         */
        System.out.println("\nPlayer 1, place your ships on the game field");
        drawBoard(boardP1);
        requestCoordinates(1);
        waitForEnter("\nPress Enter and pass the move to another player");
        System.out.println("\nPlayer 2, place your ships on the game field");
        drawBoard(boardP2);
        requestCoordinates(2);
        waitForEnter("\nPress Enter and pass the move to another player");
        while (true) {
            takeAShot(occupationBoardP2,1);
            waitForEnter("Press Enter and pass the move to another player");
            takeAShot(occupationBoardP1,2);
            waitForEnter("Press Enter and pass the move to another player");
        }
    }
    // PREPARTION OF THE BOARD
    //this method !!!char boards!!! BOARDS WITH DEFALUT VALUES "~"
    static char[][] initBoardWithDefaultValues() {
        char[][] board = new char[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                board[i][j] = '~';
            }
        }
        return board;
    }
    // OUTPUT STREAM
    // this is is method to output the actual game board
    static void drawBoard(char[][] board) {
        char letters = 'A';
        int digits = 1;
        System.out.println();
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 10; j++) {
                if (i == 0 && j == 0) {
                    System.out.print("  ");
                } else if (j == 0) {
                    System.out.print(letters + " ");
                    letters++;
                } else if (i == 0) {
                    System.out.print(digits + " ");
                    digits++;
                } else {
                    System.out.print(board[i - 1][j - 1] + " ");
                }
            }
            System.out.println();
        }
    }
    // INPUT BLOCK
    // We have boards fill with default values so we have to place the ships in our boards
    static void requestCoordinates(int player) {
        if (player == 1) {
            EnumSet.allOf(ShipType.class).forEach(shipType -> shipInput(boardP1, shipType, occupationBoardP1, player));
        } else if (player == 2) {
            EnumSet.allOf(ShipType.class).forEach(shipType -> shipInput(boardP2, shipType, occupationBoardP2, player));
        }
    }
    static void shipInput(char[][] board, ShipType shipType, int[][] occupationBoard, int player) {
        Scanner s = new Scanner(System.in);
        String c1 = null;
        String c2 = null;
        int power = shipType.getShipPower();
        boolean properInput = false;
        System.out.println(String.format("\nEnter the coordinates of the %s (%d cells):", shipType.getName(), shipType.getShipLength()));
        while (!properInput) {
            System.out.println();
            String coordinates = s.nextLine();
            c1 = coordinates.split(" ")[0];
            c2 = coordinates.split(" ")[1];
            properInput = isInputValid(shipType, c1, c2, player);
        }

        drawShip(board, c1, c2, occupationBoard,power,player);
        drawBoard(board);
    }

    static boolean isInputValid(ShipType shipType, String c1, String c2, int player) {
        return checkShipLocation(c1, c2) && checkShipLength(shipType, c1, c2) && checkShipNeighbourhood(c1, c2,player);
    }

    static boolean checkShipLocation(String c1, String c2) {
        if (c1.charAt(0) == c2.charAt(0) || c1.charAt(1) == c2.charAt(1)) {
            return true;
        } else {
            System.out.println("\nError! Wrong ship location! Try again:");
            return false;
        }
    }

    static boolean checkShipLength(ShipType shipType, String c1, String c2) {
        boolean isValid = false;
        // JEŻELI LEŻĄ W JEDNYM RZĘDZIE
        if (c1.charAt(0) == c2.charAt(0)) {
            int a = Integer.parseInt(c1.substring(1));
            int b = Integer.parseInt(c2.substring(1));
            isValid = a >= b ? a - b + 1 == shipType.getShipLength() : b - a + 1 == shipType.getShipLength();
            // JEŻELI LEŻĄ W JEDNEJ KOLUMNIE
        } else if (c1.substring(1).equals(c2.substring(1))) {
            char a = c1.charAt(0);
            char b = c2.charAt(0);
            isValid = a >= b ? a - b + 1 == shipType.getShipLength() : b - a + 1 == shipType.getShipLength();
        }
        if (!isValid) {
            System.out.println(String.format("\nError! Wrong length of the %s! Try again:", shipType.getName()));
        }
        return isValid;
    }
    static boolean checkShipNeighbourhood(String c1, String c2, int player) {
        boolean isValid = true;
        int minRow = Math.min(c1.charAt(0) - 'A', c2.charAt(0) - 'A');
        int maxRow = Math.max(c1.charAt(0) - 'A', c2.charAt(0) - 'A');
        int minCol = Math.min(Integer.parseInt(c1.substring(1)) - 1, Integer.parseInt(c2.substring(1)) - 1);
        int maxCol = Math.max(Integer.parseInt(c1.substring(1)) - 1, Integer.parseInt(c2.substring(1)) - 1);

        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minCol; j <= maxCol; j++) {
                //System.out.println(String.format("i = %d, j = %d", i, j));

                if (occupationBoardP1[i][j] == 1 && player == 1) {
                    System.out.println("\nError! You placed it too close to another one. Try again:");
                    isValid = false;
                    break;
                } else if ((occupationBoardP2[i][j] == 1 && player == 2)) {
                    System.out.println("\nError! You placed it too close to another one. Try again:");
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }

    static void drawShip(char[][] board, String c1, String c2,int[][] occupationBoard, int power, int player ) {
        if (c1.charAt(0) == c2.charAt(0)) {
            int row = c1.charAt(0) - 'A';
            int a = Integer.parseInt(c1.substring(1)) - 1;
            int b = Integer.parseInt(c2.substring(1)) - 1;
            int max = Math.max(a, b);
            int min = Math.min(a, b);

            for (int i = min; i <= max; i++) {
                board[row][i] = 'O';
                occupationBoard[row][i] = power;

            }
            addOccupation(max, min, row, true, player);

        } else if (c1.substring(1).equals(c2.substring(1))) {
            int col = Integer.parseInt(c1.substring(1)) - 1;
            int a = c1.charAt(0) - 'A';
            int b = c2.charAt(0) - 'A';
            int max = Math.max(a, b);
            int min = Math.min(a, b);
            for (int i = min; i <= max; i++) {
                board[i][col] = 'O';
                occupationBoard[i][col] = power;

            }
            addOccupation(max, min, col, false, player);
        }
    }
    static void addOccupation(int max, int min, int c, boolean isHorizontal, int player) {
        int minCol, maxCol, minRow, maxRow;

        minCol = isHorizontal ? Math.max(min - 1, 0) : Math.max(c - 1, 0);
        maxCol = isHorizontal ? Math.min(max + 1, 9) : Math.min(c + 1, 9);
        minRow = isHorizontal ? Math.max(c - 1, 0) : Math.max(min - 1, 0);
        maxRow = isHorizontal ? Math.min(c + 1, 9) : Math.min(max + 1, 9);
        if (player ==1) {
            for (int i = minRow; i <= maxRow; i++) {
                for (int j = minCol; j <= maxCol; j++) {
                    if (occupationBoardP1[i][j] < 2) {
                        occupationBoardP1[i][j] = 1;
                    }
                }
            }
        } else if (player == 2) {
            for (int i = minRow; i <= maxRow; i++) {
                for (int j = minCol; j <= maxCol; j++) {
                    if (occupationBoardP2[i][j] < 2) {
                        occupationBoardP2[i][j] = 1;
                    }
                }
            }
        }

    }

    public static void waitForEnter(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        String readString = scanner.nextLine();

        while(readString!=null) {
            if (readString.isEmpty()) {
                System.out.print("...");
                break;
            }
            if (scanner.hasNextLine()) {
                readString = scanner.nextLine();
            } else {
                readString = null;
            }
        }
    }

    static void takeAShot(int[][] occupationBoard, int player) {
        Scanner s = new Scanner(System.in);
        boolean miss = true;
        int x = 0;
        int y = 0;
        char place = ' ';
        int value = 10;
        boolean shotIsValid = false;
        if (player == 1) {
            drawBoard(fogOfWarP1);
            System.out.print("---------------------");
            drawBoard(boardP1);
            System.out.print("Player 1, it's your turn:\n");
        } else if (player == 2) {
            drawBoard(fogOfWarP2);
            System.out.print("---------------------");
            drawBoard(boardP2);
            System.out.println("\nPlayer 2, it's your turn:\n");
        }
        while (!shotIsValid) {
            String shot = s.nextLine();
            if (shot.length() > 3 || shot.length() < 2) {
                System.out.println("Error! You entered the wrong coordinates! Try again:\n");
                shotIsValid = false;
            } else if (shot.charAt(0) < 'A' || shot.charAt(0) > 'J') {
                System.out.println("Error! You entered the wrong coordinates! Try again:\n");
                shotIsValid = false;
            } else if (Integer.parseInt(shot.substring(1)) < 1 || Integer.parseInt(shot.substring(1)) > 10) {
                System.out.println("Error! You entered the wrong coordinates! Try again:\n");
                shotIsValid = false;
            } else {
                shotIsValid = true;
                x = Integer.parseInt(shot.substring(1))-1;
                y = shot.charAt(0)- 'A';
                if (occupationBoard[shot.charAt(0)- 'A'][Integer.parseInt(shot.substring(1))-1] >= 2) {
                    if (player == 1) {
                        boardP2[shot.charAt(0)- 'A'][Integer.parseInt(shot.substring(1))-1] = 'X';
                    } else {
                        boardP1[shot.charAt(0)- 'A'][Integer.parseInt(shot.substring(1))-1] = 'X';
                    }
                    place = 'X';
                    miss = false;
                    value = occupationBoard[y][x];
                    occupationBoard[y][x] = 10;
                } else {
                    place = 'M';
                    miss = true;
                }

            }
        }

        if (player == 1) {
            fogOfWar(fogOfWarP1,x,y,place,miss,value,player);
        } if (player == 2) {
            fogOfWar(fogOfWarP2,x,y,place,miss,value,player);
        }
    }
    static int gameControl(int [][] occupationBoard) {
        int cout =0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (occupationBoard[i][j] !=1) {

                    cout += occupationBoard[i][j];
                }
            }
        }
        return cout;
    }
    static void fogOfWar (char [][] fog, int x, int y, char place,boolean miss, int value, int player) {
        fog[y][x] = place;
        boolean sank = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (occupationBoardP2[i][j] == value && player == 1) {
                    sank = false;
                } else if (occupationBoardP1[i][j] == value && player == 2) {
                    sank = false;
                }
            }
        }
        if (miss) {
            System.out.println("\nYou missed!");
        } else if ((gameControl(occupationBoardP1) == 170|| gameControl(occupationBoardP2) == 170)) {
            System.out.println("\nYou sank the last ship. You won. Congratulations!");
            System.exit(0);
        } else if (sank) {
            System.out.println("\nYou sank a ship!");
        } else {
            System.out.println("\nYou hit a ship!");
        }
    }
    //END OF MAIN
}
