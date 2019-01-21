import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.awt.*; 
import java.lang.reflect.Field; 
import java.awt.Frame; 
import java.awt.Dimension; 
import java.awt.Rectangle; 
import java.awt.Toolkit; 
import java.awt.geom.RoundRectangle2D; 
import java.awt.Component; 
import java.awt.Point; 
import javax.swing.JFrame; 
import javax.swing.SwingUtilities; 
import javax.swing.JRootPane; 
import javax.swing.plaf.basic.BasicInternalFrameUI; 
import javax.swing.JOptionPane; 
import javax.swing.JPanel; 
import javax.swing.JLabel; 
import javax.swing.JPasswordField; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ToDoApp extends PApplet {





















///////////////////////////////////////////////////////////////////////////////////

boolean loggedInState = false;
int touchCounter = 0;
int backgroundSize = 300;


PImage bg;
PImage log_out_icon;

Account accounts_list;
ToDoList todo_list;

Button createUser;
Button loginUser;
Button createItem;
Button exitApp;
Button[] remove_buttons;

Point framePosition;

public void setup() {
  
  
  PImage main_icon = loadImage("Assets/main_icon.png");
  surface.setIcon(main_icon);
  
  get_frame().removeNotify();
  get_frame().setUndecorated(true);
  get_frame().setShape(new RoundRectangle2D.Double(0, 0, 800, 800, 150, 150));
  get_frame().setPreferredSize(new Dimension(700, 700));
  get_frame().setMinimumSize(new Dimension(700, 700));
  get_frame().pack();
  Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
  framePosition = new Point(dim.width / 2 - get_frame().getSize().width / 2, dim.height / 2 - get_frame().getSize().height / 2);
  get_frame().setLocation(framePosition.x, framePosition.y);
  get_frame().addNotify();
  
  accounts_list = new Account("accounts.txt", "list.txt");
  
  createUser = new Button(width / 2, 325, 250, 35, 66, 244, 226, 100, "Create User", 25);
  loginUser = new Button(width / 2, 380, 250, 35, 66, 244, 226, 100, "Login", 25);
  exitApp = new Button(width - 30, 20, 15, 15, 244, 66, 66, 100, "", 1);
  createItem = new Button(width / 2, 605, 300, 35, 66, 244, 244, 100, "Add Item To List", 25); 
  bg = loadImage("Assets/bg.jpg");
  log_out_icon = loadImage("Assets/log_out_icon.png");
  
  PFont main_font = loadFont("VarelaRound-Regular.vlw");
  textFont(main_font);
}

public void draw() {
  background(255);
  
  // BACKGROUND IMAGE
  imageMode(CORNER);
  image(bg, 0, 0);
  
  // Exit Button
  exitApp.isRounded = true;
  exitApp.display();
  
  // BACKGROUND CONTENTS
  rectMode(CENTER);
  fill(66, 244, 178, 75);
  noStroke();
  rect(width / 2, height / 2, backgroundSize, backgroundSize);
  
  // TITLE
  textAlign(CENTER);
  fill(255);
  textSize(60);
  text("ToDo App", width / 2, height / 6.5f);
  
  if(!loggedInState) {
    // User Account Buttons
    createUser.display();
    loginUser.display();
  } else {
    createUser.isDisplaying = false;
    loginUser.isDisplaying = false;
    backgroundSize = 450;
    createItem.display();
    if(todo_list.main_list.length == 0) {
      fill(255);
      textSize(25);
      textAlign(CENTER);
      text("Looks like you've got nothing\ngoing on, why don't you click that\nbutton down below to\nadd an item", width / 2, height / 2 - 50);
    } else {
      todo_list.display(width / 2, height / 2 - 150, 450);
      remove_buttons = new Button[todo_list.current_list.length];
      for(int i = 0; i < todo_list.current_list.length; i++) {
        remove_buttons[i] = new Button(480, (int)todo_list.listBounds[i][1] + 10, 35, 35, 255, 255, 255, 0, "X", 30);
        remove_buttons[i].setTextColor(244, 66, 66);
        if(todo_list.listBounds[i][1] != 0) {
          remove_buttons[i].display();
        }
      }
    }
    imageMode(CENTER);
    image(log_out_icon, 100, 50, 50, 50);
  }
}

// HANDLE INTERACTION
public void mousePressed() {
  if(exitApp.buttonPressed(mouseX, mouseY) && exitApp.isDisplaying) {
    exit();
  } else if(createUser.buttonPressed(mouseX, mouseY) && createUser.isDisplaying) {
    String username = JOptionPane.showInputDialog("Please enter your username:");
    if(username != "") {
      JPanel panel = new JPanel();
      JLabel label = new JLabel("Please enter your password:");
      JPasswordField password = new JPasswordField(10);
      panel.add(label);
      panel.add(password);
      String[] options = new String[]{ "OK", "Cancel" };
      int option = JOptionPane.showOptionDialog(null, panel, "Password", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
      if(option == 0) {
        if(String.valueOf(password.getPassword()) != null) {
          if(accounts_list.doesContainUsername(username) == false) {
            accounts_list.addUser(username, String.valueOf(password.getPassword()));
            JOptionPane.showMessageDialog(null, "Welcome " + username + " you have been added to the account list\nplease login with your new account credentials");
          } else {
            JOptionPane.showMessageDialog(null, "There's already an account in the system with the same credentials, please try another.");
          }
        } else {
          JOptionPane.showMessageDialog(null, "Uh-Oh, your password can't be nothing!");
        }
      }
    } else {
      JOptionPane.showMessageDialog(null, "Uh-Oh, your username can't be nothing!");
    }
  } else if(loginUser.buttonPressed(mouseX, mouseY) && loginUser.isDisplaying) {
    String username = JOptionPane.showInputDialog("Please enter your username:");
    if(username != "") {
      JPanel panel = new JPanel();
      JLabel label = new JLabel("Please enter your password:");
      JPasswordField password = new JPasswordField(10);
      panel.add(label);
      panel.add(password);
      String[] options = new String[]{ "OK", "Cancel" };
      int option = JOptionPane.showOptionDialog(null, panel, "Password", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
      if(option == 0) {
        if(String.valueOf(password.getPassword()) != null) {
          String[] account = { username, String.valueOf(password.getPassword()) };
          accounts_list.refreshAccountsList();
          if(accounts_list.doesContain(account)) {
            JOptionPane.showMessageDialog(null, "Welcome, " + username + " you have been logged in");
            todo_list = new ToDoList("list.txt", username);
            loggedInState = true;
          } else {
            JOptionPane.showMessageDialog(null, "Uh-Oh, couldn't find that user and password combination");
          }
        } else {
          JOptionPane.showMessageDialog(null, "Uh-Oh, your password can't be nothing!");
        }
      }
    } else if(username != null) {
      JOptionPane.showMessageDialog(null, "Uh-Oh, your username can't be nothing!");
    }
  } else if(createItem != null && createItem.buttonPressed(mouseX, mouseY) && createItem.isDisplaying) {
    String new_item = JOptionPane.showInputDialog("What do you need to do? (Bigger than 6 characters)");
    if(new_item != null) {
      if(new_item.length() > 6) {
        todo_list.addItem(new_item, "ON");
        JOptionPane.showMessageDialog(null, "Great! We added to your todo list :)");
      } else {
        JOptionPane.showMessageDialog(null, "Whoops, less than 6 characters :(");
      }
    } else {
      JOptionPane.showMessageDialog(null, "Uh-Oh, your item can't be nothing!");
    }
  } else if(todo_list != null && todo_list.backList.buttonPressed(mouseX, mouseY) && todo_list.backList.isDisplaying) {
    if(todo_list.currentList != 1) {
      todo_list.currentList -= 1;
      todo_list.setUpMainContents();
    } else {
      JOptionPane.showMessageDialog(null, "This is the first page!");
    }
  } else if(todo_list != null && todo_list.frontList.buttonPressed(mouseX, mouseY) && todo_list.backList.isDisplaying) {
    int highest = 0;
    for(int i = 0; i < todo_list.main_list.length; i++) {
      if(Integer.parseInt(todo_list.main_list[i][2]) > highest) {
        highest = Integer.parseInt(todo_list.main_list[i][2]);
      }
    }
    if(todo_list.currentList != highest) {
      todo_list.currentList += 1;
      todo_list.setUpMainContents();
    } else {
      JOptionPane.showMessageDialog(null, "This is the last page!");
    }
  } else {
    if(remove_buttons != null) {
      for(int i = 0; i < remove_buttons.length; i++) {
        if(remove_buttons[i].buttonPressed(mouseX, mouseY) && remove_buttons[i].isDisplaying) {
          todo_list.removeItemAt(i + 1);
        }
        
        if(i == remove_buttons.length - 1) {
          if(todo_list.inBounds(mouseX, mouseY, width / 2, height / 2, backgroundSize, backgroundSize)) {
            todo_list.userChangedItemStatus(mouseX, mouseY);
          } else {
            if(mouseX > (100 - (50 / 2)) && mouseX < (100 + (50 / 2))) {
              if(mouseY > (50 - (50 / 2)) && mouseY < (50 + (50 / 2))) {
                backgroundSize = 300;
                loggedInState = false;
              }
            }
          }
        }
      }
    }
  }
}

public JFrame get_frame() {
  JFrame frame = null;
  try {
    Field f = ((PSurfaceAWT) surface).getClass().getDeclaredField("frame");
    f.setAccessible(true);
    frame  = (JFrame) (f.get(((PSurfaceAWT) surface)));
  }
  catch(Exception error) {
    println(error);
  }
  return frame;
}

public void mouseDragged() {
  float dx = mouseX - pmouseX;
  float dy = mouseY - pmouseY;
  framePosition.x += (int)dx;
  framePosition.y += (int)dy;
  get_frame().setLocation(framePosition.x, framePosition.y);
}
class Account {
  
  String path;
  String listPath;
  PrintWriter textString;
  String[] textContents;
  String[][] accounts;
  
  Account(String path_, String listPath_) {
    path = path_;
    listPath = listPath_;
    refreshAccountsList();
  }
  
  public void addUser(String username, String password) {
    textString = createWriter(path);
    for(int i = 0; i < textContents.length; i++) {
      textString.println(textContents[i]);
    }
    textString.println("Username: " + username);
    textString.println("Password: " + password);
    textString.flush();
    textString.close();
    
    String[] listFileReader = loadStrings(listPath);
    PrintWriter listFileWriter = createWriter(listPath);
    for(int i = 0; i < listFileReader.length; i++) {
      if(listFileReader[i] != "") {
        listFileWriter.println(listFileReader[i]);
      }
    }
    listFileWriter.println("Username: " + username);
    listFileWriter.println("");
    listFileWriter.println("");
    listFileWriter.flush();
    listFileWriter.close();
  }
  
  public boolean doesContain(String[] contents) {
    refreshAccountsList();
    for(int i = 0; i < accounts.length; i++) {
      if(contents[0].equals(accounts[i][0]) == true && contents[1].equals(accounts[i][1]) == true) {
        return true;
      }
    }
    return false;
  }
  
  public boolean doesContainUsername(String username) {
    refreshAccountsList();
    for(int i = 0; i < accounts.length; i++) {
      if(username.equals(accounts[i][0]) == true) {
        return true;
      }
    }
    return false;
  }
  
  public void refreshAccountsList() {
    textContents = loadStrings(path);
    accounts = new String[textContents.length / 2][2];
    int counter = 0;
    while(counter < accounts.length) {
      accounts[counter][0] = textContents[counter * 2].substring(10);
      accounts[counter][1] = textContents[(counter * 2) + 1].substring(10);
      counter += 2;
    }
  }
}
class Button {
  
  int[] frame = new int[4];
  
  int backgroundColor = color(0);
  int textColor = color(255);
  
  String contents = "";
  int textSize_ = 0;
  
  boolean isRounded = false;
  boolean isDisplaying;
  
  Button(int x, int y, int w, int h, int r, int g, int b, int a, String text_, int textSize__) {
    setFrame(x, y, w, h);
    setBackgroundColor(r, g, b, a);
    setText(text_);
    setTextSize(textSize__);
    
    isDisplaying = false;
  }
  
  public void display() {
    isDisplaying = true;
    fill(backgroundColor);
    noStroke();
    if(isRounded) {
      ellipse(frame[0], frame[1], frame[2], frame[3]);
    } else {
      rectMode(CENTER);
      rect(frame[0], frame[1], frame[2], frame[3]);
    }
    textAlign(CENTER);
    fill(textColor);
    textSize(textSize_);
    text(contents, frame[0], frame[1] + 10);
  }
  
  public void setBackgroundColor(int r, int g, int b, int a) {
    backgroundColor = color(r, g, b, a);
  }
  
  public void setFrame(int x, int y, int w, int h) {
    frame[0] = x;
    frame[1] = y;
    frame[2] = w;
    frame[3] = h;
  }
  
  public void setText(String text_) {
    contents = text_;
  }
  
  public void setTextSize(int size) {
    textSize_ = size;
  }
  
  public void setTextColor(int r, int g, int b) {
    textColor = color(r, g, b);
  }
  
  public boolean buttonPressed(float x, float y) {
    if(x > (frame[0] - (frame[2] / 2)) && x < (frame[0] + (frame[2] / 2))) {
      if(y > (frame[1] - (frame[3] / 2)) && y < (frame[1] + (frame[3] / 2))) {
        return true;
      }
    }
    return false;
  }
}
class ToDoList {
  
  // STATUS: ON, OFF
  
  PrintWriter listFileWriter;
  String[] listFileReader;
  
  String[][] main_list;
  String[][] current_list;
  
  String main_path;
  String main_account;
  
  float[][] listBounds;
  boolean isDisplaying;
  
  Button backList;
  Button frontList;
  int currentList = 1;
  
  ToDoList(String path, String account) {
    main_path = path;
    main_account = account;
    setUpMainContents();
    
    isDisplaying = false;
    listFileReader = loadStrings(main_path);
    int[] range = getValidRanges(listFileReader);
    listBounds = new float[range[1] - range[0]][4];
    
    backList = new Button(235, height / 4 - 20, 205, 35, 277, 210, 252, 100, "<-", 35);
    frontList = new Button(465, height / 4 - 20, 205, 35, 277, 210, 252, 100, "->", 35);
  }
  
  public void setUpMainContents() {
    listFileReader = loadStrings(main_path);
    int[] range = getValidRanges(listFileReader);
    main_list = new String[(range[1] - range[0]) - 1][3];
    for(int i = 0; i < main_list.length; i++) {
      if(listFileReader[i + range[0]].length() > 3) {
        main_list[i][0] = listFileReader[i + range[0]].substring(0, listFileReader[i + range[0]].length() - 3);
        main_list[i][1] = listFileReader[i + range[0]].substring(listFileReader[i + range[0]].length() - 3);
        main_list[i][2] = str((int)(((i + 1) / 7) + 1));
      }
    }
    int amountInCurrentList = 0;
    for(int i = 0; i < main_list.length; i++) {
      if(main_list[i][2].equals(str(currentList))) {
        amountInCurrentList += 1;
      }
    }
    current_list = new String[amountInCurrentList][3];
    int counter = 0;
    for(int i = 0; i < main_list.length; i++) {
      if(main_list[i][2].equals(str(currentList))) {
        current_list[counter] = main_list[i];
        counter += 1;
      }
    }
  }  
  public int[] getValidRanges(String[] reader) {
    int[] range = new int[2];
    for(int i = 0; i < reader.length; i++) {
      if(reader[i].equals("Username: " + main_account)) {
        range[0] = i + 1;
        break;
      }
      if(i == reader.length - 1 && range[0] == 0) {
        println("ERROR: USER " + main_account + " NOT FOUND IN " + main_path + " FILE, STOPPING PROGRAM...");
        exit();
      }
    }
    
    for(int i = range[0]; i != reader.length; i++) {
      if(reader[i].length() > 10) {
        if(reader[i].substring(0, 10) == "Username: ") {
          range[1] = i - 1;
          break;
        }
      } else if(reader[i].length() == 0 || i == reader.length - 1) {
        range[1] = i + 1;
        break;
      } else {
        range[1] = i;
        break;
      }
    }
    
    return range;
  }
  
  public void addItem(String item, String status) {
    int[] range = getValidRanges(listFileReader);
    listFileReader = loadStrings(main_path);
    listFileWriter = createWriter(main_path);
    boolean addedItem = false;
    for(int i = 0; i < listFileReader.length; i++) {
      if(i == range[0] && addedItem == false) {
        listFileWriter.println("-" + item + " " + status);
        addedItem = true;
      }
      listFileWriter.println(listFileReader[i]);
    }
    listFileWriter.flush();
    listFileWriter.close();
    setUpMainContents();
    range = getValidRanges(listFileReader);
    listBounds = new float[range[1] - range[0]][4];
  }
  
  public void display(float x, float y, float w) {
    isDisplaying = true;
    listFileReader = loadStrings(main_path);
    backList.display();
    frontList.display();
    listBounds = new float[current_list.length][4];
    for(int i = 0; i < current_list.length; i++) {
      float yPos = ((y + (50 * i)) + (15 * i)) ;
      if(i == 0) {
        yPos = y;
      }
      if(current_list[i][1].equals("OFF")) {
        fill(151);
      } else {
        fill(255);
      }
      noStroke();
      rectMode(CORNER);
      rect((x / 2) - 40, yPos - 15, w - 25, 50, 15);
      listBounds[i][0] = x + 2.5f;
      listBounds[i][1] =    yPos;
      listBounds[i][2] =  w - 25;
      listBounds[i][3] =      50;
      fill(0);
      textSize(25);
      textAlign(LEFT);
      text(current_list[i][0].substring(1), (x / 2) - 25, yPos + 15);
      stroke(0);
      fill(255);
      ellipse(w + 75, yPos + 10, 40, 40);
      if(current_list[i][1].equals(" ON")) {
        fill(255, 255, 0);
        noStroke();
        ellipse(w + 75, yPos + 10, 25, 25);
      }
    }
  }
  
  public boolean inBounds(float x, float y, int x_, int y_, int w_, int h_) {
    if(x > (x_ - (w_ / 2)) && x < (x_ + (w_ / 2))) {
      if(y > (y_ - (h_ / 2)) && y < (y_ + (h_ / 2))) {
        return true;
      }
    }
    return false;
  }
  
  public void userChangedItemStatus(float x, float y) {
    listFileReader = loadStrings(main_path);
    for(int i = 0; i < current_list.length; i++) {
      if(x > (listBounds[i][0] / 2) - 40 && x < ((listBounds[i][0] / 2) - 40) + (listBounds[i][2] - 25)) {
        if(y > (listBounds[i][1] - 25) && y < (listBounds[i][1] - 25) + listBounds[i][3]) {
          if(current_list[i][1].equals(" ON")) {
            current_list[i][1] = "OFF";
            changeItemStatus(current_list[i], current_list[i][1]);
            break;
          } else if(current_list[i][1].equals("OFF")) {
            current_list[i][1] = " ON";
            changeItemStatus(current_list[i], current_list[i][1]);
            break;
          }
        }
      }
    }
    setUpMainContents();
  }
  
  public void changeItemStatus(String[] item, String status) {
    listFileReader = loadStrings(main_path);
    listFileWriter = createWriter(main_path);
    int startingIndex = 0;
    for(int i = 0; i < listFileReader.length; i++) {
      if(listFileReader[i].length() > item[0].length()) {
        if(item[0].equals(listFileReader[i].substring(0, listFileReader[i].length() - 3))) {
          startingIndex = i;
        }
      }
    }
    for(int i = 0; i < startingIndex; i++) {
      listFileWriter.println(listFileReader[i]);
    }
    for(int i = startingIndex; i < startingIndex + 1; i++) {
      if(trim(status).equals("ON")) {
        listFileWriter.println(item[0] + status);
      } else {
        listFileWriter.println(trim(item[0]) + " " + status);
      }
    }
    for(int i = startingIndex + 1; i < listFileReader.length; i++) {
      listFileWriter.println(listFileReader[i]);
    }
    listFileWriter.flush();
    listFileWriter.close();
    setUpMainContents();
  }
  
  public void removeItemAt(int index) {
    listFileReader = loadStrings(main_path);
    listFileWriter = createWriter(main_path);
    for(int i = 0; i < listFileReader.length; i++) {
      if(i != index) {
        listFileWriter.println(listFileReader[i]);
      }
    }
    listFileWriter.flush();
    listFileWriter.close();
    setUpMainContents();
  }
}
  public void settings() {  size(700, 700); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ToDoApp" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
