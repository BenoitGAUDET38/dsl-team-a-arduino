// Wiring code generated from an ArduinoML model
// Application name: Change states with one button

long debounce = 200;

enum STATE {led, snooze, none};
STATE currentState = none;

boolean button1BounceGuard = false;
long button1LastDebounceTime = 0;
#include <LiquidCrystal.h>
LiquidCrystal lcd(2,3,4,5,6,7,8); // screen [LCD Actuator]

long timerSinceNewState = millis();

void setup(){
  pinMode(9, INPUT);  // button1 [Sensor]
  lcd.begin(16,2); // screen [Actuator]
  pinMode(11, OUTPUT); // snooze1 [Actuator]
  pinMode(12, OUTPUT); // led1 [Actuator]
}

void loop() {
	switch(currentState){
		case led:
			digitalWrite(12,HIGH);
			digitalWrite(11,LOW);
			lcd.setCursor(0,0);
			lcd.print("Led : ON");
			lcd.setCursor(0,1);
			lcd.print("Buzzer : OFF");
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && button1BounceGuard) {
				button1LastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = snooze;
				lcd.clear();
			}
		break;
		case snooze:
			digitalWrite(12,LOW);
			digitalWrite(11,HIGH);
			lcd.setCursor(0,0);
			lcd.print("Led : OFF");
			lcd.setCursor(0,1);
			lcd.print("Buzzer : ON");
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && button1BounceGuard) {
				button1LastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = none;
				lcd.clear();
			}
		break;
		case none:
			digitalWrite(12,LOW);
			digitalWrite(11,LOW);
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && button1BounceGuard) {
				button1LastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = led;
				lcd.clear();
			}
		break;
	}
}
