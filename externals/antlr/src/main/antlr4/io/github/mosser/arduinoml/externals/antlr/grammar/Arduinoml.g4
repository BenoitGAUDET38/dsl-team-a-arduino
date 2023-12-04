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
    state       :   initial? name=IDENTIFIER '{'  (action+)? (actionLCD+)? (transition+)?  '}';
    action      :   receiver=IDENTIFIER '<=' value=SIGNAL;
    actionLCD   :   receiver=IDENTIFIER (':' text=STRING ('row' rowNumber=NUMBER)? )? '<=' isDisplayed=BOOLEAN;
    transition  :   trigger=IDENTIFIER 'is' value=SIGNAL (more=condition)? ('(' mealy=newAction ')')? '=>' next=IDENTIFIER ;
    condition   :   operator=OPERATOR trigger=IDENTIFIER 'is' value=SIGNAL (more=condition)?;
    newAction   :   action (', ' mealy=newAction)?;
    initial     :   '->';

/*****************
 ** Lexer rules **
 *****************/

NUMBER          :   [1-9] | '10' | '11' | '12';
IDENTIFIER      :   LOWERCASE (LOWERCASE|UPPERCASE)+;
SIGNAL          :   'HIGH' | 'LOW';
OPERATOR        :   'AND' | 'OR';
BOOLEAN         :   'TRUE' | 'FALSE';
STRING          :   (LOWERCASE | UPPERCASE)+;

/*************
 ** Helpers **
 *************/

fragment LOWERCASE  : [a-z];                                 // abstract rule, does not really exists
fragment UPPERCASE  : [A-Z];
NEWLINE             : ('\r'? '\n' | '\r')+      -> skip;
WS                  : ((' ' | '\t')+)           -> skip;     // who cares about whitespaces?
COMMENT             : '#' ~( '\r' | '\n' )*     -> skip;     // Single line comments, starting with a #
