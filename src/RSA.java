import static java.lang.Math.sqrt;
import java.util.*;
import java.math.*;

public class RSA {
	ArrayList<Integer> primes = new ArrayList<>();
	int p, q, phi;
	boolean isValidPQ = true;
	int[] message = {82976, 371981, 814231, 505650, 853440, 353277, 596004, 250518, 494162, 922046,
					540928, 633792, 779152, 973836, 494176, 19498, 125267, 683832, 244888, 922046,
					522776, 395123, 915899, 132032, 620457, 568301, 878543, 623328, 746341, 710542};
	String[] alphabet = {" ", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
						"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ".", ":", "'"};
	String originalMessage = "";
	
	public RSA(int n, int e){
		findPQ(n);
		System.out.print("N = " + n + "\tp = " + p + "\tq = " + q);
		
		if(isPrime(p) && isPrime(q))
			isValidPQ = true;
		else
			isValidPQ = false;
		
		if(isValidPQ){
			phi = (p-1)*(q-1);
			
			if(hasSolution(e, 1, phi)){
				int d = solveLinearCongruence(e, 1, phi);
				int msg;
				String decrypted = "";
				
				System.out.println("\n\nDecrypting message...");
				System.out.print("	");
				
				for(int i = 0; i < message.length; i++){
					msg = decrypt(message[i], d, n).intValue();
					
					decrypted = String.valueOf(msg);
					if(decrypted.length() != 6){
						while(decrypted.length() != 6){
							decrypted = "0" + decrypted;
						}
					}
					if(i % 10 == 0)
						System.out.print("\n\t ");
						
					System.out.print(decrypted + " ");
					
					int start = 0, end = 2;
					while(end < 8){
						if(decrypted.substring(start, end) == "00")
							originalMessage += " ";
						else
							originalMessage += alphabet[Integer.valueOf(decrypted.substring(start, end))];
						start += 2;
						end += 2;
					}
				}
				System.out.println("\n\nMessage decrypted.");
				System.out.println("\n\n	ORIGINAL MESSAGE: " + originalMessage);
				
			}else{
				System.out.println("NO SOLUTION");
			}
		}
		
	}
	
	public void findPQ(int n){
		for(int i = 2; i < sqrt(n)+1; i++){
			if(isPrime(i))
				primes.add(i);
		}
		for(int i = 0; i < primes.size(); i++){
			if(n % primes.get(i) == 0){
				p = primes.get(i);
				q = n/p;
				break;
			}
		}
		if(!isPrime(q)){
			if(isPrime(n)){
				p = n;
				q = 1;
			}else
				System.out.println(n + " has no two prime factors.");
		}
	}
	
	public boolean isPrime(int num){
		for(int i = 2; i <= sqrt(num)+1; i++){
			if((num % i) == 0)
				return false;
		}
		return true;
	}

	public boolean hasSolution(int a, int b, int m){
		if(b % getGcd(a,m) != 0){
			return false;
		}
		return true;
	}
	
	public int getGcd(int a, int b){
		if(b == 0)
			return a;
		
		return getGcd(b,a%b);
	}
	
	public int egcd(int a, int b){
		int x = 0, y = 1;
		int u = 1, v = 0;
		int m = 0, n = 0, q = 0, r = 0;
		
		while(a != 0){
			q = b / a;
			r = b % a;
			m = x - u*q;
			n = y - v*q;
			b = a;
			a = r;
			x = u;
			y = v;
			u = m;
			v = n;
		}
		return x;
	}
	
	public int solveLinearCongruence(int a, int b, int m){
		int comFac = getGcd(a, getGcd(b, m));
		int solution = (b/comFac) * (egcd(a/comFac, m/comFac));
		if(solution > m){
			while(solution > m){
				solution -= m/comFac;
			}
		}else if(solution < 0){
			while(solution < 0){
				solution += m/comFac;
			}
		}
		return solution;
	}
	
	public BigInteger decrypt(int emsg, int d, int n){
		BigInteger cy = BigInteger.valueOf(emsg);
		BigInteger N = BigInteger.valueOf(n);
		BigInteger m = (cy.pow(d)).mod(N);
		
		return m;
	}
	
	public static void main(String[] args) {
		int n = 999797, e=123457;
		new RSA(n, e);
	}
}
