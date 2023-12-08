// Wiring code generated from an ArduinoML model
// Application name: sampleApplication

long debounce = 200;

enum STATE {on, off};
STATE currentState = off;

boolean buttonOneBounceGuard = false;
long buttonOneLastDebounceTime = 0;

boolean buttonTwoBounceGuard = false;
long buttonTwoLastDebounceTime = 0;
#include <LiquidCrystal.h>
LiquidCrystal lcd(2,3,4,5,6,7,8); // lcd [LCD Actuator]

long timerSinceNewState = millis();

void setup(){
  pinMode(9, INPUT);  // buttonOne [Sensor]
  pinMode(10, INPUT);  // buttonTwo [Sensor]
  pinMode(11, OUTPUT); // led [Actuator]
  lcd.begin(16,2); // lcd [Actuator]
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
			buttonOneBounceGuard = millis() - buttonOneLastDebounceTime > debounce;
			buttonTwoBounceGuard = millis() - buttonTwoLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && digitalRead(10) == HIGH && millis() - timerSinceNewState > 1000 && buttonOneBounceGuard && buttonTwoBounceGuard) {
				buttonOneLastDebounceTime = millis();
				buttonTwoLastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = on;
				lcd.clear();
			}
		break;
	}
}
