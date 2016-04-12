import java.lang.Math;
import java.util.Vector;
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
	private static int[] sumCol = {1, 1, 0, -1, -1, -1, 0, 1};
	private static int[] sumRow = {0, 1, 1, 1, 0, -1, -1, -1};
	private int[] initPoint;
	private int objWidth;
	private int objHeight;
	private int imgHeight;
	private int imgWidth;
	private int nPoints;
	private Vector<Integer> chain;
	private double length;
	private int[][] imgMatrix;

	/**
	* Construtor padrao da classe. O construtor analisa a imagem passada e gera o chain codes com todas
	* as informacoes necessarias.
	*
	* @param img - imagem a ser analisada para gerar o chain code.
	*/
	public ChainCodes(BufferedImage img){
		chain = new Vector<Integer>(0, 1);
		imgMatrix = convertToMatrix(img);
		length = 0;
		int row = initPoint[0];
		int col = initPoint[1];
		int dir = 0;
		do{
			if(imgMatrix[row + sumRow[dir]][col + sumCol[dir]] < 255){
				int i;
				for(i = (dir-1+8)%8; i != dir && imgMatrix[row + sumRow[i]][col + sumCol[i]] < 255; i = (i-1+8)%8);
				dir = (i+1)%8;
			}else{
				int i;
				for(i = (dir+1)%8; i != dir && imgMatrix[row + sumRow[i]][col + sumCol[i]] == 255; i = (i+1)%8);
				dir = i;
			}
			chain.add(dir);
			length += (dir%2 == 0)? 1 : Math.sqrt(2);
			col += sumCol[dir];
			row += sumRow[dir];
		}while(row != initPoint[0] || col != initPoint[1]);
		nPoints = chain.capacity();
	}

	/**
	* Metodo que analisa a imagem e converte seu conteudo para uma matriz de numeros inteiros. Durante a
	* conversao sao analisados e salvos a altura, a largura e o ponto inicial da imagem.
	*
	* @param img - imagem a ser analisada.
	*/
	private int[][] convertToMatrix(BufferedImage img){
		boolean found = false;
		int rowMax, rowMin, colMax, colMin;
		imgHeight = img.getHeight();
		imgWidth = img.getWidth();
		rowMin = imgHeight;
		rowMax = 0;
		colMin = imgWidth;
		colMax = 0;
		int[][] matrix = new int[imgHeight][imgWidth];

		for(int row = 0; row < imgHeight; row++){
			for(int col = 0; col < imgWidth; col++){
				matrix[row][col] =  (img.getRGB(col, row) & 0xff);
				if(matrix[row][col] < 255){
					if(!found){
						found = true;
						initPoint = new int[2];
						initPoint[0] = row;
						initPoint[1] = col;
					}
					if(row < rowMin)
						rowMin = row;
					if(row > rowMax)
						rowMax = row;
					if(col < colMin)
						colMin = col;
					if(col > colMax)
						colMax = col;
				}
			}
		}
		objWidth = colMax - colMin + 1;
		objHeight = rowMax - rowMin + 1;

		return matrix;
	}

	public void printMatrix(){
		for(int i = 0; i < imgHeight; i++){
			for(int j = 0; j < imgWidth; j++){
				if(imgMatrix[i][j] < 255)
					System.out.print(".");
				else
					System.out.print(" ");
			}
			System.out.println("");
		}
	}

	public int[] getInitPoint(){
		return initPoint;
	}

	public int getObjWidth(){
		return objWidth;
	}

	public int getObjHeight(){
		return objHeight;
	}

	public int getNPoints(){
		return nPoints;
	}

	public double getLength(){
		return length;
	}

	public String getChain(){
		return chain.toString();
	}

	public static void main(String[] args){
		File n = null;
		try{
			n = new File("./utils/circulo.png");
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
		System.out.println("initPoint: " + code.getInitPoint()[1] + code.getInitPoint()[0]);
		System.out.println("width: " + code.getObjWidth() + " height: " + code.getObjHeight());
		System.out.println("nPoints: " + code.getNPoints());
		System.out.println("length: " + code.getLength());
		System.out.println("chain: " + code.getChain());
	}
}
