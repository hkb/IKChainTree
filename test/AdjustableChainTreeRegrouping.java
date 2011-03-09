package test;

import chainTree.AdjustableChainTree;
import chainTree.CTNode;
import chainTree.ChainTreePainter;

/**
 * Graphical test to verify the regrouping functionality of the 
 * AdjustableChainThree class.
 * 
 * @author hkb
 *
 */
public class AdjustableChainTreeRegrouping {
	public static void main(String[] args) {
		AdjustableChainTree cTree = new AdjustableChainTree("4ZNF");
		
		ChainTreePainter.displayChainTree(cTree);
		
		CTNode nd = cTree.group(3, 7);
		ChainTreePainter.displayChainTree(cTree);
		
		ChainTreePainter.displayChainSubtree(nd, cTree);
	}
}
