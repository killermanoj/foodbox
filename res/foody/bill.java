package sample.foody;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class bill {

    public static void ss() throws AWTException, IOException {
        Robot robot =new Robot();
        Rectangle rectangle = new Rectangle(275,95,1013,897);
        BufferedImage myimg  = robot.createScreenCapture(rectangle);
        Image img = SwingFXUtils.toFXImage(myimg,null);
        ImageIO.write(myimg,"png",new File("bill.png"));
        System.out.println("took ss");

    }
}
