/**
 * 
 */
package node;

import expressions.ExpressionNodeAdd;
import expressions.ExpressionNodeAnd;
import expressions.ExpressionNodeAssign;
import expressions.ExpressionNodeBinaryAnd;
import expressions.ExpressionNodeBinaryOr;
import expressions.ExpressionNodeBinaryXor;
import expressions.ExpressionNodeBlock;
import expressions.ExpressionNodeBracketCurlyLeft;
import expressions.ExpressionNodeBracketCurlyRight;
import expressions.ExpressionNodeBracketEdgeLeft;
import expressions.ExpressionNodeBracketEdgeRight;
import expressions.ExpressionNodeBracketRoundLeft;
import expressions.ExpressionNodeBracketRoundRight;
import expressions.ExpressionNodeCall;
import expressions.ExpressionNodeColon;
import expressions.ExpressionNodeCompare;
import expressions.ExpressionNodeDec;
import expressions.ExpressionNodeDiv;
import expressions.ExpressionNodeEmpty;
import expressions.ExpressionNodeEqual;
import expressions.ExpressionNodeFor;
import expressions.ExpressionNodeGreater;
import expressions.ExpressionNodeGreaterEqual;
import expressions.ExpressionNodeIdentifier;
import expressions.ExpressionNodeIf;
import expressions.ExpressionNodeInc;
import expressions.ExpressionNodeJumpByArg;
import expressions.ExpressionNodeLess;
import expressions.ExpressionNodeLessEqual;
import expressions.ExpressionNodeMod;
import expressions.ExpressionNodeMul;
import expressions.ExpressionNodeNeg;
import expressions.ExpressionNodeNot;
import expressions.ExpressionNodeOr;
import expressions.ExpressionNodePointer;
import expressions.ExpressionNodeStepListByComma;
import expressions.ExpressionNodeStepListBySemicolon;
import expressions.ExpressionNodeSub;
import expressions.ExpressionNodeSwitch;
import expressions.ExpressionNodeTypeCast;
import expressions.ExpressionNodeVariable;
import expressions.ExpressionNodeXor;

/**
 * @author LU132BOD
 *
 */
public interface NodeDispatcher<T>
{
    default public NodeDispatcherResult<T> visit(final AttributeNode node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final CapitalIdentifierNode node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ClassNode node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final IdentifierNode node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final MethodNode node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final NamespaceNode node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final PathNode node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeAdd node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeAnd node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeAssign node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeBlock node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeBracketCurlyLeft node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeBracketCurlyRight node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeBracketEdgeLeft node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeBracketEdgeRight node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeBracketRoundLeft node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeBracketRoundRight node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeBinaryAnd node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeBinaryOr node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeBinaryXor node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeCall node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeColon node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeCompare node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeDec node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeDiv node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeEmpty node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeEqual node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeFor node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeGreater node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeGreaterEqual node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeIdentifier node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeInc node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeIf node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeJumpByArg node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeLess node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeLessEqual node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeMod node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeMul node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeNeg node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeNot node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeOr node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodePointer node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeStepListByComma node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeStepListBySemicolon node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeSub node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeSwitch node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeTypeCast node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeVariable node) throws Exception
    {
        return null;
    }

    default public NodeDispatcherResult<T> visit(final ExpressionNodeXor node) throws Exception
    {
        return null;
    }

}
