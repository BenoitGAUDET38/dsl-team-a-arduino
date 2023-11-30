// Wiring code generated from an ArduinoML model
// Application name: Multi-state alarm

long debounce = 200;

enum STATE {buzzer_on, led_on, off};
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
		case buzzer_on:
			digitalWrite(13,HIGH);
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			if( digitalRead(9) == HIGH && button1BounceGuard) {
				button1LastDebounceTime = millis();
				currentState = led_on;
			}
		break;
		case led_on:
			digitalWrite(13,LOW);
			digitalWrite(12,HIGH);
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			if( digitalRead(9) == HIGH && button1BounceGuard) {
				button1LastDebounceTime = millis();
				currentState = off;
			}
		break;
		case off:
			digitalWrite(13,LOW);
			digitalWrite(12,LOW);
			button1BounceGuard = millis() - button1LastDebounceTime > debounce;
			if( digitalRead(9) == HIGH && button1BounceGuard) {
				button1LastDebounceTime = millis();
				currentState = buzzer_on;
			}
		break;
	}
}
