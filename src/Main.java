import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Main {

	public static void main(String[] args) throws Exception {
		try {
			// 读取标准输入
			ANTLRInputStream input = new ANTLRInputStream(System.in);

			// 新建词法分析器
			NewLexer lexer = new NewLexer(input);

			// 词法符号缓冲区
			CommonTokenStream tokens = new CommonTokenStream(lexer);

			// 语法分析器
			HelloParser parser = new HelloParser(tokens);

			//error 处理
			parser.setErrorHandler(new ErrorStrategy());

			// 针对hello规则开始语法分析
			ParseTree tree = parser.hello();

			// visitor模式
			//			MyVisitor visitor = new MyVisitor();
			//			visitor.visit(tree);
			//			while(!MyVisitor.stack.isEmpty()){
			//				System.out.println(MyVisitor.stack.pop());;
			//			}

			// 遍历语法分析书
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(new Translate(), tree);
		} catch (Exception e) {
			System.out.println(e);
			System.exit(-1);
		}
		System.exit(0);
	}
}

