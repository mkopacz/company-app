package pl.kopacz.service;

import com.lowagie.text.DocumentException;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import pl.kopacz.service.dto.ProductReportDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService implements InitializingBean {

    private TemplateEngine templateEngine;

    public byte[] createProductReport(List<ProductReportDTO> reportData) throws DocumentException {
        Map<String, Object> variables = prepareProductReportVariables(reportData);
        return createReport("productReport", variables);
    }

    private Map<String, Object> prepareProductReportVariables(List<ProductReportDTO> reportData) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("reports", reportData);
        return variables;
    }

    private byte[] createReport(String templateName, Map<String, Object> variables) throws DocumentException {
        String content = generateHtml(templateName, variables);
        ByteOutputStream os = generatePdf(content);
        return os.getBytes();
    }

    private String generateHtml(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }

    private ByteOutputStream generatePdf(String content) throws DocumentException {
        ByteOutputStream os = new ByteOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(content);

        renderer.layout();
        renderer.createPDF(os);

        return os;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(prepareTemplateResolver());
        this.templateEngine = templateEngine;
    }

    private TemplateResolver prepareTemplateResolver() {
        TemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("reports/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("XHTML");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

}
