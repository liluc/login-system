package testing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.security.MessageDigest;

/**
 * Used to simulate the process of checking the corresponding username and password.
 * author @Lucas Li
 */


public class Passwords {
    //set all the fields.
    private JFrame jf, regijf;
    private JPanel logp, regip;
    private JTextField logname, logpass, reginame, regipass, regimail;
    private JButton register, login, forget;
    private JLabel name1, password1, name2, password2, email2, noti, nothing, notibad;
    private ArrayList<String> passwords;
    private ArrayList<String> badPasswords;
    private boolean check = true;
    private String reginame2, regipass2, regimail2;
    private int checkRegi, checkForget, checkRenew;

    public Passwords(String name, File users) {
        //creat all the fields
        jf = new JFrame(name);
        logp = new JPanel();
        regip = new JPanel();
        logname = new JTextField();
        logpass = new JTextField();
        reginame = new JTextField();
        regipass = new JTextField();
        regimail = new JTextField();
        register = new JButton("Register");
        login = new JButton("Login");
        forget = new JButton("forget your password?");
        name1 = new JLabel("Name");
        name2 = new JLabel("Name");
        password1 = new JLabel("Password");
        password2 = new JLabel("Password");
        email2 = new JLabel("E-Mail");
        noti = new JLabel();
        nothing = new JLabel();
        notibad = new JLabel();
        passwords = new ArrayList<>();
        badPasswords = new ArrayList<>();
        checkRegi = 0;
        checkForget = 0;
        checkRenew = 0;

        jf.setSize(700, 300);

        //set the array of bad passwords
        try {
            File bad = new File("BadPasswords.txt");
            Scanner scan = new Scanner(bad);
            while (scan.hasNextLine()) {
                badPasswords.add(scan.nextLine());
            }
        } catch (Exception e) {
            System.out.printf("Error: \n%s", e);
        }

        //set the array of passwords
        try {
            Scanner scan = new Scanner(users);
            while (scan.hasNextLine()) {
                passwords.add(scan.nextLine());
            }
        } catch (Exception e) {
            System.out.printf("Error: \n%s", e);
        }

        //put everything together
        jf.getContentPane().add(logp);

        //build a better-looking GUI for logging in.
        logp.setLayout(null);
        logp.add(noti);
        logp.add(name1);
        logp.add(logname);
        logp.add(password1);
        logp.add(logpass);
        logp.add(login);
        logp.add(forget);
        logp.add(register);

        //set the login window's appearance
        noti.setBounds(110, 25, 480, 25);
        name1.setBounds(110, 50, 100, 25);
        logname.setBounds(250, 50, 350, 25);
        password1.setBounds(110, 75, 100, 20);
        logpass.setBounds(250, 75, 350, 25);
        login.setBounds(450, 160, 100, 25);
        forget.setBounds(420, 185, 150, 20);
        register.setBounds(150, 160, 100, 25);


        //set functions of the buttons and text fields
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noti.setText("");
                String enterName = logname.getText();
                String enterPass = logpass.getText();
                String enname1 = "";
                String enpass1 = "";
                int count = 0;

                enname1 = enterName;
                enpass1 = encryption(enterPass);
                for (int i = 1; i < passwords.size(); i++) {
                    count++;
                    StringTokenizer tok = new StringTokenizer(passwords.get(i), ",");
                    String enname2 = tok.nextToken();
                    String enpass2 = tok.nextToken();
                    //check if the username and the passwords matches
                    if (enname1.equals(enname2) && enpass1.equals(enpass2)) {
                        noti.setText("Log in successfully");
                        logname.setText("");
                        logpass.setText("");
                        break;

                    } else if(count == passwords.size() - 1){
                        noti.setText("Username and passwords do not match");
                        logname.setText("");
                        logpass.setText("");
                        break;
                    }
                }


            }
        });

        //set the action of the register button in the login window
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register.setEnabled(false);

                //create and set the register window.
                regijf = new JFrame("Register");
                regijf.setSize(700, 300);
                JButton register2 = new JButton("Register");
                regijf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                reginame = new JTextField();
                regipass = new JTextField();
                regimail = new JTextField();
                name2 = new JLabel("Name");
                password2 = new JLabel("Password");
                email2 = new JLabel("E-Mail");
                notibad = new JLabel();
                regip = new JPanel();

                regijf.add(regip);
                regip.setLayout(null);
                regip.add(name2);
                regip.add(reginame);
                regip.add(password2);
                regip.add(regipass);
                regip.add(email2);
                regip.add(regimail);
                regip.add(register2);
                regip.add(notibad);

                //set the size of all the windows and buttons and labels
                regijf.setLocation(0, 300);
                notibad.setBounds(110, 20, 480, 25);
                name2.setBounds(110, 50, 100, 25);
                reginame.setBounds(250, 50, 350, 25);
                password2.setBounds(110, 75, 100, 20);
                regipass.setBounds(250, 75, 350, 25);
                email2.setBounds(110, 100, 100, 25);
                regimail.setBounds(250, 100, 350, 25);
                register2.setBounds(450, 180, 100, 25);

                regijf.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        super.windowClosed(e);
                        register.setEnabled(true);

                    }
                });

                //set the register button in the register window.
                register2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        check = true;
                        reginame2 = reginame.getText();
                        regipass2 = regipass.getText();
                        regimail2 = regimail.getText();

                        //check if the password is bad.
                        for (String x : badPasswords) {
                            if (regipass2.equals(x)) {
                                notibad.setText("Your passwords is too weak!");
                                regipass.setText("");
                                check = false;
                            }
                        }


                        for (int i = 1; i < passwords.size(); i++){
                            StringTokenizer tok = new StringTokenizer(passwords.get(i), ",");
                            String filename = tok.nextToken();
                            if(reginame2.equals(filename)){
                                notibad.setText("The username have been used, please choose another one");
                                reginame.setText("");
                                check = false;
                            }
                        }

                        for (int i = 0; i < reginame2.length(); i++){
                            if(reginame2.charAt(i) == ',') {
                                check = false;
                                notibad.setText("Please do not use \",\" in your username");
                                reginame.setText("");
                            }
                        }
                        //check if the user has entered all the information.
                        if (reginame2.equals("")) {
                            notibad.setText("Please enter your name!");
                            check = false;
                        } else if (regipass2.equals("")) {
                            notibad.setText("Please enter your passwords!");
                            check = false;
                        } else if (regimail2.equals("")) {
                            notibad.setText("Please enter your E-mail!");
                            check = false;
                        }

                        //if the user has entered all the information and the password is not bad.
                        if (check) {
                            String enname = "", enpass = "", enmail = "";
                            enname = reginame2;
                            enpass = encryption(regipass2);
                            enmail = encryption(regimail2);
                            notibad.setText("Register successfully!");
                            try {
                                FileWriter fw = new FileWriter(users, true);
                                PrintWriter output = new PrintWriter(fw);
                                output.println(enname + "," + enpass + "," + enmail);
                                output.close();
                                passwords.add(enname + "," + enpass + "," + enmail);
                                reginame.setText("");
                                regimail.setText("");
                                regipass.setText("");
                            } catch (Exception ex) {
                                System.out.printf("Error: \n%s", ex);
                            }
                        }

                    }
                });

                regijf.setVisible(true);

            }
        });

        //set the "forget your passwords" button in the login window
        forget.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forget.setEnabled(false);

                //set the username-email check window and the set a new email window.
                JFrame forgetjf = new JFrame("Get your password");
                JLabel username = new JLabel("Username");
                JTextField user = new JTextField(logname.getText());
                JLabel email = new JLabel("E-mail");
                JTextField mail = new JTextField();
                JButton get = new JButton("reset Your passwords");
                JLabel pass = new JLabel();
                JButton returnPre = new JButton("Return");

                forgetjf.setLayout(null);
                forgetjf.getContentPane().add(username);
                forgetjf.getContentPane().add(user);
                forgetjf.getContentPane().add(email);
                forgetjf.getContentPane().add(mail);
                forgetjf.getContentPane().add(get);
                forgetjf.getContentPane().add(pass);
                forgetjf.getContentPane().add(returnPre);

                forgetjf.setSize(700, 300);
                pass.setBounds(110, 25, 480, 25);
                username.setBounds(110, 50, 100, 25);
                user.setBounds(250, 50, 350, 25);
                email.setBounds(110, 75, 100, 20);
                mail.setBounds(250, 75, 350, 25);
                get.setBounds(450, 160, 200, 25);
                returnPre.setBounds(110,160,200,25);

                forgetjf.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        super.windowClosed(e);
                        forget.setEnabled(true);
                    }
                });

                forgetjf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                //set the "reset your password" button in the checking email window.
                get.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int count = 0;
                        for (int i = 1; i < passwords.size(); i++) {
                            count ++;
                            StringTokenizer tok = new StringTokenizer(passwords.get(i), ",");
                            String username = tok.nextToken();
                            String passW = tok.nextToken();
                            String email = tok.nextToken();
                            if (username.equals(user.getText()) && email.equals(encryption(mail.getText()))) {
                                get.setEnabled(false);
                                JFrame change = new JFrame("Change your passwords");
                                JPanel resetp = new JPanel();
                                JLabel newPass = new JLabel("New Passwords");
                                JLabel notiRegi = new JLabel();
                                JTextField newpassenter = new JTextField();
                                JButton renew = new JButton("Change passwords");
                                JButton returnPre = new JButton("Return");

                                resetp.setLayout(null);
                                resetp.add(newPass);
                                resetp.add(newpassenter);
                                resetp.add(renew);
                                resetp.add(notiRegi);
                                resetp.add(returnPre);

                                newPass.setBounds(110, 65, 100, 25);
                                newpassenter.setBounds(250, 65, 350, 25);
                                renew.setBounds(450, 135, 200, 25);
                                notiRegi.setBounds(110, 30, 350, 25);
                                returnPre.setBounds(110,135,200,25);

                                //set the "Return" in the 4th window


                                //set the "reset your passwords button in the 4th window..
                                renew.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        check = true;
                                        String newPass = newpassenter.getText();
                                        //check if the new password is bad or not.
                                        for (String x : badPasswords) {
                                            if (newPass.equals(x)) {
                                                notiRegi.setText("Your passwords is too weak!");
                                                newpassenter.setText("");
                                                check = false;
                                            }
                                        }
                                        //if everything goes on pretty well. Then...
                                        if (check) {
                                            String enuser = user.getText();
                                            String enpass = encryption(newPass);
                                            try {
                                                for (int i = 1; i < passwords.size(); i++) {
                                                    StringTokenizer token = new StringTokenizer(passwords.get(i), ",");
                                                    String user = token.nextToken();
                                                    String pass = token.nextToken();
                                                    String email = token.nextToken();
                                                    if (user.equals(enuser)) {
                                                        passwords.set(i, user + "," + enpass + "," + email);
                                                        notiRegi.setText("Passwords renew successfully!");
                                                        newpassenter.setText("");
                                                    }
                                                }
                                                PrintWriter output = new PrintWriter(users);
                                                for (String x : passwords) {
                                                    output.println(x);
                                                }
                                                output.close();
                                            } catch (Exception ex) {
                                                System.out.println(ex);
                                            }
                                        }

                                    }

                                });

                                change.add(resetp);
                                change.setSize(700, 300);
                                change.setVisible(true);
                                change.addWindowListener(new WindowAdapter() {
                                    @Override
                                    public void windowClosing(WindowEvent e) {
                                        super.windowClosing(e);
                                        get.setEnabled(true);
                                    }
                                });
                                returnPre.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        change.dispose();
                                    }
                                });
                                change.setDefaultCloseOperation(2);
                                pass.setText("");
                                break;
                            } else if (count == passwords.size() - 1){
                                pass.setText("Your username and email do not match!");

                            }
                        }
                    }
                });

                returnPre.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        forgetjf.dispose();
                    }
                });
                forgetjf.setVisible(true);
            }

        });

        jf.setDefaultCloseOperation(3);
        jf.setVisible(true);
    }

    //a method used for encryption. Much more easier than code everything in the constructor.
    public String encryption(String str) {
        String r = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte byteData[] = md.digest();
            for (int i = 0; i < byteData.length; ++i) {
                r += (Integer.toHexString((byteData[i] & 0xFF) | 0x100).substring(1, 3));
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return r;
    }
}
