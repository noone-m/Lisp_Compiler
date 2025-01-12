grammar Lisp;

// Root rule
program: expression* EOF ;

expression : NUMBER
    |NIL
    |T                
    | SYMBOL                   
    | STRING 
    | PLUS expression+        
    | MINUS expression+        
    | MULTIPLY expression+     
    | DIVIDE expression+
    |raw_list
    | list  ;

boundedExpression: 
    AND expression+        
    | OR expression+           
    | NOT expression           
    | EQUAL_OPERATOR expression+  
    | GREATER_EQUAL expression+    
    | LESS_EQUAL expression+    
    | LESS expression+         
    | GREATER expression+      
    | IF expression ( (expression expression) | expression )
    | COND  cond_branch+  
    | DEFUN SYMBOL '(' parameters? ')' expression // function definition
    | SETQ SYMBOL expression
    | DEFPARAMETER SYMBOL expression 
    | WRITE expression
    | WRITE_LINE STRING 
    | FORMAT expression STRING expression*
    | EQUAL expression expression
    | FUNCALL expression parameters* //todo
    | CAR expression           
    | CDR expression           
    | CONS expression expression 
    | LAMBDA '(' parameters? ')' expression       //todo           
    ;

// Define a list as a sequence of expressions in parentheses
list: '(' (boundedExpression|expression)* ')' ;


parameters: SYMBOL+ ;


// Define a conditional branch for `COND`
cond_branch: '(' expression expression+  ')' ;

raw_list: '\'(' expression* ')';

STRING: '"' ( ~["\\] | '\\' ['"\\nrt] )* '"';

LPAREN: '('; 
RPAREN: ')';
COMMENT: ';' ~[\r\n]* -> skip ;
MULTILINE_COMMENT: '#|' .*? '|#' -> skip ;

T : [Tt];
NIL: [Nn] [Ii] [Ll];

// Keywords 
IF: [iI] [fF] ;
DEFINE: [dD] [eE] [fF] [iI] [nN] [eE] ;
LAMBDA: [lL] [aA] [mM] [bB] [dD] [aA] ;
FUNCALL: [Ff] [Uu] [Nn] [Cc] [Aa] [Ll] [Ll];
WRITE: [wW] [rR] [iI] [tT] [eE] ;
WRITE_LINE: [wW] [rR] [iI] [tT] [eE] '-' [lL] [iI] [nN] [eE] ;
SETQ: [sS] [eE] [tT] [qQ] ;
DEFPARAMETER: [Dd] [eE] [fF] [pP] [aA] [rR] [aA] [mM] [eE] [tT] [eE] [rR];
DEFUN: [dD] [eE] [fF] [uU] [nN] ;
PRINT: [Pp] [Rr] [Ii] [Nn] [Tt] ;
FORMAT: [Ff] [Oo] [Rr] [Mm] [Aa] [tT];
DOTIMES: [dD] [oO] [Tt] [iI] [mM] [eE] [sS];
COND: [cC] [oO] [nN] [dD] ;
AND: [aA] [nN] [dD] ;
OR: [oO] [rR] ;
NOT: [nN] [oO] [tT] ;
EQUAL : [eE] [qQ] [uU] [aA] [lL] ;
CAR: [cC] [aA] [rR] ;
CDR: [cC] [dD] [rR] ;
CONS: [cC] [oO] [nN] [sS] ;
ABS: [aA] [bB] [sS] ;
LOG : [lL] [oO] [gG];
GCD: [gG] [cC] [dD];
FLOOR : [Ff] [lL] [Oo] [Oo] [Rr];
SIN : [sS] [iI] [nN];
SINH : [sS] [iI] [nN] [hH];

ROUND: [rR] [oO] [uU] [nN] [dD];
ICM: [iI] [cC] [mM];




// Operator tokens
PLUS: '+' ;
MINUS: '-' ;
MULTIPLY: '*' ;
DIVIDE: '/' ;
EQUAL_OPERATOR: '=' ;
LESS: '<' ;
GREATER: '>';
GREATER_EQUAL: '>=' ;
LESS_EQUAL: '<=' ;

SYMBOL: ('*'  |  '?' | '\\' | [a-zA-Z!<>_-]) ([a-zA-Z0-9_-] | '*' | '?') * ;

NUMBER: '-'? [0-9]+ ('.' [0-9]+)?;

WS: [ \t\r\n]+ -> skip ; 
