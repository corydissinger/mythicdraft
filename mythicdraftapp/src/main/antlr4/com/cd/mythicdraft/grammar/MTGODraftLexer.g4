lexer grammar MTGODraftLexer;

PLAYERS_FIELD: 'Players' COLON;
EVENT_FIELD: 'Event ' POUND COLON ' ';
TIMESTAMP_FIELD: 'Time' COLON TAB;
SELECTED: '--> ';
TAB: ('    ' | '\t');
HYPHEN_LEFT: '------ ';
HYPHEN_RIGHT: ' ------ ';
COLON: ':';
POUND: '#';
PACK: 'Pack ';
PICK: ' pick ';

SEPARATOR: '\r'? '\n';
NUMBER: ('0'..'9')+;

//Lexer rule precedence is killing me. If CARD_NAME is defind after USER_NAME, then
//All single-word card names will fail to be matched
SET_NAME: ('A'..'Z' | '0'..'9')('A'..'Z' | '0'..'9')('A'..'Z' | '0'..'9');
CARD_NAME: CARD_NAME_PART (CARD_NAME_SEPARATOR CARD_NAME)*;
USER_NAME: ('a'..'z' | '\'' | ',' | 'A'..'Z' | '0'..'9' | '_' | '.' | '\u00C6' | 'Æ' | '-')+;

//Split so that it can recognize one pronoun | or a sequence of pronouns and nouns... ugh
fragment CARD_NAME_PART: ('a'..'z' | '\'' | ',' | 'A'..'Z' | '_' | '.' | '\u00C6' | 'Æ' | CARD_NUMBER)+;
fragment CARD_NAME_SEPARATOR: (' ' | '-' | '/');

//Super rare edge cases, hopefully
fragment CARD_NUMBER: ('1'..'9')(',')('0'..'9')('0'..'9')('0'..'9');

fragment YEAR: ('0'..'9')('0'..'9')('0'..'9')('0'..'9');
fragment MONTH: ('0'..'9') | ('0'..'9')('0'..'9');
fragment DAY: ('0'..'9') | ('0'..'9')('0'..'9');

fragment HOURS: ('0'..'9') | ('0'..'9')('0'..'9');
fragment MINUTES: ('0'..'9')('0'..'9');
fragment SECONDS: ('0'..'9')('0'..'9');

// Example for a valid timestamp: 2014-03-25 20:21:22
TIMESTAMP: (MONTH)('/')(DAY)('/')(YEAR)(' ')(HOURS)(':')(MINUTES)(':')(SECONDS)(' ')('AM'|'PM');