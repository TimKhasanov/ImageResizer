import org.imgscalr.Scalr;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ImageResizer implements Runnable {
    private ConcurrentLinkedQueue<File>files;
    private int newWidth;
    private String dstFolder;
    private long start;
    private int newHeight;

    public ImageResizer( ConcurrentLinkedQueue<File>files, int newWidth, String dstFolder, long start) {
        this.files = files;
        this.newWidth = newWidth;
        this.dstFolder = dstFolder;
        this.start = start;
    }


    public static BufferedImage resize(BufferedImage src, int targetWidth, int targetHeight) {
        return Scalr.resize(src, targetWidth, targetHeight);
    }


    @Override
    public void run() {

        try {
            for (File file : files) {
                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    continue;
                }
                newHeight = (int) Math.round(image.getHeight() / (image.getWidth() / (double) newWidth)
                );
                BufferedImage newImage = resize(image, newWidth, newHeight);
                File newFile = new File(dstFolder + "/" + file.getName());
                ImageIO.write(newImage, "jpg", newFile);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Finished after start - " + (System.currentTimeMillis() - start) + "ms");
    }


}
