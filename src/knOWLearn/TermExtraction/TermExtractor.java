package knOWLearn.TermExtraction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import knOWLearn.Utils.Document.Document;
import knOWLearn.Utils.Document.DocumentDataBase;

/**
 * @author Samuel Vieyra
 * samuel.vieyra@infotec.com.mx
 */

public class TermExtractor {

    private DocumentDataBase database;
    private Double threshold = 0.3;
    
    
    public TermExtractor(String path) throws FileNotFoundException, IOException {
        database = new DocumentDataBase(path);
    }
    
    public TermExtractor(String[] path) throws FileNotFoundException, IOException {
        database = new DocumentDataBase(path);
    }
    
    public TermExtractor(String path, boolean lineAsDocument) throws FileNotFoundException, IOException {
        database = new DocumentDataBase(path,lineAsDocument);
    }
    
    public TermExtractor(String[] path, boolean lineAsDocument) throws FileNotFoundException, IOException {
        database = new DocumentDataBase(path,lineAsDocument);
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public DocumentDataBase getDatabase() {
        return database;
    }
    
    public List<String> calculateFrequentTerms(){
        List<String> frequentTerms = new ArrayList<String>();
        Map<String,List<Document>> terms = database.getTerms();
        int documentNumber = database.getDocuments().size();
        for (Map.Entry<String, List<Document>> term : terms.entrySet()) {
            if( ((double) term.getValue().size()/ (double) documentNumber) >= threshold ){
                frequentTerms.add(term.getKey());
                System.out.println(term.getKey() + ": " + (double)term.getValue().size()/(double)documentNumber);
            }
        }
        return frequentTerms;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String filePath = "/home/vieyra/Corpus/knOWLearn evaluation/";
        String file = "Journal of Product Innovation Management.txt";
       
        TermExtractor extractor = new TermExtractor(filePath + file, true);
        extractor.setThreshold(0.1);
        extractor.calculateFrequentTerms();
    }
    
}
