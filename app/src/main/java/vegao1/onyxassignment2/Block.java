package vegao1.onyxassignment2;

import java.util.ArrayList;
import java.util.Random;
import java.util.ArrayList;
import java.util.Random;
/**
 * Created by vegao1 on 5/4/2017.
 */

public class Block {
    private int type;
    private boolean top;
    private boolean bot;
    private boolean right;
    private boolean left;

    private boolean hasTop;
    private boolean hasBot;
    private boolean hasRight;
    private boolean hasLeft;

    private int x_cord=-1;
    private int y_cord=-1;

    public Grid grid;
    public Block(Grid map) {
        grid = map;
    }//constructor
    //ask if on edge
    public void has(){
        hasLeft=(x_cord>0);
        hasRight=(x_cord<grid.getMapX()-1);
        hasTop=(y_cord>0);
        hasBot=(y_cord<grid.getMapY()-1);
    }
    //ask if on particular side
    public boolean askLeft(){
        has();
        if(hasLeft){
            left = grid.getBlockType(x_cord - 1, y_cord) == this.type;
            return left;
        }
        else{
            return false;
        }
    }
    public boolean askRight(){
        has();
        if(hasRight){
            right = grid.getBlockType(x_cord + 1, y_cord) == this.type;
            return right;
        }
        else{
            return false;
        }
    }
    public boolean askTop(){
        has();
        if(hasTop){
            top = grid.getBlockType(x_cord , y_cord-1) == this.type;
            return top;
        }
        else{
            return false;
        }
    }
    public boolean askBot(){
        has();
        if(hasBot){
            bot = grid.getBlockType(x_cord , y_cord+1) == this.type;
            return bot;
        }
        else{
            return false;
        }
    }


    //returns if any block on any side match
    public boolean ask(){
        return ( askBot()|| askTop() || askRight() ||askLeft() );
    }//end ask


