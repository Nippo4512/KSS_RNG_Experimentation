package com.company;

import java.util.Scanner;


public class Main {

    private static final Scanner input = new Scanner(System.in);

    public static int nextbits[] = new int[16];   //next bits (blank)
    public static boolean bnb[] = new boolean[16];

    public static void main(String[] args) {

        while (true) {
            System.out.print("Input two hexadecimal RNG values (XXXX): ");
            String hexcurr = input.nextLine();

            int y = 0;

            String initbincurr = hexToBin(hexcurr);   //convert hex to binary

            String zeroes = "";

            for (int i = initbincurr.length(); i < 16; i++){
                zeroes = zeroes + "0";
            }

            String bincurr = zeroes + initbincurr;

            String[] strcurrbits = bincurr.split("");       //convert binary to 16 separate strings
            boolean[] bcb = new boolean[16];   //for xor
            int[] currbits = new int[16];   //convert these strings to integers

            for (int i = 0; i < 16; i++) {
                currbits[i] = Integer.parseInt(strcurrbits[i]);
                if (currbits[i] == 0){
                    bcb[i] = false;}
                else{
                    bcb[i] = true;}
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
            for (int i = 0; i < 5; i++){
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

            for (int i = inithex.length(); i < 4; i++){
                zeroes = zeroes + "0";
            }

            String hex = zeroes + inithex;
            hex = hex.toUpperCase();

            System.out.println("Next RNG Value (Hex): " + hex);


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
}

