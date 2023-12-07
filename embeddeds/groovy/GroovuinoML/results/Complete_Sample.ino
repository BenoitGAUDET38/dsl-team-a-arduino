// Wiring code generated from an ArduinoML model
// Application name: Sample application

long debounce = 200;

enum STATE {on, off};
STATE currentState = off;

boolean button1BounceGuard = false;
long button1LastDebounceTime = 0;

boolean button2BounceGuard = false;
long button2LastDebounceTime = 0;
#include <LiquidCrystal.h>
LiquidCrystal lcd(2,3,4,5,6,7,8); // screen [LCD Actuator]

long timerSinceNewState = millis();

void setup(){
  pinMode(9, INPUT);  // button1 [Sensor]
  pinMode(10, INPUT);  // button2 [Sensor]
  lcd.begin(16,2); // screen [Actuator]
  pinMode(11, OUTPUT); // led1 [Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(11,HIGH);
			lcd.setCursor(0,0);
			lcd.print("Led : ON");
			if (millis() - timerSinceNewState > 10000) {
				digitalWrite(11,LOW);
				timerSinceNewState = millis();
				currentState = off;
				lcd.clear();
			}
		break;
		case off:
			lcd.setCursor(0,0);
			lcd.print("Led : OFF");
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && button1BounceGuard) {
				button1LastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = on;
				lcd.clear();
			}
		break;
	}
}
