import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
* Esta e a classe responsavel por criar um chain code de uma imagem. Esta classe analisa um objeto da classe
* {@link BufferedImage BufferedImage} para obter o chain code da imagem.
*
* @author Italo Tobler Silva - nUSP 8551910
* @author Vinicius Candiani - nUSP 8551910
*/
public class ChainCodes{
	private int initPoint;
	private int width;
	private int height;
	private int nPoints;
	private double length;
	private int[][] imgMatrix;

	/**
	* Construtor padrao da classe. O construtor analisa a imagem passada e gera o chain codes com todas
	* as informacoes necessarias.
	*
	* @param img - imagem a ser analisada para gerar o chain code.
	*/
	public ChainCodes(BufferedImage img){
		imgMatrix = convertToMatrix(img);
	}

	/**
	* Metodo que analisa a imagem e converte seu conteudo para uma matriz de numeros inteiros. Durante a
	* conversao sao analisados e salvos a altura, a largura e o ponto inicial da imagem.
	*
	* @param img - imagem a ser analisada.
	*/
	private int[][] convertToMatrix(BufferedImage img){
		boolean found = false;
		height = img.getHeight();
		width = img.getWidth();
		int[][] matrix = new int[height][width];

		for(int row = 0; row < height; row++){
			for(int col = 0; col < width; col++){
				matrix[row][col] =  (img.getRGB(col, row) & 0xff);
				if(!found && matrix[row][col] < 255){
					found = true;
					initPoint = (width * row) + col;
				}
			}
		}

		return matrix;
	}

	public void printMatrix(){
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				if(imgMatrix[i][j] > 0)
					System.out.print(" ");
				else
					System.out.print(".");
			}
			System.out.println("");
		}
	}

	public static void main(String[] args){
		File n = null;
		try{
			n = new File("./utils/aform.png");
		}catch(NullPointerException e){
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("abriu o arquivo");

		BufferedImage image = null;
		try{
			image = ImageIO.read(n);
		}catch(java.io.IOException e){
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("obteve a imagem");

		ChainCodes code = new ChainCodes(image);
		code.printMatrix();
	}
}
