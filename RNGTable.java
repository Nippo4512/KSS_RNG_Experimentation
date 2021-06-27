package com.company;

import java.util.*;

public class Main {

    private static final Scanner input = new Scanner(System.in);

    public static int[] nextbits = new int[16];   //next bits (blank)
    public static boolean[] bnb = new boolean[16];
    public static boolean[] bcb = new boolean[16];   //for xor
    public static int[] currbits = new int[16];   //convert these strings to integers
    public static int[] star = new int[2];
    public static int[] windowRNG = {300, 300, 300, 300, 300};
    public static int rngNum1;
    public static int[] framelist = new int[99];
    public static int[] initframelist = new int[99];
    public static String[] hexList = new String[99];
    public static int maxNum = 0;
    public static String[] hexcurrList = new String[99];
    public static int[] rngNum1List = new int[99];
    public static int[] actionList = new int[99];
    public static boolean[] allWork = new boolean[99];

    public static int[][] ufaTable = new int[8][8];

    public static void main(String[] args) {

        /*Notes for Later:
        Either restart from the other program or include any corrections made to the other program
        First program was somewhat concluded to be accurate based on tests in game and calculating rng
        Had to subtract 1 from actions since the frame that matters is the frame that the enemy changes the rng on
        Either way the chart is still not accurate to the original program so work on matching it up with the OG since the OG is looking to be accurate

*/

        System.out.println("Note that the first and second star value account for one wheelie dash in between jumps.");


        while (true) {

            System.out.print("Input min frame number: ");
            int initframes = input.nextInt();
            System.out.print("Input max frame number: ");
            int maxframes = input.nextInt();

            System.out.print("Generating Tables");

            for (int z = 0; z <= 1; z++) {
                String hexcurr = framesToHex(initframes, 0); //starting at frame 700
                String hex;
                int frames;
                String StrrngNum1;
                int numOfFrames = 0;
                String[] hexParts;

                for (int e = 0; e < 8; e++) {
                    for (int f = 0; f < 8; f++) {

                        hexcurr = framesToHex(initframes, 0);
                        maxNum = 0;
                        rngNum1 = 0;
                        star[1] = (e + 1);
                        star[0] = (f + 1);

                        for (int i = 0; i < 99; i++) {
                            framelist[i] = 0;
                            initframelist[i] = 0;
                            actionList[i] = 0;
                            hexcurrList[i] = framesToHex(initframes, 0);
                            hexList[i] = "";
                            rngNum1List[i] = 0;
                            allWork[i] = true;
                            numOfFrames = 0;
                        }
                        for (int i = 0; i < 5; i++) {
                            windowRNG[i] = 300;
                        }


                        frames = initframes;

                        while ((frames < maxframes)) {
                            hex = calculateNextHex(hexcurr);
                            frames++;
                            hexcurr = hex;

                            hexParts = hex.split("");
                            StrrngNum1 = hexParts[0] + hexParts[1];
                            rngNum1 = Integer.parseInt(StrrngNum1, 16);


                            for (int i = 3; i >= 0; i--) {
                                windowRNG[i + 1] = windowRNG[i];
                            }
                            windowRNG[0] = calcDirectionBasedOnDecimal(rngNum1);

                            if ((windowRNG[0] == star[0]) && (windowRNG[4] == star[1])) {
                                framelist[numOfFrames] = frames;
                                hexcurrList[numOfFrames] = hexcurr;
                                hexList[numOfFrames] = hex;
                                rngNum1List[numOfFrames] = rngNum1;
                                allWork[numOfFrames] = false;
                                numOfFrames++;
                            }
                        }


                        for (int i = 0; i < numOfFrames; i++) {
                            initframelist[i] = framelist[i];
                            //System.out.print("RNG Point " + i + ": " + (framelist[i]) + " (" + hexList[i] + ") rngNum: " + rngNum1List[i]);

                            hexList[i] = calculateNextHex(hexList[i]);

                            hexParts = hexList[i].split("");
                            StrrngNum1 = hexParts[0] + hexParts[1];
                            rngNum1List[i] = Integer.parseInt(StrrngNum1, 16);

                            //System.out.print(" - The hex that actually matters: " + hexList[i] + " and the new rngNum1: " + rngNum1List[i]);


                            if (z == 1) {
                                while (isBetween(rngNum1List[i], 128, 191)) {
                                    hexList[i] = calculateNextHex(hexcurrList[i]);
                                    framelist[i]++;
                                    hexcurrList[i] = hexList[i];

                                    hexParts = hexList[i].split("");
                                    StrrngNum1 = hexParts[0] + hexParts[1];
                                    rngNum1List[i] = Integer.parseInt(StrrngNum1, 16);
                                }
                            } else {
                                while (isBetween(rngNum1List[i], 64, 127)) {
                                    hexList[i] = calculateNextHex(hexcurrList[i]);
                                    framelist[i]++;
                                    hexcurrList[i] = hexList[i];

                                    hexParts = hexList[i].split("");
                                    StrrngNum1 = hexParts[0] + hexParts[1];
                                    rngNum1List[i] = Integer.parseInt(StrrngNum1, 16);
                                }
                            }
                            actionList[i] = framelist[i] - initframelist[i] - 1;
                            //System.out.print("- ActionList = " + framelist[i] + " - " + initframelist[i]);
                            if (actionList[i] < 0) actionList[i] = 0;
                            //System.out.println(" - Frames to advance: " + (actionList[i]));
                        }
                        //Universal Frame Advancement between all values
                        maxNum = actionList[0];
                        //for (int i = 0; i < numOfFrames; i++) {
                        //    if (actionList[i] > maxNum) {
                        //        maxNum = actionList[i];
                        //    }
                        //}

                        while (!areAllTrue(allWork)) {
                            for (int i = 0; i < numOfFrames; i++) {
                                hexcurrList[i] = framesToHex(framelist[i], (maxNum - actionList[i]));

                                hexParts = hexcurrList[i].split("");
                                StrrngNum1 = hexParts[0] + hexParts[1];
                                rngNum1List[i] = Integer.parseInt(StrrngNum1, 16);

                                if (z == 1) {
                                    if (isBetween(rngNum1List[i], 128, 191)) {
                                        hexList[i] = calculateNextHex(hexcurrList[i]);
                                        framelist[i]++;
                                        hexcurrList[i] = hexList[i];
                                        maxNum++;
                                    } else {
                                        allWork[i] = true;
                                    }
                                } else {
                                    if (isBetween(rngNum1List[i], 64, 127)) {
                                        hexList[i] = calculateNextHex(hexcurrList[i]);
                                        framelist[i]++;
                                        hexcurrList[i] = hexList[i];
                                        maxNum++;
                                    } else {
                                        allWork[i] = true;
                                    }
                                }
                            }
                        }
                        //System.out.println("Stars: " + (e + 1) + ", " + (f + 1));
                        //System.out.println("Possible RNG Points: " + numOfFrames);
                        //System.out.println("Universal Frame Advancement: " + maxNum);
                        ufaTable[f][e] = maxNum;
                        //System.out.println("ufaTable[" + e + "][" + f + "]: " + ufaTable[e][f]);
                        //System.out.println("---------------------------------------");
                    }
                    System.out.print(".");
                }
                if (z == 0) {
                    System.out.println("\n\nSlime / Magician");
                } else {
                    System.out.println("\n\nPuppet");
                }
                System.out.println("   || 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 ");
                System.out.println("====================================");
                for (int e = 0; e < 8; e++) {
                    if (e != 0) System.out.println("\n------------------------------------");
                    System.out.print(" " + (e + 1) + " |");
                    for (int f = 0; f < 8; f++) {
                        if (numOfDigits(ufaTable[e][f]) == 2) {
                            System.out.print("| " + ufaTable[e][f]);
                        } else if (numOfDigits(ufaTable[e][f]) == 3) {
                            System.out.print("|" + ufaTable[e][f]);
                        } else {
                            System.out.print("| " + ufaTable[e][f] + " ");
                        }
                    }
                }
                System.out.println("\n");
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

    public static boolean areAllTrue(boolean[] array)
    {
        for(boolean b : array) if(!b) return false;
        return true;
    }

    public static int numOfDigits(int num){
        int count = 0;
        while(num!=0){
            num = num/10;
            count++;
        }
        return count;
    }
}


