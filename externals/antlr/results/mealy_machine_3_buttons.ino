// Wiring code generated from an ArduinoML model
// Application name: mealyMachineThreeButtons

long debounce = 200;

enum STATE {error, on};
STATE currentState = on;

boolean buttonOneBounceGuard = false;
long buttonOneLastDebounceTime = 0;

boolean buttonTwoBounceGuard = false;
long buttonTwoLastDebounceTime = 0;

boolean buttonThreeBounceGuard = false;
long buttonThreeLastDebounceTime = 0;

long timerSinceNewState = millis();

void setup(){
  pinMode(8, INPUT);  // buttonOne [Sensor]
  pinMode(9, INPUT);  // buttonTwo [Sensor]
  pinMode(10, INPUT);  // buttonThree [Sensor]
  pinMode(11, OUTPUT); // ledOne [Actuator]
  pinMode(12, OUTPUT); // ledTwo [Actuator]
}

void loop() {
	switch(currentState){
		case error:
		break;
		case on:
			digitalWrite(11,LOW);
			digitalWrite(12,LOW);
			buttonOneBounceGuard = millis() - buttonOneLastDebounceTime > debounce;
			if (digitalRead(8) == HIGH && buttonOneBounceGuard) {
				buttonOneLastDebounceTime = millis();
				digitalWrite(11,HIGH);
				timerSinceNewState = millis();
				currentState = error;
			}
			buttonTwoBounceGuard = millis() - buttonTwoLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && buttonTwoBounceGuard) {
				buttonTwoLastDebounceTime = millis();
				digitalWrite(12,HIGH);
				timerSinceNewState = millis();
				currentState = error;
			}
			buttonThreeBounceGuard = millis() - buttonThreeLastDebounceTime > debounce;
			if (digitalRead(10) == HIGH && buttonThreeBounceGuard) {
				buttonThreeLastDebounceTime = millis();
				digitalWrite(11,HIGH);
				digitalWrite(12,HIGH);
				timerSinceNewState = millis();
				currentState = error;
			}
		break;
	}
}
