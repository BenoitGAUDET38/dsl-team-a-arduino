// Wiring code generated from an ArduinoML model
// Application name: Multi-state alarm

long debounce = 200;

enum STATE {buzzer_on, led_on, off};
STATE currentState = off;

boolean button1BounceGuard = false;
long button1LastDebounceTime = 0;

void setup(){
  pinMode(9, INPUT);  // button1 [Sensor]
  pinMode(11, OUTPUT); // led1 [Actuator]
  pinMode(12, OUTPUT); // buzzer1 [Actuator]
}

void loop() {
	switch(currentState){
		case buzzer_on:
			digitalWrite(12,HIGH);
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && button1BounceGuard) {
				button1LastDebounceTime = millis();
				currentState = led_on;
			}
		break;
		case led_on:
			digitalWrite(12,LOW);
			digitalWrite(11,HIGH);
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && button1BounceGuard) {
				button1LastDebounceTime = millis();
				currentState = off;
			}
		break;
		case off:
			digitalWrite(12,LOW);
			digitalWrite(11,LOW);
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			if (digitalRead(9) == HIGH && button1BounceGuard) {
				button1LastDebounceTime = millis();
				currentState = buzzer_on;
			}
		break;
	}
}
