sensor "button1" pin 9
sensor "button2" pin 10
actuatorLCD "screen" bus 1
actuator "snooze" pin 11
actuator "led" pin 12

state "led-on" means "led" becomes "high"
state "led-off" means "led" becomes "low"

//state "snooze-on" means "snooze" becomes "high"
//state "snooze-off" means "snooze" becomes "low"

//state "on" means "screen" display "Mouradox UwU"
//state "off" means "screen" hide ""

initial "led-off"
//initial "snooze-off"

from "led-off" to "led-on" when "button1" becomes "high"
from "led-on" to "led-off" when "button1" becomes "low"

//from "snooze-off" to "snooze-on" when "button2" becomes "high"
//from "snooze-on" to "snooze-off" when "button2" becomes "low"

export "Very simple LCD "