package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */

// KEV SHARMA 
// RUID- 194008599
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/**KEV SHARMA**/

		Node sum = null; // Will hold the sum of poly1 and poly2 but will be in descending order.
		
		// If both polynomials are empty then return null;
		if(poly1==null && poly2==null)
			return null;
		
		while(poly1 != null || poly2!= null)
		{
			// Add remaining term to sum for the polynomial that is not null
			if(poly1 == null)
			{
				sum = new Node(poly2.term.coeff,poly2.term.degree, sum);
				poly2 = poly2.next;
			}
			else if(poly2 == null)
			{
				sum = new Node(poly1.term.coeff, poly1.term.degree, sum);
				poly1 = poly1.next;
			}
			else if(poly1.term.degree == poly2.term.degree)
			{
				if((poly1.term.coeff + poly2.term.coeff) == 0) //if the sum of both coefficients is 0, then don't add to list;
				{
					poly1 = poly1.next; poly2 = poly2.next;
					continue;
				}
				sum = new Node((poly1.term.coeff+poly2.term.coeff), poly1.term.degree, sum);
				poly1 = poly1.next; poly2 = poly2.next;
			}
			else if(poly1.term.degree < poly2.term.degree)
			{
				sum = new Node(poly1.term.coeff, poly1.term.degree, sum);
				poly1 = poly1.next;
			}
			else if(poly2.term.degree < poly1.term.degree)
			{
				sum = new Node(poly2.term.coeff, poly2.term.degree, sum);
				poly2 = poly2.next;
			}
		}
		
		Node result = null; // Stores contents of sum in reverse so output matches that of guideline.
		while(sum != null)
		{
			result = new Node(sum.term.coeff,sum.term.degree,result);
			sum = sum.next;
		}
		
		return result;
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/**KEV SHARMA**/

		Node product = null; 
		
		// if either linked list is null
		if(poly1 == null || poly2 == null)
			return product;
		
		// Multiply both linked lists and store components in linked list 'product'
		for(Node ptr1 = poly1; ptr1 != null; ptr1 = ptr1.next)
			for(Node ptr2 = poly2; ptr2 != null; ptr2 = ptr2.next)
				product = new Node(ptr1.term.coeff*ptr2.term.coeff, ptr1.term.degree+ptr2.term.degree, product);
				// First node will hold highest degree(n) which tells me there will be n+1 terms in new list max;
				
		
		// Create a new linked list that will store 
		Node product2 = null;
		for(int i=0; i<=product.term.degree; i++) //Where n is the highest degree
		{
			int sum = 0;
			for(Node ptr = product; ptr != null; ptr = ptr.next)
				if(i == ptr.term.degree)
					sum += ptr.term.coeff;
			
			if(sum!=0)
				product2 = new Node(sum, i, product2);
		}
		
		Node result = null; // Stores contents of sum in reverse so output matches that of guideline.
		while(product2 != null)
		{
			result = new Node(product2.term.coeff,product2.term.degree, result);
			product2 = product2.next;
		}
		
		return result;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/**KEV SHARMA**/
		float evaluation = 0;
		for(Node ptr = poly; ptr!=null; ptr = ptr.next)
			evaluation += ptr.term.coeff*(Math.pow(x, ptr.term.degree));
		
		return evaluation;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
