sensor "button1" pin 8
sensor "button2" pin 9
actuator "led1" pin 11
actuator "led2" pin 12

state "on" means "led1" becomes "low" and "led2" becomes "low"
state "error"

initial "on"

from "on" to "error" when "button1" becomes "high" with "led1" becomes "high"
from "on" to "error" when "button2" becomes "high" with "led2" becomes "high"

export "Mealy machine for Arduino with only 2 buttons"