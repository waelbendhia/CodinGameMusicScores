import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/

class Sheet{
    boolean[][] image;
    int[] lineCoords;
    private int pixelCounter;
    private int lineThickness;
    private int lineSpacing;

    public Sheet(int w, int h){
        image = new boolean[w][h];
        lineCoords = new int[5];
        pixelCounter=0;
    }

    private void insertPixel(boolean pixel){
        image[pixelCounter%image.length][pixelCounter/image.length] = pixel;
        pixelCounter++;
    }

    public void insert(String stringIm){
        String[] split = stringIm.split(" ");
        boolean isWhite = false;
        int pixels = 0;
        for(int i = 0; i < split.length; i++){
            isWhite = split[i].equals("W");
            i++;
            pixels = Integer.parseInt(split[i]);
            while(pixels>0){
                insertPixel(isWhite);
                pixels--;
            }
        }
    }

    public int countLines(){
        boolean wasOnBlack = false;
        int lines = 0;
        for(int x = 0; x<image.length; x++){
            for(int y = 0; y<image[0].length; y++){
                if(wasOnBlack && image[x][y]) wasOnBlack = false;
                if(!wasOnBlack && !image[x][y]){
                    wasOnBlack = true;
                    lineCoords[lines]=y;
                    lines++;
                }
                if(wasOnBlack && !image[x][y]) lineThickness++;
            }
            if(lines != 0){
                lineThickness /= lines;
                lineSpacing = lineCoords[1] - lineCoords[0];
                return lines;
            }
        }
        return lines;
    }

    private boolean isInLine(int y){
        for(int i = 0; i<lineCoords.length; i++){
            if( y >= lineCoords[i] && y <= lineCoords[i] + lineThickness ) return true;
        }
        if( y >= lineCoords[4]+lineSpacing && y <= lineCoords[4]+lineSpacing + lineThickness ) return true;
        return false;
    }

    private char getQuarterOrHalf(int x, int y){
        if(image[x+(lineThickness*2)][y]) return 'H';
        else return 'Q';
    }

    private char getNote(int y){
        //return A, A is located below the third line from the top
        if(y > lineCoords[2]+lineThickness && y < lineCoords[3] - lineThickness) return 'A';
        //return B, B is locted on the third line from the top
        if(y > lineCoords[2]-lineThickness && y < lineCoords[2] + lineThickness) return 'B';
        //return C, C is located below the staff on the sixth line
        if(y > lineCoords[4]+lineSpacing-lineThickness && y < lineCoords[4]+lineSpacing + lineThickness) return 'C';
        //return C, C is located below the second line from the top
        if(y > lineCoords[1]+lineThickness && y < lineCoords[2] - lineThickness) return 'C';
        //return D. D is located below the staff
        if(y > lineCoords[4]+lineThickness && y < lineCoords[4]+lineSpacing - lineThickness) return 'D';
        //return D, D is located on the second line from the top
        if(y > lineCoords[1]-lineThickness && y < lineCoords[1] + lineThickness) return 'D';
        //return E. E is located on the fifth line from the top
        if(y > lineCoords[4]-lineThickness && y < lineCoords[4] + lineThickness) return 'E';
        //return E. E is located below the first line from the top
        if(y > lineCoords[0]+lineThickness && y < lineCoords[1] - lineThickness) return 'E';
        //return F, F is located below the fourth line from the top
        if(y > lineCoords[3]+lineThickness && y < lineCoords[4] - lineThickness) return 'F';
        //return F, F is located on the first line from the top
        if(y > lineCoords[0]-lineThickness && y < lineCoords[0] + lineThickness) return 'F';
        //return G, G is located on the fourth line from the top
        if(y > lineCoords[3]-lineThickness && y < lineCoords[3] + lineThickness) return 'G';
        //return G, G above the first line from the top
        if(y > lineCoords[0]-lineSpacing +lineThickness && y < lineCoords[0] - lineThickness) return 'G';
        return morePreciseGetNote(y);
    }

    private char morePreciseGetNote(int y){
        //We will snap y to the closest line or half line
        double yf = y;
        yf -= lineCoords[0];
        yf /= (lineSpacing/2.0);
        yf = Math.round(yf);
        yf *= lineSpacing/2.0;
        yf += lineCoords[0];
        y = (int)Math.round(yf);
        return getNote(y);
    }
    public String findNotes(){
        String output ="";
        for(int x = 0; x<image.length; x++){
            for(int y=0; y<image[x].length; y++){
                if(!image[x][y]){
                    if(!isInLine(y)){
                        if(output.length()!=0) output+=" ";
                        //System.err.println("FOUND A NOTE AT " + x + " " + y);
                        //System.err.println("NOTE IS " + getNote(y) + getQuarterOrHalf(x,y));
                        output += "" + getNote(y) + getQuarterOrHalf(x,y);
                        x += lineSpacing+((lineThickness<2) ? 1 : (lineThickness/2));
                    }
                }
            }
        }
        return output;
    }

    public String getLineCoords(){
        String output = "Thickness: " + lineThickness + " ";
        for(int i =0; i<5; i++)
            output+= " " + lineCoords[i];
        return output;
    }



    @Override
    public String toString(){
        String out = "";
        float avg=0;
        for(int y = 0; y<image[0].length; y++){
            for(int x = 0; x<image.length; x++){
                if(image[x][y]) out+="W";
                else out+= " ";
            }
            out += '\n';
        }
        return out;
    }
}

class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int W = in.nextInt();
        System.err.println(W);
        int H = in.nextInt();
        System.err.println(H);
        Sheet sheet = new Sheet(W, H);
        in.nextLine();
        String IMAGE = in.nextLine();
        sheet.insert(IMAGE);
        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");
        sheet.countLines();


        System.out.println(sheet.findNotes());
        //System.out.println(sheet.toString());
    }
}