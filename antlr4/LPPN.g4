// ----------------------------------------------------------------------------
// Copyright (C) 2015 G. Sileno
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//
// To contact the authors:
// http://www.leibnizcenter.org/~sileno
//----------------------------------------------------------------------------

grammar LPPN;

@header {}

program
: (statement_list)? EOF
;

statement_list
: statement DOT (statement_list)?
;

statement
: head ( (IFP|IFT) body )?
| body THENP head
| consequent CAUSEDBY antecedent
| antecedent CAUSES consequent
;

head
: term
;

antecedent
: body
;

consequent
: head
;

body
: bterm
| bterm AND bterm
| bterm OR bterm
| bterm XOR bterm
;

bterm
: term
| NAF term
;

term
: atom
| NEG atom
;

atom
: IDENTIFIER
;

// Tokens should be before the rest.
// Otherwise, the lexer would take the one which applies most (e.g. identifier)

NEG : 'not' | '-';   // strong negation
NAF : 'naf' ;        // default negation
AND : 'and' | '&&' ;
OR  : 'or'  | '||' ;
XOR : 'xor' ;

SEQ : 'seq' | '&' ; // sequencial operator
PAR : 'par' | '|' ; // parallel operator

IFP : '<|' ; // declarative programming for places
THENP : '|>' ; // declarative programming for places
IFT : '<-' ; // declarative programming for transitions
CAUSEDBY : '<=' ; // reactive programming
CAUSES : '=>' ;

DOT : '.' ;  // end statements

INTEGER : '0'
| [1-9] ([0-9])* ;

IDENTIFIER: [a-z_] ([0-9a-z])*;
VARIABLE: [A-Z] ([0-9a-zA-Z_])*;

// in addition to ASP comments (%)
// I added the standard C, Java comment style.
SINGLE_LINE_COMMENT
: ('%' | '//') ~[\r\n]* -> channel(HIDDEN)
;
MULTILINE_COMMENT
: '/*' .*? ( '*/' | EOF ) -> channel(HIDDEN)
;

// necessary to remove trailing spaces
SPACES
: [ \u000B\t\r\n] -> channel(HIDDEN)
;