    //make list of matching blocks
    public boolean eval() {
        ArrayList<Block> vert = new ArrayList<Block>();
        ArrayList<Block> hori = new ArrayList<Block>();
        //add self to list of kill
        vert.add(this);
        hori.add(this);
        Block temp;
        int i;
        has();
        ask();
        if(this.top){
            //evalForceRunner('t',vert,hori);
            i=0;
            do {
                temp=grid.getBlock(x_cord,y_cord-i);
                i++;
                if((y_cord-i)<0) {
                    temp.setHasTop(false);
                    temp.setTop(false);
                    break;
                }
                if(temp.askTop()) {
                    temp = grid.getBlock(x_cord, y_cord-i);
                    if (temp!=this)
                        vert.add(temp);
                    else{break;}
                }
            }while (temp.askTop());
        }//end top
        if(this.bot){
            //evalForceRunner('b',vert,hori);
            i=0;
            do {
                temp=grid.getBlock(x_cord,y_cord+i);
                i++;
                if((y_cord+i)>8) {
                    temp.setHasBot(false);
                    temp.setBot(false);
                    break;
                }
                if(temp.askBot()) {
                    temp = grid.getBlock(x_cord,y_cord+i);
                    if (temp!=this) {
                        vert.add(temp);
                    } else{break;}
                }
            }while (temp.askBot());
        }
        if(this.left){
            //evalForceRunner('l',vert,hori);
            i=0;
            do {
                temp=grid.getBlock(x_cord-i,y_cord);
                i++;
                if((x_cord-i)<0) {
                    temp.setHasLeft(false);
                    temp.setLeft(false);
                    break;
                }
                if(temp.askLeft()) {
                    temp = grid.getBlock(x_cord-i, y_cord);
                    if (temp!=this) {
                        hori.add(temp);
                    } else{break;}
                }
            }while (temp.askLeft());
        }
        if(this.right){
            //evalForceRunner('r',vert,hori);
            i=0;
            do {
                temp=grid.getBlock(x_cord+i,y_cord);
                i++;
                if((x_cord+i)>8) {
                    temp.setHasRight(false);
                    temp.setRight(false);
                    break;
                }
                if (temp.askRight())
                    temp = grid.getBlock(x_cord+i, y_cord);
                if (temp!=this) {
                    hori.add(temp);
                } else{break;}

            }while (temp.askRight());
        }
        return hasMatch(vert,hori);
    }//end eval
    private boolean checkCopy(ArrayList<Block> vlist, ArrayList<Block> hlist){
        int count=0;
        for (Block v : vlist){
            if(hlist.contains(v))
            {
                count++;
                hlist.remove(v);
            }
        }//v
        return (count>0);
    }
    //has match and callz kill on lists
    private boolean hasMatch(ArrayList<Block> vlist, ArrayList<Block> hlist){
        boolean hasMatch=false;
        boolean cflag=false;
        if (vlist.size()>2&&hlist.size()>2) {
            cflag=checkCopy(vlist,hlist);
        }
        if (vlist.size()>2||cflag){
            grid.kill(vlist);
            hasMatch=true;
        }
        if (hlist.size()>2||cflag){
            grid.kill(hlist);
            hasMatch=true;
        }
        return hasMatch;
    }
    //choose type
    public int random(){
        has();
        Random rand = new Random();
        type = rand.nextInt(4);
        return this.type;
    }
    //getters
    public int getType() {
        return type;
    }
    public int getX() {
        return x_cord;
    }
    public int getY() {
        return y_cord;
    }
    //setters
    public void setType(int type) {
        this.type = type;
    }
    public void setX_cord(int xcord) {
        x_cord = xcord;
    }
    public void setY_cord(int ycord) {
        y_cord = ycord;
    }
    public void setTop(boolean top) {
        this.top = top;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setHasTop(boolean hasTop) {
        this.hasTop = hasTop;
    }

    public void setHasBot(boolean hasBot) {
        this.hasBot = hasBot;
    }

    public void setHasRight(boolean hasRight) {
        this.hasRight = hasRight;
    }

    public void setHasLeft(boolean hasLeft) {
        this.hasLeft = hasLeft;
    }
    //
    //
    //force eval to run
    private void evalForceRunner(char direction,ArrayList<Block> vList,ArrayList<Block> hList)
    {
        int x=0;
        boolean oldBlockError=false;
        Block temp=null;
        switch (direction) {
            case 'r':
                //case right
                do{
                    x++;
                    try {
                        temp=null;
                        temp=grid.getBlock(x_cord+x,y_cord);
                    } catch (Exception e){
                        System.out.println(e);
                        temp=this;//probz dont need
                        try{temp=grid.getBlock(x_cord,y_cord);oldBlockError=true;}
                        catch (Exception z){System.out.println("give up r");}
                        if (temp!=this){
                            temp.right=false;
                            temp.hasRight=false;
                        }
                    } finally {
                        if (!oldBlockError) {
                            if (temp == null){return;}
                            else
                            {
                                if (temp != this)
                                    hList.add(temp);
                            }
                        }
                        else{return;}
                    }
                }while(temp.askRight());
                return;//end right
            case 'l':
                //left
                oldBlockError=false;
                x = 0;
                do{
                    x++;
                    try {
                        temp=null;
                        temp=grid.getBlock(x_cord-x,y_cord);
                    } catch (Exception e){
                        System.out.println(e);
                        temp=this;
                        try{temp=grid.getBlock(x_cord,y_cord);oldBlockError=true;}
                        catch (Exception z){System.out.println("give up l");}
                        if (temp!=this){
                            temp.left=false;
                            temp.hasLeft=false;
                        }
                    } finally {
                        if (!oldBlockError) {
                            if (temp == null){return;}
                            else
                            {
                                if (temp != this)
                                    hList.add(temp);
                            }
                        }
                        else{return;}
                    }
                }while(temp.askLeft());
                return;
            case 't':
                //top
                oldBlockError=false;
                int y = 0;
                do{
                    y++;
                    try {
                        temp=null;
                        temp=grid.getBlock(x_cord,y_cord-y);
                    } catch (Exception e){
                        System.out.println(e);
                        temp=this;
                        try{temp=grid.getBlock(x_cord,y_cord);oldBlockError=true;}
                        catch (Exception z){System.out.println("give up t");}
                        if (temp!=this){
                            temp.top=false;
                            temp.hasTop=false;
                        }
                    } finally {
                        if (!oldBlockError) {
                            if (temp == null){return;}
                            else
                            {
                                if (temp != this)
                                    vList.add(temp);
                            }
                        }
                        else{return;}
                    }
                }while(temp.askLeft());
                return;
            case 'b':
                //bottom
                oldBlockError=false;
                int yy = 0;
                do{
                    yy++;
                    try {
                        temp=null;
                        temp=grid.getBlock(x_cord,y_cord+yy);
                    } catch (Exception e){
                        System.out.println(e);
                        temp=this;
                        try{temp=grid.getBlock(x_cord,y_cord);oldBlockError=true;}
                        catch (Exception z){System.out.println("give up l");}
                        if (temp!=this){
                            temp.left=false;
                            temp.hasLeft=false;
                        }
                    } finally {
                        if (!oldBlockError) {
                            if (temp == null){return;}
                            else
                            {
                                if (temp != this)
                                    vList.add(temp);
                            }
                        }
                        else{return;}
                    }
                }while(temp.askLeft());
                return;
        }//end switch
    }//end focre runner
}
//eof
