import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Map;

public class Translate extends HelloBaseListener {

    public String output = "";
    public Map<String, String> vars = new HashMap<>();
    public ParseTreeProperty<String> strings = new ParseTreeProperty<>();

    public static void sout(String s){
        System.out.print(s);
    }
    public static void debugSout(String s){
//        System.out.println(s);
    }
       
    @Override public void exitHello(HelloParser.HelloContext ctx) { }

    @Override public void exitFuncDef(HelloParser.FuncDefContext ctx) {
        sout("define dso_local i32 @main(){\n\t"+ strings.get(ctx.block()) +"}");
    }

    @Override public void exitBlock(HelloParser.BlockContext ctx) {
        String tmp = "";
        for(HelloParser.BlockItemContext i: ctx.blockItem()){
            tmp+=strings.get(i.stmt())+'\n';
        }
        strings.put(ctx,tmp);
    }

    @Override public void exitBlockItem(HelloParser.BlockItemContext ctx) { }
       
    @Override public void exitStmt(HelloParser.StmtContext ctx) {
        strings.put(ctx,"ret i32 "+ strings.get(ctx.exp()));
    }
    @Override public void exitExp(HelloParser.ExpContext ctx) {
        String tmp = strings.get(ctx.addExp());
        strings.put(ctx,tmp);
        debugSout("exp"+tmp);
    }
    @Override public void exitAddExp(HelloParser.AddExpContext ctx) {
        String tmp = strings.get(ctx.mulExp());
        strings.put(ctx,tmp);
        debugSout("add exp"+tmp);
    }
    @Override public void exitMulExp(HelloParser.MulExpContext ctx) {
        String tmp = "";
        if(ctx.getChildCount()==1){
            tmp = strings.get(ctx.unaryExp());
        }else{

        }
        debugSout("mul exp"+ tmp);
        strings.put(ctx,tmp);
    }


    // unaryExp 表达式计算
    @Override public void exitCalcResES(HelloParser.CalcResESContext ctx) {

    }
    @Override public void exitNormResES(HelloParser.NormResESContext ctx) {
        String tmp = strings.get(ctx.primaryExp());
        strings.put(ctx,tmp);
        debugSout("exit n es"+tmp);
    }
    @Override public void exitSymbolResES(HelloParser.SymbolResESContext ctx) {
        int tmp = Integer.parseInt(strings.get(ctx.unaryExp()));
        String symbol = ctx.unaryOp().getText();
        switch (symbol){
            case "+":
                break;
            case "-":
                tmp = -tmp;
                break;
            case "!":
                break;
        }
        debugSout("tmp is "+tmp);
        strings.put(ctx,tmp+"");
    }

    @Override public void exitPrimaryExp1(HelloParser.PrimaryExp1Context ctx) {
        String tmp = strings.get(ctx.exp());
        strings.put(ctx,tmp);
        debugSout("p e 1 "+tmp);
    }

    @Override public void exitPrimaryExp2(HelloParser.PrimaryExp2Context ctx) { }

    @Override public void exitPrimaryExp3(HelloParser.PrimaryExp3Context ctx) {
        debugSout("----");
        debugSout(ctx.Number()+"");
        int tmp = 0;
        String number = ctx.Number().toString();
        if(number.startsWith("0x")||number.startsWith("0X")){
            tmp = Integer.valueOf(number,16);
        }else if(number.startsWith("0")){
            tmp = Integer.valueOf(number,8);
        }
        strings.put(ctx,tmp+"");
    }


    @Override public void enterEveryRule(ParserRuleContext ctx) { }
       
    @Override public void exitEveryRule(ParserRuleContext ctx) { }
       
    @Override public void visitTerminal(TerminalNode node) { }
       
    @Override public void visitErrorNode(ErrorNode node) {
    }
}
