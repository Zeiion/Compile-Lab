//Define a grammar called Hello
grammar Hello;

hello : compUnit;
compUnit     : funcDef;
decl         : constDecl | varDecl;
constDecl    : 'const' BType constDef (',' constDef)* ';';
constDef     : Ident '=' constInitVal;
constInitVal : constExp;
constExp     : addExp;
varDecl      : BType varDef (',' varDef)* ';';
varDef       : Ident
                | Ident '=' initVal;
initVal      : exp;
funcDef      : BType Ident '(' ')' block; // 保证当前 Ident 只为 "main"
block        : '{' blockItem* '}';
blockItem    : decl | stmt;
stmt         : lVal '=' exp ';' # stmt1
                | block # stmt2
                | exp? ';' # stmt3
                | 'if' '(' cond ')' stmt ('else' stmt)? # stmt4
                | 'return' exp ';' # stmt5
                ;
exp          : addExp;
cond         : lOrExp ;
lVal         : Ident;
primaryExp   : '(' exp ')' # primaryExp1
                | lVal # primaryExp2
                | Number # primaryExp3
                ;
addExp       : mulExp # addExp1
                | addExp ('+' | '-') mulExp  # addExp2;
mulExp       : unaryExp # mulExp1
                | mulExp ('*' | '/' | '%') unaryExp # mulExp2
                ;
unaryExp     : Ident '(' funcRParams? ')' # calcResES
                | primaryExp # normResES
                | unaryOp unaryExp # symbolResES
                ;
unaryOp      : '+' | '-' | '!'  ;// 保证 '!' 只出现在 Cond 中
funcRParams  : exp (',' exp)*;
relExp       : addExp
                | relExp ('<' | '>' | '<=' | '>=') addExp;
eqExp        : relExp
                | eqExp ('==' | '!=') relExp;
lAndExp      : eqExp
                | lAndExp '&&' eqExp;
lOrExp       : lAndExp
                | lOrExp '||' lAndExp;

fragment INT: 'int';
BType : INT;
FuncType: INT;
Ident : Nondigit (Nondigit | Digit)*;
Number : DecimalConst | OctalConst | HexaDecimalConst;
fragment DecimalConst : '0' | [1-9] Digit*;
fragment OctalConst  : '0' [0-7]+;
fragment HexaDecimalConst  : '0' [xX] [0-9a-fA-F]+;

fragment Nondigit : [_a-zA-Z];
fragment Digit : [0-9];

LINE_COMMENT : '//' .*? '\r'? '\n' ->skip; //跳过 // 注释
COMMENT : '/*' .*? '*/' ->skip;  //跳过 /**/ 注释
WS : [ \t\r\n]+ -> skip ; // 跳过空格、tabs、换行符
