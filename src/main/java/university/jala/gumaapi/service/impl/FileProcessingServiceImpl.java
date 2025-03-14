package university.jala.gumaapi.service.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import university.jala.gumaapi.service.FileProcessingService;

@Service
@Log4j2
public class FileProcessingServiceImpl implements FileProcessingService {

    //Logger logger = LoggerFactory.getLogger(FileProcessingService.class);
    public String processFile(MultipartFile file) {
        String text;

        try (final PDDocument document = PDDocument.load(file.getInputStream())) {
            final PDFTextStripper pdfStripper = new PDFTextStripper();
            text = pdfStripper.getText(document);
        } catch (final Exception ex) {
            text = "Error parsing PDF";
        }
        log.info(text);

        return text;
    }
}