package vegao1.onyxassignment2;

import java.util.ArrayList;

public class Grid {
    private Block[][] blockMap;//map
    private int mapX;//x len
    private int mapY;//y len
    public int score = 0;

    public Grid(int r, int c){
        blockMap = new Block[r][c];
        mapX = blockMap.length;
        mapY = blockMap[0].length;
    }
    /*
    public boolean initialize(){

        for (int i = 0; i <blockMap.length; i++) {
            for (int j = 0; j <blockMap[0].length; j++) {
                Block b = new Block(this, i, j);
                b.has();
                b.random();
                System.out.println("Block Created at x:" + i + " y: " + j + "\tCheck: getx: " + b.getX() + " gety: " + b.getY());
            }
        }
        return true;
    }
    */

    public void updateAsk(){
        boolean eval = false;
        for (int i= 0;i<blockMap.length;i++){
            for (int j=0;j<blockMap[i].length;j++){
                if( blockMap[i][j].ask() ){
                    eval = ( eval || blockMap[i][j].eval() );
                }
            }
        }
        if (eval) {
            updateAsk();
        }
    }

    public int getBlockType(int x, int y){
        if (x>=mapX){
            return -1;
        }
        if (y>=mapY){
            return -1;
        }
        if(x<0){
            return -1;
        }
        if(y<0){
            return -1;
        }
        return blockMap[x][y].getType();
    }

    public int getMapX() {
        return mapX;
    }

    public int getMapY() {
        return mapY;
    }

    public void addBlock(Block b, int x, int y){
        //x=(mapX==x)?x-1:x;
        //y=(mapY==y)?y-1:y;//will over write last block if err...
        blockMap[x][y] =  b;
    }

    public Block getBlock(int x, int y) /*throws Exception*/{
        //if (x<0||y<0||x>=mapX||y>=mapY) {throw new Exception("out of bounds block");}
        return blockMap[x][y];
    }

    public void switchBlock(Block b1, Block b2){
        int tempx = b1.getX(), tempy=b1.getY();
        int temp2x = b2.getX(), temp2y=b2.getY();

        blockMap[tempx][tempy] = b2;
        blockMap[temp2x][temp2y] = b1;
        //
        blockMap[tempx][tempy].setX_cord(tempx);
        blockMap[tempx][tempy].setY_cord(tempy);
        blockMap[temp2x][temp2y].setX_cord(temp2x);
        blockMap[temp2x][temp2y].setY_cord(temp2y);
        //re-evaluate their surroundings
        blockMap[tempx][tempy].has();
        blockMap[temp2x][temp2y].has();
        blockMap[tempx][tempy].ask();
        blockMap[temp2x][temp2y].ask();
    }
    private ArrayList<Block> orderKillList(ArrayList<Block> old){
        int temp = mapY;
        int x = old.get(0).getX();//cuz same for all in coll
        ArrayList<Block> top2bottom = new ArrayList<Block>();
        for (int i=0;i<old.size();i++){
            if (old.get(i).getY()<temp){
                temp = old.get(i).getY();
            }
        }
        for (int i=0;i<old.size();i++){
            try{
                top2bottom.add(this.getBlock(x,temp+i));//check this
            }
            catch (Exception e){
                System.out.println("kill list");
                break;//found end of board dont add more blocks
            }
        }
        return top2bottom;
    }
    public void kill(ArrayList<Block> dead){
        score += dead.size();
        if (dead.get(0).getY()== dead.get(1).getY()){ //horizontal match found
            System.out.println("horizontal match found");
            for (Block block : dead){
                int xVal = block.getX();
                if(block.getY()==0){
                    block.random();
                    block.ask();
                    continue;
                }
                for (int i= block.getY();i>0;i--){
                    switchBlock(blockMap[xVal][i],blockMap[xVal][i-1]);
                    blockMap[xVal][i].ask();
                    blockMap[xVal][i-1].ask();
                }
                block.random();
                block.ask();
            }
        }
        else {// vertical match found
            System.out.println("Vertical match found");
            dead = orderKillList(dead);
            if (0==dead.get(0).getY()){
                for(Block b : dead){
                    b.random();
                    b.ask();
                }
                return;
            }
            for (Block block : dead){
                block.random();
                block.ask();
                int xVal = block.getX();
                for (int i=block.getY();i>0;i--){//switch chain a block to top
                    switchBlock(blockMap[xVal][i],blockMap[xVal][i-1]);
                    blockMap[xVal][i].ask();
                    blockMap[xVal][i-1].ask();
                }
            }
        }
    }
}