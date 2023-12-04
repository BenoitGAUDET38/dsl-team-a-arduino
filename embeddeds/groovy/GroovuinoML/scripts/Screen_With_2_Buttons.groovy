sensor "button1" pin 9
sensor "button2" pin 10
actuatorLCD "screen" bus 1
actuator "snooze1" pin 11
actuator "led1" pin 12

state "both" means "led1" becomes "high" and "snooze1" becomes "high" andLCD "screen" display "Led : ON" row 0 andLCD "screen" display "Buzzer : ON" row 1
state "led" means "led1" becomes "high" and "snooze1" becomes "low" andLCD "screen" display "Led : ON" row 0 andLCD "screen" display "Buzzer : OFF" row 1
state "snooze" means "led1" becomes "low" and "snooze1" becomes "high" andLCD "screen" display "Led : OFF" row 0 andLCD "screen" display "Buzzer : ON" row 1
state "none" means "led1" becomes "low" and "snooze1" becomes "low" andLCD "screen" display "Led : OFF" row 0 andLCD "screen" display "Buzzer : OFF" row 1

initial "none"

from "none" to "led" when "button1" becomes "high"
from "led" to "none" when "button1" becomes "high"

from "none" to "snooze" when "button2" becomes "high"
from "snooze" to "none" when "button2" becomes "high"

from "led" to "both" when "button2" becomes "high"
from "both" to "led" when "button2" becomes "high"

from "snooze" to "both" when "button1" becomes "high"
from "both" to "snooze" when "button1" becomes "high"

export "Change LCD state with two buttons"