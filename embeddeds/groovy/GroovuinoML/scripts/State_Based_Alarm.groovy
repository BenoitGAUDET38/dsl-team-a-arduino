sensor "button1" onPin 9
actuator "led1" pin 11

state "on" means "led1" becomes "high"
state "off" means "led1" becomes "low"

initial "off"

from "on" to "off" when "button1" becomes "high"
from "off" to "on" when "button1" becomes "high"

export "State-based alarm"