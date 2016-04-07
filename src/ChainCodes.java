import java.io.File;
import java.awt.image.BufferedImage;

public class ChainCodes{
	private int initPoint;
	private width;
	private height;
	private nPoints;
	private double length;
	private int[][] imgMatrix;

	public ChainCodes(BufferedImage img){
		imgMatrix = convertToMatrix(img);
		for(){
		}
	}

	private int[][] convertToMatrix(BufferedImage img){
		int h = img.getHeight();
		int w = img.getWidth();
		imgMatrix = new int[heigth][width]

		// To do: pegar init point usando uma flag
		for(int row = 0; row < height; row++){
			for(int col = 0; col < wodth; col++){
				imgMatrix[row][col] =  (img.getRGB(col, row) & 0xff);
			}
		}

		return imgMatrix;
	}

	public static void main(String[] args){
		File n = null;
		n = new File("image.png");
		BufferedImage image = ImageIO.read(n);
		ChainCodes code = new ChainCodes(image);
	}
}
