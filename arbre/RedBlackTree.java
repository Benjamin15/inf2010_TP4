package arbre;
import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;

public class RedBlackTree<T extends Comparable<? super T> > 
{
	enum ChildType{ left, right }
	private RBNode<T> root;  // Racine de l'arbre
   
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
/*
 * recherche d'une clé
 */
   public T find(int key){
      return find(root, key);
   }
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
  /*
   * insertion d'une valeur 
   */
   public void insert(T val){
      insertNode( new RBNode<T>( val ) );
   }
   
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
    * Troisième cas possible lors de l'insertion
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
    * Si : Le parent P est rouge, l'oncle est noir, le noeud est l'enfant de gauche de P et P est l'enfant de droite de G ou symétrie
    * Alors : rotation droite autour de P, ou rotation gauche si symétrie, puis cas 5 sur l'enfant droite ou gauche de X selon symétrie
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
    * Cas d'insertion numéro 5
    * Si P rouge, oncle noir, X enfant de droite et P enfant de droite ou symétrie
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
      return;
   }
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
      return;
   }
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
      return;
   }
   
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
      return;
   }

   
   private void printTreePreOrder( RBNode<T> P )
   {
     // A MODIFIER/COMPLETER
	 //pour ne pas afficher la virgule apres le dernier element
   }
   private void printTreePostOrder( RBNode<T> P )
   {
      // A MODIFIER/COMPLETER
   }
     
   private void printTreeAscendingOrder( RBNode<T> P )
   {
      // A MODIFIER/COMPLETER
	   
   }
  
   private void printTreeDescendingOrder( RBNode<T> P )
   {
      // A MODIFIER/COMPLETER
	   
   }
   
   public void printTreeLevelOrder()
   {
      if(root == null)
         System.out.println( "Empty tree" );
      else
      {
         System.out.print( "LevelOrdre [ ");
         
         Queue<RBNode<T>> q = new LinkedList<RBNode<T>>();
         q.add(root);
         
         // A COMPLETER
		 
		 
         System.out.println( " ]");
      }
      return;
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
