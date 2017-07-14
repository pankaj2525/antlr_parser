lexer grammar MONGOLexer;

//@lexer::members {
//public static final int MYSQLCOMMENT = 1;
//public static final ERRORCHANNEL= 2;
//}


// SKIP


// SKIP
SPACE
:
	[ \t\r\n]+ -> channel ( HIDDEN )
;
//SPEC_MYSQL_COMMENT:                  '/*!' .+? '*/' -> channel(MYSQLCOMMENT);
//COMMENT_INPUT:                       '/*' .*? '*/' -> channel(HIDDEN);
//LINE_COMMENT:                        ('-- ' | '#') ~[\r\n]* ('\r'? '\n' | EOF) -> channel(HIDDEN);


// Keywords
// Common Keywords
FUNCTION
:
K_FUNCTION
;
DB
:
	D B
;
BOOLEAN
:
	(
		TRUE
		| FALSE
	)
;

TRUE
:
	'true'
;

FALSE
:
	'false'
;

NULL
:
	'null'
;

STRING
:
	'"'
	(
		ESC
		| ~["\\]
	)* '"'
;

NUMBER
:
	'-'? INT_NO '.' [0-9]+ EXP?
	| '-'? INT_NO EXP
	| '-'? INT_NO
;

FIND
:
	'find'
;

INSERT
:
	'insert'
;

INSERTONE
:
	'insertOne'
;

INSERTMANY
:
	'insertMany'
;

UPDATE
:
	'update'
;

UPDATEMANY
:
	'updateMany'
;

UPDATEONE
:
	'updateOne'
;

DELETEONE
:
	'deleteOne'
;

REPLACEONE
:
	'replaceOne'
;

DELETEMANY
:
	'deleteMany'
;

AGGREGATE
:
	'aggregate'
;

BULKWRITE
:
	'bulkWrite'
;


CREATEROLE
:
	'createRole'
;

UPDATEROLE
:
	'updateRole'
;

DROPROLE
:
	'dropPole'
;

DROPALLROLES
:
	'dropAllRoles'
;

GRANTPRIVILEGESTOROLE
:
	'grantPrivilegesToRole'
;

REVOKEPRIVILEGESFROMROLE
:
	'revokePrivilegesFromRole'
;

GRANTROLESTOROLE
:
	'grantRolesToRole'
;

REVOKEROLESFROMROLE
:
	'revokeRolesFromRole'
;
FN_ARG_VAL
:
NUMBER|BOOLEAN|STRING

;

GETROLE
:
	'getRole'
;

GETROLES
:
	'getRoles'
;

ADMINCOMMAND
:
	'adminCommand'
;

CLONECOLLECTION
:
	'cloneCollection'
;

CLONEDATABASE
:
	'cloneDatabase'
;

COPYDATABASE
:
	'copyDatabase'
;

CREATEVIEW
:
	'createView'
;

CURRENTOP
:
	'currenTop'
;

DROPDATABASE
:
	'dropDatabase'
;
EVAL
:
	'eval'
;
// Identifiers
TEXT
:
	ALPHA_LITERAL
;

ALPHA
:
	ALPHABETS
;


// Identifiers


// Charsets

UNDERSCORE
:
	'_'
;

DOT
:
	'.'
;

OPEN_ROUND_BRACKET
:
	'('
;

CLOSE_ROUND_BRACKET
:
	')'
;

OPEN_SQUARE_BRACKET
:
	'['
;

CLOSE_SQUARE_BRACKET
:
	']'
;

OPEN_CURLY_BRACKET
:
	'{'
;

CLOSE_CURLY_BRACKET
:
	'}'
;

ISTO_CHAR
:
	':'
;

COMMAR_CHAR
:
	','
;

EQUAL_TO
:'='
;


KEYWORDS
:
	[$_]? [a-zA-Z] [a-zA-Z]*
;

DATE
:
	DATETYPE '(' CustomDate? ')'
;

DATETYPE
:
	(
		'new '
	)?
	(
		'ISO'
	)? 'Date'
;

CustomDate
:
	'"' YYYY '-' MM '-' DD T HH ':' MIN ':' SEC MILLISEC? Z '"'
;

MILLISEC
:
	(
		'.' [0-9] [0-9] [0-9]
	)
;

YYYY
:
	[0-9] [0-9] [0-9] [0-9]
;

MM
:
	[0-9] [0-9]
;

DD
:
	[0-9] [0-9]
;

HH
:
	[0-9] [0-9]
;

MIN
:
	[0-9] [0-9]
;

SEC
:
	[0-9] [0-9]
;

// Fragments for Literal primitives

fragment
ID_LITERAL
:
	[a-zA-Z_$0-9]*? [a-zA-Z_$]+? [a-zA-Z_$0-9]*
;

fragment
ALPHA_LITERAL
:
	(
		[_$]? [a-zA-Z_0-9]+
	)
;

fragment
ALPHABETS
: [a-zA-Z]+
;

fragment
HEX_DIGIT
:
	[0-9A-Fa-f]
;

// Letters

fragment
A
:
	[aA]
;

fragment
B
:
	[bB]
;

fragment
C
:
	[cC]
;

fragment
D
:
	[dD]
;

fragment
E
:
	[eE]
;

fragment
F
:
	[fF]
;

fragment
G
:
	[gG]
;

fragment
H
:
	[hH]
;

fragment
I
:
	[iI]
;

fragment
J
:
	[jJ]
;

fragment
K
:
	[kK]
;

fragment
L
:
	[lL]
;

fragment
M
:
	[mM]
;

fragment
N
:
	[nN]
;

fragment
O
:
	[oO]
;

fragment
P
:
	[pP]
;

fragment
Q
:
	[qQ]
;

fragment
R
:
	[rR]
;

fragment
S
:
	[sS]
;

fragment
T
:
	[tT]
;

fragment
U
:
	[uU]
;

fragment
V
:
	[vV]
;

fragment
W
:
	[wW]
;

fragment
X
:
	[xX]
;

fragment
Y
:
	[yY]
;

fragment
Z
:
	[zZ]
;

fragment
ESC
:
	'\\'
	(
		["\\/bfnrt]
		| UNICODE
	)
;

fragment
UNICODE
:
	'u' HEX HEX HEX HEX
;

fragment
HEX
:
	[0-9a-fA-F]
;

fragment
INT_NO
:
	'0'
	| [1-9] [0-9]*
;

// no leading zeros

fragment
EXP
:
	[Ee] [+\-]? INT_NO
;
fragment
K_FUNCTION:
'function'
;