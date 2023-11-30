// Wiring code generated from an ArduinoML model
// Application name: Very simple alarm

long debounce = 200;

enum STATE {on, off};
STATE currentState = off;

boolean button1BounceGuard = false;
long button1LastDebounceTime = 0;

void setup(){
  pinMode(9, INPUT);  // button1 [Sensor]
  pinMode(12, OUTPUT); // led1 [Actuator]
  pinMode(13, OUTPUT); // buzzer1 [Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(12,HIGH);
			digitalWrite(13,HIGH);
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			if( digitalRead(9) == LOW && button1BounceGuard) {
				button1LastDebounceTime = millis();
				currentState = off;
			}
		break;
		case off:
			digitalWrite(12,LOW);
			digitalWrite(13,LOW);
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			if( digitalRead(9) == HIGH && button1BounceGuard) {
				button1LastDebounceTime = millis();
				currentState = on;
			}
		break;
	}
}
