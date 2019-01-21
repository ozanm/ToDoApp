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
  
  void addUser(String username, String password) {
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
  
  boolean doesContain(String[] contents) {
    refreshAccountsList();
    for(int i = 0; i < accounts.length; i++) {
      if(contents[0].equals(accounts[i][0]) == true && contents[1].equals(accounts[i][1]) == true) {
        return true;
      }
    }
    return false;
  }
  
  boolean doesContainUsername(String username) {
    refreshAccountsList();
    for(int i = 0; i < accounts.length; i++) {
      if(username.equals(accounts[i][0]) == true) {
        return true;
      }
    }
    return false;
  }
  
  void refreshAccountsList() {
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
