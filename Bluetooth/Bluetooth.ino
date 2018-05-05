char command = 'B';
#define l1 13
#define l2 12
#define l3 11

  void setup()
  {
    Serial.begin(9600);
    pinMode(l1, OUTPUT);
    pinMode(l2, OUTPUT);
    pinMode(l3, OUTPUT);
   
  }

  void loop()
  {
     while(Serial.available() > 0)
    {
      command = ((byte)Serial.read());
      delay(1);
    }
    
    switch(command){
      
      case 'A': digitalWrite(l1,HIGH);
            break;
            
      case 'B': digitalWrite(l1,LOW);
            break;
            
      case 'C': digitalWrite(l2,HIGH);
            break;
      
      case 'D': digitalWrite(l2,LOW);
            break;

      case 'E': digitalWrite(l3,HIGH);
            break;

      case 'F': digitalWrite(l3,LOW);
            break;
      }
                    delay(10);
 }
