package knOWLearn.Utils.Document;

import edu.upc.freeling.ListSentence;
import edu.upc.freeling.ListWord;
import edu.upc.freeling.Word;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import knOWLearn.Utils.Resources;
import org.semanticwb.TextAnalysis.NLPTools.FreelingParser;

/**
 * @author Samuel Vieyra
 * samuel.vieyra@infotec.com.mx
 */

public class Document {
    private String textDocument = "";
    private List<String> tokenizedDocument = new ArrayList<String>();
    private List<Word> parsedDocument;
//    private ListWord words;
    private String language = "en";

    public Document(File fileDocument) throws FileNotFoundException, IOException {
        readFile(fileDocument);
        tokenize();
    }
    
    public Document(File fileDocument, String language) throws FileNotFoundException, IOException {
        readFile(fileDocument);
        this.language = language;
        tokenize();
    }
    
    public Document(String textDocument) {
        this.textDocument = textDocument;
        tokenize();
    }
    
    public Document(String textDocument, String language) {
        this.textDocument = textDocument;
        this.language = language;
        tokenize();
    }

    private void readFile(File file) throws FileNotFoundException, IOException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        while(line != null){
            textDocument += line;
            line = br.readLine();
        }
    }
    
    private void tokenize(){
        FreelingParser parser = FreelingParser.getInstance(language).getInstance();
        ListSentence analyzedSentence = parser.analyzeSentence(textDocument);
        parsedDocument = parser.getParsedSentence(analyzedSentence);
        for (Word fWord : parsedDocument) {
//            String word = words.front().getForm();
            String word =  fWord.getForm();
            if( word.length() > 1 &&    // Word length greater than 1
                !Resources.getStopwors(language).contains(word.trim().toLowerCase()) //No stopwords
                && fWord.getTag().startsWith("NN")  //Only nouns
                && !word.matches("-?\\d+(\\.\\d+)?") //No numbers
                && !word.matches("^[^A-Za-z]+$") ) { //No only composed by symbols
                tokenizedDocument.add(word);
            }
        }
   
    }

    public String getTextDocument() {
        return textDocument;
    }

    public List<String> getTokenizedDocument() {
        return tokenizedDocument;
    }

//    public ListWord getWords() {
//        return words;
//    }
//    
    public static void main(String[] args) {
        String document = "Previous research suggested that social support, belongingness and mastery goals were related to the quality of cooperative learning (CL). In this in-depth study we explored how to differentiate between four effective CL teams and four ineffective CL teams, in terms of students' goals and perceptions of instructional conditions. Apart from the earlier mentioned goals we found students' preference for social responsibility, learning for a certificate and entertainment goals to be salient in the CL setting. Mastery and social responsibility goals were prevalent in effective teams, while learning for a certificate and entertainment goals were prevalent in ineffective teams. Moreover, the type of task, group composition and teacher support were mentioned as reasons for effective or ineffective CL.";
        Document d = new Document(document, "en");
        System.out.println(" ------ Listing Tokenized document ------");
        for(String word : d.getTokenizedDocument()){
            System.out.print("'" + word + "' ");
        }
        System.out.println("\n ----------- Printing ListWord -----------");
        List<Word> words = d.parsedDocument;
        for (Word word : words) {
            System.out.print(word.getForm() + "/" + word.getTag() + " ");
        }
        
    }
}
