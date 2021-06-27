package com.company;

import java.util.*;

public class Main {

    private static final Scanner input = new Scanner(System.in);

    public static int[] nextbits = new int[16];
    public static boolean[] bnb = new boolean[16];
    public static boolean[] bcb = new boolean[16]; 
    public static int[] currbits = new int[16]; 
    public static int[] star = new int[20];
    public static int[] windowRNG = {300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300};
    public static int rngNum1;
    public static int[] framelist = new int[9999];
    public static String[] hexList = new String[9999];

    public static void main(String[] args) {

        System.out.println("=== Kirby Super Star Full-Range RNG Searcher ====\n");
        System.out.println("- This program accepts 1 to 6 values.\n- Note that inputting a low number of values will output a high number of outcomes.\n- (inputting a single value may take a while to display)\n");

        int initframes = 0;
        int maxframes = 65536;

        String strStars;
        String[] strStarsParts;
        int amountToCalculate;

        while (true) {

            String hex = framesToHex(initframes, 0);
            int frames = initframes;
            int numOfFrames = 0;


            System.out.print("Input stars in order with no spaces (XXXXXX, stars must be integers from 1 - 8): ");
            strStars = input.nextLine();

            amountToCalculate = strStars.length();
            strStarsParts = strStars.split("");


            System.out.print("- Attempting to calculate " + amountToCalculate + " star directions...");
            for (int i = 0; i < amountToCalculate && i < 6; i++) {
                star[i] = Integer.parseInt(strStarsParts[i]);
            }
            System.out.println();



            while ((frames < maxframes)) {
                hex = calculateNextHex(hex);
                frames++;
                rngNum1 = parseNum1(hex);


                for (int i = 9; i >= 0; i--) {
                    windowRNG[i + 1] = windowRNG[i];
                }
                windowRNG[0] = calcDirectionBasedOnDecimal(rngNum1);

                switch(amountToCalculate) {

                    case 1:
                        if (windowRNG[0] == star[0]){
                            framelist[numOfFrames] = frames;
                            hexList[numOfFrames] = hex;
                            numOfFrames++;
                        }
                        break;

                    case 2:
                        if ((windowRNG[2] == star[0]) && (windowRNG[0] == star[1])){
                            framelist[numOfFrames] = frames;
                            hexList[numOfFrames] = hex;
                            numOfFrames++;
                        }
                        break;

                    case 3:
                        if ((windowRNG[4] == star[0]) && (windowRNG[2] == star[1]) && (windowRNG[0] == star[2])){
                            framelist[numOfFrames] = frames;
                            hexList[numOfFrames] = hex;
                            numOfFrames++;
                        }
                        break;

                    case 4:
                        if ((windowRNG[6] == star[0]) && (windowRNG[4] == star[1]) && (windowRNG[2] == star[2]) && (windowRNG[0] == star[3])){
                            framelist[numOfFrames] = frames;
                            hexList[numOfFrames] = hex;
                            numOfFrames++;
                        }
                        break;

                    case 5:
                        if ((windowRNG[8] == star[0]) && (windowRNG[6] == star[1]) && (windowRNG[4] == star[2]) && (windowRNG[2] == star[3]) && (windowRNG[0] == star[4])){
                            framelist[numOfFrames] = frames;
                            hexList[numOfFrames] = hex;
                            numOfFrames++;
                        }
                        break;

                    case 6:
                        if ((windowRNG[10] == star[0]) && (windowRNG[8] == star[1]) && (windowRNG[6] == star[2]) && (windowRNG[4] == star[3]) && (windowRNG[2] == star[4]) && (windowRNG[0] == star[5])) {
                            framelist[numOfFrames] = frames;
                            hexList[numOfFrames] = hex;
                            numOfFrames++;
                        }
                        break;

                    default:
                        numOfFrames = -1;
                        break;

                }
            }

            if (numOfFrames == 0) {
                System.out.println("- No values found for this set.\n");
            }
            else if (numOfFrames == -1){
                System.out.println("- Please input 6 star directions.\n");
            }
            else {
                System.out.println("---------------------------------------------------------------");
                for (int i = 0; i < numOfFrames; i++) {
                    System.out.println("- Starting RNG Point: " + (framelist[i] - (amountToCalculate * 2)) + " - Hex: " + framesToHex(framelist[i] - (amountToCalculate * 2), 0) + " - RNG Number: " + parseNum1(framesToHex(framelist[i] - (amountToCalculate * 2), 0)));
                    System.out.println("- Ending RNG Point (after jumps): " + (framelist[i]) + " - Hex: " + hexList[i] + " - RNG Number: " + parseNum1(hexList[i]));
                    System.out.println("---------------------------------------------------------------");
                }
                System.out.println();
            }

        }
    }



