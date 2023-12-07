// Wiring code generated from an ArduinoML model
// Application name: temporalTransition

long debounce = 200;

enum STATE {on, off};
STATE currentState = off;

boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;

long timerSinceNewState = millis();

void setup(){
  pinMode(9, INPUT);  // button [Sensor]
  pinMode(11, OUTPUT); // led [Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(11,HIGH);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && millis() - timerSinceNewState > 5000 && buttonBounceGuard) {
				buttonLastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = off;
			}
			if (millis() - timerSinceNewState > 60000) {
				timerSinceNewState = millis();
				currentState = off;
			}
		break;
		case off:
			digitalWrite(11,LOW);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && millis() - timerSinceNewState > 5000 && buttonBounceGuard) {
				buttonLastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = on;
			}
		break;
	}
}
