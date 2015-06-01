package org.leibnizcenter.pneu.parsers.LPPN;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class LPPNLoaderListener extends ASPBaseListener {

    private final static Logger log = Logger.getLogger("ASPLoaderListener");

    // setting the internal databases for atoms, formulas and rules

    AtomBase atomBase = null;
    FormulaBase formulaBase = null;
    RuleBase ruleBase = null;

    public void ASPLoaderListener() { }

    public void setAtomBase(AtomBase ab) {
        atomBase = ab;
    }

    public void setFormulaBase(FormulaBase fb) {
        formulaBase = fb;
        if (atomBase != null) log.warn("Atom database previously defined: overwriting it.");
        atomBase = formulaBase.getAtomBase();
    }

    public void setRuleBase(RuleBase rb) {
        ruleBase = rb;
        if (formulaBase != null) log.warn("Formula database previously defined: overwriting it.");
        formulaBase = ruleBase.getFormulaBase();
        if (atomBase != null) log.warn("Atom database previously defined: overwriting it.");
        atomBase = formulaBase.getAtomBase();
    }

    public void setProgram(Program program) {
        if (ruleBase != null) log.warn("Rule database previously defined: overwriting it.");
        ruleBase = program.getRuleBase();
        if (formulaBase != null) log.warn("Formula database previously defined: overwriting it.");
        formulaBase = ruleBase.getFormulaBase();
        if (atomBase != null) log.warn("Atom database previously defined: overwriting it.");
        atomBase = formulaBase.getAtomBase();
    }

    // to enrich the parsing tree with ids to already computed values (atoms, formulas, ...)

    // Atom Nodes
    private ParseTreeProperty<Integer> atomIdNodes = new ParseTreeProperty<Integer>();

    public void setAtomIdNode(ParseTree node, Integer atomId) {
        atomIdNodes.put(node, atomId);
    }

    public Integer getAtomIdNode(ParseTree node) {
        return atomIdNodes.get(node);
    }

    // Formula Nodes
    private ParseTreeProperty<Integer> formulaIdNodes = new ParseTreeProperty<Integer>();

    public void setFormulaIdNode(ParseTree node, Integer formulaId) {
        formulaIdNodes.put(node, formulaId);
    }

    public Integer getFormulaIdNode(ParseTree node) {
        return formulaIdNodes.get(node);
    }

    // List Nodes
    private ParseTreeProperty<List<Integer>> listIdNodes = new ParseTreeProperty<List<Integer>>();

    public void addListIdNode(ParseTree node, Integer id) {
        List<Integer> list = listIdNodes.get(node);
        if (list == null) {
            list = new ArrayList<Integer>();
            listIdNodes.put(node, list);
        }
        list.add(id);
    }

    public void setListIdNode(ParseTree node, List<Integer> list) {
        List<Integer> currentList = listIdNodes.get(node);
        if (currentList != null) log.warn("List elements previously defined: overwriting it.");
        listIdNodes.put(node, list);
    }

    public List<Integer> getListIdNode(ParseTree node) {
        return listIdNodes.get(node);
    }

    ///////////////// LISTENERS

    public void exitAtom(ASPParser.AtomContext ctx) {
        Integer atomId = atomBase.add(ctx.IDENTIFIER().getText());
        setAtomIdNode(ctx, atomId);

        log.trace("attaching atom (id " + atomId + ") to Atom node.");
    }

    public void exitTerm(ASPParser.TermContext ctx) {

        Integer formulaId;
        Integer atomId = getAtomIdNode(ctx.atom());

        Atom atom = atomBase.read(atomId);

        if (ctx.NEG() != null) {
            log.trace("creating formula NEG from atom (id " + atomId + ").");
            formulaId = formulaBase.add(atom, Op.NEG);
        } else {
            log.trace("creating formula from atom (id " + atomId + ").");
            formulaId = formulaBase.add(atom);
        }

        log.trace("attaching formula (id " + formulaId + ") to Term node.");
        setFormulaIdNode(ctx, formulaId);
    }

    public void exitList_terms(ASPParser.List_termsContext ctx) {
        if (ctx.COMMA() != null) {
            Integer formulaId = getFormulaIdNode(ctx.term());
            addListIdNode(ctx, formulaId);
            log.trace("attaching formula (id " + formulaId + ") to new List_terms node.");

            List<Integer> list = getListIdNode(ctx);
            List<Integer> childList = getListIdNode(ctx.list_terms());
            list.addAll(childList);
            log.trace("merging list with child List_terms node.");

        } else {
            Integer formulaId = getFormulaIdNode(ctx.term());
            addListIdNode(ctx, formulaId);
            log.trace("attaching single formula (id " + formulaId + ") to new List_terms node.");
        }
    }

    public void exitBterm(ASPParser.BtermContext ctx) {

        Integer formulaId = null;

        Integer childFormulaId = getFormulaIdNode(ctx.term());
        if (childFormulaId == null) {
            if (ctx.NAF() != null) {
                Integer atomId = getFormulaIdNode(ctx.term().atom());
                Atom atom = atomBase.read(atomId);
                log.trace("creating formula NAF from atom (id " + atomId + ").");
                formulaId = formulaBase.add(atom, Op.NAF);
            }
        } else {
            if (ctx.NAF() != null) {
                Formula formula  = formulaBase.read(childFormulaId);
                log.trace("creating formula NAF from formula (id " + childFormulaId + ")");
                formulaId = formulaBase.add(formula, Op.NAF);
            }
        }

        // when there is not NAF we clone the internal term up
        if (formulaId == null) {
            formulaId = childFormulaId;
        }

        setFormulaIdNode(ctx, formulaId);
        log.trace("attaching formula (id " + formulaId + ") to BTerm node.");

    }

    public void exitList_bterms(ASPParser.List_btermsContext ctx) {
        if (ctx.COMMA() != null) {
            Integer formulaId = getFormulaIdNode(ctx.bterm());
            addListIdNode(ctx, formulaId);
            log.trace("attaching formula (id " + formulaId + ") to new List_bterms node.");

            List<Integer> list = getListIdNode(ctx);
            List<Integer> childList = getListIdNode(ctx.list_bterms());
            list.addAll(childList);
            log.trace("merging list with child List_bterms node.");

        } else {
            Integer formulaId = getFormulaIdNode(ctx.bterm());
            addListIdNode(ctx, formulaId);
            log.trace("attaching single formula (id " + formulaId + ") to new List_bterms node.");
        }
    }

    public void exitHead(ASPParser.HeadContext ctx) {
        if (ctx.LACC() == null) { // single term
            // passing up the child formula
            setFormulaIdNode(ctx, getFormulaIdNode(ctx.term()));
        } else { // CHOICE operation

            // get the list of inputs given by the child tree
            List<Integer> list = getListIdNode(ctx.list_terms());

            String ctx_l = null, ctx_r = null;

            if (ctx.l != null) ctx_l = ctx.l.getText();
            if (ctx.r != null) ctx_r = ctx.r.getText();

            Integer formulaId = computeChoice(list, ctx_l, ctx_r);

            // record the formula on the tree
            setFormulaIdNode(ctx, formulaId);
        }

    }

    public void exitBody(ASPParser.BodyContext ctx) {
        if (ctx.LACC() == null) {

            // get the list of inputs given by the child tree
            List<Integer> list = getListIdNode(ctx.list_bterms());

            // get all the formulas in the list
            List<Formula> formulas = new ArrayList<Formula>();
            for (Integer formulaId : list) {
                formulas.add(formulaBase.read(formulaId));
            }

            // if more then 1 input create the AND operation
            Integer formulaId;
            if (list.size() > 1) {
                formulaId = formulaBase.add(formulas, Op.AND);
            } else { // else just copy the inner formula
                formulaId = list.get(0);
            }

            // record the formula on the tree
            setFormulaIdNode(ctx, formulaId);

        } else { // CHOICE operator

            // get the list of inputs given by the child tree
            List<Integer> list = getListIdNode(ctx.list_bterms());

            String ctx_l = null, ctx_r = null;

            if (ctx.l != null) ctx_l = ctx.l.getText();
            if (ctx.r != null) ctx_r = ctx.r.getText();

            Integer formulaId = computeChoice(list, ctx_l, ctx_r);

            // record the formula on the tree
            setFormulaIdNode(ctx, formulaId);
        }
    }

    public void exitStatement(ASPParser.StatementContext ctx) {
        if (ctx.IF() != null) { // rule

            Integer headId = getFormulaIdNode(ctx.head());
            Integer bodyId = getFormulaIdNode(ctx.body());

            Formula head = formulaBase.read(headId);
            Formula body = formulaBase.read(bodyId);

            ruleBase.add(head, body);

        } else { // fact

            // TODO

        }
    }

    // common controllers

    public Integer computeChoice(List<Integer> list, String ctx_l, String ctx_r) {

        // get all the formulas in the list
        List<Formula> formulas = new ArrayList<Formula>();
        for (Integer formulaId : list) {
            formulas.add(formulaBase.read(formulaId));
        }

        // get the min and max options from the operator, if present
        Integer l, r;
        Integer size = list.size();
        if (ctx_l == null) l = 0;
        else {
            l = Integer.parseInt(ctx_l);
            if (l > size) {
                l = size;
                log.warn("choice left parameter too high (" + l + "), set to the size of the set of inputs (" + size + ").");
            }
        }
        if (ctx_r == null) r = size;
        else {
            r = Integer.parseInt(ctx_r);
            if (r > size) {
                log.warn("choice right parameter too high (" + r + "), set to the size of the set of inputs (" + size + ").");
                r = size;
            }
        }

        // translate the CHOICE operator in more known descriptions
        // options[0] is the min of inputs to be chosen within the list
        // options[1] is the max of inputs to be chosen within the list

        Op op = Op.CHOICE;
        if (l == list.size() && l == r) op = Op.AND;
        else if (l == 1 && r == list.size()) op = Op.OR;
        else if (l == 1 && r == 1) op = Op.XOR;

        Integer formulaId;

        if (op == Op.CHOICE) {
            List<Integer> params = new ArrayList<Integer>();
            params.add(l);
            params.add(r);
            // create the CHOICE operation (with options)
            formulaId = formulaBase.add(formulas, op, params);
        } else {
            // create AND/OR/XOR operations (no options)
            formulaId = formulaBase.add(formulas, op);
        }

        return formulaId;
    }
}

