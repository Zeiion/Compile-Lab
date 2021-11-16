import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Translate extends HelloBaseListener {

       
    @Override public void exitHello(HelloParser.HelloContext ctx) { }

    @Override public void exitCompUnit(HelloParser.CompUnitContext ctx) { }

    @Override public void exitDecl(HelloParser.DeclContext ctx) { }
       
    @Override public void exitConstDecl(HelloParser.ConstDeclContext ctx) { }
       
    @Override public void exitConstDef(HelloParser.ConstDefContext ctx) { }
       
    @Override public void exitConstInitVal(HelloParser.ConstInitValContext ctx) { }

    @Override public void exitConstExp(HelloParser.ConstExpContext ctx) { }
       
    @Override public void exitVarDecl(HelloParser.VarDeclContext ctx) { }
       
    @Override public void exitVarDef(HelloParser.VarDefContext ctx) { }
       
    @Override public void exitInitVal(HelloParser.InitValContext ctx) { }

    @Override public void exitFuncDef(HelloParser.FuncDefContext ctx) { }

    @Override public void exitBlock(HelloParser.BlockContext ctx) { }

    @Override public void exitBlockItem(HelloParser.BlockItemContext ctx) { }
       
    @Override public void exitStmt(HelloParser.StmtContext ctx) { }
       
    @Override public void exitExp(HelloParser.ExpContext ctx) { }
       
    @Override public void exitCond(HelloParser.CondContext ctx) { }
       
    @Override public void exitLVal(HelloParser.LValContext ctx) { }

    @Override public void exitPrimaryExp(HelloParser.PrimaryExpContext ctx) { }
       
    @Override public void exitAddExp(HelloParser.AddExpContext ctx) { }
       
    @Override public void exitMulExp(HelloParser.MulExpContext ctx) { }

    @Override public void exitUnaryExp(HelloParser.UnaryExpContext ctx) { }
       
    @Override public void exitUnaryOp(HelloParser.UnaryOpContext ctx) { }
       
    @Override public void exitFuncRParams(HelloParser.FuncRParamsContext ctx) { }
       
    @Override public void exitRelExp(HelloParser.RelExpContext ctx) { }
       
    @Override public void exitEqExp(HelloParser.EqExpContext ctx) { }
       
    @Override public void exitLAndExp(HelloParser.LAndExpContext ctx) { }

    @Override public void exitLOrExp(HelloParser.LOrExpContext ctx) { }


    @Override public void enterEveryRule(ParserRuleContext ctx) { }
       
    @Override public void exitEveryRule(ParserRuleContext ctx) { }
       
    @Override public void visitTerminal(TerminalNode node) { }
       
    @Override public void visitErrorNode(ErrorNode node) {
        System.out.println(node);
    }
}
