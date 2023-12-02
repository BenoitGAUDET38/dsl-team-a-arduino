sensor "button1" pin 9
sensor "button2" pin 10
actuatorLCD "screen" bus 1
actuator "snooze1" pin 11
actuator "led1" pin 12

state "both" means "led1" becomes "high" and "snooze1" becomes "high"
state "led" means "led1" becomes "high" and "snooze1" becomes "low"
state "snooze" means "led1" becomes "low" and "snooze1" becomes "high"
state "none" means "led1" becomes "low" and "snooze1" becomes "low"

//state "on" means "screen" display "Mouradox UwU"
//state "off" means "screen" hide ""

initial "none"

from "none" to "led" when "button1" becomes "high"
from "led" to "none" when "button1" becomes "low"

from "none" to "snooze" when "button2" becomes "high"
from "snooze" to "none" when "button2" becomes "low"

from "snooze" to "both" when "button1" becomes "high"
from "both" to "snooze" when "button2" becomes "low"

from "led" to "both" when "button2" becomes "high"
from "both" to "led" when "button2" becomes "low"

export "Very simple LCD "