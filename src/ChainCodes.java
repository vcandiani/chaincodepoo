import java.lang.Math;
import java.util.Vector;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
* Esta e a classe responsavel por criar um chain code de uma imagem. Esta classe analisa um arquivo contendo
* uma imagem para obter o seu chain code.
*
* @author Italo Tobler Silva - nUSP 8551910
* @author Vinicius Candiani - nUSP 8551910
*/
public class ChainCodes{
	// sumRow e sumCol armazenam o quanto a posicao deve ser incrementada para cada direcao(de 0 a 7)
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
	* Construtor padrao da classe. O construtor recebe um nome de arquivo que deve pertencer
	* a imagem que se deseja analisar.
	*
	* @param fname - nome do arquivo contendo a imagem a ser analisada
	* @throws IOException 
	*/
	public ChainCodes(String fname) throws IOException{
		File n = null;
		BufferedImage img = null;
		
		// Tenta abrir a image, e caso haja erro, da throw na excecao
		try{
			n = readFile(fname);
			img = readImg(n);
		}catch(IOException e){
			throw e;
		}
		
		// Cria-se o vetor para armazenar o chain code
		chain = new Vector<Integer>(0, 1);
		// A imagem e convertida para uma matriz de inteiro contendo valores entre 0(preto) e 255(branco)
		imgMatrix = convertToMatrix(img);
		length = 0;
		int row = initPoint[0];
		int col = initPoint[1];
		// A primeira direcao a ser checada e a direita
		int dir = 0;
		// Esse loop para quando a posicao atual voltar ao ponto inicial
		do{
			// Caso a direcao aponte para um espaco nao branco, gira no sentido anti horario
			// ate encontrar um espaco em branco
			if(imgMatrix[row + sumRow[dir]][col + sumCol[dir]] < 255){
				int i;
				for(i = (dir-1+8)%8; i != (dir-3+8)%8 && imgMatrix[row + sumRow[i]][col + sumCol[i]] < 255; i = (i-1+8)%8);
				// Armazena a direcao anterior(ultimo espaco nao branco)
				dir = (i+1)%8;
			// Caso aponte para um espaco branco, gira ate encontrar um espaco nao branco
			}else{
				int i;
				for(i = (dir+1)%8; i != dir && imgMatrix[row + sumRow[i]][col + sumCol[i]] == 255; i = (i+1)%8);
				// armazena a direcao atual
				dir = i;
			}
			// A direcao e adicionada ao chain codes, o comprimento e incrementado e a posicao atualizada
			chain.add(dir);
			length += (dir%2 == 0)? 1 : Math.sqrt(2);
			col += sumCol[dir];
			row += sumRow[dir];
		}while(row != initPoint[0] || col != initPoint[1]);
		// Quando a analise termina, o numero de pontos e salvo
		nPoints = chain.capacity();
	}
	
	/**
	* Metodo que tenta abrir um arquivo com o nome passado.
	*
	* @param fname - nome do arquivo a ser aberto
	* @return arquivo aberto ou null em caso de erro
	* @throws IOException
	*/
	public static File readFile(String fname) throws IOException{
		File n = null;
		try{
			n = new File(fname);
		}catch(NullPointerException e){
			throw new IOException("Nao foi possivel abrir o arquivo");
		}
		return n;
	}
	
	/**
	* Metodo que tenta obter uma imagem a partir de um arquivo aberto.
	*
	* @param n - arquivo aberto
	* @return imagem obtida ou null em caso de erro
	* @throws IOException
	*/
	public static BufferedImage readImg(File n) throws IOException{
		BufferedImage image = null;
		try{
			image = ImageIO.read(n);
		}catch(IOException e){
			throw new IOException("Nao foi possivel obter a imagem");
		}
		return image;
	}


	/**
	* Metodo que analisa a imagem e converte seu conteudo para uma matriz de numeros inteiros. Durante a
	* conversao sao analisados e salvos a altura, a largura e o ponto inicial da imagem.
	*
	* @param img - imagem a ser analisada.
	*/
	private int[][] convertToMatrix(BufferedImage img){
		// found indica se o ponto inicial foi encontrado
		boolean found = false;
		int rowMax, rowMin, colMax, colMin;
		// imgHeight e imgWidth armazenam o tamanho total da imagem
		imgHeight = img.getHeight();
		imgWidth = img.getWidth();
		// Essas 4 variaveis armazenam os limites do objeto nao branco na imagem
		rowMin = imgHeight;
		rowMax = 0;
		colMin = imgWidth;
		colMax = 0;
		int[][] matrix = new int[imgHeight][imgWidth];

		for(int row = 0; row < imgHeight; row++){
			for(int col = 0; col < imgWidth; col++){
				// Em cada posicao da matriz armazena-se um valor de 0(preto) a 255(branco)
				// para indicar a cor da imagem nesse ponto (considera-se imagem preto e branco)
				matrix[row][col] =  (img.getRGB(col, row) & 0xff);
				// Ao encontrar um ponto nao branco
				if(matrix[row][col] < 255){
					// Caso seja o ponto inicial, altera o indicador e armazena as coordenadas
					if(!found){
						found = true;
						initPoint = new int[2];
						initPoint[0] = row;
						initPoint[1] = col;
					}
					// Atualiza os valores do limite da imagem
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
		// Ao terminar de copiar a imagem, calcula-se a largura e a altura do objeto nao branco
		objWidth = colMax - colMin + 1;
		objHeight = rowMax - rowMin + 1;

		return matrix;
	}

	/**
	* Getter do ponto inicial. As coordenadas sao armazenadas em um int[].
	*
	* @return coordenadas do ponto inicial
	*/
	public int[] getInitPoint(){
		return initPoint;
	}

	/**
	* Getter da largura do objeto na imagem.
	*
	* @return largura da imagem
	*/
	public int getObjWidth(){
		return objWidth;
	}

	/**
	* Getter da altura do objeto na imagem.
	*
	* @return altura do objeto na imagem
	*/
	public int getObjHeight(){
		return objHeight;
	}

	/**
	* Getter do numero de pontos da borda.
	*
	* @return numero de pontos da borda
	*/
	public int getNPoints(){
		return nPoints;
	}

	/**
	* Getter do comprimento da borda.
	*
	* @return comprimento da borda
	*/
	public double getLength(){
		return length;
	}

	/**
	* Getter do chain code da imagem. O chain code fica armazenado em um {@link Vector Vector}, mas
	* a funcao retorna uma string contendo todos os valores separados por virgula.
	*
	* @return {@link String String} contendo o chain code
	*/
	public String getChain(){
		return chain.toString().substring(1, chain.toString().length()-1);
	}

}
