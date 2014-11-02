/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mazegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author HLOHLOMI
 */
public class MazeGame implements ActionListener
{

     JFrame frame          = new JFrame();
     JFrame Name          = new JFrame();
     JFrame Levels          = new JFrame();
     JLabel  name          = new JLabel(" Enter Your Name : ");
     JTextField namefield  = new JTextField(10);
     JButton ok            = new JButton("OK");
     
     JRadioButton easy = new JRadioButton("Easy");
     JRadioButton medium = new JRadioButton("Medium");
     JRadioButton hard = new JRadioButton("Hard");
     
     MenuBar bar     = new MenuBar();
     Menu   Help     = new Menu("Help");
     Menu   Game     = new Menu("Game");
     MenuItem aid    = new MenuItem("Help");
     MenuItem New    = new MenuItem("New Game");
     MenuItem ChangeName    = new MenuItem("Change Name");
     MenuItem Level   = new MenuItem("Level");
     MenuItem Scores = new MenuItem("View Scores");
     MenuItem Exit   = new MenuItem("Exit");
     
     String PlayerName;
     String levell = "Easy";
     int count = -1;

     
     MainForm m;
     Scores scoreObject;
     
     
     
     public MazeGame()
     {
         Name.setLayout(new GridLayout(3,1));
         Name.setLocation(600,300);
         
         name.setFont(new Font("Bookman Old Style " , Font.BOLD , 15));
         namefield.setFont(new Font("Bookman Old Style " , Font.BOLD , 20));
         Name.setSize(200,200);
         Name.add(name);
         Name.add(namefield);
         Name.add(ok);
         
         Name.setVisible(true);
         ok.addActionListener(this);
         
         Levels.setLayout(new FlowLayout());
         Levels.add(easy);
         Levels.add(medium);
         Levels.add(hard);
         Levels.pack();
         
         Help.add(aid);
         
         
         Game.add(New);
         Game.add(Level);
         Game.add(ChangeName);
         Game.add(Scores);
         Game.add(Exit);
         
         bar.add(Game);
         bar.add(Help);
         
         New.addActionListener(this);
         aid.addActionListener(this);
         Scores.addActionListener(this);
         ChangeName.addActionListener(this);
         Level.addActionListener(this);
         Exit.addActionListener(this);
         easy.addActionListener(this);
         medium.addActionListener(this);
         hard.addActionListener(this);
     }
    public void display()
     {
         frame.getContentPane().setBackground(Color.PINK);
            frame.remove(m);
            frame.dispose();
            
            m = new MainForm(PlayerName,levell);
            m.setBackground(Color.GRAY);

            frame.add(m);
            frame.setSize(646,670);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
                
            frame.setVisible(true);
     }
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()== easy)
        {
            easy.setSelected(false);
            hard.setSelected(false);
            medium.setSelected(false);
            Levels.dispose();
            levell = "Easy";
            display();
        }
        if(e.getSource()== medium)
        {
            easy.setSelected(false);
            hard.setSelected(false);
            medium.setSelected(false);
            Levels.dispose();
            levell = "Medium";
            display();
        }
        if(e.getSource()== hard)
        {
            easy.setSelected(false);
            hard.setSelected(false);
            medium.setSelected(false);
            Levels.dispose();
            levell = "Hard";
            display();
        }
        if(e.getSource() == ok)
        {
           count ++;
            PlayerName = namefield.getText();
            if(count >= 1)
            {
              frame.remove(m);
              frame.dispose();
            }
            frame.getContentPane().setBackground(Color.PINK);
            m = new MainForm(PlayerName, levell);
            m.setBackground(Color.GRAY);
         
            frame.setMenuBar(bar);
            frame.add(m);
            frame.setSize(646,670);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
                
            
            frame.setVisible(true);
             
           
           Name.dispose();
        }
        if(e.getSource() == aid)
        {
            
            help obj = new help();
            obj.setLocationRelativeTo(null);
            obj.setVisible(true);
            
        }
        if(e.getSource() == New)
        {
            
            display();
        }
        if(e.getSource() == Level)
        {
            Levels.setLocationRelativeTo(null);
            Levels.setVisible(true);
        }
      
        
        if(e.getSource() == Scores)
        {
            scoreObject = new Scores(m.Playername , m.Level , m.score+1);
            scoreObject.setLocation(360,100);
            scoreObject.setVisible(true); 
            
        }
        
        if(e.getSource() == Exit)
        {
            System.exit(0);
        }
        if(e.getSource() == ChangeName)
        {
            Name.setVisible(true);
        }
    }
    
    public static void main(String[] args) 
    {
        try
        {
            for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            {
                if("Nimbus".equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(Exception e)
        {
            System.out.print("not there");
        }
       
        MazeGame game = new MazeGame();
    }

    
}