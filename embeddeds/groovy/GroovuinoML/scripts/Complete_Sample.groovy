sensor "button1" pin 9
sensor "button2" pin 10
actuatorLCD "screen" bus 1
actuator "led1" pin 11

state "on" means "led1" becomes "high" and "screen" display "Led : ON" row 0
state "off"

initial "off"

from "off" to "on" when "button1" becomes "high" and "button2" becomes "high" after 1000.ms
from "on" to "off" after 10.s with "led1" becomes "low" and "screen" display "Led : OFF" row 0

export "Sample application"