    private static String hexToBin(String hex) {
        int i = Integer.parseInt(hex, 16);
        String bin = Integer.toBinaryString(i);
        while (bin.length() < 8) {
            bin = "0" + bin;
        }
        return bin;
    }

    private static void xorCalculate(int count, int bitnum, boolean negative, boolean a, boolean b, boolean c, boolean d, boolean e){
        boolean result;
        int intresult;
        int negresult;

        if (count == 5) {
            result = (a ^ b ^ c ^ d ^ e);
        }
        else{
            result = (a ^ b ^ c );
        }
        intresult = boolToInt(result);

        if (negative) {
            if (intresult == 0)
                negresult = 1;
            else
                negresult = 0;
            intresult = negresult;
        }

        if (intresult != 0) {
            nextbits[bitnum] = 1;
            bnb[bitnum] = true;
        } else {
            nextbits[bitnum] = 0;
            bnb[bitnum] = false;
        }
    }

    private static String calculateNextHex(String initialHex) {

        String initbincurr = hexToBin(initialHex);   //convert hex to binary

        String zeroes = "";

        for (int i = initbincurr.length(); i < 16; i++) {
            zeroes = zeroes + "0";
        }

        String bincurr = zeroes + initbincurr;

        String[] strcurrbits = bincurr.split("");       //convert binary to 16 separate strings


        for (int i = 0; i < 16; i++) {
            currbits[i] = Integer.parseInt(strcurrbits[i]);
            if (currbits[i] == 0) {
                bcb[i] = false;
            } else {
                bcb[i] = true;
            }
        }
        //Calculations:
        //1
        xorCalculate(5, 0, true, bcb[6], bcb[7], bcb[8], bcb[10], bcb[11]);
        //2
        xorCalculate(5, 1, false, bcb[6], bcb[8], bcb[9], bcb[11], bcb[12]);
        //3
        xorCalculate(5, 2, false, bcb[7], bcb[9], bcb[10], bcb[12], bcb[13]);
        //4
        xorCalculate(3, 3, false, bnb[0], bcb[13], bcb[14], true, true);
        //5
        xorCalculate(3, 4, false, bnb[1], bcb[14], bcb[15], true, true);
        //6
        xorCalculate(3, 5, false, bnb[2], bcb[15], bcb[0], true, true);
        //7
        xorCalculate(3, 6, false, bnb[3], bcb[0], bcb[1], true, true);
        //8
        xorCalculate(3, 7, false, bnb[4], bcb[1], bcb[2], true, true);
        //9 - 13
        for (int i = 0; i < 5; i++) {
            nextbits[i + 8] = currbits[i + 3];
        }
        //14
        xorCalculate(3, 13, true, bcb[6], bcb[7], bcb[8], true, true);
        //15
        xorCalculate(3, 14, false, bcb[6], bcb[8], bcb[9], true, true);
        //16
        xorCalculate(3, 15, false, bcb[7], bcb[9], bcb[10], true, true);

        String completeBinary = "";

        for (int i = 0; i < 16; i++) {
            completeBinary = completeBinary + nextbits[i];
        }

        int b = Integer.parseInt(completeBinary, 2);
        String inithex = Integer.toHexString(b);

        zeroes = "";

        for (int i = inithex.length(); i < 4; i++) {
            zeroes = zeroes + "0";
        }

        String hex = zeroes + inithex;

        return hex;
    }

    private static int calcDirectionBasedOnDecimal(int num){
        int output;
        if (isBetween(num, 0, 31))
            output = 1;
        else if (isBetween(num, 32, 63))
            output = 2;
        else if (isBetween(num, 64, 95))
            output = 3;
        else if (isBetween(num, 96, 127))
            output = 4;
        else if (isBetween(num, 128, 159))
            output = 5;
        else if (isBetween(num, 160, 191))
            output = 6;
        else if (isBetween(num, 192, 223))
            output = 7;
        else
            output = 8;

        return output;
    }

    private static int boolToInt(boolean result) {
        int output;
        if (result){
            output = 1;
        }
        else{
            output = 0;
        }
        return output;
    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    private static String framesToHex(int frames, int advancements) {
        String output = "7777";
        for (int i = 0; i < frames; i++){
            output = calculateNextHex(output);
            //System.out.println("Hex: " + output + " Frames: " + frames);
        }
        if (advancements != 0) {
            for (int i = 0; i < advancements; i++){
                output = calculateNextHex(output);
            }
        }
        return output;
    }

    public static int parseNum1(String hex){
        String[] hexParts = hex.split("");
        String StrrngNum1 = hexParts[0] + hexParts[1];
        return Integer.parseInt(StrrngNum1, 16);
    }

    public static boolean areAllTrue(boolean[] array)
    {
        for(boolean b : array) if(!b) return false;
        return true;
    }
}


