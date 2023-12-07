// Wiring code generated from an ArduinoML model
// Application name: simpleAlarm

long debounce = 200;

enum STATE {on, off};
STATE currentState = off;

boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;

long timerSinceNewState = millis();

void setup(){
  pinMode(9, INPUT);  // button [Sensor]
  pinMode(12, OUTPUT); // led [Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(12,HIGH);
			if (millis() - timerSinceNewState > 800) {
				timerSinceNewState = millis();
				currentState = off;
			}
		break;
		case off:
			digitalWrite(12,LOW);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && buttonBounceGuard) {
				buttonLastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = on;
			}
		break;
	}
}
