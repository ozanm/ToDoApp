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

void setup() {
  size(700, 700);
  
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

void draw() {
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
  text("ToDo App", width / 2, height / 6.5);
  
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
void mousePressed() {
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

JFrame get_frame() {
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

void mouseDragged() {
  float dx = mouseX - pmouseX;
  float dy = mouseY - pmouseY;
  framePosition.x += (int)dx;
  framePosition.y += (int)dy;
  get_frame().setLocation(framePosition.x, framePosition.y);
}
