package online_learning_system;

import java.awt.BorderLayout;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.UnknownHostException;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.TreeMap;


public class student_main {
    // initialize socket and input output streams




    public static void main(String[] args) throws IOException, ClassNotFoundException {
        JFrame Application = new JFrame();
        Application.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;

        student_white_board p = new student_white_board();
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        Application.add(p, constraints);

        chat_box_student chatbox = new chat_box_student("127.0.0.1");
        constraints.weightx = 1;
        constraints.weighty = 0.2;
        constraints.gridx = 0;
        ++constraints.gridy;
        Application.add(chatbox, constraints);

        Application.setSize(1300,1300);
        Application.setVisible(true);
        Application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        Thread chatThread = new Thread(chatbox);
//        Thread board = new Thread(p);
//        chatbox.connect();
        p.receive();

    }

}