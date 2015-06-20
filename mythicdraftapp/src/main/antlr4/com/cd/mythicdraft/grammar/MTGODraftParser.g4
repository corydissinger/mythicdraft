parser grammar MTGODraftParser;

options {
	tokenVocab = MTGODraftLexer;
}

file: eventField timeField playersField packsField EOF;

eventField: EVENT_FIELD eventId SEPARATOR;
eventId: NUMBER;

timeField: TIMESTAMP_FIELD time SEPARATOR;
time: TIMESTAMP;

playersField: PLAYERS_FIELD SEPARATOR playersList+ SEPARATOR;
playersList: ((TAB aPlayer | SELECTED currentPlayer ) SEPARATOR);
currentPlayer: USER_NAME;
aPlayer: USER_NAME;

packsField: pack*;
pack: HYPHEN_LEFT set HYPHEN_RIGHT SEPARATOR SEPARATOR packPicks*;
set: SET_NAME;

packPicks: PACK packNumber PICK pickNumber COLON SEPARATOR picks SEPARATOR;
packNumber: NUMBER;
pickNumber: NUMBER; 
picks: (( TAB availablePick | SELECTED pick ) SEPARATOR)+;

availablePick: CARD_NAME;
pick: CARD_NAME;