/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mazegame;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class MainForm extends JPanel implements ActionListener
{
    //  instances of all classes to be implemented
    public Map m        = new Map();
    private Player p     = new Player();
    private Muffin muf   = new Muffin();
    private Cookie c     = new Cookie();
    private Cash cash    = new Cash();
    private Present pr   = new Present();
    private Time t       = new Time();
    private Burger b     = new Burger();
    private Meat n       = new Meat();
    private Drink l      = new Drink();
    private Chest tr     = new Chest();
    private Apple a      = new Apple();
    private Rock r       = new Rock();
    private Fire fire    = new Fire();
    private Balls balls  = new Balls();
    
    private boolean isGameOver  = false;
    private boolean givePoints  = false;
    Scores scoreObject;

    ImageIcon tempFireworks = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//fireworks1.gif");
    Image fireworks = tempFireworks.getImage();
    
    JFrame resumegame = new JFrame();
    JLabel  resumeprompt = new JLabel("Resume From This Level?");
    JButton Yes = new JButton("Yes");
    JButton No = new JButton("No");
    
    Random rand = new Random();
    int diamondNum = 0;
    int rockTime   = 0;
    int fireTime   = 0;
    int levelTime  = 0;
    int ballTime   = 0;
    int gameOverTime = 0;
    int timeBonus = 0;
    int startTime = 0;
    

    // the main timer that calls the actionPerformed method througout the whole code
    Timer timer;
    
    // audio variables used in playing sounds
    public static AudioPlayer ap;
    public static AudioStream as;
    public static AudioData ad;
    
    String Playername = "";
    String Difficulty = "";
    int Level = 0;
    int score = 0;
    int tempscore;
    int seconds;
    int tempTime = 25;
    int pause  = 0;
    int showDiamonds = 0;
    int pointsTime  = 0;
    int extraTime = 0;
    
    //  used to determine the location to place the hint when the hammer id found
    int xHint  = 0;
    int yHint  = 0;
    
    //  holds the location of the vall to be broken in the presence of a hammer
    int xWallIndex = 0;
    int yWallIndex = 0;
    
    //  holds the time that controls which side the rock fall in level 7
    int rockSide   = 0;
    
    //
    boolean isPaused     = false;
    boolean canMoveD     = true;
    boolean canMoveH     = true;
    boolean showHint     = false;
    boolean showDust     = false;
    boolean gameComplete = false;
    boolean drawExtraTime= false;
    boolean start = true;
    
    BufferedWriter bw; 
    Date date = new Date();
    String Time1,Time2;
    
    
    Font f = new Font("Bookman Old Style",Font.BOLD,25);
    Color color = Color.BLUE;
    
    Font ff = new Font ("Copperplate Gothic Light", Font.BOLD, 40);
    int extraX = 60, extraY = 590;
    
    public MainForm(String Name , String diff)  // import name from first window
    {
        Playername = Name;
        Difficulty = diff;
        addKeyListener(new AL());
        setFocusable(true);
        score = -1;
        tempscore = -1;
        seconds = p.secs;
        
        
        if(Difficulty.equals("Easy"))
        {
            timeBonus = 25;
        }
        else if(Difficulty.equals("Medium"))
        {
            timeBonus = 20;
        }
        else if(Difficulty.equals("Hard"))
        {
            timeBonus = 15;
        }
        
        resumegame.setLayout(new FlowLayout());
        resumegame.setSize(200, 100);
        resumegame.setLocationRelativeTo(null);
        resumegame.add(resumeprompt);
        resumegame.add(Yes);
        resumegame.add(No);
        
        Yes.addActionListener(this);
        No.addActionListener(this);
        
        timer = new Timer(25 , this);
        timer.start();
       
        
    }
    
    // plays music given the wav path as a string
    public static void music(String x)    
    {
         ap = AudioPlayer.player;
  
        try
        {
          as = new AudioStream(new FileInputStream(x));      
        }
        catch(Exception e)
        {
            System.out.print(e.toString());
        }
        ap.start(as);
    }
    
    // checks collisions between object of all classes and allocates
    public void checkCollisions()
     {
         if((p.px == r.x) && (p.py == r.y) && r.isVisible)
         {
             
             ap.stop(as);
             isGameOver = true;
             music("C://Hlohlomi Softwares//Maze Game//Files//stone.wav");
         }
         if((p.px == l.x) && (p.py == l.y)&&(l.isVisible))
         {
             l.isVisible = false;
             score += l.points;
             music("C://Hlohlomi Softwares//Maze Game//Files//drink.wav");
         }
         if((p.px == n.x) && (p.py == n.y)&&(n.isVisible))
         {
             n.isVisible = false;
             score += n.points;
             music("C://Hlohlomi Softwares//Maze Game//Files//chip.wav");
         }
         if((p.px == cash.x) && (p.py == cash.y)&&(cash.isVisible))
         {
             cash.isVisible = false;
             score += cash.points;
             music("C://Hlohlomi Softwares//Maze Game//Files//cash.wav");
         }
         if((p.px == muf.x) && (p.py == muf.y)&&(muf.isVisible))
         {
             muf.isVisible = false;
             score += muf.points;
             music("C://Hlohlomi Softwares//Maze Game//Files//apple.wav");
         }
         if((p.px == b.x) && (p.py == b.y)&&(b.isVisible))
         {
             b.isVisible = false;
             score += b.points;
             music("C://Hlohlomi Softwares//Maze Game//Files//apple.wav");
         }
         if((p.px == a.x) && (p.py == a.y)&&(a.isVisible))
         {
             a.isVisible = false;
             score += a.points;
             music("C://Hlohlomi Softwares//Maze Game//Files//apple.wav");
         }
         if((p.px == t.x) && (p.py == t.y)&&(t.isVisible))
         {
             t.isVisible = false;
             seconds += timeBonus;
             drawExtraTime = true;
             
         }
         if((p.px == c.x) && (p.py == c.y)&&(c.isVisible))
         {
             c.isVisible = false;
             score += c.points;
             music("C://Hlohlomi Softwares//Maze Game//Files//biscuit.wav");
         }
         if((p.px == fire.x) && (p.py == fire.y)&&(fire.isVisible))
         {
             p.isBurned = true;
             isGameOver = true;
             music("C://Hlohlomi Softwares//Maze Game//Files//burning.wav");
         }
         if((p.px == pr.x) && (p.py == pr.y)&&(pr.isVisible))
         {
             pr.isVisible = false;
             givePoints = true;
             score += pr.points;
             tempscore += pr.points;
             canMoveH = false;
             music("C://Hlohlomi Softwares//Maze Game//Files//gift.wav");
         }
         
         if((p.px == tr.x) && (p.py == tr.y)&&(tr.isVisible))
         {
             tr.isVisible = false;
             diamondNum = rand.nextInt(2);
             score += tr.diamondPoints;
             canMoveD = false;
             music("C://Hlohlomi Softwares//Maze Game//Files//D1.wav");
         }
         if(((p.px == balls.x1)&&(p.py == balls.y1))&&balls.is1Visible)
         {
             
             isGameOver = true;
             music("C://Hlohlomi Softwares//Maze Game//Files//stone.wav");
         }
          if(((p.px == balls.x2)&&(p.py == balls.y2))&&balls.is2Visible)
         {
             
             isGameOver = true;
             music("C://Hlohlomi Softwares//Maze Game//Files//stone.wav");
         }
           if(((p.px == balls.x3)&&(p.py == balls.y3))&&balls.is3Visible)
         {
             
             isGameOver = true;
             music("C://Hlohlomi Softwares//Maze Game//Files//stone.wav");
         }
         if((p.px == m.L1x) && (p.py == m.L1y))
         {
             m.L1Complete = true;
             score += 10;
             Level +=1;
             levelTime = 0;
             reset();
             m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map2.txt");
             m.readFile();
             m.closeFile();
             
         }
         if((p.px == m.L2x) && (p.py == m.L2y))
         {
             m.L2Complete = true;
             score += 20;
             Level +=1;
             levelTime = 0;
             reset();
             m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map3.txt");
             m.readFile();
             m.closeFile();

         }
         if((p.px == m.L3x) && (p.py == m.L3y))
         {
             m.L3Complete = true;
             score += 30;
             Level +=1;
             levelTime = 0;
             reset();
             m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map4.txt");
             m.readFile();
             m.closeFile();   
         }
         if((p.px == m.L4x) && (p.py == m.L4y))
         {
             m.L4Complete = true;
             score += 40;
             Level +=1;
             levelTime = 0;
             reset();
             m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map5.txt");
             m.readFile();
             m.closeFile();   
         }
         if((p.px == m.L5x) && (p.py == m.L5y))
         {
             m.L5Complete = true;
             score += 50;
             Level +=1;
             levelTime = 0;
             reset();
             m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map6.txt");
             m.readFile();
             m.closeFile();   
         }
         if((p.px == m.L6x) && (p.py == m.L6y))
         {
             m.L6Complete = true;
             score += 60;
             Level +=1;
             levelTime = 0;
             reset();
             m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map7.txt");
             m.readFile();
             m.closeFile();   
         }
         if((p.px == m.L7x) && (p.py == m.L7y))
         {
             m.L7Complete = true;
             score += 70;
             Level +=1;
             levelTime = 0;
             reset();
             m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map8.txt");
             m.readFile();
             m.closeFile();   
         }
         if((p.px == m.L8x) && (p.py == m.L8y))
         {
             m.L8Complete = true;
             score += 80;
             Level +=1;
             levelTime = 0;
             reset();
             m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map9.txt");
             m.readFile();
             m.closeFile();   
         }
         if((p.px == m.L9x) && (p.py == m.L9y))
         {
             m.L9Complete = true;
             score += 90;
             Level +=1;
             levelTime = 0;
             reset();
             m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map10.txt");
             m.readFile();
             m.closeFile();   
         }
         if((p.px == m.L10x) && (p.py == m.L10y))
         {
             m.L10Complete = true;
             score += 100;
             Level +=1;
             levelTime = 0;
             reset();
             m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map11.txt");
             m.readFile();
             m.closeFile();   
         }
         if((p.px == m.L11x) && (p.py == m.L11y))
         {
             m.L11Complete = true;
             score += 110;
             Level +=1;
             levelTime = 0;
             reset();
             m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map12.txt");
             m.readFile();
             m.closeFile();   
         }
         if((p.px == m.L12x) && (p.py == m.L12y))
         {
             m.L12Complete = true;
             reset();
             score += 120;
             tempscore+=120;
             levelTime = 0;
             gameComplete = true;
             seconds +=10;
             
         }
         
         
         
     }
    public void insertIntoBoard()
    {
        int Temprows = 0;
        int rows = 0;
        int shifting = 8;
        boolean shiftRows = false;
        Time1 = ((Integer.toString(date.getYear()).substring(1)).
                        concat("/"+Integer.toString(date.getMonth()+1))).
                        concat("/"+Integer.toString(date.getDate()));
        Time2 = (Integer.toString(date.getHours()).concat(":"+Integer.toString(date.getMinutes())));
        
        
        try
        {
           
            bw = new BufferedWriter(new FileWriter("C://Hlohlomi Softwares//Maze Game//Files//Scores.txt" , true));
            
            
               
                
                bw.append(Time1+"\n");
                bw.append(Time2+"\n");
                bw.append(Playername+"\n");
                bw.append(Integer.toString(Level)+"\n");
                bw.append(Integer.toString(score+1)+"\n");
                
                bw.newLine();
                
            
            bw.close();
            System.out.print("Written to file");
        }
        catch(Exception e)
        {
            
        }
    }
    
    // resets locations of all objects and prepares them for the next map
    public void reset()
    {
             p.px = 1;
             p.py = 1;
             p.tileX = 1;
             p.tileY = 1;
             m.L1x   = 0;
             m.L1y   = 0;
             m.L2x   = 0;
             m.L2y   = 0;
             m.L3x   = 0;
             m.L3y   = 0;
             m.L4x   = 0;
             m.L4y   = 0;
             m.L5x   = 0;
             m.L5y   = 0;
             m.L6x   = 0;
             m.L6y   = 0;
             m.L7x   = 0;
             m.L7y   = 0;
             m.L8x   = 0;
             m.L8y   = 0;
             m.L9x   = 0;
             m.L9y   = 0;
             m.L10x   = 0;
             m.L10y   = 0;
             m.L11x   = 0;
             m.L11y   = 0;
             m.L12x   = 0;
             m.L12y   = 0;
             m.L13x   = 0;
             m.L13y   = 0;
             m.L14x   = 0;
             m.L14y   = 0;
             m.L15x   = 0;
             m.L15y   = 0;
             c.x     = 0;
             c.y     = 0;
             c.isVisible = true;
             a.x     = 0;
             a.y     = 0;
             a.isVisible = true;
             tr.x     = 0;
             tr.y     = 0;
             tr.isVisible = true;
             pr.x    = 0;
             pr.y    = 0;
             pr.isVisible = true;
             cash.x  = 0;
             cash.y  = 0;
             cash.isVisible = true;
             b.x     = 0;
             b.y     = 0;
             b.isVisible = true;
             n.x     = 0;
             n.y     = 0;
             n.isVisible = true;
             t.x     = 0;
             t.y     = 0;
             t.isVisible = true;
             l.x     = 0;
             l.y     = 0;
             l.isVisible = true;
             muf.x   = 0;
             muf.y   = 0;
             muf.isVisible = true;
             r.isVisible = false;
    }
       
    // returs player details, name, level, score to the score form
    public void getScores()
    {
        scoreObject = new Scores(Playername , Level , score+1);
        scoreObject.setLocation(360,100);
        scoreObject.setVisible(true); 
        
        
    }
    //  method called to re structure the board after moving and collisions
    public void actionPerformed(ActionEvent e)
    {      
          if(isGameOver && !gameComplete)
          {
              
              tempscore = score;
              timer.stop();
              insertIntoBoard();
              getScores();
              
              System.out.print("In gameOver \n");
          }
           if(seconds == 0 && !gameComplete)
           {
               timer.stop();
               resumegame.setVisible(true);
               
               
               if(e.getSource() == Yes)
               {
                  resumegame.dispose();
                  timer.start();
                  isGameOver = false;
                  seconds = 15;
                  score = -1;
                  tempscore = -1;
                  resumeGame();
                  
               }
             if(e.getSource() == No)
             {
                 resumegame.setVisible(false);
                 timer.stop();
                 insertIntoBoard();
                 getScores();
                 
             }
               
           }
           else
           {
               checkCollisions();
               repaint();
               
               
               if(m.L6Complete && !m.L7Complete)
               {
                   prepareStones();
               }
               
               if(m.L3Complete && !m.L4Complete)
               { 
                   prepareFire();
               }
               else
               {
                   fire.isVisible = false;
               }
               if(m.L7Complete && !m.L8Complete)
               {
                   ballTime+=25;
                   prepareBalls();
               }
               if(m.L8Complete)
               {
                   balls.is1Visible = false;
                   balls.is2Visible = false;
                   balls.is3Visible = false;
               }
           }
        
    }
    
    public void prepareBalls()
    {
        
        if(ballTime>1000 && ballTime< 2000 && balls.x1>-1)
        {
 
            balls.is1Visible = true;
            balls.move1();
            
        }

        if(ballTime>2000 && ballTime< 3000&& balls.x2>-1)
        {
            balls.is1Visible = false;
            balls.is2Visible = true;
            balls.move2();

        }
        if(ballTime>3000 && ballTime< 4000&& balls.x3>-1)
        {
            balls.is2Visible = false;
            balls.is3Visible = true;
            balls.move3();

        }
      
        if(ballTime >= 3450)
        {
            ballTime = 0;
            balls.x1 = 18;
            balls.x2 = 18;
            balls.x3 = 18;
            balls.is3Visible = false;

        }
        
    }
    public void prepareStones()
    {
        r.isVisible = true;
                   
                   if(rockSide == 4000)   // fall right after 4seconds
                   {
                       r.x = 18; 
                       r.y = 1;
                       music("C://Hlohlomi Softwares//Maze Game//Files//falling.wav");
                   }
                   if(rockSide == 8000)   // fall left after 4seconds
                   {
                      r.x = 1; 
                      r.y = 1;
                      rockSide = 0;
                      music("C://Hlohlomi Softwares//Maze Game//Files//falling.wav");
                   }
                   
                   if(rockTime == 200)    // move the stone at 1 block in 200ms
                   {
                       rockTime =0;
                       r.move();    
                   }
    }
    public void prepareFire()
    {
        fire.isVisible = true;
        
                   if(fire.x == 18 && fire.y<16)
                   {
                       if(fireTime == 100)
                       {
                           fire.moveDown();
                           fireTime = 0;
                       }
                   }
                   if(fire.x > 1 && fire.y == 16)
                   {

                           fire.moveLeft();
                           fireTime = 0;
                   }
                   if(fire.x == 1 && fire.y <= 16)
                   {
                       if(fireTime == 100)
                       {
                           fire.moveUp();
                            fireTime = 0;
                       }
                   }
                   if(fire.x <18 && fire.y==1)
                   {

                           fire.moveRight();
                           fireTime = 0;
                   }
                   
              
    }
    public void resumeGame()
    {
        if(Level == 1)
                  {
                      m.L1Complete = true;
                      levelTime = 0;
                      reset();
                      m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map2.txt");
                      m.readFile();
                      m.closeFile();
                  }
                  else if(Level == 2)
                  {

                    m.L2Complete = true;
                    levelTime = 0;
                    reset();
                    m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map3.txt");
                    m.readFile();
                    m.closeFile();
                 }

                 else if(Level == 3)
                 {
                     m.L3Complete = true;

                     levelTime = 0;
                     reset();
                     m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map4.txt");
                     m.readFile();
                     m.closeFile();
                 }
                  else if(Level == 4)
                 {
                     m.L4Complete = true;

                     levelTime = 0;
                     reset();
                     m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map5.txt");
                     m.readFile();
                     m.closeFile();
                 }
                  else if(Level == 5)
                 {
                     m.L5Complete = true;

                     levelTime = 0;
                     reset();
                     m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map6.txt");
                     m.readFile();
                     m.closeFile();
                 }
                   else if(Level == 6)
                 {
                     m.L6Complete = true;

                     levelTime = 0;
                     reset();
                     m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map7.txt");
                     m.readFile();
                     m.closeFile();
                 }
                   else if(Level == 7)
                 {
                     m.L7Complete = true;

                     levelTime = 0;
                     reset();
                     m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map8.txt");
                     m.readFile();
                     m.closeFile();
                 } else if(Level == 8)
                 {
                     m.L8Complete = true;

                     levelTime = 0;
                     reset();
                     m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map9.txt");
                     m.readFile();
                     m.closeFile();
                 }
                   else if(Level == 9)
                 {
                     m.L9Complete = true;

                     levelTime = 0;
                     reset();
                     m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map10.txt");
                     m.readFile();
                     m.closeFile();
                 }
                   else if(Level == 10)
                 {
                     m.L10Complete = true;

                     levelTime = 0;
                     reset();
                     m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map11.txt");
                     m.readFile();
                     m.closeFile();
                 }
                   else if(Level == 11)
                 {
                     m.L11Complete = true;

                     levelTime = 0;
                     reset();
                     m.openFile("C://Hlohlomi Softwares//Maze Game//Files//map12.txt");
                     m.readFile();
                     m.closeFile();
                 }
    }
    public void prepareMap(Graphics g)
    {
        for(int y = 0; y<18;y++)     // draws blocks according to the read file using characters
        {
           for(int x = 0 ; x <20; x++)
           {
              try
              {

                if(m.getMap(x,y).equals("g")||m.getMap(x,y).equals("c")||m.getMap(x,y).equals("m")||
                   m.getMap(x,y).equals("t")||m.getMap(x,y).equals("n")||m.getMap(x,y).equals("p")||
                   m.getMap(x,y).equals("b")||m.getMap(x,y).equals("d")||m.getMap(x,y).equals("l")||
                   m.getMap(x,y).equals("x")||m.getMap(x,y).equals("a"))
                {
                    g.drawImage(m.getGrass(),x*32,y*32,null);
                }
                if(m.getMap(x,y).equals("w"))
                {
                    g.drawImage(m.getWall(),x*32,y*32,null);
                }
                if(m.getMap(x,y).equals("c")&&(c.isVisible))
                {
                    g.drawImage(c.getCookie(),x*32,y*32,null);
                    c.x = x;
                    c.y = y;
                }
                if(m.getMap(x,y).equals("p")&&(pr.isVisible))
                {
                    g.drawImage(pr.getPresent(),x*32,y*32,null);
                    pr.x = x;
                    pr.y = y;
                }
                if(m.getMap(x,y).equals("m")&&(muf.isVisible))
                {
                    g.drawImage(muf.getMuffin(),x*32,y*32,null);
                    muf.x = x;
                    muf.y = y;
                }
                if(m.getMap(x,y).equals("t")&&(t.isVisible))
                {
                    g.drawImage(t.getTime(),x*32,y*32,null);
                    t.x = x;
                    t.y = y;
                }
                if(m.getMap(x,y).equals("l")&&(l.isVisible))
                {
                    g.drawImage(l.getDrink(),x*32,y*32,null);
                    l.x = x;
                    l.y = y;
                    
                }
                if(m.getMap(x,y).equals("a")&&(a.isVisible))
                {
                    g.drawImage(a.getApple(),x*32,y*32,null);
                    a.x = x;
                    a.y = y;
                    
                }
                if(m.getMap(x,y).equals("n")&&(n.isVisible))
                {
                    g.drawImage(n.getMeat(),x*32,y*32,null);
                    n.x = x;
                    n.y = y;
                }
                if(m.getMap(x,y).equals("d")&&(cash.isVisible))
                {
                    g.drawImage(cash.getcash(),x*32,y*32,null);
                    cash.x = x;
                    cash.y = y;
                }
                if(m.getMap(x,y).equals("b")&&(b.isVisible))
                {
                    g.drawImage(b.getBurger(),x*32,y*32,null);
                    b.x = x;
                    b.y = y;
                }
                if(m.getMap(x,y).equals("x")&&(tr.isVisible))
                {
                    g.drawImage(tr.getChest(),x*32,y*32,null);
                    tr.x = x;
                    tr.y = y;
                }
                if(m.getMap(x,y).equals("1"))
                {
                    g.drawImage(m.L1,x*32,y*32,null);
                    m.L1x = x;
                    m.L1y = y;
                }
                if(m.getMap(x,y).equals("2"))
                {
                    g.drawImage(m.L2,x*32,y*32,null);
                    m.L2x = x;
                    m.L2y = y;
                }
                if(m.getMap(x,y).equals("3"))
                {
                    g.drawImage(m.L3,x*32,y*32,null);
                    m.L3x = x;
                    m.L3y = y;
                }
                if(m.getMap(x,y).equals("4"))
                {
                    g.drawImage(m.L4,x*32,y*32,null);
                    m.L4x = x;
                    m.L4y = y;
                }
                if(m.getMap(x,y).equals("5"))
                {
                    g.drawImage(m.L5,x*32,y*32,null);
                    m.L5x = x;
                    m.L5y = y;
                }
                if(m.getMap(x,y).equals("6"))
                {
                    g.drawImage(m.L6,x*32,y*32,null);
                    m.L6x = x;
                    m.L6y = y;
                }
                if(m.getMap(x,y).equals("7"))
                {
                    g.drawImage(m.L7,x*32,y*32,null);
                    m.L7x = x;
                    m.L7y = y;
                }
                if(m.getMap(x,y).equals("8"))
                {
                    g.drawImage(m.L8,x*32,y*32,null);
                    m.L8x = x;
                    m.L8y = y;
                }
                if(m.getMap(x,y).equals("9"))
                {
                    g.drawImage(m.L9,x*32,y*32,null);
                    m.L9x = x;
                    m.L9y = y;
                }
                if(m.getMap(x,y).equals("0"))
                {
                    g.drawImage(m.L10,x*32,y*32,null);
                    m.L10x = x;
                    m.L10y = y;
                }
                if(m.getMap(x,y).equals("!"))
                {
                    g.drawImage(m.L11,x*32,y*32,null);
                    m.L11x = x;
                    m.L11y = y;
                }
                if(m.getMap(x,y).equals("@"))
                {
                    g.drawImage(m.L12,x*32,y*32,null);
                    m.L12x = x;
                    m.L12y = y;
                }
                
            }
            catch (Exception e)
            {
            }
                    
          }
        }
    }
    public void writeLevel(Graphics g)
    {
        if(m.L1Complete&& levelTime<=1000)
        {
            g.drawString("L E V E L 2", 170, 310);
        }
        if(m.L2Complete&& levelTime<=1000)
        {
            m.L1Complete = false;
            g.drawString("L E V E L 3", 170, 310);
        }
        if(m.L3Complete&& levelTime<=1000)
        {
            m.L2Complete = false;
            g.drawString("L E V E L 4", 170, 310);
        }
        if(m.L4Complete&& levelTime<=500)
        {
            m.L3Complete = false;
            g.drawString("L E V E L 5", 170, 310);
        }
        if(m.L5Complete&& levelTime<=1000)
        {
            m.L4Complete = false;
            g.drawString("L E V E L 6", 170, 310);
        }
        if(m.L6Complete&& levelTime<=1000)
        {
            m.L5Complete = false;
            g.drawString("L E V E L 7", 170, 310);
        }
        if(m.L7Complete&& levelTime<=1000)
        {
            m.L6Complete = false;
            g.drawString("L E V E L 8", 170, 310);
        }
        if(m.L8Complete&& levelTime<=1000)
        {
            m.L7Complete = false;
            g.drawString("L E V E L 9", 170, 310);
        }
        if(m.L9Complete&& levelTime<=1000)
        {
            m.L8Complete = false;
            g.drawString("L E V E L 10", 170, 310);
        }
        
        if(m.L10Complete&& levelTime<=1000)
        {
            m.L9Complete = false;
            g.drawString("L E V E L 11", 170, 310);
        }
        
        if(m.L11Complete&& levelTime<=1000)
        {
            m.L10Complete = false;
           // g.drawString("L E V E L 12", 170, 310);
        }
        
        if(m.L12Complete&& levelTime<=1000)
        {
            m.L11Complete = false;
        }
        
    
        levelTime +=25;
    }
    // to enable motion using predetermined keys
    public class AL extends KeyAdapter
    {
        public void keyPressed(KeyEvent e)
        {
          int keycode = e.getKeyCode();
          try
          {
            if((keycode == KeyEvent.VK_DOWN  || keycode == KeyEvent.VK_X)&& 
                    !isPaused && canMoveH && canMoveD && !p.isBurned)  // moving now
            {
                if(!m.getMap(p.getTileX() , p.getTileY()+1).equals("w"))   // stop motion via walls
                {
                   p.move(0 ,1);
                   showHint = false;
                }

            }
            if((keycode == KeyEvent.VK_UP || keycode == KeyEvent.VK_W)&&
                    !isPaused && canMoveH && canMoveD && !p.isBurned)
            {
                if(!m.getMap(p.getTileX() , p.getTileY()-1).equals("w"))
                {
                   p.move(0 ,-1);
                   showHint = false;
                }
                    
            }
            if((keycode == KeyEvent.VK_LEFT  || keycode == KeyEvent.VK_A)
                    && !isPaused && canMoveH && canMoveD && !p.isBurned)
            {
                if(!m.getMap(p.getTileX()-1 , p.getTileY()).equals("w"))
                {
                   p.move(-1,0);
                   showHint = false;
                }
            }
            if((keycode == KeyEvent.VK_RIGHT || keycode == KeyEvent.VK_D)
                    && !isPaused && canMoveH && canMoveD && !p.isBurned)
            {
                if(!m.getMap(p.getTileX()+1 , p.getTileY()).equals("w"))
                {
                   p.move(1,0);
                   showHint = false;
                }

            }
            if(keycode == KeyEvent.VK_SPACE && !gameComplete)       // pause the game
            {
                if(pause == 0)      //pause
                {
                    isPaused = true;
                    pause++;
                    
                }
                else if(pause == 1)  //resume
                {
                    isPaused = false;
                    timer.start();
                    pause = 0;           //reset to cater for the next pause
                }
            }
            
          }
          catch(Exception u){}
        }
        public void keyReleased(KeyEvent e)
        {
            
        }
        public void keyTyped(KeyEvent e)
        {
            
        }
    }
    
     public void paint(Graphics g)    // responsible for evry image and string on the window
    {                                 // also refreshed in intervals of the timer
        super.paint(g);
        
        prepareMap(g);
        g.drawImage(p.getPlayer(),p.px *32, p.py * 32,null);
        g.setFont(f);
        g.setColor(color);
        g.drawString("TIME  :", 50, 605);
        g.drawString("SCORE :", 350, 605);
        g.drawString("sec", 230, 605);
        
        if(tempscore<=score)
        {
            tempscore +=1;  
        }

        g.drawString(Integer.toString(tempscore), 500, 605);
        
        if(tempTime != 1000)
        {
            tempTime +=25;
        }
        else if(tempTime == 1000)
        {
            seconds -= 1;
            tempTime = 0;
            
        }
        if(start)
        { 
            startTime +=25;
            
            if(startTime<1000)
            {
                g.drawString(Integer.toString(seconds) , 170 , 605);
                Font font = new Font("Curlz MT", Font.BOLD , 60);
                g.setFont(font);
                g.setColor(Color.white);
               g.drawString("S T A R T", 190, 300);
            }
            else
            {
                start = false;
                startTime = 0;
            }
        }
        if(drawExtraTime)
        {
            extraTime+=25;
            extraX+=8;
            extraY-=8;
            if(extraTime<250)
            {
                g.setFont(ff);
                g.setColor(Color.white);
                g.drawString("EXTRA TIME ", extraX, extraY);
            }
            else if(extraTime >=250 && extraTime < 500)
            {
                ff = new Font ("Copperplate Gothic Light", Font.BOLD, 30);
                g.setFont(ff);
                g.setColor(Color.white);
                g.drawString("EXTRA TIME ", extraX, extraY);
            }
            else if(extraTime >=500 && extraTime <= 750)
            {
                ff = new Font ("Copperplate Gothic Light", Font.BOLD, 20);
                g.setFont(ff);
                g.setColor(Color.white);
                g.drawString("EXTRA TIME ", extraX, extraY);
            }
            else if(extraTime >750)
            {
                drawExtraTime = false;
                extraTime = 0;
                extraX = 60;
                extraY = 590;
            }
        }
        g.drawString(Integer.toString(seconds) , 170 , 605);
        Font font = new Font("Curlz MT", Font.BOLD , 60);
        g.setFont(font);
        g.setColor(Color.white);
        if(isPaused)
        { 
            g.drawString("P A U S E D", 170, 310);
            timer.stop();
        }
     
        writeLevel(g);
        if(gameComplete && levelTime <=10000)
        {
            canMoveD = false;
            g.drawString("W O N D E R F U L", 90, 310);
            g.drawImage(fireworks,220,310,null);
            g.drawImage(fireworks,110,110,null);
            g.drawImage(fireworks,310,110,null);
        }
        else if(gameComplete && levelTime >10000)
        {
            levelTime = 0;
            timer.stop();
            insertIntoBoard();
            scoreObject = new Scores(Playername , Level ,score+1);
            scoreObject.setLocation(360,100);
            scoreObject.setVisible(true); 
            System.out.print("In Wonderful \n");
        }
        
        if(givePoints && pointsTime <= 2000)
        {
            Font newfont = new Font("Curlz MT", Font.BOLD , 40);
            g.setFont(newfont);
            g.setColor(Color.white);
            g.drawString("+ 1000", p.px * 32, (p.py -1)*32);
            pointsTime +=25;
            canMoveH = true;
        }
        else
        {
            givePoints = false;
            pointsTime = 0;
            
        }
        
        if(!canMoveD && showDiamonds <2000 && tr.diamondIsVisible)  // drawing the diamonds after finding a treasure chest
        {                                                           // for 4 seconds
            
            g.drawImage(tr.getDiamonds(diamondNum),tr.x * 32,tr.y* 32-32, this);
        }
        else if(!canMoveD && showDiamonds == 2000 && tr.diamondIsVisible)
        {
            canMoveD = true;                                         // person can now move after diamond display
            showDiamonds = 0;                                        // reset the time
        }
        if(!canMoveD && showDiamonds < 2000)
        {
            showDiamonds+=25;                                       // accumulates timed each time timer refreshes evry 25ms
        }
        
        if(r.isVisible)
        {
            g.drawImage(r.getRock() , r.x * 32, r.y * 32 , null);
            rockTime += 25;
            rockSide += 25;
        } 
        if(fire.isVisible) 
        {
            g.drawImage(fire.fire , fire.x * 32, fire.y * 32 , null);
            fireTime +=25;
        }
        if(balls.is1Visible)
        {
            g.drawImage(balls.balls[0] ,balls.x1 * 32, balls.y1 *32, null);
        }
        if(balls.is2Visible)
        {
            g.drawImage(balls.balls[1] ,balls.x2 * 32 , balls.y2 * 32, null);
        }
        if(balls.is3Visible)
        {
            g.drawImage(balls.balls[2] ,balls.x3  * 32, balls.y3 * 32, null);
        }


    }

//******************* BEGIN CLASSES INSTANTIATED IN MAIN CLASS ****************************************//

class Player
{
    public int x , y , tileX, tileY;
    public int px , py;
    public boolean isBurned = false;
    private Image player;
    private int score = 0;
    private int secs = 30;
    private int mins = 0;
    
    public Player()
    {
        //dimensions of tile
        x     = 32;
        y     = 32;
        
        //location of first tile
        tileX = 1;
        tileY = 1;
        
        px = 1;
        py = 1;
        //safely initializing the player picture
        ImageIcon temp = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//you.png");
        player         = temp.getImage(); 
        
        
    }
    
    public void move(int dx , int dy)
    {
        tileX +=dx;
        tileY +=dy;
        
        if(dx == 1)
            px +=1;
        else if(dx == -1)
            px -= 1;
        else if(dy == 1)
            py += 1;
        else if(dy == -1)
            py -= 1;
    }
    public Image getPlayer()
    {
        return player;
    }

    public int getTileX()
    {
        return tileX;
    }
    public int getTileY()
    {
        return tileY;
    }
}

class Map
{
    private Scanner s; 
    public boolean L1Complete , L2Complete , L3Complete , L4Complete, L5Complete,
                    L6Complete , L7Complete , L8Complete , L9Complete ,L10Complete,
                    L11Complete , L12Complete , L13Complete , L14Complete ,L15Complete;    
    public String blocks[] = new String[18];
    
    public Image  wall , grass , L1 , L2 , L3 , L4 , L5 , L6 ,L7 ,L8, L9 ,L10 ,
            L11,L12,L13,L14,L15;
    public int    L1x,L1y,L2x,L2y,L3x,L3y,L4x,L4y,L5x,L5y,L6x,L6y,L7x,L7y,L8x,L8y,L9x,L9y,
            L10x,L10y,L11x,L11y,L12x,L12y,L13x,L13y,L14x,L14y,L15x,L15y;
    
    public Map()
    {
        ImageIcon temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//grass.jpg");
                  grass  = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//wall.jpg");
                  wall   = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//L1.jpg");
                  L1     = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//L2.jpg");
                  L2     = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//L3.jpg");
                  L3     = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//L4.jpg");
                  L4     = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//L5.jpg");
                  L5     = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//L6.jpg");
                  L6     = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//L7.jpg");
                  L7     = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//L8.jpg");
                  L8     = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//L9.jpg");
                  L9     = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//L10.jpg");
                  L10    = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//L11.jpg");
                  L11    = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//L12.jpg");
                  L12    = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//L13.jpg");
                  L13    = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//L14.jpg");
                  L14    = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//L15.jpg");
                  L15    = temp.getImage();
                  
                  
        L1Complete = false;
        L2Complete = false;
        L3Complete = false;
        L4Complete = false;
        L5Complete = false;
        L6Complete = false;
        L7Complete = false;
        L8Complete = false;
        L9Complete = false;
        L10Complete= false;
        L11Complete= false;
        L12Complete= false;
        L13Complete= false;
        L14Complete= false;
        L15Complete= false;
        
        
        openFile("C://Hlohlomi Softwares//Maze Game//Files//map1.txt");
        readFile();
        closeFile();
    
    }
    
    public Image getGrass()
    {
        return grass;  
    }
    public Image getWall()
    {
        return wall;  
    }
    
    public String getMap(int x , int y)
    {
        String index =null;
        try
        {
          index = blocks[y].substring(x, x+1);
        }
        catch(Exception e){}
        return index;
    }
    public void openFile(String filename)
    {
        try
        {
            s = new Scanner(new File(filename));
        }
        catch(Exception e)
        {
           
        }
        
    }
    public void closeFile()
    {
        s.close();    
    }
    public void  readFile()
    {
        while(s.hasNext())
        {
            for(int i = 0; i<18;i++)
            {
                blocks[i] = s.next();
            }
        }
        
    }
}

class Cookie
{
    Image cookie;
    public int x = 0;
    public int y =0;
    public boolean isVisible = true;
    private int points = 20;
    
    public Cookie()
    {
        ImageIcon temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//cookie.png");
                  cookie = temp.getImage();
    }
    
    public Image getCookie()
    {
        return cookie;
    }
}

class Muffin
{
    Image muffin;
    public int x = 0;
    public int y =0;
    public boolean isVisible = true;
    private int points = 20;
    
    public Muffin()
    {
        ImageIcon temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//muffin.png");
                  muffin = temp.getImage();
    }
    
    public Image getMuffin()
    {
        return muffin;
    }
}

class Cash
{
    Image cash;
    public int x = 0;
    public int y =0;
    public boolean isVisible = true;
    private int points = 30;
    public Cash()
    {
        ImageIcon temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//cash.png");
                  cash = temp.getImage();
    }
    
    public Image getcash()
    {
        return cash;
    }
}

class Time
{
    Image time;
    public int x = 0;
    public int y =0;
    public boolean isVisible = true;
    
    
    public Time()
    {
        ImageIcon temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//time.png");
                  time   = temp.getImage();
    }
    
    public Image getTime()
    {
        return time;
    }
    
}

class Drink
{
    private Image drink;
    public int x = 0;
    public int y =0;
    public boolean isVisible = true;
    private int points = 25;
    public Drink()
    {
        ImageIcon temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//drink.png");
                  drink = temp.getImage();
    }
    public Image getDrink()
    {
        return drink;
    }
    
}

class Burger
{
    Image burger;
    public int x = 0;
    public int y =0;
    public boolean isVisible = true;
    private int points = 35;
    public Burger()
    {
        ImageIcon temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//burger.png");
                  burger = temp.getImage();
    }
    
    public Image getBurger()
    {
        return burger;
    }
}

class Apple
{
    Image apple;
    public int x = 0;
    public int y =0;
    public boolean isVisible = true;
    private int points = 30;
    public Apple()
    {
        ImageIcon temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//apple.png");
                  apple = temp.getImage();
    }
    
    public Image getApple()
    {
        return apple;
    }
}

class Present
{
    Image present ;
    public int x = 0;
    public int y =0;
    public boolean isVisible        = true;

    
    public int points = 1000;
    public Present()
    {
        ImageIcon temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//present.png");
                  present = temp.getImage();
                  
                  

    }
    
    public Image getPresent()
    {
        return present;
    }
 
}

class Fire
{
    Image fire;
    public int x = 18;
    public int y =1;
    public boolean isVisible = false;

    public Fire()
    {
        ImageIcon temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//fire.png");
                  fire   = temp.getImage();
    }
    
    public void moveLeft()
    {
        x--;
    }
    public void moveRight()
    {
        x++;
    }
    public void moveUp()
    {
        y--;
    }
    public void moveDown()
    {
        y++;
    }
}

class Meat
{
    Image meat;
    public int x = 0;
    public int y =0;
    public boolean isVisible = true;
    private int points = 35;
    public Meat()
    {
        ImageIcon temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//meat.png");
                  meat   = temp.getImage();
    }
    
    public Image getMeat()
    {
        return meat;
    }
}

class Rock
{
    Image rock;
    public int x = 1;
    public int y = 2;
    public boolean isVisible = false;
    
    public Rock()
    {
        ImageIcon temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//rock.png");
                  rock   = temp.getImage();
    }
    
    public Image getRock()
    {
        return rock;
    }
    public void move()
    {
        if(y<16)
          y +=1;
    }
}

class Chest
{
    Image chest ,  diamond1, diamond2,diamond3;
    public int x = 0;
    public int y =0;
    public boolean isVisible = true;
    public boolean diamondIsVisible  = true;
    private int points = 35;
    private int diamondPoints = 100;
    public Chest()
    {
        ImageIcon temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//chest.png");
                  chest   = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//D1.png");
                  diamond1 = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//D2.png");
                  diamond2 = temp.getImage();
                  
                  temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//D3.png");
                  diamond3 = temp.getImage();
    }
    
    public Image getChest()
    {
        return chest;
    }
    public Image getDiamonds(int index)
    {
        Image Diamonds[] =  new Image[3];
        Diamonds[0] = diamond1;
        Diamonds[1] = diamond2;
        Diamonds[2] = diamond3;
        
        return Diamonds[index];
    }
   
  }

class Balls
{
    Image balls[] = new Image[3];
    String ballnames [] = new String[3];
    public int x1 = 18;
    public int x2 = 18;
    public int x3 = 18;
    public int y1 = 4;
    public int y2 = 7;
    public int y3 = 10;
    
    public boolean is1Visible = false;
    public boolean is2Visible = false;
    public boolean is3Visible = false;
    Random rand = new Random();
    int x = 0;
    
    public Balls()
    {
        ballnames[0] = "ball1.png";
        ballnames[1] = "ball2.png";
        ballnames[2] = "ball3.png";
        ImageIcon temp   = new ImageIcon("C://Hlohlomi Softwares//Maze Game//Files//ball2.png");
        for(int i =0 ; i<3 ; i++)
        {
                  balls[i]   = temp.getImage();
        }
        
    }
    public void setBall()
    {
        x = rand.nextInt(2);
        ImageIcon temp   = new ImageIcon(ballnames[x]);
        for(int i =0 ; i<3 ; i++)
        {
                  balls[i]   = temp.getImage();
        }
        
    }
    
    public void move1()
    {
        x1--;
    }
    public void move2()
    {
        x2--;
    }
    public void move3()
    {
        x3--;
    }
  }

}