parser grammar MONGOParser;

options {
	tokenVocab = MONGOLexer;
}


mongo_statements
:
	mongo_statement EOF
;
mongo_statement
:	database_command
	| role_management_methods
	| intialQuerry collection_methods (DOT  collection_methods)*
	| db_name DOT  database_methods

;
role_management_methods: 
	DB DOT role_method parameter
;
role_method: 
	CREATEROLE
	|UPDATEROLE
	|DROPROLE
	|DROPALLROLES
	|GRANTPRIVILEGESTOROLE
	|REVOKEPRIVILEGESFROMROLE
	|GRANTROLESTOROLE
	|REVOKEROLESFROMROLE
	|GETROLE
	|GETROLES
;



database_command
: 
	TEXT (SINGLESPACE) TEXT	
	| (DB DOT TEXT OPEN_ROUND_BRACKET json_input CLOSE_ROUND_BRACKET)
;


cursor_methods
:
	TEXT OPEN_ROUND_BRACKET CLOSE_ROUND_BRACKET
;

aggregate_statement
:
	AGGREGATE
;

intialQuerry
:
	db_name DOT collection_name
	(
		OPEN_ROUND_BRACKET STRING CLOSE_ROUND_BRACKET
	)? DOT
	
;

document
:
	OPEN_ROUND_BRACKET json_input CLOSE_ROUND_BRACKET
;

document_array
:
	OPEN_ROUND_BRACKET OPEN_SQUARE_BRACKET json_input CLOSE_SQUARE_BRACKET
	(
		COMMAR_CHAR OPEN_CURLY_BRACKET json_input CLOSE_CURLY_BRACKET
	)* CLOSE_ROUND_BRACKET
;
database_methods
:
(
	db_string_or_doc_input_method params_string_or_doc
	| db_string_doc_input_method params_string_string_doc
	| db_string_input_method params_string
	| db_strings_input_method params_strings
	| db_strings_array_doc_input_method params_strings_array_doc
	| db_boolean_or_doc_input_method params_boolean_or_doc
	| db_no_arg_input_method params_no_arg
	| db_javascriptfn_args_input_method params_jsfunction_args
	
)
;


collection_methods
:
	| single_doc_input_method document
	| multiple_doc_input_method document_array
	| single_or_multiple_doc_input_method
	(
		document
		| document_array
	)
	| aggregate_statement parameter
	| query_statements (
		document
		| document_array
		| parameter
	) (
		DOT cursor_methods
	)*
	| operations_input_method operations_input_method_input
	
;
methodDeclaration
:
FUNCTION formalParameters
(OPEN_CURLY_BRACKET
.*?
CLOSE_CURLY_BRACKET

)
;
formalParameters
    :   OPEN_ROUND_BRACKET (formalParameterList)? CLOSE_ROUND_BRACKET
    ;
formalParameterList
:
ALPHA (EQUAL_TO FN_ARG_VAL )? ( COMMAR_CHAR ALPHA (EQUAL_TO FN_ARG_VAL )?)* 
;

operations_input_method_input
:
	OPEN_ROUND_BRACKET OPEN_SQUARE_BRACKET
	OPEN_CURLY_BRACKET
		(
			INSERTONE
			| UPDATEONE
			| UPDATEMANY
			| REPLACEONE
			| DELETEONE
			| DELETEMANY
		) ISTO_CHAR value CLOSE_CURLY_BRACKET
	(
		COMMAR_CHAR OPEN_CURLY_BRACKET
		(
			INSERTONE
			| UPDATEONE
			| UPDATEMANY
			| REPLACEONE
			| DELETEONE
			| DELETEMANY
		) ISTO_CHAR value CLOSE_CURLY_BRACKET
	)* CLOSE_SQUARE_BRACKET CLOSE_ROUND_BRACKET
	| OPEN_ROUND_BRACKET
	(
		OPEN_CURLY_BRACKET
		(
			INSERTONE
			| UPDATEONE
			| UPDATEMANY
			| REPLACEONE
			| DELETEONE
			| DELETEMANY
		) ISTO_CHAR value CLOSE_CURLY_BRACKET
	) CLOSE_ROUND_BRACKET
