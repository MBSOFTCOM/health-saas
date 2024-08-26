package cn.iocoder.yudao.module.ppd.controller.admin.pdf;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.ppd.utils.ITextRendererUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 打印PDF")
@RestController
@RequestMapping("/tb/printPdf")
@Validated
public class PdfController {

    @Resource
    private Configuration configuration;


    @PostMapping("/physical")
    @Operation(summary = "打印体检单PDF")
    public CommonResult<ResponseEntity<byte[]>> printPdf(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // 不建议直接创建Template实例，开销比较大，可以直接通过Configuration实例获取，有缓存机制
            Template template = configuration.getTemplate("physical.ftl");
            StringWriter result = new StringWriter(1024);
            template.process(null, result);
            String content = result.toString();

            ITextRenderer renderer = ITextRendererUtils.getiTextRenderer(content);

            renderer.setDocumentFromString(content);
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();

        } catch (Exception e) {
            return success(ResponseEntity.internalServerError().body(null));
        }

        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.APPLICATION_PDF);
        respHeaders.setContentDisposition(ContentDisposition.inline().filename("physical.pdf", StandardCharsets.UTF_8).build());
        return success(new ResponseEntity<>(outputStream.toByteArray(), respHeaders, HttpStatus.OK));
    }





}
