package arbre;
import java.util.LinkedList;
import java.util.Queue;

public class RedBlackTree<T extends Comparable<? super T> > 
{
	enum ChildType{ left, right }
	/**
	 * racine de l'arbre
	 */
	private RBNode<T> root; 

	public RedBlackTree(){ 
		root = null;
	}

	public void printFancyTree(){
		printFancyTree( root, "", ChildType.right);
	}
	private void printFancyTree( RBNode<T> t, String prefix, ChildType myChildType){
		//
		System.out.print( prefix + "|__"); // un | et trois _  
		if( t != null ){
			boolean isLeaf = (t.isNil()) || ( t.leftChild.isNil() && t.rightChild.isNil() );
			System.out.println( t );
			String _prefix = prefix;
			if( myChildType == ChildType.left )
				_prefix += "|   "; // un | et trois espaces
			else
				_prefix += "   " ; // trois espace
			if( !isLeaf ){
				printFancyTree( t.leftChild, _prefix, ChildType.left );
				printFancyTree( t.rightChild, _prefix, ChildType.right );
			}
		}
		else
			System.out.print("null\n");
	}


	/**
	 * recherche d'une cl�
	 * @param key
	 * @return
	 */
	public T find(int key){
		return find(root, key);
	}

	/**
	 * Methode r�cursive de recherche d'une cl� � partir d'un noeud
	 * @param current
	 * @param key
	 * @return
	 */
	private T find(RBNode<T> current, int key)
	{
		if (current.value != null)
		{
			if (current.value.hashCode() > key)
				return find (current.leftChild, key);
			else if (current.value.hashCode() < key)
				return find(current.rightChild, key);
			else
				return current.value;
		}
		else
			return null;
	}


	/**
	 * Methode permettant l'insertion d'une valeur
	 * @param val
	 */
	public void insert(T val){
		insertNode( new RBNode<T>( val ) );
	}


	/**
	 * Methode r�cursive permettant l'insertion d'un noeud
	 * @param newNode
	 */
	private void insertNode( RBNode<T> newNode ){ 
		if (root == null)  // Si arbre vide
			root = newNode;
		else{
			RBNode<T> position = root; // On se place a la racine     
			while( true ) // on insere le noeud (ABR standard)
			{
				int newKey = newNode.value.hashCode();
				int posKey = position.value.hashCode();
				if (newKey < posKey ){
					if ( position.leftChild.isNil()){
						position.leftChild = newNode;
						newNode.parent = position;
						break;
					} 
					else 
						position = position.leftChild;
				}else if ( newKey > posKey ){
					if ( position.rightChild.isNil()){
						position.rightChild = newNode;
						newNode.parent = position;
						break;
					}
					else 
						position = position.rightChild;
				}else // pas de doublons
					return;
			}
		}
		insertionCases( newNode );
	}

	/**
	 * permet d'inserer un nouvel element X
	 * @param X
	 */
	private void insertionCases( RBNode<T> X ){
		insertionCase1( X );
	}
	/**
	 * premier cas possible lors de l'insertion
	 * Si X est la racine de l'arbre, il prend la couleur noire.
	 * @param X
	 */
	private void insertionCase1 ( RBNode<T> X )
	{
		if (X.equals(root))
			X.setToBlack();
		else
			insertionCase2(X);
	}
	/**
	 * Deuxieme cas possible lors de l'insertion
	 * Si le parent P est de couleur noir, tout est correct. Si il est de couleur rouge, on lance insertionCase3
	 * @param X
	 */
	private void insertionCase2( RBNode<T> X )
	{
		if (X.parent.isRed())
			insertionCase3(X);
	}

	/**
	 * Troisi�me cas possible lors de l'insertion
	 * Parent P et oncle O rouge : On les change en noir, et on met le grand parent G en rouge
	 * Ensuite : On relance InsertionCase sur G
	 * Sinon : InsertionCase4
	 * @param X
	 */
	private void insertionCase3( RBNode<T> X )
	{
		if (X.parent.isRed() && X.uncle().isRed())
		{
			X.parent.setToBlack();
			X.uncle().setToBlack();
			X.grandParent().setToRed();
			insertionCases(X.grandParent());
		}
		else 
			insertionCase4(X);
	}

