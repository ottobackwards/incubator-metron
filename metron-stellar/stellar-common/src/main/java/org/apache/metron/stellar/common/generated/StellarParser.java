// Generated from org/apache/metron/stellar/common/generated/Stellar.g4 by ANTLR 4.5
package org.apache.metron.stellar.common.generated;

//CHECKSTYLE:OFF
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class StellarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		IN=1, LAMBDA_OP=2, DOUBLE_QUOTE=3, SINGLE_QUOTE=4, COMMA=5, PERIOD=6, 
		AND=7, OR=8, NOT=9, TRUE=10, FALSE=11, EQ=12, NEQ=13, LT=14, LTE=15, GT=16, 
		GTE=17, QUESTION=18, COLON=19, IF=20, THEN=21, ELSE=22, NULL=23, MINUS=24, 
		PLUS=25, DIV=26, MUL=27, LBRACE=28, RBRACE=29, LBRACKET=30, RBRACKET=31, 
		LPAREN=32, RPAREN=33, NIN=34, EXISTS=35, EXPONENT=36, INT_LITERAL=37, 
		DOUBLE_LITERAL=38, FLOAT_LITERAL=39, LONG_LITERAL=40, IDENTIFIER=41, STRING_LITERAL=42, 
		COMMENT=43, WS=44;
	public static final int
		RULE_transformation = 0, RULE_transformation_expr = 1, RULE_if_expr = 2, 
		RULE_then_expr = 3, RULE_else_expr = 4, RULE_conditional_expr = 5, RULE_logical_expr = 6, 
		RULE_b_expr = 7, RULE_in_expr = 8, RULE_comparison_expr = 9, RULE_transformation_entity = 10, 
		RULE_comp_operator = 11, RULE_func_args = 12, RULE_op_list = 13, RULE_list_entity = 14, 
		RULE_kv_list = 15, RULE_map_entity = 16, RULE_arithmetic_expr = 17, RULE_arithmetic_expr_mul = 18, 
		RULE_functions = 19, RULE_arithmetic_operands = 20, RULE_identifier_operand = 21, 
		RULE_lambda_without_args = 22, RULE_lambda_with_args = 23, RULE_lambda_variables = 24, 
		RULE_single_lambda_variable = 25, RULE_lambda_variable = 26;
	public static final String[] ruleNames = {
		"transformation", "transformation_expr", "if_expr", "then_expr", "else_expr", 
		"conditional_expr", "logical_expr", "b_expr", "in_expr", "comparison_expr", 
		"transformation_entity", "comp_operator", "func_args", "op_list", "list_entity", 
		"kv_list", "map_entity", "arithmetic_expr", "arithmetic_expr_mul", "functions", 
		"arithmetic_operands", "identifier_operand", "lambda_without_args", "lambda_with_args", 
		"lambda_variables", "single_lambda_variable", "lambda_variable"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'->'", "'\"'", "'''", "','", "'.'", null, null, null, null, 
		null, "'=='", "'!='", "'<'", "'<='", "'>'", "'>='", "'?'", "':'", null, 
		null, null, null, "'-'", "'+'", "'/'", "'*'", "'{'", "'}'", "'['", "']'", 
		"'('", "')'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "IN", "LAMBDA_OP", "DOUBLE_QUOTE", "SINGLE_QUOTE", "COMMA", "PERIOD", 
		"AND", "OR", "NOT", "TRUE", "FALSE", "EQ", "NEQ", "LT", "LTE", "GT", "GTE", 
		"QUESTION", "COLON", "IF", "THEN", "ELSE", "NULL", "MINUS", "PLUS", "DIV", 
		"MUL", "LBRACE", "RBRACE", "LBRACKET", "RBRACKET", "LPAREN", "RPAREN", 
		"NIN", "EXISTS", "EXPONENT", "INT_LITERAL", "DOUBLE_LITERAL", "FLOAT_LITERAL", 
		"LONG_LITERAL", "IDENTIFIER", "STRING_LITERAL", "COMMENT", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Stellar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public StellarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class TransformationContext extends ParserRuleContext {
		public Transformation_exprContext transformation_expr() {
			return getRuleContext(Transformation_exprContext.class,0);
		}
		public TerminalNode EOF() { return getToken(StellarParser.EOF, 0); }
		public TransformationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transformation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterTransformation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitTransformation(this);
		}
	}

	public final TransformationContext transformation() throws RecognitionException {
		TransformationContext _localctx = new TransformationContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_transformation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			transformation_expr();
			setState(55);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Transformation_exprContext extends ParserRuleContext {
		public Transformation_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transformation_expr; }
	 
		public Transformation_exprContext() { }
		public void copyFrom(Transformation_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ComparisonExpressionContext extends Transformation_exprContext {
		public Comparison_exprContext comparison_expr() {
			return getRuleContext(Comparison_exprContext.class,0);
		}
		public ComparisonExpressionContext(Transformation_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterComparisonExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitComparisonExpression(this);
		}
	}
	public static class LogicalExpressionContext extends Transformation_exprContext {
		public Logical_exprContext logical_expr() {
			return getRuleContext(Logical_exprContext.class,0);
		}
		public LogicalExpressionContext(Transformation_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterLogicalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitLogicalExpression(this);
		}
	}
	public static class TransformationEntityContext extends Transformation_exprContext {
		public Transformation_entityContext transformation_entity() {
			return getRuleContext(Transformation_entityContext.class,0);
		}
		public TransformationEntityContext(Transformation_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterTransformationEntity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitTransformationEntity(this);
		}
	}
	public static class InExpressionContext extends Transformation_exprContext {
		public In_exprContext in_expr() {
			return getRuleContext(In_exprContext.class,0);
		}
		public InExpressionContext(Transformation_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterInExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitInExpression(this);
		}
	}
	public static class ArithExpressionContext extends Transformation_exprContext {
		public Arithmetic_exprContext arithmetic_expr() {
			return getRuleContext(Arithmetic_exprContext.class,0);
		}
		public ArithExpressionContext(Transformation_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterArithExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitArithExpression(this);
		}
	}
	public static class TransformationExprContext extends Transformation_exprContext {
		public TerminalNode LPAREN() { return getToken(StellarParser.LPAREN, 0); }
		public Transformation_exprContext transformation_expr() {
			return getRuleContext(Transformation_exprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(StellarParser.RPAREN, 0); }
		public TransformationExprContext(Transformation_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterTransformationExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitTransformationExpr(this);
		}
	}
	public static class ConditionalExprContext extends Transformation_exprContext {
		public Conditional_exprContext conditional_expr() {
			return getRuleContext(Conditional_exprContext.class,0);
		}
		public ConditionalExprContext(Transformation_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterConditionalExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitConditionalExpr(this);
		}
	}

	public final Transformation_exprContext transformation_expr() throws RecognitionException {
		Transformation_exprContext _localctx = new Transformation_exprContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_transformation_expr);
		try {
			setState(67);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				_localctx = new ConditionalExprContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(57);
				conditional_expr();
				}
				break;
			case 2:
				_localctx = new TransformationExprContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(58);
				match(LPAREN);
				setState(59);
				transformation_expr();
				setState(60);
				match(RPAREN);
				}
				break;
			case 3:
				_localctx = new ArithExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(62);
				arithmetic_expr(0);
				}
				break;
			case 4:
				_localctx = new TransformationEntityContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(63);
				transformation_entity();
				}
				break;
			case 5:
				_localctx = new ComparisonExpressionContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(64);
				comparison_expr(0);
				}
				break;
			case 6:
				_localctx = new LogicalExpressionContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(65);
				logical_expr();
				}
				break;
			case 7:
				_localctx = new InExpressionContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(66);
				in_expr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class If_exprContext extends ParserRuleContext {
		public Logical_exprContext logical_expr() {
			return getRuleContext(Logical_exprContext.class,0);
		}
		public If_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_if_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterIf_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitIf_expr(this);
		}
	}

	public final If_exprContext if_expr() throws RecognitionException {
		If_exprContext _localctx = new If_exprContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_if_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
			logical_expr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Then_exprContext extends ParserRuleContext {
		public Transformation_exprContext transformation_expr() {
			return getRuleContext(Transformation_exprContext.class,0);
		}
		public Then_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_then_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterThen_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitThen_expr(this);
		}
	}

	public final Then_exprContext then_expr() throws RecognitionException {
		Then_exprContext _localctx = new Then_exprContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_then_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			transformation_expr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Else_exprContext extends ParserRuleContext {
		public Transformation_exprContext transformation_expr() {
			return getRuleContext(Transformation_exprContext.class,0);
		}
		public Else_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_else_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterElse_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitElse_expr(this);
		}
	}

	public final Else_exprContext else_expr() throws RecognitionException {
		Else_exprContext _localctx = new Else_exprContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_else_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			transformation_expr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Conditional_exprContext extends ParserRuleContext {
		public Conditional_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditional_expr; }
	 
		public Conditional_exprContext() { }
		public void copyFrom(Conditional_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class TernaryFuncWithoutIfContext extends Conditional_exprContext {
		public If_exprContext if_expr() {
			return getRuleContext(If_exprContext.class,0);
		}
		public TerminalNode QUESTION() { return getToken(StellarParser.QUESTION, 0); }
		public Then_exprContext then_expr() {
			return getRuleContext(Then_exprContext.class,0);
		}
		public TerminalNode COLON() { return getToken(StellarParser.COLON, 0); }
		public Else_exprContext else_expr() {
			return getRuleContext(Else_exprContext.class,0);
		}
		public TernaryFuncWithoutIfContext(Conditional_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterTernaryFuncWithoutIf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitTernaryFuncWithoutIf(this);
		}
	}
	public static class TernaryFuncWithIfContext extends Conditional_exprContext {
		public TerminalNode IF() { return getToken(StellarParser.IF, 0); }
		public If_exprContext if_expr() {
			return getRuleContext(If_exprContext.class,0);
		}
		public TerminalNode THEN() { return getToken(StellarParser.THEN, 0); }
		public Then_exprContext then_expr() {
			return getRuleContext(Then_exprContext.class,0);
		}
		public TerminalNode ELSE() { return getToken(StellarParser.ELSE, 0); }
		public Else_exprContext else_expr() {
			return getRuleContext(Else_exprContext.class,0);
		}
		public TernaryFuncWithIfContext(Conditional_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterTernaryFuncWithIf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitTernaryFuncWithIf(this);
		}
	}
	public static class TernaryWithIfNoElseContext extends Conditional_exprContext {
		public TerminalNode IF() { return getToken(StellarParser.IF, 0); }
		public If_exprContext if_expr() {
			return getRuleContext(If_exprContext.class,0);
		}
		public TerminalNode THEN() { return getToken(StellarParser.THEN, 0); }
		public Then_exprContext then_expr() {
			return getRuleContext(Then_exprContext.class,0);
		}
		public TernaryWithIfNoElseContext(Conditional_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterTernaryWithIfNoElse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitTernaryWithIfNoElse(this);
		}
	}

	public final Conditional_exprContext conditional_expr() throws RecognitionException {
		Conditional_exprContext _localctx = new Conditional_exprContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_conditional_expr);
		try {
			setState(93);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				_localctx = new TernaryFuncWithoutIfContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(75);
				if_expr();
				setState(76);
				match(QUESTION);
				setState(77);
				then_expr();
				setState(78);
				match(COLON);
				setState(79);
				else_expr();
				}
				break;
			case 2:
				_localctx = new TernaryWithIfNoElseContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(81);
				match(IF);
				setState(82);
				if_expr();
				setState(83);
				match(THEN);
				setState(84);
				then_expr();
				}
				break;
			case 3:
				_localctx = new TernaryFuncWithIfContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(86);
				match(IF);
				setState(87);
				if_expr();
				setState(88);
				match(THEN);
				setState(89);
				then_expr();
				setState(90);
				match(ELSE);
				setState(91);
				else_expr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Logical_exprContext extends ParserRuleContext {
		public Logical_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logical_expr; }
	 
		public Logical_exprContext() { }
		public void copyFrom(Logical_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class LogicalExpressionAndContext extends Logical_exprContext {
		public B_exprContext b_expr() {
			return getRuleContext(B_exprContext.class,0);
		}
		public TerminalNode AND() { return getToken(StellarParser.AND, 0); }
		public Logical_exprContext logical_expr() {
			return getRuleContext(Logical_exprContext.class,0);
		}
		public LogicalExpressionAndContext(Logical_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterLogicalExpressionAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitLogicalExpressionAnd(this);
		}
	}
	public static class BoleanExpressionContext extends Logical_exprContext {
		public B_exprContext b_expr() {
			return getRuleContext(B_exprContext.class,0);
		}
		public BoleanExpressionContext(Logical_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterBoleanExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitBoleanExpression(this);
		}
	}
	public static class LogicalExpressionOrContext extends Logical_exprContext {
		public B_exprContext b_expr() {
			return getRuleContext(B_exprContext.class,0);
		}
		public TerminalNode OR() { return getToken(StellarParser.OR, 0); }
		public Logical_exprContext logical_expr() {
			return getRuleContext(Logical_exprContext.class,0);
		}
		public LogicalExpressionOrContext(Logical_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterLogicalExpressionOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitLogicalExpressionOr(this);
		}
	}

	public final Logical_exprContext logical_expr() throws RecognitionException {
		Logical_exprContext _localctx = new Logical_exprContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_logical_expr);
		try {
			setState(104);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				_localctx = new LogicalExpressionAndContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(95);
				b_expr();
				setState(96);
				match(AND);
				setState(97);
				logical_expr();
				}
				break;
			case 2:
				_localctx = new LogicalExpressionOrContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(99);
				b_expr();
				setState(100);
				match(OR);
				setState(101);
				logical_expr();
				}
				break;
			case 3:
				_localctx = new BoleanExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(103);
				b_expr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class B_exprContext extends ParserRuleContext {
		public Comparison_exprContext comparison_expr() {
			return getRuleContext(Comparison_exprContext.class,0);
		}
		public In_exprContext in_expr() {
			return getRuleContext(In_exprContext.class,0);
		}
		public B_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_b_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterB_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitB_expr(this);
		}
	}

	public final B_exprContext b_expr() throws RecognitionException {
		B_exprContext _localctx = new B_exprContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_b_expr);
		try {
			setState(108);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(106);
				comparison_expr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(107);
				in_expr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class In_exprContext extends ParserRuleContext {
		public In_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_in_expr; }
	 
		public In_exprContext() { }
		public void copyFrom(In_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NInExpressionStatementContext extends In_exprContext {
		public Identifier_operandContext identifier_operand() {
			return getRuleContext(Identifier_operandContext.class,0);
		}
		public TerminalNode NIN() { return getToken(StellarParser.NIN, 0); }
		public B_exprContext b_expr() {
			return getRuleContext(B_exprContext.class,0);
		}
		public NInExpressionStatementContext(In_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterNInExpressionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitNInExpressionStatement(this);
		}
	}
	public static class InExpressionStatementContext extends In_exprContext {
		public Identifier_operandContext identifier_operand() {
			return getRuleContext(Identifier_operandContext.class,0);
		}
		public TerminalNode IN() { return getToken(StellarParser.IN, 0); }
		public B_exprContext b_expr() {
			return getRuleContext(B_exprContext.class,0);
		}
		public InExpressionStatementContext(In_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterInExpressionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitInExpressionStatement(this);
		}
	}

	public final In_exprContext in_expr() throws RecognitionException {
		In_exprContext _localctx = new In_exprContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_in_expr);
		try {
			setState(118);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				_localctx = new InExpressionStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(110);
				identifier_operand();
				setState(111);
				match(IN);
				setState(112);
				b_expr();
				}
				break;
			case 2:
				_localctx = new NInExpressionStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(114);
				identifier_operand();
				setState(115);
				match(NIN);
				setState(116);
				b_expr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Comparison_exprContext extends ParserRuleContext {
		public Comparison_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparison_expr; }
	 
		public Comparison_exprContext() { }
		public void copyFrom(Comparison_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NotFuncContext extends Comparison_exprContext {
		public TerminalNode NOT() { return getToken(StellarParser.NOT, 0); }
		public TerminalNode LPAREN() { return getToken(StellarParser.LPAREN, 0); }
		public Logical_exprContext logical_expr() {
			return getRuleContext(Logical_exprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(StellarParser.RPAREN, 0); }
		public NotFuncContext(Comparison_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterNotFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitNotFunc(this);
		}
	}
	public static class ComparisonExpressionParensContext extends Comparison_exprContext {
		public TerminalNode LPAREN() { return getToken(StellarParser.LPAREN, 0); }
		public Logical_exprContext logical_expr() {
			return getRuleContext(Logical_exprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(StellarParser.RPAREN, 0); }
		public ComparisonExpressionParensContext(Comparison_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterComparisonExpressionParens(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitComparisonExpressionParens(this);
		}
	}
	public static class ComparisonExpressionWithOperatorContext extends Comparison_exprContext {
		public List<Comparison_exprContext> comparison_expr() {
			return getRuleContexts(Comparison_exprContext.class);
		}
		public Comparison_exprContext comparison_expr(int i) {
			return getRuleContext(Comparison_exprContext.class,i);
		}
		public Comp_operatorContext comp_operator() {
			return getRuleContext(Comp_operatorContext.class,0);
		}
		public ComparisonExpressionWithOperatorContext(Comparison_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterComparisonExpressionWithOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitComparisonExpressionWithOperator(this);
		}
	}
	public static class OperandContext extends Comparison_exprContext {
		public Identifier_operandContext identifier_operand() {
			return getRuleContext(Identifier_operandContext.class,0);
		}
		public OperandContext(Comparison_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterOperand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitOperand(this);
		}
	}

	public final Comparison_exprContext comparison_expr() throws RecognitionException {
		return comparison_expr(0);
	}

	private Comparison_exprContext comparison_expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Comparison_exprContext _localctx = new Comparison_exprContext(_ctx, _parentState);
		Comparison_exprContext _prevctx = _localctx;
		int _startState = 18;
		enterRecursionRule(_localctx, 18, RULE_comparison_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(131);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				_localctx = new NotFuncContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(121);
				match(NOT);
				setState(122);
				match(LPAREN);
				setState(123);
				logical_expr();
				setState(124);
				match(RPAREN);
				}
				break;
			case 2:
				{
				_localctx = new ComparisonExpressionParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(126);
				match(LPAREN);
				setState(127);
				logical_expr();
				setState(128);
				match(RPAREN);
				}
				break;
			case 3:
				{
				_localctx = new OperandContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(130);
				identifier_operand();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(139);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ComparisonExpressionWithOperatorContext(new Comparison_exprContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_comparison_expr);
					setState(133);
					if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
					setState(134);
					comp_operator();
					setState(135);
					comparison_expr(5);
					}
					} 
				}
				setState(141);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Transformation_entityContext extends ParserRuleContext {
		public Identifier_operandContext identifier_operand() {
			return getRuleContext(Identifier_operandContext.class,0);
		}
		public Transformation_entityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transformation_entity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterTransformation_entity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitTransformation_entity(this);
		}
	}

	public final Transformation_entityContext transformation_entity() throws RecognitionException {
		Transformation_entityContext _localctx = new Transformation_entityContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_transformation_entity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			identifier_operand();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Comp_operatorContext extends ParserRuleContext {
		public Comp_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comp_operator; }
	 
		public Comp_operatorContext() { }
		public void copyFrom(Comp_operatorContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ComparisonOpContext extends Comp_operatorContext {
		public TerminalNode EQ() { return getToken(StellarParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(StellarParser.NEQ, 0); }
		public TerminalNode LT() { return getToken(StellarParser.LT, 0); }
		public TerminalNode LTE() { return getToken(StellarParser.LTE, 0); }
		public TerminalNode GT() { return getToken(StellarParser.GT, 0); }
		public TerminalNode GTE() { return getToken(StellarParser.GTE, 0); }
		public ComparisonOpContext(Comp_operatorContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterComparisonOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitComparisonOp(this);
		}
	}

	public final Comp_operatorContext comp_operator() throws RecognitionException {
		Comp_operatorContext _localctx = new Comp_operatorContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_comp_operator);
		int _la;
		try {
			_localctx = new ComparisonOpContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQ) | (1L << NEQ) | (1L << LT) | (1L << LTE) | (1L << GT) | (1L << GTE))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Func_argsContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(StellarParser.LPAREN, 0); }
		public Op_listContext op_list() {
			return getRuleContext(Op_listContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(StellarParser.RPAREN, 0); }
		public Func_argsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_func_args; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterFunc_args(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitFunc_args(this);
		}
	}

	public final Func_argsContext func_args() throws RecognitionException {
		Func_argsContext _localctx = new Func_argsContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_func_args);
		try {
			setState(152);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(146);
				match(LPAREN);
				setState(147);
				op_list(0);
				setState(148);
				match(RPAREN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(150);
				match(LPAREN);
				setState(151);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Op_listContext extends ParserRuleContext {
		public Identifier_operandContext identifier_operand() {
			return getRuleContext(Identifier_operandContext.class,0);
		}
		public Conditional_exprContext conditional_expr() {
			return getRuleContext(Conditional_exprContext.class,0);
		}
		public Op_listContext op_list() {
			return getRuleContext(Op_listContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(StellarParser.COMMA, 0); }
		public Op_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterOp_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitOp_list(this);
		}
	}

	public final Op_listContext op_list() throws RecognitionException {
		return op_list(0);
	}

	private Op_listContext op_list(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Op_listContext _localctx = new Op_listContext(_ctx, _parentState);
		Op_listContext _prevctx = _localctx;
		int _startState = 26;
		enterRecursionRule(_localctx, 26, RULE_op_list, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(155);
				identifier_operand();
				}
				break;
			case 2:
				{
				setState(156);
				conditional_expr();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(167);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(165);
					switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
					case 1:
						{
						_localctx = new Op_listContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_op_list);
						setState(159);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(160);
						match(COMMA);
						setState(161);
						identifier_operand();
						}
						break;
					case 2:
						{
						_localctx = new Op_listContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_op_list);
						setState(162);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(163);
						match(COMMA);
						setState(164);
						conditional_expr();
						}
						break;
					}
					} 
				}
				setState(169);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class List_entityContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(StellarParser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(StellarParser.RBRACKET, 0); }
		public Op_listContext op_list() {
			return getRuleContext(Op_listContext.class,0);
		}
		public List_entityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_list_entity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterList_entity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitList_entity(this);
		}
	}

	public final List_entityContext list_entity() throws RecognitionException {
		List_entityContext _localctx = new List_entityContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_list_entity);
		try {
			setState(176);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(170);
				match(LBRACKET);
				setState(171);
				match(RBRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(172);
				match(LBRACKET);
				setState(173);
				op_list(0);
				setState(174);
				match(RBRACKET);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Kv_listContext extends ParserRuleContext {
		public Identifier_operandContext identifier_operand() {
			return getRuleContext(Identifier_operandContext.class,0);
		}
		public TerminalNode COLON() { return getToken(StellarParser.COLON, 0); }
		public Transformation_exprContext transformation_expr() {
			return getRuleContext(Transformation_exprContext.class,0);
		}
		public Kv_listContext kv_list() {
			return getRuleContext(Kv_listContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(StellarParser.COMMA, 0); }
		public Kv_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_kv_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterKv_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitKv_list(this);
		}
	}

	public final Kv_listContext kv_list() throws RecognitionException {
		return kv_list(0);
	}

	private Kv_listContext kv_list(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Kv_listContext _localctx = new Kv_listContext(_ctx, _parentState);
		Kv_listContext _prevctx = _localctx;
		int _startState = 30;
		enterRecursionRule(_localctx, 30, RULE_kv_list, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(179);
			identifier_operand();
			setState(180);
			match(COLON);
			setState(181);
			transformation_expr();
			}
			_ctx.stop = _input.LT(-1);
			setState(191);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new Kv_listContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_kv_list);
					setState(183);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(184);
					match(COMMA);
					setState(185);
					identifier_operand();
					setState(186);
					match(COLON);
					setState(187);
					transformation_expr();
					}
					} 
				}
				setState(193);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Map_entityContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(StellarParser.LBRACE, 0); }
		public Kv_listContext kv_list() {
			return getRuleContext(Kv_listContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(StellarParser.RBRACE, 0); }
		public Map_entityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_map_entity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterMap_entity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitMap_entity(this);
		}
	}

	public final Map_entityContext map_entity() throws RecognitionException {
		Map_entityContext _localctx = new Map_entityContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_map_entity);
		try {
			setState(200);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(194);
				match(LBRACE);
				setState(195);
				kv_list(0);
				setState(196);
				match(RBRACE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(198);
				match(LBRACE);
				setState(199);
				match(RBRACE);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Arithmetic_exprContext extends ParserRuleContext {
		public Arithmetic_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arithmetic_expr; }
	 
		public Arithmetic_exprContext() { }
		public void copyFrom(Arithmetic_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ArithExpr_soloContext extends Arithmetic_exprContext {
		public Arithmetic_expr_mulContext arithmetic_expr_mul() {
			return getRuleContext(Arithmetic_expr_mulContext.class,0);
		}
		public ArithExpr_soloContext(Arithmetic_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterArithExpr_solo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitArithExpr_solo(this);
		}
	}
	public static class ArithExpr_minusContext extends Arithmetic_exprContext {
		public Arithmetic_exprContext arithmetic_expr() {
			return getRuleContext(Arithmetic_exprContext.class,0);
		}
		public TerminalNode MINUS() { return getToken(StellarParser.MINUS, 0); }
		public Arithmetic_expr_mulContext arithmetic_expr_mul() {
			return getRuleContext(Arithmetic_expr_mulContext.class,0);
		}
		public ArithExpr_minusContext(Arithmetic_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterArithExpr_minus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitArithExpr_minus(this);
		}
	}
	public static class ArithExpr_plusContext extends Arithmetic_exprContext {
		public Arithmetic_exprContext arithmetic_expr() {
			return getRuleContext(Arithmetic_exprContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(StellarParser.PLUS, 0); }
		public Arithmetic_expr_mulContext arithmetic_expr_mul() {
			return getRuleContext(Arithmetic_expr_mulContext.class,0);
		}
		public ArithExpr_plusContext(Arithmetic_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterArithExpr_plus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitArithExpr_plus(this);
		}
	}

	public final Arithmetic_exprContext arithmetic_expr() throws RecognitionException {
		return arithmetic_expr(0);
	}

	private Arithmetic_exprContext arithmetic_expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Arithmetic_exprContext _localctx = new Arithmetic_exprContext(_ctx, _parentState);
		Arithmetic_exprContext _prevctx = _localctx;
		int _startState = 34;
		enterRecursionRule(_localctx, 34, RULE_arithmetic_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new ArithExpr_soloContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(203);
			arithmetic_expr_mul(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(213);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(211);
					switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
					case 1:
						{
						_localctx = new ArithExpr_plusContext(new Arithmetic_exprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_arithmetic_expr);
						setState(205);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(206);
						match(PLUS);
						setState(207);
						arithmetic_expr_mul(0);
						}
						break;
					case 2:
						{
						_localctx = new ArithExpr_minusContext(new Arithmetic_exprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_arithmetic_expr);
						setState(208);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(209);
						match(MINUS);
						setState(210);
						arithmetic_expr_mul(0);
						}
						break;
					}
					} 
				}
				setState(215);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Arithmetic_expr_mulContext extends ParserRuleContext {
		public Arithmetic_expr_mulContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arithmetic_expr_mul; }
	 
		public Arithmetic_expr_mulContext() { }
		public void copyFrom(Arithmetic_expr_mulContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ArithExpr_divContext extends Arithmetic_expr_mulContext {
		public List<Arithmetic_expr_mulContext> arithmetic_expr_mul() {
			return getRuleContexts(Arithmetic_expr_mulContext.class);
		}
		public Arithmetic_expr_mulContext arithmetic_expr_mul(int i) {
			return getRuleContext(Arithmetic_expr_mulContext.class,i);
		}
		public TerminalNode DIV() { return getToken(StellarParser.DIV, 0); }
		public ArithExpr_divContext(Arithmetic_expr_mulContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterArithExpr_div(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitArithExpr_div(this);
		}
	}
	public static class ArithExpr_mul_soloContext extends Arithmetic_expr_mulContext {
		public Arithmetic_operandsContext arithmetic_operands() {
			return getRuleContext(Arithmetic_operandsContext.class,0);
		}
		public ArithExpr_mul_soloContext(Arithmetic_expr_mulContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterArithExpr_mul_solo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitArithExpr_mul_solo(this);
		}
	}
	public static class ArithExpr_mulContext extends Arithmetic_expr_mulContext {
		public List<Arithmetic_expr_mulContext> arithmetic_expr_mul() {
			return getRuleContexts(Arithmetic_expr_mulContext.class);
		}
		public Arithmetic_expr_mulContext arithmetic_expr_mul(int i) {
			return getRuleContext(Arithmetic_expr_mulContext.class,i);
		}
		public TerminalNode MUL() { return getToken(StellarParser.MUL, 0); }
		public ArithExpr_mulContext(Arithmetic_expr_mulContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterArithExpr_mul(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitArithExpr_mul(this);
		}
	}

	public final Arithmetic_expr_mulContext arithmetic_expr_mul() throws RecognitionException {
		return arithmetic_expr_mul(0);
	}

	private Arithmetic_expr_mulContext arithmetic_expr_mul(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Arithmetic_expr_mulContext _localctx = new Arithmetic_expr_mulContext(_ctx, _parentState);
		Arithmetic_expr_mulContext _prevctx = _localctx;
		int _startState = 36;
		enterRecursionRule(_localctx, 36, RULE_arithmetic_expr_mul, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new ArithExpr_mul_soloContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(217);
			arithmetic_operands();
			}
			_ctx.stop = _input.LT(-1);
			setState(227);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(225);
					switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
					case 1:
						{
						_localctx = new ArithExpr_mulContext(new Arithmetic_expr_mulContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_arithmetic_expr_mul);
						setState(219);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(220);
						match(MUL);
						setState(221);
						arithmetic_expr_mul(3);
						}
						break;
					case 2:
						{
						_localctx = new ArithExpr_divContext(new Arithmetic_expr_mulContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_arithmetic_expr_mul);
						setState(222);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(223);
						match(DIV);
						setState(224);
						arithmetic_expr_mul(2);
						}
						break;
					}
					} 
				}
				setState(229);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class FunctionsContext extends ParserRuleContext {
		public FunctionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functions; }
	 
		public FunctionsContext() { }
		public void copyFrom(FunctionsContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class TransformationFuncContext extends FunctionsContext {
		public TerminalNode IDENTIFIER() { return getToken(StellarParser.IDENTIFIER, 0); }
		public Func_argsContext func_args() {
			return getRuleContext(Func_argsContext.class,0);
		}
		public TransformationFuncContext(FunctionsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterTransformationFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitTransformationFunc(this);
		}
	}

	public final FunctionsContext functions() throws RecognitionException {
		FunctionsContext _localctx = new FunctionsContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_functions);
		try {
			_localctx = new TransformationFuncContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			match(IDENTIFIER);
			setState(231);
			func_args();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Arithmetic_operandsContext extends ParserRuleContext {
		public Arithmetic_operandsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arithmetic_operands; }
	 
		public Arithmetic_operandsContext() { }
		public void copyFrom(Arithmetic_operandsContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class VariableContext extends Arithmetic_operandsContext {
		public TerminalNode IDENTIFIER() { return getToken(StellarParser.IDENTIFIER, 0); }
		public VariableContext(Arithmetic_operandsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitVariable(this);
		}
	}
	public static class NumericFunctionsContext extends Arithmetic_operandsContext {
		public FunctionsContext functions() {
			return getRuleContext(FunctionsContext.class,0);
		}
		public NumericFunctionsContext(Arithmetic_operandsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterNumericFunctions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitNumericFunctions(this);
		}
	}
	public static class LongLiteralContext extends Arithmetic_operandsContext {
		public TerminalNode LONG_LITERAL() { return getToken(StellarParser.LONG_LITERAL, 0); }
		public LongLiteralContext(Arithmetic_operandsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterLongLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitLongLiteral(this);
		}
	}
	public static class FloatLiteralContext extends Arithmetic_operandsContext {
		public TerminalNode FLOAT_LITERAL() { return getToken(StellarParser.FLOAT_LITERAL, 0); }
		public FloatLiteralContext(Arithmetic_operandsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterFloatLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitFloatLiteral(this);
		}
	}
	public static class CondExprContext extends Arithmetic_operandsContext {
		public TerminalNode LPAREN() { return getToken(StellarParser.LPAREN, 0); }
		public Conditional_exprContext conditional_expr() {
			return getRuleContext(Conditional_exprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(StellarParser.RPAREN, 0); }
		public CondExprContext(Arithmetic_operandsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterCondExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitCondExpr(this);
		}
	}
	public static class ParenArithContext extends Arithmetic_operandsContext {
		public TerminalNode LPAREN() { return getToken(StellarParser.LPAREN, 0); }
		public Arithmetic_exprContext arithmetic_expr() {
			return getRuleContext(Arithmetic_exprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(StellarParser.RPAREN, 0); }
		public ParenArithContext(Arithmetic_operandsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterParenArith(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitParenArith(this);
		}
	}
	public static class IntLiteralContext extends Arithmetic_operandsContext {
		public TerminalNode INT_LITERAL() { return getToken(StellarParser.INT_LITERAL, 0); }
		public IntLiteralContext(Arithmetic_operandsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterIntLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitIntLiteral(this);
		}
	}
	public static class DoubleLiteralContext extends Arithmetic_operandsContext {
		public TerminalNode DOUBLE_LITERAL() { return getToken(StellarParser.DOUBLE_LITERAL, 0); }
		public DoubleLiteralContext(Arithmetic_operandsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterDoubleLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitDoubleLiteral(this);
		}
	}

	public final Arithmetic_operandsContext arithmetic_operands() throws RecognitionException {
		Arithmetic_operandsContext _localctx = new Arithmetic_operandsContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_arithmetic_operands);
		try {
			setState(247);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				_localctx = new NumericFunctionsContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(233);
				functions();
				}
				break;
			case 2:
				_localctx = new DoubleLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(234);
				match(DOUBLE_LITERAL);
				}
				break;
			case 3:
				_localctx = new IntLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(235);
				match(INT_LITERAL);
				}
				break;
			case 4:
				_localctx = new LongLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(236);
				match(LONG_LITERAL);
				}
				break;
			case 5:
				_localctx = new FloatLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(237);
				match(FLOAT_LITERAL);
				}
				break;
			case 6:
				_localctx = new VariableContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(238);
				match(IDENTIFIER);
				}
				break;
			case 7:
				_localctx = new ParenArithContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(239);
				match(LPAREN);
				setState(240);
				arithmetic_expr(0);
				setState(241);
				match(RPAREN);
				}
				break;
			case 8:
				_localctx = new CondExprContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(243);
				match(LPAREN);
				setState(244);
				conditional_expr();
				setState(245);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Identifier_operandContext extends ParserRuleContext {
		public Identifier_operandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier_operand; }
	 
		public Identifier_operandContext() { }
		public void copyFrom(Identifier_operandContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ArithmeticOperandsContext extends Identifier_operandContext {
		public Arithmetic_exprContext arithmetic_expr() {
			return getRuleContext(Arithmetic_exprContext.class,0);
		}
		public ArithmeticOperandsContext(Identifier_operandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterArithmeticOperands(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitArithmeticOperands(this);
		}
	}
	public static class LambdaWithArgsExprContext extends Identifier_operandContext {
		public Lambda_with_argsContext lambda_with_args() {
			return getRuleContext(Lambda_with_argsContext.class,0);
		}
		public LambdaWithArgsExprContext(Identifier_operandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterLambdaWithArgsExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitLambdaWithArgsExpr(this);
		}
	}
	public static class StringLiteralContext extends Identifier_operandContext {
		public TerminalNode STRING_LITERAL() { return getToken(StellarParser.STRING_LITERAL, 0); }
		public StringLiteralContext(Identifier_operandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitStringLiteral(this);
		}
	}
	public static class LambdaWithoutArgsExprContext extends Identifier_operandContext {
		public Lambda_without_argsContext lambda_without_args() {
			return getRuleContext(Lambda_without_argsContext.class,0);
		}
		public LambdaWithoutArgsExprContext(Identifier_operandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterLambdaWithoutArgsExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitLambdaWithoutArgsExpr(this);
		}
	}
	public static class ListContext extends Identifier_operandContext {
		public List_entityContext list_entity() {
			return getRuleContext(List_entityContext.class,0);
		}
		public ListContext(Identifier_operandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitList(this);
		}
	}
	public static class MapConstContext extends Identifier_operandContext {
		public Map_entityContext map_entity() {
			return getRuleContext(Map_entityContext.class,0);
		}
		public MapConstContext(Identifier_operandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterMapConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitMapConst(this);
		}
	}
	public static class LogicalConstContext extends Identifier_operandContext {
		public TerminalNode TRUE() { return getToken(StellarParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(StellarParser.FALSE, 0); }
		public LogicalConstContext(Identifier_operandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterLogicalConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitLogicalConst(this);
		}
	}
	public static class NullConstContext extends Identifier_operandContext {
		public TerminalNode NULL() { return getToken(StellarParser.NULL, 0); }
		public NullConstContext(Identifier_operandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterNullConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitNullConst(this);
		}
	}
	public static class ExistsFuncContext extends Identifier_operandContext {
		public TerminalNode EXISTS() { return getToken(StellarParser.EXISTS, 0); }
		public TerminalNode LPAREN() { return getToken(StellarParser.LPAREN, 0); }
		public TerminalNode IDENTIFIER() { return getToken(StellarParser.IDENTIFIER, 0); }
		public TerminalNode RPAREN() { return getToken(StellarParser.RPAREN, 0); }
		public ExistsFuncContext(Identifier_operandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterExistsFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitExistsFunc(this);
		}
	}
	public static class CondExpr_parenContext extends Identifier_operandContext {
		public TerminalNode LPAREN() { return getToken(StellarParser.LPAREN, 0); }
		public Conditional_exprContext conditional_expr() {
			return getRuleContext(Conditional_exprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(StellarParser.RPAREN, 0); }
		public CondExpr_parenContext(Identifier_operandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterCondExpr_paren(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitCondExpr_paren(this);
		}
	}

	public final Identifier_operandContext identifier_operand() throws RecognitionException {
		Identifier_operandContext _localctx = new Identifier_operandContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_identifier_operand);
		int _la;
		try {
			setState(265);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				_localctx = new LogicalConstContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(249);
				_la = _input.LA(1);
				if ( !(_la==TRUE || _la==FALSE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				break;
			case 2:
				_localctx = new LambdaWithArgsExprContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(250);
				lambda_with_args();
				}
				break;
			case 3:
				_localctx = new LambdaWithoutArgsExprContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(251);
				lambda_without_args();
				}
				break;
			case 4:
				_localctx = new ArithmeticOperandsContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(252);
				arithmetic_expr(0);
				}
				break;
			case 5:
				_localctx = new StringLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(253);
				match(STRING_LITERAL);
				}
				break;
			case 6:
				_localctx = new ListContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(254);
				list_entity();
				}
				break;
			case 7:
				_localctx = new MapConstContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(255);
				map_entity();
				}
				break;
			case 8:
				_localctx = new NullConstContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(256);
				match(NULL);
				}
				break;
			case 9:
				_localctx = new ExistsFuncContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(257);
				match(EXISTS);
				setState(258);
				match(LPAREN);
				setState(259);
				match(IDENTIFIER);
				setState(260);
				match(RPAREN);
				}
				break;
			case 10:
				_localctx = new CondExpr_parenContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(261);
				match(LPAREN);
				setState(262);
				conditional_expr();
				setState(263);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Lambda_without_argsContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(StellarParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(StellarParser.RPAREN, 0); }
		public TerminalNode LAMBDA_OP() { return getToken(StellarParser.LAMBDA_OP, 0); }
		public Transformation_exprContext transformation_expr() {
			return getRuleContext(Transformation_exprContext.class,0);
		}
		public Lambda_without_argsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambda_without_args; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterLambda_without_args(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitLambda_without_args(this);
		}
	}

	public final Lambda_without_argsContext lambda_without_args() throws RecognitionException {
		Lambda_without_argsContext _localctx = new Lambda_without_argsContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_lambda_without_args);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(267);
			match(LPAREN);
			setState(268);
			match(RPAREN);
			setState(269);
			match(LAMBDA_OP);
			setState(270);
			transformation_expr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Lambda_with_argsContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(StellarParser.LPAREN, 0); }
		public Lambda_variablesContext lambda_variables() {
			return getRuleContext(Lambda_variablesContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(StellarParser.RPAREN, 0); }
		public TerminalNode LAMBDA_OP() { return getToken(StellarParser.LAMBDA_OP, 0); }
		public Transformation_exprContext transformation_expr() {
			return getRuleContext(Transformation_exprContext.class,0);
		}
		public Single_lambda_variableContext single_lambda_variable() {
			return getRuleContext(Single_lambda_variableContext.class,0);
		}
		public Lambda_with_argsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambda_with_args; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterLambda_with_args(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitLambda_with_args(this);
		}
	}

	public final Lambda_with_argsContext lambda_with_args() throws RecognitionException {
		Lambda_with_argsContext _localctx = new Lambda_with_argsContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_lambda_with_args);
		try {
			setState(282);
			switch (_input.LA(1)) {
			case LPAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(272);
				match(LPAREN);
				setState(273);
				lambda_variables();
				setState(274);
				match(RPAREN);
				setState(275);
				match(LAMBDA_OP);
				setState(276);
				transformation_expr();
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(278);
				single_lambda_variable();
				setState(279);
				match(LAMBDA_OP);
				setState(280);
				transformation_expr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Lambda_variablesContext extends ParserRuleContext {
		public List<Lambda_variableContext> lambda_variable() {
			return getRuleContexts(Lambda_variableContext.class);
		}
		public Lambda_variableContext lambda_variable(int i) {
			return getRuleContext(Lambda_variableContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(StellarParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(StellarParser.COMMA, i);
		}
		public Lambda_variablesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambda_variables; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterLambda_variables(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitLambda_variables(this);
		}
	}

	public final Lambda_variablesContext lambda_variables() throws RecognitionException {
		Lambda_variablesContext _localctx = new Lambda_variablesContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_lambda_variables);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(284);
			lambda_variable();
			setState(289);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(285);
				match(COMMA);
				setState(286);
				lambda_variable();
				}
				}
				setState(291);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Single_lambda_variableContext extends ParserRuleContext {
		public Lambda_variableContext lambda_variable() {
			return getRuleContext(Lambda_variableContext.class,0);
		}
		public Single_lambda_variableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_single_lambda_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterSingle_lambda_variable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitSingle_lambda_variable(this);
		}
	}

	public final Single_lambda_variableContext single_lambda_variable() throws RecognitionException {
		Single_lambda_variableContext _localctx = new Single_lambda_variableContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_single_lambda_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(292);
			lambda_variable();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Lambda_variableContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(StellarParser.IDENTIFIER, 0); }
		public Lambda_variableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambda_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).enterLambda_variable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StellarListener ) ((StellarListener)listener).exitLambda_variable(this);
		}
	}

	public final Lambda_variableContext lambda_variable() throws RecognitionException {
		Lambda_variableContext _localctx = new Lambda_variableContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_lambda_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(294);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 9:
			return comparison_expr_sempred((Comparison_exprContext)_localctx, predIndex);
		case 13:
			return op_list_sempred((Op_listContext)_localctx, predIndex);
		case 15:
			return kv_list_sempred((Kv_listContext)_localctx, predIndex);
		case 17:
			return arithmetic_expr_sempred((Arithmetic_exprContext)_localctx, predIndex);
		case 18:
			return arithmetic_expr_mul_sempred((Arithmetic_expr_mulContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean comparison_expr_sempred(Comparison_exprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 4);
		}
		return true;
	}
	private boolean op_list_sempred(Op_listContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 3);
		case 2:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean kv_list_sempred(Kv_listContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean arithmetic_expr_sempred(Arithmetic_exprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return precpred(_ctx, 2);
		case 5:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean arithmetic_expr_mul_sempred(Arithmetic_expr_mulContext _localctx, int predIndex) {
		switch (predIndex) {
		case 6:
			return precpred(_ctx, 2);
		case 7:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3.\u012b\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\5\3F\n\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7`\n\7\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\5\bk\n\b\3\t\3\t\5\to\n\t\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\5\ny\n\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\5\13\u0086\n\13\3\13\3\13\3\13\3\13\7\13\u008c\n\13\f\13\16\13\u008f"+
		"\13\13\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u009b\n\16\3"+
		"\17\3\17\3\17\5\17\u00a0\n\17\3\17\3\17\3\17\3\17\3\17\3\17\7\17\u00a8"+
		"\n\17\f\17\16\17\u00ab\13\17\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00b3"+
		"\n\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\7\21\u00c0"+
		"\n\21\f\21\16\21\u00c3\13\21\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u00cb"+
		"\n\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\7\23\u00d6\n\23\f\23"+
		"\16\23\u00d9\13\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\7\24\u00e4"+
		"\n\24\f\24\16\24\u00e7\13\24\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3"+
		"\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\5\26\u00fa\n\26\3\27\3\27"+
		"\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\5\27\u010c\n\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\5\31\u011d\n\31\3\32\3\32\3\32\7\32\u0122\n\32\f"+
		"\32\16\32\u0125\13\32\3\33\3\33\3\34\3\34\3\34\2\7\24\34 $&\35\2\4\6\b"+
		"\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\66\2\4\3\2\16\23\3\2"+
		"\f\r\u013b\28\3\2\2\2\4E\3\2\2\2\6G\3\2\2\2\bI\3\2\2\2\nK\3\2\2\2\f_\3"+
		"\2\2\2\16j\3\2\2\2\20n\3\2\2\2\22x\3\2\2\2\24\u0085\3\2\2\2\26\u0090\3"+
		"\2\2\2\30\u0092\3\2\2\2\32\u009a\3\2\2\2\34\u009f\3\2\2\2\36\u00b2\3\2"+
		"\2\2 \u00b4\3\2\2\2\"\u00ca\3\2\2\2$\u00cc\3\2\2\2&\u00da\3\2\2\2(\u00e8"+
		"\3\2\2\2*\u00f9\3\2\2\2,\u010b\3\2\2\2.\u010d\3\2\2\2\60\u011c\3\2\2\2"+
		"\62\u011e\3\2\2\2\64\u0126\3\2\2\2\66\u0128\3\2\2\289\5\4\3\29:\7\2\2"+
		"\3:\3\3\2\2\2;F\5\f\7\2<=\7\"\2\2=>\5\4\3\2>?\7#\2\2?F\3\2\2\2@F\5$\23"+
		"\2AF\5\26\f\2BF\5\24\13\2CF\5\16\b\2DF\5\22\n\2E;\3\2\2\2E<\3\2\2\2E@"+
		"\3\2\2\2EA\3\2\2\2EB\3\2\2\2EC\3\2\2\2ED\3\2\2\2F\5\3\2\2\2GH\5\16\b\2"+
		"H\7\3\2\2\2IJ\5\4\3\2J\t\3\2\2\2KL\5\4\3\2L\13\3\2\2\2MN\5\6\4\2NO\7\24"+
		"\2\2OP\5\b\5\2PQ\7\25\2\2QR\5\n\6\2R`\3\2\2\2ST\7\26\2\2TU\5\6\4\2UV\7"+
		"\27\2\2VW\5\b\5\2W`\3\2\2\2XY\7\26\2\2YZ\5\6\4\2Z[\7\27\2\2[\\\5\b\5\2"+
		"\\]\7\30\2\2]^\5\n\6\2^`\3\2\2\2_M\3\2\2\2_S\3\2\2\2_X\3\2\2\2`\r\3\2"+
		"\2\2ab\5\20\t\2bc\7\t\2\2cd\5\16\b\2dk\3\2\2\2ef\5\20\t\2fg\7\n\2\2gh"+
		"\5\16\b\2hk\3\2\2\2ik\5\20\t\2ja\3\2\2\2je\3\2\2\2ji\3\2\2\2k\17\3\2\2"+
		"\2lo\5\24\13\2mo\5\22\n\2nl\3\2\2\2nm\3\2\2\2o\21\3\2\2\2pq\5,\27\2qr"+
		"\7\3\2\2rs\5\20\t\2sy\3\2\2\2tu\5,\27\2uv\7$\2\2vw\5\20\t\2wy\3\2\2\2"+
		"xp\3\2\2\2xt\3\2\2\2y\23\3\2\2\2z{\b\13\1\2{|\7\13\2\2|}\7\"\2\2}~\5\16"+
		"\b\2~\177\7#\2\2\177\u0086\3\2\2\2\u0080\u0081\7\"\2\2\u0081\u0082\5\16"+
		"\b\2\u0082\u0083\7#\2\2\u0083\u0086\3\2\2\2\u0084\u0086\5,\27\2\u0085"+
		"z\3\2\2\2\u0085\u0080\3\2\2\2\u0085\u0084\3\2\2\2\u0086\u008d\3\2\2\2"+
		"\u0087\u0088\f\6\2\2\u0088\u0089\5\30\r\2\u0089\u008a\5\24\13\7\u008a"+
		"\u008c\3\2\2\2\u008b\u0087\3\2\2\2\u008c\u008f\3\2\2\2\u008d\u008b\3\2"+
		"\2\2\u008d\u008e\3\2\2\2\u008e\25\3\2\2\2\u008f\u008d\3\2\2\2\u0090\u0091"+
		"\5,\27\2\u0091\27\3\2\2\2\u0092\u0093\t\2\2\2\u0093\31\3\2\2\2\u0094\u0095"+
		"\7\"\2\2\u0095\u0096\5\34\17\2\u0096\u0097\7#\2\2\u0097\u009b\3\2\2\2"+
		"\u0098\u0099\7\"\2\2\u0099\u009b\7#\2\2\u009a\u0094\3\2\2\2\u009a\u0098"+
		"\3\2\2\2\u009b\33\3\2\2\2\u009c\u009d\b\17\1\2\u009d\u00a0\5,\27\2\u009e"+
		"\u00a0\5\f\7\2\u009f\u009c\3\2\2\2\u009f\u009e\3\2\2\2\u00a0\u00a9\3\2"+
		"\2\2\u00a1\u00a2\f\5\2\2\u00a2\u00a3\7\7\2\2\u00a3\u00a8\5,\27\2\u00a4"+
		"\u00a5\f\3\2\2\u00a5\u00a6\7\7\2\2\u00a6\u00a8\5\f\7\2\u00a7\u00a1\3\2"+
		"\2\2\u00a7\u00a4\3\2\2\2\u00a8\u00ab\3\2\2\2\u00a9\u00a7\3\2\2\2\u00a9"+
		"\u00aa\3\2\2\2\u00aa\35\3\2\2\2\u00ab\u00a9\3\2\2\2\u00ac\u00ad\7 \2\2"+
		"\u00ad\u00b3\7!\2\2\u00ae\u00af\7 \2\2\u00af\u00b0\5\34\17\2\u00b0\u00b1"+
		"\7!\2\2\u00b1\u00b3\3\2\2\2\u00b2\u00ac\3\2\2\2\u00b2\u00ae\3\2\2\2\u00b3"+
		"\37\3\2\2\2\u00b4\u00b5\b\21\1\2\u00b5\u00b6\5,\27\2\u00b6\u00b7\7\25"+
		"\2\2\u00b7\u00b8\5\4\3\2\u00b8\u00c1\3\2\2\2\u00b9\u00ba\f\3\2\2\u00ba"+
		"\u00bb\7\7\2\2\u00bb\u00bc\5,\27\2\u00bc\u00bd\7\25\2\2\u00bd\u00be\5"+
		"\4\3\2\u00be\u00c0\3\2\2\2\u00bf\u00b9\3\2\2\2\u00c0\u00c3\3\2\2\2\u00c1"+
		"\u00bf\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2!\3\2\2\2\u00c3\u00c1\3\2\2\2"+
		"\u00c4\u00c5\7\36\2\2\u00c5\u00c6\5 \21\2\u00c6\u00c7\7\37\2\2\u00c7\u00cb"+
		"\3\2\2\2\u00c8\u00c9\7\36\2\2\u00c9\u00cb\7\37\2\2\u00ca\u00c4\3\2\2\2"+
		"\u00ca\u00c8\3\2\2\2\u00cb#\3\2\2\2\u00cc\u00cd\b\23\1\2\u00cd\u00ce\5"+
		"&\24\2\u00ce\u00d7\3\2\2\2\u00cf\u00d0\f\4\2\2\u00d0\u00d1\7\33\2\2\u00d1"+
		"\u00d6\5&\24\2\u00d2\u00d3\f\3\2\2\u00d3\u00d4\7\32\2\2\u00d4\u00d6\5"+
		"&\24\2\u00d5\u00cf\3\2\2\2\u00d5\u00d2\3\2\2\2\u00d6\u00d9\3\2\2\2\u00d7"+
		"\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8%\3\2\2\2\u00d9\u00d7\3\2\2\2"+
		"\u00da\u00db\b\24\1\2\u00db\u00dc\5*\26\2\u00dc\u00e5\3\2\2\2\u00dd\u00de"+
		"\f\4\2\2\u00de\u00df\7\35\2\2\u00df\u00e4\5&\24\5\u00e0\u00e1\f\3\2\2"+
		"\u00e1\u00e2\7\34\2\2\u00e2\u00e4\5&\24\4\u00e3\u00dd\3\2\2\2\u00e3\u00e0"+
		"\3\2\2\2\u00e4\u00e7\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e6"+
		"\'\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e8\u00e9\7+\2\2\u00e9\u00ea\5\32\16"+
		"\2\u00ea)\3\2\2\2\u00eb\u00fa\5(\25\2\u00ec\u00fa\7(\2\2\u00ed\u00fa\7"+
		"\'\2\2\u00ee\u00fa\7*\2\2\u00ef\u00fa\7)\2\2\u00f0\u00fa\7+\2\2\u00f1"+
		"\u00f2\7\"\2\2\u00f2\u00f3\5$\23\2\u00f3\u00f4\7#\2\2\u00f4\u00fa\3\2"+
		"\2\2\u00f5\u00f6\7\"\2\2\u00f6\u00f7\5\f\7\2\u00f7\u00f8\7#\2\2\u00f8"+
		"\u00fa\3\2\2\2\u00f9\u00eb\3\2\2\2\u00f9\u00ec\3\2\2\2\u00f9\u00ed\3\2"+
		"\2\2\u00f9\u00ee\3\2\2\2\u00f9\u00ef\3\2\2\2\u00f9\u00f0\3\2\2\2\u00f9"+
		"\u00f1\3\2\2\2\u00f9\u00f5\3\2\2\2\u00fa+\3\2\2\2\u00fb\u010c\t\3\2\2"+
		"\u00fc\u010c\5\60\31\2\u00fd\u010c\5.\30\2\u00fe\u010c\5$\23\2\u00ff\u010c"+
		"\7,\2\2\u0100\u010c\5\36\20\2\u0101\u010c\5\"\22\2\u0102\u010c\7\31\2"+
		"\2\u0103\u0104\7%\2\2\u0104\u0105\7\"\2\2\u0105\u0106\7+\2\2\u0106\u010c"+
		"\7#\2\2\u0107\u0108\7\"\2\2\u0108\u0109\5\f\7\2\u0109\u010a\7#\2\2\u010a"+
		"\u010c\3\2\2\2\u010b\u00fb\3\2\2\2\u010b\u00fc\3\2\2\2\u010b\u00fd\3\2"+
		"\2\2\u010b\u00fe\3\2\2\2\u010b\u00ff\3\2\2\2\u010b\u0100\3\2\2\2\u010b"+
		"\u0101\3\2\2\2\u010b\u0102\3\2\2\2\u010b\u0103\3\2\2\2\u010b\u0107\3\2"+
		"\2\2\u010c-\3\2\2\2\u010d\u010e\7\"\2\2\u010e\u010f\7#\2\2\u010f\u0110"+
		"\7\4\2\2\u0110\u0111\5\4\3\2\u0111/\3\2\2\2\u0112\u0113\7\"\2\2\u0113"+
		"\u0114\5\62\32\2\u0114\u0115\7#\2\2\u0115\u0116\7\4\2\2\u0116\u0117\5"+
		"\4\3\2\u0117\u011d\3\2\2\2\u0118\u0119\5\64\33\2\u0119\u011a\7\4\2\2\u011a"+
		"\u011b\5\4\3\2\u011b\u011d\3\2\2\2\u011c\u0112\3\2\2\2\u011c\u0118\3\2"+
		"\2\2\u011d\61\3\2\2\2\u011e\u0123\5\66\34\2\u011f\u0120\7\7\2\2\u0120"+
		"\u0122\5\66\34\2\u0121\u011f\3\2\2\2\u0122\u0125\3\2\2\2\u0123\u0121\3"+
		"\2\2\2\u0123\u0124\3\2\2\2\u0124\63\3\2\2\2\u0125\u0123\3\2\2\2\u0126"+
		"\u0127\5\66\34\2\u0127\65\3\2\2\2\u0128\u0129\7+\2\2\u0129\67\3\2\2\2"+
		"\30E_jnx\u0085\u008d\u009a\u009f\u00a7\u00a9\u00b2\u00c1\u00ca\u00d5\u00d7"+
		"\u00e3\u00e5\u00f9\u010b\u011c\u0123";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}