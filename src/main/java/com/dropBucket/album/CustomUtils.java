package com.dropBucket.album;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by gaurav on 29/3/18.
 */
@Slf4j
public class CustomUtils {

    public static File ConvertMultipartFileTOFile(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getName();
        String fileNameWithoutExtension = fileName.substring(0,fileName.indexOf('.'));
        String fileExtension = '.' + fileName.substring(fileName.indexOf('.')+1);
        Path path = Files.createTempFile(fileNameWithoutExtension,fileExtension);
        File file = path.toFile();
//        File file = new File(multipartFile.getName());
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
            byte[] bytes = multipartFile.getBytes();
            fileOutputStream.write(bytes);
        } catch (FileNotFoundException e) {
            log.info("FileNotFoundException while converting multipart file to file",e.getMessage());
        } catch (IOException e) {
            log.info(" IOException while converting multipart file to file",e.getMessage());
        }
        return file;
    }

    public static File createThumbnailFromImage(File file, String fileName) {
        File thumbnailFile = null;
        try {
            String fileNameWithoutExtension = fileName.substring(0,fileName.indexOf('.'));
            String fileExtension = '.' + fileName.substring(fileName.indexOf('.')+1);

            Path path = Files.createTempFile(fileNameWithoutExtension,fileExtension);
            thumbnailFile = path.toFile();

            BufferedImage bufferedImage = ImageIO.read(file);

            int thumbnailWidth = 150;

            int widthToScale, heightToScale;
            if (bufferedImage.getWidth() > bufferedImage.getHeight()) {

                heightToScale = (int)(1.1 * thumbnailWidth);
                widthToScale = (int)((heightToScale * 1.0) / bufferedImage.getHeight()
                        * bufferedImage.getWidth());

            } else {
                widthToScale = (int)(1.1 * thumbnailWidth);
                heightToScale = (int)((widthToScale * 1.0) / bufferedImage.getWidth()
                        * bufferedImage.getHeight());
            }

            BufferedImage resizedImage = new BufferedImage(widthToScale,
                    heightToScale, bufferedImage.getType());
            Graphics2D g = resizedImage.createGraphics();

            g.setComposite(AlphaComposite.Src);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.drawImage(bufferedImage, 0, 0, widthToScale, heightToScale, null);
            g.dispose();

            int x = (resizedImage.getWidth() - thumbnailWidth) / 2;
            int y = (resizedImage.getHeight() - thumbnailWidth) / 2;

            if (x < 0 || y < 0) {
                throw new IllegalArgumentException("Width of new thumbnail is bigger than original image");
            }

            BufferedImage thumbnailBufferedImage = resizedImage.getSubimage(x, y, thumbnailWidth, thumbnailWidth);

            try {
                ImageIO.write(thumbnailBufferedImage, "JPG", thumbnailFile);
            }
            catch (IOException ioe) {
                System.out.println("Error writing image to file");
                throw ioe;
            }

        } catch (IOException e) {
            log.info(" IOException while converting multipart file to file",e.getMessage());
        }
        return thumbnailFile;
    }
}
