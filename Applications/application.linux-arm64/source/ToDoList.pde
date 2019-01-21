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
  
  void setUpMainContents() {
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
  int[] getValidRanges(String[] reader) {
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
  
  void addItem(String item, String status) {
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
  
  void display(float x, float y, float w) {
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
      listBounds[i][0] = x + 2.5;
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
  
  boolean inBounds(float x, float y, int x_, int y_, int w_, int h_) {
    if(x > (x_ - (w_ / 2)) && x < (x_ + (w_ / 2))) {
      if(y > (y_ - (h_ / 2)) && y < (y_ + (h_ / 2))) {
        return true;
      }
    }
    return false;
  }
  
  void userChangedItemStatus(float x, float y) {
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
  
  void changeItemStatus(String[] item, String status) {
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
  
  void removeItemAt(int index) {
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
