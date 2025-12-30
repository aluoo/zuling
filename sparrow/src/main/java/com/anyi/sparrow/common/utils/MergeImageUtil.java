package com.anyi.sparrow.common.utils;

import sun.awt.image.BufferedImageGraphicsConfig;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author ys
 * @version 1.0
 * @date 2022/12/5 16:33
 * @Description 图片合并工具类
 */
public final class MergeImageUtil {

    private static class ImageType {
        private static final String DEFAULT_NAME = "image";
        private static final String PNG = "png";
        private static final String BASE64_PREFIX = "data:image/png;base64,";
    }

    /**
     * 将两个图片合并成一个图片【垂直合并】
     * @param first 图片1文件路径
     * @param second 图片2文件路径
     * @return 返回合并之后的图片
     */
    public static BufferedImage mergeImage(String first, String second) throws IOException {
        FileInputStream img1 = new FileInputStream(first);
        FileInputStream img2 = new FileInputStream(second);
        BufferedImage image01 = ImageIO.read(img1);
        BufferedImage image02 = ImageIO.read(img2);
        return mergeImage(image01, image02, false);
    }

    /**
     * 将两个图片合并成一个图片【垂直合并】
     * @param first 图片1
     * @param second 图片2
     * @return 返回合并之后的图片
     */
    public static BufferedImage mergeImage(BufferedImage first, BufferedImage second) {
        return mergeImage(first, second, false);
    }

    /**
     * 合并两个图片
     * @param first 图片1
     * @param second 图片2
     * @param horizontal 等于true,则两个图片水平合并显示, 否则两个图片垂直合并显示
     * @return 返回合并之后的图片
     */
    public static BufferedImage mergeImage(BufferedImage first, BufferedImage second, boolean horizontal) {
        return mergeImage(first, second, horizontal, 0);
    }

    /**
     * 合并两个图片
     * @param first 图片1
     * @param second 图片2
     * @param horizontal 等于true,则两个图片水平合并显示, 否则两个图片垂直合并显示
     * @param gap 图片之间的间距
     * @return 返回合并之后的图片
     */
    public static BufferedImage mergeImage(BufferedImage first, BufferedImage second, boolean horizontal, int gap) {
        return mergeImage(first, second, horizontal, false, gap);
    }

    /**
     * 合并两个图片
     * @param first 图片1
     * @param second 图片2
     * @param horizontal 等于true,则两个图片水平合并显示, 否则两个图片垂直合并显示
     * @param center 图片是否水平居中, horizontal 等于 false 垂直合并才有效
     * @param gap 图片之间的间距
     * @return 返回合并之后的图片
     */
    public static BufferedImage mergeImage(BufferedImage first, BufferedImage second, boolean horizontal, boolean center, int gap) {
        return mergeImage(first, second, horizontal, center, false, gap, null);
    }

    /**
     * 合并两个图片
     * @param first 图片1
     * @param second 图片2
     * @param horizontal 等于true,则两个图片水平合并显示, 否则两个图片垂直合并显示
     * @param center 图片是否水平居中, horizontal 等于 false 垂直合并才有效
     * @param transparent 合并后的图片背景是否透明色
     * @param gap 图片之间的间距
     * @param color 图片的背景颜色
     * @return 返回合并之后的图片
     */
    public static BufferedImage mergeImage(BufferedImage first, BufferedImage second, boolean horizontal, boolean center, boolean transparent, int gap, Color color) {
        if (first == null) {
            return second;
        } else if (second == null) {
            return first;
        }
        // 获取原始图片宽度
        int firstWidth = first.getWidth();
        int firstHeight = first.getHeight();
        int secondWidth = second.getWidth();
        int secondHeight = second.getHeight();
        int picGap = gap * 2;
        // 合并后的图片宽高
        int mergeWidth = Math.max(firstWidth, secondWidth) + picGap;
        int mergeHeight = firstHeight + secondHeight + picGap;
        if (horizontal) {
            mergeWidth = firstWidth + secondWidth + picGap;
            mergeHeight = Math.max(firstHeight, secondHeight) + picGap;
        }

        // 创建目标图片对象
        BufferedImage target = new BufferedImage(mergeWidth, mergeHeight, BufferedImage.TYPE_INT_RGB);
        if (transparent) {
            // 设置图片背景为透明的
            BufferedImageGraphicsConfig config = BufferedImageGraphicsConfig.getConfig(target);
            target = config.createCompatibleImage(mergeWidth, mergeHeight, Transparency.TRANSLUCENT);
        }

        // 创建绘制目标图片对象
        Graphics2D graphics = target.createGraphics();
        int x1, y1;
        int x2, y2;
        if (horizontal) {
            // 水平合并
            x1 = gap;
            y1 = gap;
            x2 = firstWidth + gap;
            y2 = gap;
        } else {
            // 垂直合并
            if (center) {
                // 计算居中位置
                x1 = (mergeWidth - firstWidth) / 2;
                x2 = (mergeWidth - secondWidth) / 2;
            } else {
                x1 = gap;
                x2 = gap;
            }
            y1 = gap;
            y2 = firstHeight + gap;
        }

        // 图片的背景颜色
        if (color != null) {
            graphics.setColor(color);
            graphics.fillRect(0, 0, mergeWidth, mergeHeight);
        }

        // 按照顺序绘制图片
        graphics.drawImage(first, x1, y1, firstWidth, firstHeight, null);
        graphics.drawImage(second, x2, y2, secondWidth, secondHeight, null);
        graphics.dispose();
        // 返回合并后的图片对象
        return target;
    }

    /**
     * 保存合并后的图片到文件
     * @param image 图片
     */
    public static void writeToFile(BufferedImage image,File file) throws IOException {
        // 保存图片
        ImageIO.write(image, ImageType.PNG, file);
    }

    /**
     * 图片转换成 Base64 编码字符串
     * @param image 图片
     * @return 返回转换之后的 base64 编码字符串
     */
    public static String writeToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // 将图片写入字节输出流里面
        ImageIO.write(image, ImageType.PNG, bos);
        // 字节输出流转换成字节数组
        byte[] bytes = bos.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String base64 = encoder.encodeBuffer(bytes).trim();
        // 将字符串中的所有【\n】、【\r】删除
        base64 = base64.replaceAll("\n", "").replaceAll("\r", "");
        // 返回 base64 编码字符串
        return ImageType.BASE64_PREFIX + base64;
    }

    /** ====================================================================================================== */

    public static void main(String[] args) throws Exception {

        FileInputStream img1 = new FileInputStream("C:\\Users\\41846\\Desktop\\package.jpg");
        FileInputStream img2 = new FileInputStream("C:\\Users\\41846\\Desktop\\obu.jpg");
        BufferedImage image01 = ImageIO.read(img1);
        BufferedImage image02 = ImageIO.read(img2);

        // 合并两个图片，背景不透明
        BufferedImage target = mergeImage(image01, image02, false, true, false, 0, Color.white);
        File file = new File("D:\\003.jpg");
        //writeToFile("D:\\003.jpg", target);
        System.out.println("合并成功......");

        // 合并两个图片，背景透明,ETC目前用这种
        target = mergeImage(image01, image02, true, true, true, 20, null);
        //writeToFile("D:\\004.jpg", target);
        System.out.println("合并成功......");
    }
}
