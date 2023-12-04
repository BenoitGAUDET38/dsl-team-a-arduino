// Wiring code generated from an ArduinoML model
// Application name: screenHelloWorld

long debounce = 200;

enum STATE {on, step, off};
STATE currentState = off;

boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;
#include <LiquidCrystal.h>
LiquidCrystal lcd(2,3,4,5,6,7,8); // lcd [LCD Actuator]

void setup(){
  pinMode(9, INPUT);  // button [Sensor]
  lcd.begin(16,2); // lcd [Actuator]
  pinMode(11, OUTPUT); // buzzer [Actuator]
  pinMode(12, OUTPUT); // led [Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(11,HIGH);
			digitalWrite(11,LOW);
			lcd.setCursor(0,0);
			lcd.print("BuzzerOn");
			lcd.setCursor(0,1);
			lcd.print("LedOff");
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && buttonBounceGuard) {
				buttonLastDebounceTime = millis();
				currentState = step;
				lcd.clear();
			}
		break;
		case step:
			digitalWrite(11,LOW);
			digitalWrite(12,HIGH);
			lcd.setCursor(0,0);
			lcd.print("BuzzerOff");
			lcd.setCursor(0,1);
			lcd.print("LedOn");
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && buttonBounceGuard) {
				buttonLastDebounceTime = millis();
				currentState = off;
				lcd.clear();
			}
		break;
		case off:
			digitalWrite(12,LOW);
			digitalWrite(11,LOW);
			lcd.clear();
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && buttonBounceGuard) {
				buttonLastDebounceTime = millis();
				currentState = on;
				lcd.clear();
			}
		break;
	}
}
