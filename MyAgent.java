import sun.security.util.Debug;
import za.ac.wits.snake.DevelopmentAgent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Random;


public class MyAgent extends DevelopmentAgent {

    public static void main(String[] args) throws IOException {
        MyAgent agent = new MyAgent();
        MyAgent.start(agent, args);
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String initString = br.readLine();
            String[] temp = initString.split(" ");
            int nSnakes = Integer.parseInt(temp[0]);

            // read in obstacles and do something with them!
            
            //Apple Health
            double appleHeath = 5.0;
            String applePrev = "0 0";

            
            while (true) {
                String line = br.readLine();
                if (line.contains("Game Over")) {
                    break;
                }

                int nObstacles = 3;
                int[][][] obstacles = new int[3][5][2];
                for (int obstacle = 0; obstacle < nObstacles; obstacle++) {
                    String obs = br.readLine();
                    //System.out.println("log "+obs);
                    String[] positions = obs.split(" ");
                    for (int i = 0; i < positions.length; i++) {
                        String[] obsPart = positions[i].split(",");
                        //System.out.println("log " + i);
                        obstacles[obstacle][i][0] = Integer.parseInt(obsPart[1]);
                        obstacles[obstacle][i][1] = Integer.parseInt(obsPart[0]);
                    }
                }
                int[][] obs1 = obstacles[0];
                int[][] obs2 = obstacles[1];
                int[][] obs3 = obstacles[2];

                //System.out.println("log " + obs1[2][0]);

                boolean setObstacles = false;

                //System.out.println("log " + j);

                //get apple info AND APPLE HEALTH
                String apple1 = line;
                if(apple1.equals(applePrev)){
                    appleHeath = appleHeath - 0.1;

                }
                else {
                    applePrev = apple1;
                    appleHeath = 5.0;
                }
                
                String[] appleCoords = apple1.split(" ");
                int appleX = Integer.parseInt(appleCoords[0]);
                int appleY = Integer.parseInt(appleCoords[1]);
                Point applePoint = new Point(appleX, appleY);

                //prepare for Snake initialisation
                Point myHead = new Point();
                Point myTail = new Point();
                Snake mySnake = null;

                //other snakes
                Point snakeHead = new Point();
                Point snakeTail = new Point();
                Snake[] otherSnakes = new Snake[4];

                int[][][] snakeObs = new int[4][100][2];

                int mySnakeNum = Integer.parseInt(br.readLine());
                for (int i = 0; i < nSnakes; i++) {
                    String snakeLine = br.readLine();
                    String[] snakeInfo = snakeLine.split(" ");
                    if (i == mySnakeNum) {
                        //System.out.println("log "+snakeLine);
                        String[] head = snakeInfo[3].split(",");
                        String[] tail = snakeInfo[snakeInfo.length-1].split(",");
                        int myHeadX = Integer.parseInt(head[0]);
                        int myHeadY = Integer.parseInt(head[1]);
                        int myTailX = Integer.parseInt(tail[0]);
                        int myTailY = Integer.parseInt(tail[1]);
                        myHead = new Point(myHeadX, myHeadY);
                        myTail = new Point(myTailX, myTailY);
                        mySnake = new Snake(myHead, myTail, i);
                        otherSnakes[i] = new Snake(myHead, myTail, i);
                        mySnake.setAlive();
                    } else {
                        //Debug.println("hi", snakeLine);
                        if (snakeInfo[0].equals("alive")){
                            String[] head = snakeInfo[3].split(",");
                            String[] tail = snakeInfo[snakeInfo.length-1].split(",");
                            int myHeadX = Integer.parseInt(head[0]);
                            int myHeadY = Integer.parseInt(head[1]);
                            int myTailX = Integer.parseInt(tail[0]);
                            int myTailY = Integer.parseInt(tail[1]);
                            snakeHead = new Point(myHeadX, myHeadY);
                            snakeTail = new Point(myTailX, myTailY);
                            otherSnakes[i] = new Snake(snakeHead, snakeTail, i);
                        }
                    }
                    //Debug.println(""+i, "running for < times");
                    if (snakeInfo[0].equals("alive")){
                        for (int j = 0; j < otherSnakes[i].getLength(); j++){
                            ArrayDeque<Point> snakePoints = otherSnakes[i].getFullBody();
                            for (Point p : snakePoints){
                                snakeObs[i][j][0] = p.y;
                                snakeObs[i][j][1] = p.x;
                            }
                        }
                    }

                    //Debug.println(""+i, ""+otherSnakes[i].getLength());
                }

                int[][] snakeObs1 = snakeObs[0];
                int[][] snakeObs2 = snakeObs[1];
                int[][] snakeObs3 = snakeObs[2];
                int[][] snakeObs4 = snakeObs[3];

                //finished reading, calculate move:
//                int move = new Random().nextInt(4);
//                System.out.println(Direction.RIGHT);



                Node initialNode = new Node(myHead.y, myHead.x);
                Node finalNode = new Node(appleY, appleX);
                int rows = 50;
                int cols = 50;
                AStar aStar = new AStar(rows, cols, initialNode, finalNode);
                //int[][] blocksArray = new int[][]{{1, 3}, {2, 3}, {3, 3}};
                aStar.setBlocks(snakeObs1);
                aStar.setBlocks(snakeObs2);
                aStar.setBlocks(snakeObs3);
                aStar.setBlocks(snakeObs4);
                aStar.setBlocks(obs1);
                aStar.setBlocks(obs2);
                aStar.setBlocks(obs3);
                //System.out.println("log Successfully placed obstacles");

                List<Node> path = aStar.findPath();
                /*for (int i = 1; i < path.size(); i++) {
                    //System.out.println("log Head X: " + myHead.y + " Head Y: " + myHead.x);
                    //System.out.println("log" + path.get(i));
                    if (myHead.x == path.get(i).getCol()) {
                        //System.out.println("log Y to move to: " + node.getRow());
                        //System.out.println("log Current Y: " + myHead.y);
                        if (myHead.y > path.get(i).getRow()) {
                            if (mySnake.getDirection() == Direction.SOUTH) {
                                if (appleX > myHead.x) System.out.println(Direction.EAST);
                                else if (appleX < myHead.x) System.out.println(Direction.WEST);
                            }
                            else { System.out.println(Direction.NORTH); }
                        }
                        else if (myHead.y < path.get(i).getRow()) {
                            if (mySnake.getDirection() == Direction.NORTH) {
                                if (appleX > myHead.x) System.out.println(Direction.EAST);
                                else if (appleX < myHead.x) System.out.println(Direction.WEST);
                            }
                            else { System.out.println(Direction.SOUTH); }
                        }
                    }
                    else if (myHead.y == path.get(i).getRow()) {
                        //System.out.println("log X to move to: " + node.getCol());
                        //System.out.println("log Current X: " + myHead.x);
                        if (myHead.x > path.get(i).getCol()) {
                            if (mySnake.getDirection() == Direction.EAST) {
                                if (appleY > myHead.y) System.out.println(Direction.SOUTH);
                                else if (appleY < myHead.y) System.out.println(Direction.NORTH);
                            }
                            else { System.out.println(Direction.WEST); }
                        }
                        else if (myHead.x < path.get(i).getCol()) {
                            if (mySnake.getDirection() == Direction.WEST) {
                                if (appleY > myHead.y) System.out.println(Direction.SOUTH);
                                else if (appleY < myHead.y) System.out.println(Direction.NORTH);
                            }
                            else { System.out.println(Direction.EAST); }
                        }
                    }

                }*/
                //System.out.println("log Head X: " + myHead.y + " Head Y: " + myHead.x);
                //System.out.println("log" + path.get(1));
                //if (!lifeDescription.equals("alive")) System.out.println("log bruh");
                if(appleHeath>0){
                                    if (myHead.x == path.get(1).getCol()) {
                    //System.out.println("log Y to move to: " + node.getRow());
                    //System.out.println("log Current Y: " + myHead.y);
                    if (myHead.y > path.get(1).getRow()) {
                        if (mySnake.getDirection() == Direction.SOUTH) {
                            if (appleX > myHead.x) System.out.println(Direction.EAST);
                            else if (appleX < myHead.x) System.out.println(Direction.WEST);
                        }
                        else { System.out.println(Direction.NORTH); }
                    }
                    else if (myHead.y < path.get(1).getRow()) {
                        if (mySnake.getDirection() == Direction.NORTH) {
                            if (appleX > myHead.x) System.out.println(Direction.EAST);
                            else if (appleX < myHead.x) System.out.println(Direction.WEST);
                        }
                        else { System.out.println(Direction.SOUTH); }
                    }
                }
                else if (myHead.y == path.get(1).getRow()) {
                    //System.out.println("log X to move to: " + node.getCol());
                    //System.out.println("log Current X: " + myHead.x);
                    if (myHead.x > path.get(1).getCol()) {
                        if (mySnake.getDirection() == Direction.EAST) {
                            if (appleY > myHead.y) System.out.println(Direction.SOUTH);
                            else if (appleY < myHead.y) System.out.println(Direction.NORTH);
                        }
                        else { System.out.println(Direction.WEST); }
                    }
                    else if (myHead.x < path.get(1).getCol()) {
                        if (mySnake.getDirection() == Direction.WEST) {
                            if (appleY > myHead.y) System.out.println(Direction.SOUTH);
                            else if (appleY < myHead.y) System.out.println(Direction.NORTH);
                        }
                        else { System.out.println(Direction.EAST); }
                    }
                }
                }
                

                //System.out.println("log Head X: " + myHead.y + " Head Y: " + myHead.x);
                /*if (appleX > myHead.x){
                    if (mySnake.getDirection() == Direction.WEST){
                        if (appleY > myHead.y) System.out.println(Direction.SOUTH);
                        else if (appleY < myHead.y) System.out.println(Direction.NORTH);
                    }
                    else { System.out.println(Direction.EAST); }
                }
                else if (appleX < myHead.x){
                    if (mySnake.getDirection() == Direction.EAST){
                        if (appleY > myHead.y) System.out.println(Direction.SOUTH);
                        else if (appleY < myHead.y) System.out.println(Direction.NORTH);
                    }
                    else { System.out.println(Direction.WEST); }
                }
                else if (appleY > myHead.y){
                    System.out.println(Direction.SOUTH);
                }
                else if (appleY < myHead.y){
                    System.out.println(Direction.NORTH);
                }
                else if (myHead.x >= 49 && myHead.y <= 0){
                    if (mySnake.getDirection() == Direction.EAST) System.out.println(Direction.SOUTH);
                    else { System.out.println(Direction.WEST); }
                }
                else if (myHead.x >= 49 && myHead.y >= 49){
                    if (mySnake.getDirection() == Direction.SOUTH) System.out.println(Direction.WEST);
                    else { System.out.println(Direction.NORTH); }
                }
                else if (myHead.x <= 0 && myHead.y <= 0){
                    if (mySnake.getDirection() == Direction.WEST) System.out.println(Direction.SOUTH);
                    else { System.out.println(Direction.EAST); }
                }
                else if (myHead.x <= 0 && myHead.y >= 49){
                    if (mySnake.getDirection() == Direction.SOUTH) System.out.println(Direction.EAST);
                    else { System.out.println(Direction.NORTH); }
                }*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getManhattanDistance(Point a, Point b){
        int dx = Math.abs(a.x - b.x);
        int dy = Math.abs(a.y - b.y);
        return dx+dy;
    }

}
