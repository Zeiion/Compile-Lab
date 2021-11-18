import java.util.HashMap;
import java.util.Stack;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class MyVisitor extends HelloBaseVisitor<String>{
	public static Stack<String> stack = new Stack<>();
	@Override public String visitHello(HelloParser.HelloContext ctx) {
		System.out.println("hello");
		stack.push("hello");
		return visitChildren(ctx); }

	@Override public String visitCompUnit(HelloParser.CompUnitContext ctx) {
		System.out.println("compunit");
		stack.push("compunit");
		return visitChildren(ctx); }

	@Override public String visitFuncDef(HelloParser.FuncDefContext ctx) {
		System.out.println("visitFuncDef");
		stack.push("funcdef");
		return visitChildren(ctx); }
}
