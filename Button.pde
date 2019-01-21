class Button {
  
  int[] frame = new int[4];
  
  color backgroundColor = color(0);
  color textColor = color(255);
  
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
  
  void display() {
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
  
  void setBackgroundColor(int r, int g, int b, int a) {
    backgroundColor = color(r, g, b, a);
  }
  
  void setFrame(int x, int y, int w, int h) {
    frame[0] = x;
    frame[1] = y;
    frame[2] = w;
    frame[3] = h;
  }
  
  void setText(String text_) {
    contents = text_;
  }
  
  void setTextSize(int size) {
    textSize_ = size;
  }
  
  void setTextColor(int r, int g, int b) {
    textColor = color(r, g, b);
  }
  
  boolean buttonPressed(float x, float y) {
    if(x > (frame[0] - (frame[2] / 2)) && x < (frame[0] + (frame[2] / 2))) {
      if(y > (frame[1] - (frame[3] / 2)) && y < (frame[1] + (frame[3] / 2))) {
        return true;
      }
    }
    return false;
  }
}
