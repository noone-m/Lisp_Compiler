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
IF: [iI] [fF] ;
DEFINE: [dD] [eE] [fF] [iI] [nN] [eE] ;
LAMBDA: [lL] [aA] [mM] [bB] [dD] [aA] ;
WRITE: [wW] [rR] [iI] [tT] [eE] ;
WRITE_LINE: [wW] [rR] [iI] [tT] [eE] '-' [lL] [iI] [nN] [eE] ;
SETQ: [sS] [eE] [tT] [qQ] ;
DEFUN: [dD] [eE] [fF] [uU] [nN] ;
COND: [cC] [oO] [nN] [dD] ;
AND: [aA] [nN] [dD] ;
OR: [oO] [rR] ;
NOT: [nN] [oO] [tT] ;
CAR: [cC] [aA] [rR] ;
CDR: [cC] [dD] [rR] ;
CONS: [cC] [oO] [nN] [sS] ;
STRING: '"' (~["\r\n])* '"' ;

// Operator tokens
PLUS: '+' ;
MINUS: '-' ;
MULTIPLY: '*' ;
DIVIDE: '/' ;
EQUAL: '=' ;
LESS: '<' ;
GREATER: '>' ;

SYMBOL: [a-zA-Z_][a-zA-Z0-9_]* ;

NUMBER: '-'? [0-9]+ ('.' [0-9]+)?;

WS: [ \t\r\n]+ -> skip ; 
