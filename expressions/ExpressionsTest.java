package expressions;

import exception.ParserException;
import misc.SourceStream;

public class ExpressionsTest
{

    public ExpressionsTest()
    {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args)
    {
        SourceStream stream = SourceStream.getInstance();

        stream.clear();
        stream.addString("for(a:b:=c*d; b == e; ++b)");
        stream.addString("{m: n := o;}");
        stream.addString(";");
        ExpressionsTest.startExpression(stream);

        ExpressionsTest.testAll();
    }

    private static void testAll()
    {
        SourceStream stream = SourceStream.getInstance();

        stream.clear();
        stream.addString("a + b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a - b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a * b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a / b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a % b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("- a + b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("+ a + b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a + - b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a + + b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a - + b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a - - b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a * b - c");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a * b - c / d");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a + b / c");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a + b / c - d");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a * (b - c - d)");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("(a * b - c) / d");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("(a + b) * (c / d)");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("(a) (b - c)");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a (b)");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a ^ b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a^b(c)");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("(d)a^b(c)");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a b+");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a /* b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("(a + b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a - b)");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a ()");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("(a) b");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a,b,c");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("(a)b(c,d(e,f),g) + i");
        stream.addString("");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("if(a == b){a+b}");
        stream.addString("STOP");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("if(a == b){a+b}");
        stream.addString("else{a-b}");
        stream.addString("STOP");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("for(a; a == b; ++a){a+b}");
        stream.addString("STOP");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("c / if(a == b){a+b}");
        stream.addString("else");
        stream.addString("{a-b};");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("compare(a; b)");
        stream.addString("[<]");
        stream.addString("   { a }");
        stream.addString("[==]");
        stream.addString("   { 0 }");
        stream.addString("[>]");
        stream.addString("   { b }");
        stream.addString(";");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("switch(a)");
        stream.addString("['A']");
        stream.addString("   { 'A' }");
        stream.addString("['B']");
        stream.addString("   { 'B' }");
        stream.addString("['C']");
        stream.addString("   { 'C' }");
        stream.addString("['D']");
        stream.addString("   { 'D' }");
        stream.addString("a+ b;");
        stream.addString("'A'");
        stream.addString(";");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("a := b");
        stream.addString(";");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("A : if(a < b) {B;} else {C;}");
        ExpressionsTest.startExpression(stream);

        stream.clear();
        stream.addString("d: a := b * c");
        stream.addString(";");
        ExpressionsTest.startExpression(stream);
    }

    private static void startExpression(final SourceStream stream)
    {
        System.out.println("------------------------------------------------------");
        stream.reset();
        stream.print();

        try
        {
            new Expressions(stream).process().print(0);
        }
        catch(ParserException parserException)
        {
            System.out.println("------>");
            parserException.print();
        }
        catch(Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
