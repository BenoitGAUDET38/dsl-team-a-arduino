// Wiring code generated from an ArduinoML model
// Application name: Temporal Transition

long debounce = 200;

enum STATE {on, off};
STATE currentState = off;

boolean button1BounceGuard = false;
long button1LastDebounceTime = 0;

boolean button2BounceGuard = false;
long button2LastDebounceTime = 0;

long timerSinceNewState = millis();

void setup(){
  pinMode(9, INPUT);  // button1 [Sensor]
  pinMode(10, INPUT);  // button2 [Sensor]
  pinMode(11, OUTPUT); // led1 [Actuator]
  pinMode(12, OUTPUT); // led2 [Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(11,HIGH);
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && button1BounceGuard) {
				button1LastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = off;
			}
		break;
		case off:
			digitalWrite(11,LOW);
			if (millis() - timerSinceNewState > 120000) {
				timerSinceNewState = millis();
				currentState = on;
			}
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && millis() - timerSinceNewState > 800 && button1BounceGuard) {
				button1LastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = on;
			}
			button2BounceGuard = millis() - button2LastDebounceTime > debounce;
			if (digitalRead(10) == HIGH && millis() - timerSinceNewState > 5200 && button2BounceGuard) {
				button2LastDebounceTime = millis();
				digitalWrite(12,HIGH);
				timerSinceNewState = millis();
				currentState = on;
			}
		break;
	}
}
