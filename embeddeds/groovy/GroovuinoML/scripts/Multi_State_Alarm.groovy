sensor "button1" pin 9
actuator "led1" pin 11
actuator "buzzer1" pin 12

state "buzzer_on" means "buzzer1" becomes "high"
state "led_on" means "buzzer1" becomes "low" and "led1" becomes "high"
state "off" means "buzzer1" becomes "low" and "led1" becomes "low"

initial "off"

from "off" to "buzzer_on" when "button1" becomes "high"
from "buzzer_on" to "led_on" when "button1" becomes "high"
from "led_on" to "off" when "button1" becomes "high"

export "Multi-state alarm"
