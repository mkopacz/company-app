package pl.kopacz.service;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import pl.kopacz.service.dto.ProductReportDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProductReportService {

    private TemplateEngine templateEngine;
    private ITextRenderer renderer;

    public ProductReportService() throws IOException, DocumentException {
        this.templateEngine = createTemplateEngine();
        this.renderer = createITextRenderer();
    }

    public byte[] createProductReport(ProductReportDTO reportData) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        generateReport(outputStream, reportData);
        return outputStream.toByteArray();
    }

    private void generateReport(ByteArrayOutputStream outputStream, ProductReportDTO reportData)
        throws DocumentException {

        reportData.sort();
        for (ProductReportDTO reportDataPart : reportData) {
            String content = generateContent(reportDataPart);
            generatePdf(outputStream, content);
        }
        renderer.finishPDF();
    }

    private String generateContent(ProductReportDTO reportData) {
        Context context = new Context();
        context.setVariables(prepareVariables(reportData));
        return templateEngine.process("productReport", context);
    }

    private Map<String, Object> prepareVariables(ProductReportDTO reportData) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("date", new Date());
        variables.put("report", reportData);
        return variables;
    }

    private void generatePdf(ByteArrayOutputStream outputStream, String content) throws DocumentException {
        renderer.setDocumentFromString(content, "reports/");
        renderer.layout();
        if (outputStream.size() == 0) {
            renderer.createPDF(outputStream, false);
        } else {
            renderer.writeNextDocument();
        }
    }

    private TemplateEngine createTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        TemplateResolver templateResolver = prepareTemplateResolver();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    private TemplateResolver prepareTemplateResolver() {
        TemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("reports/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("XHTML");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    private ITextRenderer createITextRenderer() throws IOException, DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        addCustomFonts(renderer);
        setCustomCallback(renderer);
        return renderer;
    }

    private void addCustomFonts(ITextRenderer renderer) throws IOException, DocumentException {
        renderer.getFontResolver().addFont("reports/fonts/arialuni.ttf",
            BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    }

    private void setCustomCallback(ITextRenderer renderer) {
        ITextOutputDevice outputDevice = renderer.getOutputDevice();
        ResourceLoaderUserAgent callback = new ResourceLoaderUserAgent(outputDevice);
        callback.setSharedContext(renderer.getSharedContext());
        renderer.getSharedContext().setUserAgentCallback(callback);
    }

    private class ResourceLoaderUserAgent extends ITextUserAgent {

        ResourceLoaderUserAgent(ITextOutputDevice outputDevice) {
            super(outputDevice);
        }

        protected InputStream resolveAndOpenStream(String uri) {
            InputStream inputStream = super.resolveAndOpenStream(uri);
            return Optional.ofNullable(inputStream).orElse(
                this.getClass().getClassLoader().getResourceAsStream(uri));
        }

    }

}
