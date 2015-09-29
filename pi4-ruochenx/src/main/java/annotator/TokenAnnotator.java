package annotator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import type.InputDocument;
import type.Passage;
import type.Question;
import type.Token;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenAnnotator extends JCasAnnotator_ImplBase {
  private Pattern tokenPattern = Pattern.compile("\\w+");
  private Pattern htmlTagPattern = Pattern.compile("</??\\w+>");
  
  public void process(JCas aJCas) {
    // find InputDocument instance
    FSIndex inputDocumentIndex = aJCas.getAnnotationIndex(InputDocument.type);
    Iterator inputDocumentIterator = inputDocumentIndex.iterator();
    while (inputDocumentIterator.hasNext()) {
      InputDocument inputDocumentAnnot = (InputDocument) inputDocumentIterator.next();
      FSArray questions = inputDocumentAnnot.getQuestions();
      // question tokenization
      for(int i=0; i<questions.size(); i++) {
        Question questionAnnot = (Question) questions.get(i);
        String questionSentence = questionAnnot.getSentence();
        tokenizer(questionSentence, questionAnnot, aJCas);
      }
      // passage tokenization
      FSArray passages = inputDocumentAnnot.getPassages();
      for(int i=0; i<passages.size(); i++) {
        Passage passageAnnot = (Passage) passages.get(i);
        String passageSentence = passageAnnot.getText();
        tokenizer(passageSentence, passageAnnot, aJCas);
      }
    }
  }
  
  public void tokenizer(String inputStr, Annotation annot, JCas jCas) {
    // remove html tags
    Matcher htmlMatcher = htmlTagPattern.matcher(inputStr);
    while (htmlMatcher.find()) {
      inputStr = inputStr.replaceAll(htmlMatcher.group(), String.format("%1$"+htmlMatcher.group().length()+"s", ""));
    }
    // tokenize
    Matcher matcher = tokenPattern.matcher(inputStr);
    while (matcher.find()) {
      Token token = new Token(jCas);
      token.setBegin(annot.getBegin() + matcher.start());
      token.setEnd(annot.getBegin() + matcher.end());
      token.setComponentId(this.getClass().getName());
      token.setScore(1.0f);
      token.addToIndexes();
    }
  }
}
