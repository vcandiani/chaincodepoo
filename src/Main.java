import java.io.IOException;

/**
* Essa classe e utilizada para o teste de execucao da classe {@link ChainCodes ChainCodes}.
*
* @author Italo Tobler Silva - nUSP 8551910
* @author Vinicius Candiani - nUSP 8551910
*/
public class Main {
	/**
	* A main e o unico metodo da classe.
	*/
	public static void main(String[] args){
		ChainCodes code;
		try{
			code = new ChainCodes(EntradaTeclado.leString());
		}catch(IOException e){
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("initPoint: " + code.getInitPoint()[1] + " " + code.getInitPoint()[0]);
		System.out.println("width: " + code.getObjWidth() + " height: " + code.getObjHeight());
		System.out.println("nPoints: " + code.getNPoints());
		System.out.println("length: " + code.getLength());
		System.out.println("chain: " + code.getChain());
	}

}
