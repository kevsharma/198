package primesync;

import java.io.*;

/**
 * @author Sesh Venugopal
 *
 */
public class PrimeDriver {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter integer bound => ");
		int limit = Integer.parseInt(br.readLine());
		System.out.print("Enter number of intervals => ");
		int ni = Integer.parseInt(br.readLine());
		PrimeCentral primeCentral = new PrimeCentral();
		new PrimeGenerator(primeCentral, limit);
		new PrimeAcceptor(primeCentral, limit, ni);
	}
}
