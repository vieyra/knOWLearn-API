package knOWLearn.Utils.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Samuel Vieyra
 * samuel.vieyra@infotec.com.mx
 */

public class DocumentDataBase {
    private List<Document> documents = new ArrayList<Document>();
    private String[] acceptedFileExtensions = {"txt","rtf"};
    private String language = "en";
    private boolean documentAsLine = false;
    
    private Map<String,List<Document>> terms = new HashMap<String, List<Document>>();
    //All terms contained in the document database
    
    public DocumentDataBase(String path) throws FileNotFoundException, IOException {
        processDocuments(path);
        getTermCoverage();
    }
    
    public DocumentDataBase(String[] documentPaths) throws FileNotFoundException, IOException {
        processDocuments(documentPaths);
        getTermCoverage();
    }
    
    public DocumentDataBase(String path, boolean documentAsLine) throws FileNotFoundException, IOException {
        this.documentAsLine = documentAsLine;
        processDocuments(path);
        getTermCoverage();
    }
    
    public DocumentDataBase(String[] documentPaths, boolean documentAsLine) throws FileNotFoundException, IOException {
        this.documentAsLine = documentAsLine;
        processDocuments(documentPaths);
        getTermCoverage();
    }
    
    private void processDocuments(String[] documentPaths) throws FileNotFoundException, IOException{
        for (String documentPath : documentPaths) {
            processDocuments(documentPath);
        }
    }
        
    private void processDocuments(String path) throws FileNotFoundException, IOException{
//        BufferedReader input = new BufferedReader(
//                new InputStreamReader(System.in, "utf-8"));
        File filePath = new File(path);
        if (filePath.exists()) {
            if (filePath.isDirectory()) {
                File[] files = filePath.listFiles();
                for (File file : files) {
                    boolean valid = false;
                    for (String extension : acceptedFileExtensions) {
                        if(file.getName().endsWith(extension)){
                            valid = true;  break;
                        }
                    }
                    if (valid) {
                        documents.add(new Document(file, language));
                    }
                }
            } else {
                boolean valid = false;
                for (String extension : acceptedFileExtensions) {
                    if (filePath.getName().endsWith(extension)) {
                        valid = true;
                        break;
                    }
                }
                if (valid) {
                    BufferedReader br = new BufferedReader(new FileReader(filePath));
                    String line = br.readLine();
                    while(line != null){
                        if (documentAsLine) {
                            documents.add(new Document(line, language));
//                            input.readLine();
                            
//                            System.out.println(line);
                            line = br.readLine();
                        }else {
                            line += "\n" + br.readLine();
                        }
                    }
                    if(!documentAsLine){
                        documents.add(new Document(line, language));
                    }
                }
            }
        }
        else{
            throw new FileNotFoundException(path + " is not a valid file or directory.");
        }
    }

    private void getTermCoverage(){
        for (Document document : documents) {
            for (String term : document.getTokenizedDocument()) {
                term = term.toLowerCase().trim();
                if (terms.containsKey(term)) {
                    if(!terms.get(term).contains(document)){
                        List<Document> docs = terms.get(term);
                        docs.add(document);
                    }
                }else{
                    List<Document> docs = new ArrayList<Document>();
                    docs.add(document);
                    terms.put(term, docs);
                }
            }
        }
    }
    
    public List<Document> getDocuments() {
        return documents;
    }

    public Map<String, List<Document>> getTerms() {
        return terms;
    }
    
       
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String filePath = "/home/vieyra/Corpus/SWBSocial/";
        String file = "sedesolv2.txt";
        
        DocumentDataBase database =  new DocumentDataBase(filePath + file, true);
        Map<String,List<Document>> terms = database.getTerms();
        for (Map.Entry<String, List<Document>> term : terms.entrySet()) {
            String string = term.getKey();
            System.out.print(string + ": {");
            List<Document> documents = term.getValue();
            for (int i = 0; i < documents.size(); i++) {
                System.out.print(database.getDocuments().indexOf(documents.get(i)) + 
                        (i < documents.size() -1? ",":"}") );
            }
            System.out.println("");
        }
    }
    
}
