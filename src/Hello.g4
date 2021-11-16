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
stmt         : lVal '=' exp ';'
                | block
                | exp? ';'
                | 'if' '(' cond ')' stmt ('else' stmt)?
                | 'return' exp ';'; // [changed]
exp          : addExp;
cond         : lOrExp ;// [new]
lVal         : Ident;
primaryExp   : '(' exp ')' # primaryExp1
                | lVal # primaryExp2
                | Number # primaryExp3
                ;
addExp       : mulExp
                | addExp ('+' | '−') mulExp;
mulExp       : unaryExp
                | mulExp ('*' | '/' | '%') unaryExp;

unaryExp     : Ident '(' funcRParams? ')' # calcResES
                | primaryExp # normResES
                | unaryOp unaryExp # symbolResES
                ;
unaryOp      : '+' | '-' | '!'  ;// 保证 '!' 只出现在 Cond 中 [changed]
funcRParams  : exp (',' exp)*;
relExp       : addExp
                | relExp ('<' | '>' | '<=' | '>=') addExp;  // [new]
eqExp        : relExp
                | eqExp ('==' | '!=') relExp;  // [new]
lAndExp      : eqExp
                | lAndExp '&&' eqExp;  // [new]
lOrExp       : lAndExp
                | lOrExp '||' lAndExp;  // [new]

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
