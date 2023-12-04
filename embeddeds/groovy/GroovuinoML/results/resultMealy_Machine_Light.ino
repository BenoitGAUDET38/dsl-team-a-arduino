// Wiring code generated from an ArduinoML model
// Application name: Mealy machine for Arduino

long debounce = 200;

enum STATE {on, error};
STATE currentState = on;

boolean button1BounceGuard = false;
long button1LastDebounceTime = 0;

boolean button2BounceGuard = false;
long button2LastDebounceTime = 0;

long timerSinceNewState = millis();

void setup(){
  pinMode(8, INPUT);  // button1 [Sensor]
  pinMode(9, INPUT);  // button2 [Sensor]
  pinMode(11, OUTPUT); // led1 [Actuator]
  pinMode(12, OUTPUT); // led2 [Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(11,LOW);
			digitalWrite(12,LOW);
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			if (digitalRead(8) == HIGH && button1BounceGuard) {
				button1LastDebounceTime = millis();
				digitalWrite(11,HIGH);
				timerSinceNewState = millis();
				currentState = error;
			}
			button2BounceGuard = millis() - button2LastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && button2BounceGuard) {
				button2LastDebounceTime = millis();
				digitalWrite(12,HIGH);
				timerSinceNewState = millis();
				currentState = error;
			}
		break;
		case error:
		break;
	}
}
