parser grammar MTGODraftParser;

options {
	tokenVocab = MTGODraftLexer;
}

file: eventField timeField playersField packsField EOF;

eventField: EVENT_FIELD eventId SEPARATOR SEPARATOR;
eventId: NUMBER;

timeField: TIMESTAMP_FIELD time SEPARATOR SEPARATOR;
time: TIMESTAMP;

playersField: PLAYERS_FIELD SEPARATOR SEPARATOR playersList+ SEPARATOR SEPARATOR;
playersList: ((TAB aPlayer | SELECTED currentPlayer ) SEPARATOR SEPARATOR);
currentPlayer: USER_NAME;
aPlayer: USER_NAME;

packsField: pack*;
pack: HYPHEN_LEFT set HYPHEN_RIGHT SEPARATOR SEPARATOR SEPARATOR SEPARATOR packPicks*;
set: SET_NAME;

packPicks: PACK packNumber PICK pickNumber COLON SEPARATOR SEPARATOR picks SEPARATOR SEPARATOR;
packNumber: NUMBER;
pickNumber: NUMBER; 
picks: (( TAB availablePick | SELECTED pick ) SEPARATOR SEPARATOR)+;

availablePick: CARD_NAME;
pick: CARD_NAME;