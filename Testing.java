package testing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

/**
 * Used for testing the swing class and the awt class
 */
public class Testing {
    public static void main(String[] args){
        File file = new File("texting.txt");
        Passwords pass = new Passwords("Testing system", file);
    }
}
