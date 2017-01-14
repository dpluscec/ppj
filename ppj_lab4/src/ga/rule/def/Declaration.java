package ga.rule.def;

import ga.Environment;
import ga.PropertyType;
import ga.node.NonTerminalNode;
import ga.rule.RuleStrategy;

public class Declaration extends RuleStrategy {

    @Override
    public void evaluate(NonTerminalNode node, Environment environment) {

        if (node.getChildNodeNumber() == 3) {

            node.getChidlAt(0).visitNode(environment);
            NonTerminalNode typeName = (NonTerminalNode) node.getChidlAt(0);
            NonTerminalNode initDeclarators = (NonTerminalNode) node.getChidlAt(1);

            initDeclarators.setProperty(PropertyType.N_TYPE,
                    typeName.getProperty(PropertyType.TYPE));

            initDeclarators.visitNode(environment);
        } else {
            // losa produkcija
        }
    }

    @Override
    public void emit(NonTerminalNode node, Environment environment) {
        node.getChidlAt(1).visitNode(environment);
    }
}
