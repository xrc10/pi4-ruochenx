package annotator;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import type.InputDocument;
import type.Question;
import type.Passage;
import type.Token;
import type.Ngram;

import java.util.ArrayList;
import java.util.Iterator;

public class NGramAnnotator extends JCasAnnotator_ImplBase {
  private int numberOfNGram;

  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    // Get config. parameter values
    numberOfNGram = Integer.parseInt(aContext.getConfigParameterValue("NumberOfNGram").toString());
  }

  public void process(JCas aJCas) {
    // find InputDocument instance
    FSIndex inputDocumentIndex = aJCas.getAnnotationIndex(InputDocument.type);
    Iterator inputDocumentIterator = inputDocumentIndex.iterator();
    while (inputDocumentIterator.hasNext()) {
      InputDocument inputDocumentAnnot = (InputDocument) inputDocumentIterator.next();
      FSArray questions = inputDocumentAnnot.getQuestions();
      // question n-grams
      for (int i = 0; i < questions.size(); i++) {
        Question questionAnnot = (Question) questions.get(i);
        nGramAnnotator(questionAnnot, aJCas);
      }
      // passages n-grams
      FSArray passages = inputDocumentAnnot.getPassages();
      for (int i = 0; i < passages.size(); i++) {
        Passage answerAnnot = (Passage) passages.get(i);
        nGramAnnotator(answerAnnot, aJCas);
      }
    }
  }

  public void nGramAnnotator(Annotation annot, JCas jCas) {
    FSIndex tokenIndex = jCas.getAnnotationIndex(Token.type);
    Iterator tokenIterator = tokenIndex.iterator();
    ArrayList<Token> tokenList = new ArrayList<Token>();
    // find tokens inside annot
    while (tokenIterator.hasNext()) {
      Token token = (Token) tokenIterator.next();
      if (inAnnot(token, annot)) {
        tokenList.add(token);
      }
    }
    for (int i = 0; i < tokenList.size() - numberOfNGram + 1; i++) {
      // construct FSArray of tokens
      FSArray tokens = new FSArray(jCas, numberOfNGram);
      for (int j = 0; j < numberOfNGram; j++) {
        tokens.set(j, tokenList.get(i + j));
      }
      // construct Ngram instances
      Ngram ngram = new Ngram(jCas);
      ngram.setN(numberOfNGram);
      ngram.setTokens(tokens);
      ngram.setBegin(tokenList.get(i).getBegin());
      ngram.setEnd(tokenList.get(i + numberOfNGram - 1).getEnd());
      ngram.setScore(1.0f);
      ngram.setComponentId(this.getClass().getName());
      ngram.addToIndexes();
    }
  }

  public boolean inAnnot(Token token, Annotation annot) {
    return (token.getBegin() >= annot.getBegin() && token.getEnd() <= annot.getEnd());
  }
}
