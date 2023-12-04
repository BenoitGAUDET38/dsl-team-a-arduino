// Wiring code generated from an ArduinoML model
// Application name: screenButtons

long debounce = 200;

enum STATE {buzzerON, ledON, bothON, off};
STATE currentState = off;

boolean buttonLedBounceGuard = false;
long buttonLedLastDebounceTime = 0;

boolean buttonBuzBounceGuard = false;
long buttonBuzLastDebounceTime = 0;
#include <LiquidCrystal.h>
LiquidCrystal lcd(2,3,4,5,6,7,8); // lcd [LCD Actuator]

void setup(){
  pinMode(9, INPUT);  // buttonLed [Sensor]
  pinMode(10, INPUT);  // buttonBuz [Sensor]
  lcd.begin(16,2); // lcd [Actuator]
  pinMode(11, OUTPUT); // buzzer [Actuator]
  pinMode(12, OUTPUT); // led [Actuator]
}

void loop() {
	switch(currentState){
		case buzzerON:
			digitalWrite(11,HIGH);
			digitalWrite(12,LOW);
			lcd.setCursor(0,0);
			lcd.print("Buzzer : ON");
			lcd.setCursor(0,2);
			lcd.print("Led : OFF");
			buttonLedBounceGuard = millis() - buttonLedLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && buttonLedBounceGuard) {
				buttonLedLastDebounceTime = millis();
				currentState = bothON;
				lcd.clear();
			}
			buttonBuzBounceGuard = millis() - buttonBuzLastDebounceTime > debounce;
			if (digitalRead(10) == HIGH && buttonBuzBounceGuard) {
				buttonBuzLastDebounceTime = millis();
				currentState = off;
				lcd.clear();
			}
		break;
		case ledON:
			digitalWrite(11,LOW);
			digitalWrite(12,HIGH);
			lcd.setCursor(0,0);
			lcd.print("Buzzer : OFF");
			lcd.setCursor(0,1);
			lcd.print("Led : ON");
			buttonLedBounceGuard = millis() - buttonLedLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && buttonLedBounceGuard) {
				buttonLedLastDebounceTime = millis();
				currentState = off;
				lcd.clear();
			}
			buttonBuzBounceGuard = millis() - buttonBuzLastDebounceTime > debounce;
			if (digitalRead(10) == HIGH && buttonBuzBounceGuard) {
				buttonBuzLastDebounceTime = millis();
				currentState = bothON;
				lcd.clear();
			}
		break;
		case bothON:
			digitalWrite(11,HIGH);
			digitalWrite(12,HIGH);
			lcd.setCursor(0,0);
			lcd.print("Buzzer : ON");
			lcd.setCursor(0,1);
			lcd.print("Led : ON");
			buttonLedBounceGuard = millis() - buttonLedLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && buttonLedBounceGuard) {
				buttonLedLastDebounceTime = millis();
				currentState = buzzerON;
				lcd.clear();
			}
			buttonBuzBounceGuard = millis() - buttonBuzLastDebounceTime > debounce;
			if (digitalRead(10) == HIGH && buttonBuzBounceGuard) {
				buttonBuzLastDebounceTime = millis();
				currentState = ledON;
				lcd.clear();
			}
		break;
		case off:
			digitalWrite(12,LOW);
			digitalWrite(11,LOW);
			lcd.setCursor(0,0);
			lcd.print("Buzzer : OFF");
			lcd.setCursor(0,1);
			lcd.print("Led : OFF");
			buttonLedBounceGuard = millis() - buttonLedLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && buttonLedBounceGuard) {
				buttonLedLastDebounceTime = millis();
				currentState = ledON;
				lcd.clear();
			}
			buttonBuzBounceGuard = millis() - buttonBuzLastDebounceTime > debounce;
			if (digitalRead(10) == HIGH && buttonBuzBounceGuard) {
				buttonBuzLastDebounceTime = millis();
				currentState = buzzerON;
				lcd.clear();
			}
		break;
	}
}
