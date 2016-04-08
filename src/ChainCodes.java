import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ChainCodes{
	private int initPoint;
	private int width;
	private int height;
	private int nPoints;
	private double length;
	private int[][] imgMatrix;

	public ChainCodes(BufferedImage img){
		imgMatrix = convertToMatrix(img);
	}

	private int[][] convertToMatrix(BufferedImage img){
		boolean found = false;
		int h = img.getHeight();
		int w = img.getWidth();
		imgMatrix = new int[h][w];

		for(int row = 0; row < height; row++){
			for(int col = 0; col < width; col++){
				imgMatrix[row][col] =  (img.getRGB(col, row) & 0xff);
				if(!found && imgMatrix[row][col] > 0){
					found = true;
					initPoint = (width * row) + col;
				}
			}
		}

		return imgMatrix;
	}

	public static void main(String[] args){
		File n = null;
		try{
			n = new File("image.png");
		}catch(NullPointerException e){
			System.out.println(e.getMessage());
			return;
		}

		BufferedImage image = null;
		try{
			image = ImageIO.read(n);
		}catch(java.io.IOException e){
			System.out.println(e.getMessage());
			return;
		}

		ChainCodes code = new ChainCodes(image);
	}
}
