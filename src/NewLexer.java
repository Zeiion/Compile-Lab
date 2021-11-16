import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.LexerNoViableAltException;

public class NewLexer extends HelloLexer{
    public NewLexer(CharStream input) {
        super(input);
    }

    public void recover(LexerNoViableAltException e){
        throw new RuntimeException();
    }
}
