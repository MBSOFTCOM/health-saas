package cn.iocoder.yudao.module.ppd.utils;

import com.itextpdf.text.pdf.BaseFont;
import com.lowagie.text.DocumentException;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;

/**
 * @author flow
 */
public class ITextRendererUtils {

    /**
     * 将数据填充完成后的html模板转化成ITextRenderer对象
     * @param content html 内容
     * @return ITextRenderer对象
     * @throws IOException io异常
     * @throws DocumentException 文档转化异常
     */
    public static ITextRenderer getiTextRenderer(String content) throws IOException, DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        // 中文字体
        fontResolver.addFont("static/fontcss/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        fontResolver.addFont("C:/Windows/Fonts/simhei.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        fontResolver.addFont("C:/Windows/Fonts/simkai.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        renderer.setDocumentFromString(content);
        renderer.layout();
        return renderer;
    }
}
