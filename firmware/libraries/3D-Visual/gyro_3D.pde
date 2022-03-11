import processing.serial.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
Serial myPort;


String data="";
float roll, pitch,yaw;
float mode = 0;

color teal = color (0,128,100);

void setup() {
  size (800, 600, P3D);
  myPort = new Serial(this, "/dev/cu.SLAB_USBtoUART", 115200); // starts the serial communication
  myPort.bufferUntil('\n');
  //surface.setResizable(true);
}
void draw() {
  //surface.setSize(800, 600);
  translate(width/2, height/2, 0);
  background(10);
  textSize(22);
  if (mode == 0)
  {
    text(" mode: ACTIVE", -100, 265);
  }
  else{
    text(" mode: SLEEP", -100, 265);
  }
  
  // Rotate the object
  rotateX(radians(-pitch));
  rotateZ(radians(roll));
  rotateY(radians(yaw));
  
  // 3D 0bject
  textSize(30);  
  //fill(0, 76, 153);
  fill(teal);
  box (386, 40, 200); // Draw box
  
  textSize(25);
  fill(255, 255, 255);
  
  text("Medistation", -183, 10, 101);
  //delay(10);
  //println("ypr:\t" + angleX + "\t" + angleY); // Print the values to check whether we are getting proper values
  //println("/" + pitch + "/" + roll+"/" + yaw );
}
// Read data from the Serial Port
void serialEvent (Serial myPort) { 
  // reads the data from the Serial Port up to the character '.' and puts it into the String variable "data".
  data = myPort.readStringUntil('\n');
  // if you got any bytes other than the linefeed:
  if (data != null) {
    data = trim(data);
    // split the string at "/"
    String items[] = split(data, '|');
    if (items.length > 1) {
      if (int(items[0]) == 0){
        //--- Roll,Pitch in degrees
        roll = float(items[1]);
        pitch = float(items[2]);
        yaw = float(items[3]);
        mode = float(items[4]);
      }
    }
  }
}
