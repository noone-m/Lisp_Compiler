grammar Lisp;

// Root rule
program: expression* EOF;


expression: SYMBOL | NUMBER | list ;

list: '(' expression* ')' ;

COMMENT: ';' ~[\r\n]* -> skip;  

MULTILINE_COMMENT: '#|' .*? '|#' -> skip; 

SYMBOL: [a-zA-Z_]+;    

NUMBER: [0-9]+;       
           
WS: [ \t\r\n]+ -> skip;           
