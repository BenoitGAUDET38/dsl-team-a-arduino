// Wiring code generated from an ArduinoML model
// Application name: Dual-check alarm

long debounce = 200;

enum STATE {on, off};
STATE currentState = off;

boolean button1BounceGuard = false;
long button1LastDebounceTime = 0;

boolean button2BounceGuard = false;
long button2LastDebounceTime = 0;

void setup(){
  pinMode(9, INPUT);  // button1 [Sensor]
  pinMode(10, INPUT);  // button2 [Sensor]
  pinMode(12, OUTPUT); // buzzer1 [Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(12,HIGH);
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			button2BounceGuard = millis() - button2LastDebounceTime > debounce;
			if (digitalRead(9) == LOW || digitalRead(10) == LOW && button1BounceGuard && button2BounceGuard) {
				button1LastDebounceTime = millis();
				button2LastDebounceTime = millis();
				currentState = off;
			}
		break;
		case off:
			digitalWrite(12,LOW);
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			button2BounceGuard = millis() - button2LastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && digitalRead(10) == HIGH && button1BounceGuard && button2BounceGuard) {
				button1LastDebounceTime = millis();
				button2LastDebounceTime = millis();
				currentState = on;
			}
		break;
	}
}
