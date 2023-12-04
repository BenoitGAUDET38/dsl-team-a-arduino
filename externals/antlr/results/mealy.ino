// Wiring code generated from an ArduinoML model
// Application name: mealy

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
				timerSinceNewState = millis();
				currentState = error;
			}
			buttonTwoBounceGuard = millis() - buttonTwoLastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && buttonTwoBounceGuard) {
				buttonTwoLastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = error;
			}
			buttonThreeBounceGuard = millis() - buttonThreeLastDebounceTime > debounce;
			if (digitalRead(10) == HIGH && buttonThreeBounceGuard) {
				buttonThreeLastDebounceTime = millis();
				timerSinceNewState = millis();
				currentState = error;
			}
		break;
	}
}
current state: State{name='on', actions=[Action{value=LOW, actuator=Brick{name='ledOne', pin=11}}, Action{value=LOW, actuator=Brick{name='ledTwo', pin=12}}], actionLCDS=[], transitions=[Transition{next=error, conditions=[Condition{sensor=Brick{name='buttonOne', pin=8}, value=HIGH, operator=EMPTY}], actions=[]}, Transition{next=error, conditions=[Condition{sensor=Brick{name='buttonTwo', pin=9}, value=HIGH, operator=EMPTY}], actions=[]}, Transition{next=error, conditions=[Condition{sensor=Brick{name='buttonThree', pin=10}, value=HIGH, operator=EMPTY}], actions=[]}]}