	/**
	 * Quatrime cas possible lors de l'insertion
	 * Si : Le parent P est rouge, l'oncle est noir, le noeud est l'enfant de gauche de P et P est l'enfant de droite de G ou sym�trie
	 * Alors : rotation droite autour de P, ou rotation gauche si sym�trie, puis cas 5 sur l'enfant droite ou gauche de X selon sym�trie
	 * Sinon : Verification cas 5 sur X
	 * @param X
	 */
	private void insertionCase4( RBNode<T> X )
	{
		boolean symetrie = ((X.parent.leftChild.equals(X) && X.grandParent().rightChild.equals(X.parent))
				^ (X.parent.rightChild.equals(X) && X.grandParent().leftChild.equals(X.parent)));
		if (X.parent.isRed() && X.uncle().isBlack() && symetrie)
		{
			if (X.parent.leftChild.equals(X) && X.grandParent().rightChild.equals(X.parent))
			{
				rotateRight(X.parent);
				insertionCase5(X.rightChild);
			}
			else
			{
				rotateLeft(X.parent);
				insertionCase5(X.leftChild);
			}
		}
		else
			insertionCase5(X);
	}

	/**
	 * Cas d'insertion num�ro 5
	 * Si P rouge, oncle noir, X enfant de droite et P enfant de droite ou sym�trie
	 * Alors : Inversement des couleurs de G et P, rotation gauche si droite-droite ou rotation droite si gauche gauche
	 * @param X
	 */
	private void insertionCase5( RBNode<T> X )
	{
		boolean symetrie = (X.parent.rightChild.equals(X) && X.grandParent().rightChild.equals(X.parent))
				^ (X.parent.leftChild.equals(X) && X.grandParent().leftChild.equals(X.parent));
		if (X.parent.isRed() && X.uncle().isBlack() && symetrie)
		{
			X.parent.inverseColor();
			X.grandParent().inverseColor();
			if (X.parent.rightChild.equals(X) && X.grandParent().rightChild.equals(X.parent))
				rotateLeft(X.grandParent());
			else
				rotateRight(X.grandParent());
		}
	}

	/**
	 * Methode permettant de faire une rotation vers la gauche sur le noeud P
	 * @param P
	 */
	private void rotateLeft( RBNode<T> P )
	{
		RBNode<T> X = P.rightChild;

		P.rightChild		= X.leftChild;
		X.leftChild			= P;
		P.rightChild.parent	= P;
		X.parent 			= P.parent;
		P.parent 			= X;
		X.parent.leftChild	= X;
	}

	/**
	 * Methode permettant de faire une rotation vers la droite sur le noeud P
	 * @param P
	 */
	private void rotateRight( RBNode<T> P)
	{
		RBNode<T> X = P.leftChild;

		P.leftChild			= X.rightChild;
		X.rightChild		= P;
		P.leftChild.parent	= P;
		X.parent 			= P.parent;
		P.parent 			= X;
		X.parent.rightChild	= X;
	}

