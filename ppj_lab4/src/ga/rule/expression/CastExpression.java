package ga.rule.expression;

import ga.Environment;
import ga.Property;
import ga.PropertyType;
import ga.SemanticException;
import ga.Types;
import ga.node.NonTerminalNode;
import ga.rule.RuleStrategy;
import ga.rule.RuleUtility;

public class CastExpression extends RuleStrategy {

    @Override
    public void evaluate(NonTerminalNode node, Environment environment) {
        if (node.getChildNodeNumber() == 1) {
            node.getChidlAt(0).visitNode(environment);

            node.setProperty(PropertyType.TYPE,
                    ((NonTerminalNode) node.getChidlAt(0)).getProperty(PropertyType.TYPE));
            node.setProperty(PropertyType.L_EXPRESSION,
                    ((NonTerminalNode) node.getChidlAt(0)).getProperty(PropertyType.L_EXPRESSION));
        } else if (node.getChildNodeNumber() == 4) {
            node.getChidlAt(1).visitNode(environment);
            node.getChidlAt(3).visitNode(environment);

            Types originalType = (Types) ((NonTerminalNode) node.getChidlAt(3)).getProperty(
                    PropertyType.TYPE).getValue();
            Types castedType = (Types) ((NonTerminalNode) node.getChidlAt(1)).getProperty(
                    PropertyType.TYPE).getValue();

            if (!isCastable(originalType, castedType)) {
                throw new SemanticException(node.toString());
            }

            node.setProperty(PropertyType.TYPE, new Property(castedType));
            node.setProperty(PropertyType.L_EXPRESSION, new Property(0));

        } else {
            // losa produkcija
        }
    }

    public static boolean isCastable(Types originalType, Types castedType) {
        if (RuleUtility.checkType(originalType, castedType)) {
            return true;
        }
        if (originalType.equals(Types.INT) && castedType.equals(Types.CHAR)) {
            return true;
        }
        if (originalType.equals(Types.INT) && castedType.equals(Types.CONST_CHAR)) {
            return true;
        }
        if (originalType.equals(Types.CONST_INT) && castedType.equals(Types.CONST_CHAR)) {
            return true;
        }
        if (originalType.equals(Types.CONST_INT) && castedType.equals(Types.CHAR)) {
            return true;
        }
        return false;
    }

    @Override
    public void emit(NonTerminalNode node, Environment environment) {
        if (node.getChildNodeNumber() == 1) {
            node.getChidlAt(0).visitNode(environment);
        } else {
            //TODO: 4 children
        }
    }
}
