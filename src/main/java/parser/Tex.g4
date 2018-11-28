grammar Tex;

options {
    language = Java;
}

@header {
package parser;
import expressions.*;
}

texstring returns [Expression expr] :
    DOLLAR inner=expression DOLLAR
        {$expr = new TexExpression($inner.expr);}
;

expression returns [Expression expr] :
    t=addition
        {$expr = $t.expr;}
    | left=expression EQ right=addition
        {$expr = new Eq($left.expr, $right.expr);}
;

addition returns [Expression expr] :
    t=multiplication
        {$expr = $t.expr;}
    | left=addition PLUS right=multiplication
        {$expr = new Plus($left.expr, $right.expr);}
    | left=addition MINUS right=multiplication
        {$expr = new Minus($left.expr, $right.expr);}
;

multiplication returns [Expression expr] :
    t=atom
        {$expr = $t.expr;}
    | left=multiplication MUL right=atom
        {$expr = new Mul($left.expr, $right.expr);}
    | DIVISION OPEN_BRACKET left=multiplication CLOSE_BRACKET OPEN_BRACKET right=atom CLOSE_BRACKET
        {$expr = new Div($left.expr, $right.expr);}
;

atom returns [Expression expr] :
    l=literal
        {$expr = $l.expr;}
    | MINUS otherAtom=atom
        {$expr = new UnaryMinus($otherAtom.expr);}
    | OPEN_PAR inner=expression CLOSE_PAR
        {$expr = new Parenthesis($inner.expr);}
    | basic=atom DOWN OPEN_BRACKET sub=addition CLOSE_BRACKET UP OPEN_BRACKET upper=addition CLOSE_BRACKET
                {$expr = new SubSup($basic.expr, $sub.expr, $upper.expr);}
    | basic=atom UP OPEN_BRACKET upper=addition CLOSE_BRACKET DOWN OPEN_BRACKET sub=addition CLOSE_BRACKET
                {$expr = new SubSup($basic.expr, $sub.expr, $upper.expr);}
    | basic=atom UP OPEN_BRACKET upper=addition CLOSE_BRACKET
            {$expr = new Sup($basic.expr, $upper.expr);}
    | basic=atom DOWN OPEN_BRACKET sub=addition CLOSE_BRACKET
            {$expr = new Sub($basic.expr, $sub.expr);}
;

literal returns [Expression expr] :
    LITERAL
        {$expr = new LiteralHolder($LITERAL.text);}
;

WS : [ \n\t\r]+ -> skip;
UP : '^';
DOWN : '_';
MINUS : '-';
PLUS : '+';
MUL : '*';
OPEN_PAR : '(';
CLOSE_PAR : ')';
OPEN_BRACKET : '{';
CLOSE_BRACKET : '}';
DIVISION : '\\divide';
LITERAL : ('a'..'z'|'A'..'Z'|'0'..'9')+;
DOLLAR : '$';
EQ : '=';



