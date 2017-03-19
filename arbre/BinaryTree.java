package arbre;

/**
 * 
 * Class permettant de contenir un arbre binaire
 *
 * @param <AnyType>
 */
public class BinaryTree<AnyType> {
	/**
	 * racine de l'abre
	 */
	private Node<AnyType> root = null;

	/**
	 * Methode permettant d'ins�rer un �l�ment dans l'arbre.
	 * @param elem
	 */
	public void insert (AnyType elem) 
	{
		if(root==null){
			this.root= new Node<AnyType>(elem);
		}else	
			insert(root, elem);      
	}



	/**
	 * Methode r�cursive permettant d'ins�rer un �l�ment dans un noeud de l'arbre.
	 * Si un element a une valeur qui est d�j� pr�sente dans l'arbre, on ne l'y ins�re pas.
	 * @param node
	 * @param elem
	 */
	@SuppressWarnings("unchecked")
	private void insert(Node<AnyType> node, AnyType elem) {
		if (elem.hashCode() < node.val.hashCode())
		{
			if (node.left != null)
				insert(node.left, elem);
			else 
				node.left = new Node<AnyType>(elem);
		}
		else if (elem.hashCode() > node.val.hashCode())
		{
			if (node.right != null)
				insert(node.right, elem);
			else 
				node.right = new Node<AnyType>(elem);
		}
	}

	/**
	 * Methode permettant de retourner la hauteur de l'arbre
	 * @return
	 */
	public int getHauteur () {
		return this.getHauteur(this.root);
	}

	/**
	 * Methode permettant l'affichage pre ordre de l'arbre
	 * @return
	 */
	public String printPrefixe() {
		return "{ " + this.printPrefixe(this.root) + " }";
	}

	/**
	 * Methode permettant l'affichage en ordre de l'arbre.
	 * @return
	 */
	public String printInFixe() {
		return "{ " + this.printInfixe(this.root) + " }";
	}

	/**
	 * Methode permettant l'affichage post ordre de l'arbre
	 * @return
	 */
	public String printPostFixe() {
		return "{ " + this.printPostfixe(this.root) + " }";
	}

	/**
	 * Methode permettant d'obtenir la hauteur depuis un noeud. Inspir� des notes de cours de inf2010 cours 5.
	 * @param tree
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int getHauteur(Node<AnyType> tree) {
		if (tree == null)
			return -1;
		else
			return 1 + Math.max(getHauteur(tree.left), getHauteur(tree.right));
	}	

	/**
	 * Methode r�cursive permettant de retourner un String contenant l'arbre en pr�ordre en partant d'un noeud 
	 * Preordre : (Noeud Gauche Droite)
	 * @param node
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String printPrefixe(Node<AnyType> node) {
		String print = "";
		if (node == null)
			return print;
		else 
			return node.val + printPrefixe(node.left) + printPrefixe(node.right) ;
	}

	/**
	 * Methode r�cursive permettant de retourner un String contenant l'arbre en ordre en partant d'un noeud 
	 * en ordre : (Gauche Noeud Droite)
	 * @param node
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String printInfixe(Node<AnyType> node) {
		String print = "";
		if (node == null)
			return print;
		else 
			return printInfixe(node.left) + node.val + printInfixe(node.right) ;
	}

	/**
	 * Methode r�cursive permettant de retourner un String contenant l'arbre en post-ordre en partant d'un noeud 
	 * post-odre : (Gauche Droite Noeud)
	 * @param node
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String printPostfixe(Node<AnyType> node) {
		String print = "";
		if (node == null)
			return print;
		else 
			return printPostfixe(node.left) + printPostfixe(node.right) + node.val;
	}

	/**
	 * 
	 * Class Node qui permet de repr�senter chaque element de notre arbre binaire.
	 *
	 * @param <AnyType>
	 */
	@SuppressWarnings("hiding")
	private class Node<AnyType> {
		/**
		 * valeur du neoud
		 */
		AnyType val;

		/**
		 * fils de droite 
		 */
		@SuppressWarnings("rawtypes")
		Node right;

		/**
		 * fils de gauche
		 */
		@SuppressWarnings("rawtypes")
		Node left;

		public Node (AnyType val) {
			this.val = val;
			right = null;
			left = null;
		}
	}
}

