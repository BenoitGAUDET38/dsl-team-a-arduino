sensor "button1" pin 9
sensor "button2" pin 10
actuatorLCD "screen" bus 1
actuator "snooze1" pin 11
actuator "led1" pin 12

state "led" means "led1" becomes "high" and "snooze1" becomes "low" andLCD "screen" display "Led : ON" row 0 andLCD "screen" display "Snooze : OFF" row 1
state "snooze" means "led1" becomes "low" and "snooze1" becomes "high" andLCD "screen" display "Led : OFF" row 0 andLCD "screen" display "Snooze : ON" row 1
state "none" means "led1" becomes "low" and "snooze1" becomes "low" andLCD "screen" display "Led : OFF" row 0 andLCD "screen" display "Snooze : OFF" row 1

initial "none"

from "none" to "led" when "button1" becomes "high"
from "led" to "snooze" when "button1" becomes "high"
from "snooze" to "none" when "button1" becomes "high"

export "Change states with one button"