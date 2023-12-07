// Wiring code generated from an ArduinoML model
// Application name: dualCheckAlarm

long debounce = 200;

enum STATE {on, off};
STATE currentState = off;

boolean buttonOneBounceGuard = false;
long buttonOneLastDebounceTime = 0;

boolean buttonTwoBounceGuard = false;
long buttonTwoLastDebounceTime = 0;

long timerSinceNewState = millis();

void setup(){
  pinMode(8, INPUT);  // buttonOne [Sensor]
  pinMode(9, INPUT);  // buttonTwo [Sensor]
  pinMode(11, OUTPUT); // buzzer [Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(11,HIGH);
			buttonOneBounceGuard = millis() - buttonOneLastDebounceTime > debounce;
			buttonTwoBounceGuard = millis() - buttonTwoLastDebounceTime > debounce;
			buttonTwoBounceGuard = millis() - buttonTwoLastDebounceTime > debounce;
			if (digitalRead(8) == LOW || digitalRead(9) == LOW || digitalRead(9) == LOW && buttonOneBounceGuard && buttonTwoBounceGuard && buttonTwoBounceGuard) {
				buttonOneLastDebounceTime = millis();
				buttonTwoLastDebounceTime = millis();
				buttonTwoLastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = off;
			}
		break;
		case off:
			digitalWrite(11,LOW);
			buttonOneBounceGuard = millis() - buttonOneLastDebounceTime > debounce;
			buttonTwoBounceGuard = millis() - buttonTwoLastDebounceTime > debounce;
			buttonTwoBounceGuard = millis() - buttonTwoLastDebounceTime > debounce;
			if (digitalRead(8) == HIGH && digitalRead(9) == HIGH && digitalRead(9) == HIGH && buttonOneBounceGuard && buttonTwoBounceGuard && buttonTwoBounceGuard) {
				buttonOneLastDebounceTime = millis();
				buttonTwoLastDebounceTime = millis();
				buttonTwoLastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = on;
			}
		break;
	}
}