	/**
	 * Methode permettant d'afficher l'arbre en pre ordre
	 */
	public void printTreePreOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "PreOrdre [ ");
			printTreePreOrder( root );
			System.out.println( " ]");
		}
	}

	/**
	 * Methode permettant d'afficher l'abre en post ordre
	 */
	public void printTreePostOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "PostOrdre [ ");
			printTreePostOrder( root );
			System.out.println( " ]");
		}
	}

	/**
	 * Methode permettant d'afficher l'arbre de fa�on ascendante.
	 */
	public void printTreeAscendingOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "AscendingOrdre [ ");
			printTreeAscendingOrder( root );
			System.out.println( " ]");
		}
	}

	/**
	 * Methode permettant d'afficher l'ordre de fa�on descendant
	 */
	public void printTreeDescendingOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "DescendingOrdre [ ");
			printTreeDescendingOrder( root );
			System.out.println( " ]");
		}
	}

	/**
	 * Methode r�cursive permettant d'afficher l'arbre en pre ordre.
	 * @param P
	 */
	private void printTreePreOrder( RBNode<T> P )
	{
		if(P != root)
			System.out.print(" ; ");
		System.out.print("(" + P.value + " , " + (P.isBlack() ? "black" : "red") + ")");
		if(P.leftChild != null && P.leftChild.value != null)
			printTreePreOrder(P.leftChild);
		if(P.rightChild != null && P.rightChild.value != null)
			printTreePreOrder(P.rightChild);
	}

	/**
	 * M�thode r�cursive permettant d'afficher en post ordre
	 * @param P
	 */
	private void printTreePostOrder( RBNode<T> P )
	{
		if(P.leftChild != null && P.leftChild.value != null)
			printTreePostOrder(P.leftChild);
		if(P.rightChild != null && P.rightChild.value != null)
			printTreePostOrder(P.rightChild);
		System.out.print("(" + P.value + " , " + (P.isBlack() ? "black" : "red") + ")");
		if(P != root)
			System.out.print(" ; ");
	}

	/**
	 * Methode r�cursive permettant d'afficher en ordre croissant
	 * @param P
	 */
	private void printTreeAscendingOrder( RBNode<T> P )
	{
		RBNode<T> F = root;
		if(P.leftChild != null && P.leftChild.value != null)
			printTreeAscendingOrder(P.leftChild);
		System.out.print("(" + P.value + " , " + (P.isBlack() ? "black" : "red") + ")");
		while (F.rightChild.value != null) // recupere l'element le plus � droite (donc le plus grand()
			F = F.rightChild;
		if (P != F)
			System.out.print(" ; ");
		if(P.rightChild != null && P.rightChild.value != null)
			printTreeAscendingOrder(P.rightChild);

	}

	/**
	 * Methode recursive permettant d'afficher en ordre d�croissant
	 * @param P
	 */
	private void printTreeDescendingOrder( RBNode<T> P )
	{
		RBNode<T> F = root;
		if(P.rightChild != null && P.rightChild.value != null)
			printTreeDescendingOrder(P.rightChild);
		System.out.print("(" + P.value + " , " + (P.isBlack() ? "black" : "red") + ")");
		while (F.leftChild.value != null) // recupere l'element le plus � gauche (donc le plus petit)
			F = F.leftChild;
		if (P != F)
			System.out.print(" ; ");
		if(P.leftChild != null && P.leftChild.value != null)
			printTreeDescendingOrder(P.leftChild);
	}

	/**
	 * M�thode permettant d'afficher l'arbre par niveau.
	 */
	public void printTreeLevelOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "LevelOrdre [ ");
			Queue<RBNode<T>> q = new LinkedList<RBNode<T>>();
			q.add(root);
			while(!q.isEmpty())
			{
				RBNode<T> P = q.poll(); // recupere puis remove l'element. Remplace pop(), qui n'existe pas dans la class Queue, comme indiqu� dans le tableau de la javadoc.
				if(P != root)
					System.out.print(" ; ");
				System.out.print("(" + P.value + " , " + (P.isBlack() ? "black" : "red") + ")");
				if(P.leftChild != null && P.leftChild.value != null)
					q.add(P.leftChild);
				if(P.rightChild != null && P.rightChild.value != null)
					q.add(P.rightChild);
			}		 
			System.out.println( " ]");
		}
	}

	private static class RBNode<T extends Comparable<? super T> > 
	{
		enum RB_COLOR { BLACK, RED }  // Couleur possible 
		RBNode<T>  parent;      // Noeud parent
		RBNode<T>  leftChild;   // Feuille gauche
		RBNode<T>  rightChild;  // Feuille droite
		RB_COLOR   color;       // Couleur du noeud
		T          value;       // Valeur du noeud
		// Constructeur (NIL)   
		RBNode() { setToBlack(); }
		// Constructeur (feuille)   
		RBNode(T val)
		{
			setToRed();
			value = val;
			leftChild = new RBNode<T>();
			leftChild.parent = this;
			rightChild = new RBNode<T>();
			rightChild.parent = this;
		}

		/**
		 * retourne le grand parent, ou null si ce dernier n'existe pas.
		 * @return
		 */
		RBNode<T> grandParent()
		{
			if (parent != null)
				return parent.parent;
			else
				return null;
		}

		/**
		 * retourne l'oncle, ou null si ce dernier n'existe pas.
		 * @return
		 */
		RBNode<T> uncle()
		{
			if (parent != null)
				return parent.sibling();
			else
				return null;
		}

		/**
		 * permet de retourner le frere du noeud courant
		 * @return
		 */
		RBNode<T> sibling()
		{
			if (parent.leftChild == null || parent.rightChild == null)
				return null;
			else if (parent.leftChild.value == value)
				return parent.rightChild;
			else 
				return parent.leftChild;
		}

		public String toString()
		{
			return value + " , " + (color == RB_COLOR.BLACK ? "black" : "red"); 
		}

		/**
		 * Methode permettant de savoir si la couleur est noir
		 * @return boolean
		 */
		boolean isBlack()
		{ 
			return (color == RB_COLOR.BLACK);
		}
		/**
		 * Methode permettant de savoir si la couleur est rouge
		 * @return boolean
		 */
		boolean isRed()
		{
			return (color == RB_COLOR.RED);
		}
		/**
		 * Methode permettant de savoir si l'on est dans une feuille (NIL)
		 * @return boolean
		 */
		boolean isNil()
		{
			return (leftChild == null) && (rightChild == null);
		}

		/**
		 * Methode permettant de mettre la couleur noire 
		 */
		void setToBlack()
		{
			color = RB_COLOR.BLACK;
		}

		/**
		 * Methode permettant de mettre la couleur rouge
		 */
		void setToRed()
		{
			color = RB_COLOR.RED;
		}

		void inverseColor()
		{
			if (color.equals(RB_COLOR.BLACK))
				setToRed();
			else if (color.equals(RB_COLOR.RED))
				setToBlack();
		}
	}
}