;

operations_input_method
:
	BULKWRITE
;


params_string_or_doc
:
OPEN_ROUND_BRACKET
(
	STRING
	| object
) CLOSE_ROUND_BRACKET
;
params_string
:
OPEN_ROUND_BRACKET
(
	STRING
) CLOSE_ROUND_BRACKET
;
params_string_string_doc
:
OPEN_ROUND_BRACKET STRING COMMAR_CHAR STRING
(
	COMMAR_CHAR object
)? CLOSE_ROUND_BRACKET
;
params_strings
:
OPEN_ROUND_BRACKET
STRING COMMAR_CHAR STRING (COMMAR_CHAR STRING)?(COMMAR_CHAR STRING)?(COMMAR_CHAR STRING)?(COMMAR_CHAR STRING)?
CLOSE_ROUND_BRACKET
;
params_strings_array_doc
:
OPEN_ROUND_BRACKET
STRING COMMAR_CHAR STRING array (COMMAR_CHAR object)?
CLOSE_ROUND_BRACKET
;
params_boolean_or_doc
:
OPEN_ROUND_BRACKET
(
	BOOLEAN
	| object
) CLOSE_ROUND_BRACKET
;
params_no_arg
:
OPEN_ROUND_BRACKET
CLOSE_ROUND_BRACKET
;
params_jsfunction_args
:
OPEN_ROUND_BRACKET
methodDeclaration (COMMAR_CHAR (FN_ARG_VAL))
CLOSE_ROUND_BRACKET
;
parameter
:
	OPEN_ROUND_BRACKET
	(
		value
		| json_input
		| OPEN_SQUARE_BRACKET json_input CLOSE_SQUARE_BRACKET
		(
			COMMAR_CHAR OPEN_CURLY_BRACKET json_input CLOSE_CURLY_BRACKET
		)*
	)* CLOSE_ROUND_BRACKET
;

other_methods
:
	TEXT
;
db_string_or_doc_input_method:
ADMINCOMMAND	
;
db_string_doc_input_method:
CLONECOLLECTION	
;
db_string_input_method:
CLONEDATABASE	
;
db_strings_input_method:
COPYDATABASE	
;
db_strings_array_doc_input_method
:
CREATEVIEW
;
db_boolean_or_doc_input_method
:
CURRENTOP
;
db_no_arg_input_method
:
DROPDATABASE
;
db_javascriptfn_args_input_method
:
EVAL
;
single_doc_input_method
:
	INSERTONE
	| UPDATEONE
	| DELETEONE
;

multiple_doc_input_method
:
	INSERTMANY
	| UPDATEMANY
	| DELETEMANY
;

single_or_multiple_doc_input_method
:
	INSERT
	| UPDATE
;

query_statements
:
	FIND
;


json_input
:
	object
	(
		COMMAR_CHAR object
	)*?
;

db_name
:
	DB
;
//Restriction on Collection Names
//Collection names should begin with an underscore or a letter character, and cannot:

//contain the $.
//be an empty string (e.g. "").
//contain the null character.
//begin with the system. prefix. (Reserved for internal use.)

collection_name
:
	TEXT
;

json
:
	value
;

object
:
	OPEN_CURLY_BRACKET pair
	(
		COMMAR_CHAR pair
	)* CLOSE_CURLY_BRACKET
	| OPEN_CURLY_BRACKET CLOSE_CURLY_BRACKET
;

pair
:
	(
		STRING
		| TEXT
	) ISTO_CHAR value
;

array
:
	OPEN_SQUARE_BRACKET value
	(
		COMMAR_CHAR value
	)* CLOSE_SQUARE_BRACKET
	| OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET
;

value
:
	STRING
	| NUMBER
	| object
	| array
	| TRUE
	| FALSE
	| NULL
	| TEXT
	| DATE
;






