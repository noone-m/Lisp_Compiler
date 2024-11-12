grammar Lisp;

// Root rule
program: expression* EOF ;


expression
    : NUMBER                   
    | SYMBOL                   
    | STRING                  
    | PLUS expression+        
    | MINUS expression+        
    | MULTIPLY expression+     
    | DIVIDE expression+       
    | AND expression+          
    | OR expression+           
    | NOT expression           
    | EQUAL expression+        
    | LESS expression+         
    | GREATER expression+      
    | IF expression expression expression 
    | COND '(' cond_branch+ ')' 
    | DEFUN SYMBOL '(' parameters? ')' expression // function definition
    | SETQ SYMBOL expression   
    | CAR expression           
    | CDR expression           
    | CONS expression expression
    | list                     
    ;

// Define a list as a sequence of expressions in parentheses
list: '(' expression* ')' ;


parameters: SYMBOL+ ;

// Define a conditional branch for `COND`
cond_branch: '(' expression expression ')' ;

COMMENT: ';' ~[\r\n]* -> skip ;
MULTILINE_COMMENT: '#|' .*? '|#' -> skip ;

// Keywords 
IF: 'if' ;
DEFINE: 'define' ;
LAMBDA: 'lambda' ;
WRITE: 'write' ;
WRITE_LINE: 'write-line' ;
SETQ: 'setq' ;
DEFUN: 'defun' ;
COND: 'cond' ;
AND: 'and' ;
OR: 'or' ;
NOT: 'not' ;
EQUAL: '=' ;
LESS: '<' ;
GREATER: '>' ;
CAR: 'car' ;
CDR: 'cdr' ;
CONS: 'cons' ;


STRING: '"' (~["\r\n])* '"' ;

// Operator tokens
PLUS: '+' ;
MINUS: '-' ;
MULTIPLY: '*' ;
DIVIDE: '/' ;

SYMBOL: [a-zA-Z_][a-zA-Z0-9_]* ;

NUMBER: [0-9]+ ;

WS: [ \t\r\n]+ -> skip ;
