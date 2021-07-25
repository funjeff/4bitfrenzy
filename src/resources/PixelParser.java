
package resources;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import engine.Sprite;

public class PixelParser {
	
	private PixelParser () {
		//Cannot be constructed
	}
	
	public static ArrayList<ParsedFrame> parse (Sprite sprite) {
		ArrayList<ParsedFrame> frames = new ArrayList<ParsedFrame> ();
		for (int i = 0; i < sprite.getFrameCount (); i ++) {
			BufferedImage workingImg = sprite.getFrame (i);
			BufferedImage newImg = workingImg;
			//Fuck off BufferedImage.TYPE_BYTE_INDEXED images
			if (workingImg.getType () != BufferedImage.TYPE_4BYTE_ABGR) {
				newImg = new BufferedImage (workingImg.getWidth (), workingImg.getHeight (), BufferedImage.TYPE_4BYTE_ABGR);
				Graphics g = newImg.getGraphics ();
				g.drawImage (workingImg, 0, 0, null);
			}
			WritableRaster alpha = newImg.getAlphaRaster ();
			WritableRaster color = newImg.getRaster ();
			ArrayList<Pixel> pixels = new ArrayList<Pixel> ();
			for (int wy = 0; wy < alpha.getHeight (); wy ++) {
				for (int wx = 0; wx < alpha.getWidth (); wx ++) {
					int pixelAlpha = alpha.getSample (wx, wy, 0);
					if (pixelAlpha != 0) {
						int workingColor = 0;
						workingColor += (color.getSample (wx, wy, 0) << 16);
						workingColor += (color.getSample (wx, wy, 1) << 8);
						workingColor += color.getSample (wx, wy, 2);
						workingColor = (workingColor | (pixelAlpha << 24));
						pixels.add (new Pixel (wx, wy, workingColor));
					}
				}
			}
			frames.add (new ParsedFrame (pixels));
		}
		return frames;
	}
}