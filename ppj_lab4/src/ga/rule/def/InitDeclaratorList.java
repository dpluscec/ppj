package ga.rule.def;

import ga.Environment;
import ga.PropertyType;
import ga.node.NonTerminalNode;
import ga.rule.RuleStrategy;

public class InitDeclaratorList extends RuleStrategy {

    @Override
    public void evaluate(NonTerminalNode node, Environment environment) {
        if (node.getChildNodeNumber() == 1) {
            NonTerminalNode initDeclarator = (NonTerminalNode) node.getChidlAt(0);
            initDeclarator.setProperty(PropertyType.N_TYPE, node.getProperty(PropertyType.N_TYPE));
            initDeclarator.visitNode(environment);
        } else if (node.getChildNodeNumber() == 3) {
            NonTerminalNode listInitDeclarator = (NonTerminalNode) node.getChidlAt(0);
            listInitDeclarator.setProperty(PropertyType.N_TYPE,
                    node.getProperty(PropertyType.N_TYPE));
            listInitDeclarator.visitNode(environment);

            NonTerminalNode initDeclarator = (NonTerminalNode) node.getChidlAt(2);
            initDeclarator.setProperty(PropertyType.N_TYPE, node.getProperty(PropertyType.N_TYPE));
            initDeclarator.visitNode(environment);
        } else {
            // losa produkcija
        }
    }

    @Override
    public void emit(NonTerminalNode node, Environment environment) {
        if (node.getChildNodeNumber() == 1) {
            node.getChidlAt(0).visitNode(environment);
        } else if (node.getChildNodeNumber() == 3) {
            node.getChidlAt(0).visitNode(environment);
            node.getChidlAt(2).visitNode(environment);
        }
    }
}
