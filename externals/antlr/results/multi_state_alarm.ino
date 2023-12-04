// Wiring code generated from an ArduinoML model
// Application name: multiStateAlarm

long debounce = 200;

enum STATE {on, step, off};
STATE currentState = off;

boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;

long timerSinceNewState = millis();

void setup(){
  pinMode(9, INPUT);  // button [Sensor]
  pinMode(12, OUTPUT); // led [Actuator]
  pinMode(11, OUTPUT); // buzzer [Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(11,HIGH);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && buttonBounceGuard) {
				buttonLastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = step;
			}
		break;
		case step:
			digitalWrite(11,LOW);
			digitalWrite(12,HIGH);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && buttonBounceGuard) {
				buttonLastDebounceTime = millis();
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
current state: State{name='step', actions=[Action{value=LOW, actuator=Brick{name='buzzer', pin=11}}, Action{value=HIGH, actuator=Brick{name='led', pin=12}}], actionLCDS=[], transitions=[Transition{next=off, conditions=[Condition{sensor=Brick{name='button', pin=9}, value=HIGH, operator=EMPTY}], actions=[]}]}
