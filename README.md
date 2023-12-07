# Domain Specific Language - Arduino ML

## Team A
### Authors
- [Antoine BUQUET](https://github.com/antoinebqt)
- [Benoit GAUDET](https://github.com/BenoitGAUDET38)
- [Ayoub IMAMI](https://github.com/AyoubIMAMI)
- [Mourad KARRAKCHOU](https://github.com/MouradKarrakchou)
---

## External DSL: ANTLR
### BNF
```java
grammar Arduinoml;

/******************
** Parser rules **
******************/

root            :   declaration bricks states EOF;

declaration     :   'application' name=IDENTIFIER;

bricks          :   (sensor|actuator|actuatorLCD)+;
   sensor      :   'sensor'   location ;
   actuator    :   'actuator' location ;
   actuatorLCD :   'actuatorLCD' location ;
   location    :   id=IDENTIFIER ':' port=NUMBER;

states          :   state+;
   state       :   initial? name=IDENTIFIER '{'  (actionSensor+)? (actionLCD+)? (transition+)?  '}';
   actionSensor:   receiver=IDENTIFIER '<=' value=SIGNAL;
   actionLCD   :   receiver=IDENTIFIER (':' text=STRING ('row' rowNumber=NUMBER)? )? '<=' isDisplayed=BOOLEAN;
   transition  :   (('after' time=NUMBER 'ms') | (trigger=IDENTIFIER 'is' value=SIGNAL )) (more=condition)? ('(' mealy=newAction ')')? '=>' next=IDENTIFIER ;
   condition   :   operator=OPERATOR ((trigger=IDENTIFIER 'is' value=SIGNAL) | ('after' time=NUMBER 'ms')) (more=condition)?;
   newAction   :   (receiverSensor=IDENTIFIER '<=' value=SIGNAL | receiverLCD=IDENTIFIER (':' text=STRING ('row' rowNumber=NUMBER)? )? '<=' isDisplayed=BOOLEAN) (', ' mealy=newAction)?;
   initial     :   '->';

/*****************
** Lexer rules **
*****************/

NUMBER          :   [1-9][0-9]*;
IDENTIFIER      :   LOWERCASE (LOWERCASE|UPPERCASE)+;
SIGNAL          :   'HIGH' | 'LOW';
OPERATOR        :   'AND' | 'OR';
BOOLEAN         :   'TRUE' | 'FALSE';
STRING          :   ["][ a-zA-Z1-9:]*["];

/*************
** Helpers **
*************/

fragment LOWERCASE  : [a-z];                                 // abstract rule, does not really exists
fragment UPPERCASE  : [A-Z];
NEWLINE             : ('\r'? '\n' | '\r')+      -> skip;
WS                  : ((' ' | '\t')+)           -> skip;     // who cares about whitespaces?
COMMENT             : '#' ~( '\r' | '\n' )*     -> skip;     // Single line comments, starting with a #
```
### Table of symbols
| Usage  | Notation |
|:------:|:--------:|
| `truc` |   truc   |
### Script example
```java
application sampleApplication

# Declaring bricks
sensor buttonOne: 9
sensor buttonTwo: 10
actuator led: 11
actuatorLCD lcd: 1

# Declaring states
on {
   led <= HIGH
   lcd : "Led : ON" <= TRUE
   after 10000 ms (led <= LOW) => off
}

-> off {
   lcd : "Led : OFF" <= TRUE
   buttonOne is HIGH AND buttonTwo is HIGH AND after 1000 ms => on
}
```
## Internal DSL: Groovy
### BNF
```java
Brick =
        ('sensor' | 'actuator' String 'pin' Int) | ('actuatorLCD' String 'bus' Int);
State =
        'state' String ['means' Action {'and' Action}];
        'initial' String;
Action =
String (('becomes' Signal) | ('display' String 'row' Int));
Signal =
        'high' | 'low';
Transition =
        'from' String 'to' String (TemporalCondition ['with' Action {'and' Action}] | 'when' Condition
{RecursiveCond} [TemporalCondition {RecursiveCond}] ['with' Action {'and' Action}]);
Condition =
String 'becomes' Signal;
RecursiveCond =
        ('and' | 'or') Condition;
TemporalCondition =
        'after' Int ('ms' | 's' | 'min');
Application =
        (Brick)+;
        (State)+;
        {Transition};
        'export' String;
```
### Table of symbols
|                 Usage                 |   Notation    |
|:-------------------------------------:|:-------------:|
|              Definition               |       =       |
|              Termination              |       ;       |
|              Alternation              |    &#124;     |
|               Grouping                |     (...)     |
| At least one repetition (one or more) |    (...)+     |
|        Optional (none or once)        |     [...]     |
|       Repetition (none or more)       |     {...}     |

### Script example
```java
sensor "button1" pin 9
sensor "button2" pin 10
actuatorLCD "screen" bus 1
actuator "led1" pin 11

state "on" means "led1" becomes "high" and "screen" display "Led : ON" row 0
state "off" means "screen" display "Led : OFF" row 0

initial "off"

from "off" to "on" when "button1" becomes "high" and "button2" becomes "high" after 1000.ms
from "on" to "off" after 10.s with "led1" becomes "low"

export "Sample application"
}
```
---
## Scenarios
### Basic
- [X] Very simple alarm
- [X] Dual-check alarm
- [X] State-based alarm
- [X] Multi-state alarm

### Extensions
- [ ] Remote Communication
- [ ] Exception Throwing
- [ ] PIN allocation generator
- [X] Temporal transitions
- [X] Supporting the LCD screen
- [ ] Handling Analogical Bricks
- [X] Mealy machine for Arduino