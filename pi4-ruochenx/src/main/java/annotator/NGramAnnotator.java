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
import java.util.Collections;
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
      // build and sort Token arrayList
      FSIndex tokenIndex = aJCas.getAnnotationIndex(Token.type);
      Iterator tokenIterator = tokenIndex.iterator();
      ArrayList<Token> tokenArray = new ArrayList<Token>();
      while (tokenIterator.hasNext()) {
        tokenArray.add((Token) tokenIterator.next());
      }
      Collections.sort(tokenArray);
      
      FSArray questions = inputDocumentAnnot.getQuestions();
      // question n-grams
      for (int i = 0; i < questions.size(); i++) {
        Question questionAnnot = (Question) questions.get(i);
        nGramAnnotator(questionAnnot, aJCas, tokenArray);
      }
      // passages n-grams
      FSArray passages = inputDocumentAnnot.getPassages();
      for (int i = 0; i < passages.size(); i++) {
        Passage answerAnnot = (Passage) passages.get(i);
        nGramAnnotator(answerAnnot, aJCas, tokenArray);
      }
    }
  }

  public void nGramAnnotator(Annotation annot, JCas jCas, ArrayList<Token> allTokenArray) {
    ArrayList<Token> tokenList = new ArrayList<Token>();
    // find tokens inside annot
    int k = 0;
    int findTokenFlag = 0;
    while(k < allTokenArray.size())
    {
      Token token = allTokenArray.get(k);
      if (inAnnot(token, annot)) {
        findTokenFlag = 1;
        tokenList.add(token);
        allTokenArray.remove(k);
      } else if(findTokenFlag == 1) {
        break;
      } else {
        k++;
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
