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
  pinMode(11, OUTPUT); // buzzer [Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(12,HIGH);
			digitalWrite(11,HIGH);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if (digitalRead(9) == LOW && buttonBounceGuard) {
				buttonLastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = off;
			}
		break;
		case off:
			digitalWrite(12,LOW);
			digitalWrite(11,LOW);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && buttonBounceGuard) {
				buttonLastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = on;
			}
		break;
	}
}
current state: State{name='off', actions=[Action{value=LOW, actuator=Brick{name='led', pin=12}}, Action{value=LOW, actuator=Brick{name='buzzer', pin=11}}], actionLCDS=[], transitions=[Transition{next=on, conditions=[Condition{sensor=Brick{name='button', pin=9}, value=HIGH, operator=EMPTY}], actions=[]}]}
