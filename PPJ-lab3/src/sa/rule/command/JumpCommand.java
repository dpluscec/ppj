package sa.rule.command;

import sa.Environment;
import sa.PropertyType;
import sa.SemanticException;
import sa.Symbol;
import sa.node.NonTerminalNode;
import sa.rule.RuleStrategy;

public class JumpCommand extends RuleStrategy {

    @Override
    public void evaluate(NonTerminalNode node, Environment environment) {
        if (node.getChildNodeNumber() == 2) {
            if (node.getChidlAt(0).getSymbol().equals(new Symbol.Terminal("KR_RETURN"))) {
                if (!checkParentFunction(node, "VOID")) {
                    throw new SemanticException(node.toString());
                }
            } else {
                if (!checkParent(node, "<naredba_petlje>")) {
                    throw new SemanticException(node.toString());
                }

            }
        } else if (node.getChildNodeNumber() == 3) {
            node.getChidlAt(1).visitNode(environment);
            if (!checkParentFunction(node,
                    ((NonTerminalNode) node.getChidlAt(1)).getProperty(PropertyType.TYPE)
                    .getValue())) {
                throw new SemanticException(node.toString());
            }
        } else {
            // loša produkcija
        }
    }

    public static boolean checkParent(NonTerminalNode node, String string) {
        return false;
        // TODO Auto-generated method stub

    }

    public static boolean checkParentFunction(NonTerminalNode node, String string) {
        return false;
        // TODO Auto-generated method stub

    }

}