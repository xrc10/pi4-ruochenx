package annotator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import type.InputDocument;
import type.Question;
import type.Score;
import type.Token;
import type.Ngram;
import type.Passage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ScoreAnnotator extends JCasAnnotator_ImplBase {
  public void process(JCas aJCas) {
    // find InputDocument instance
    FSIndex inputDocumentIndex = aJCas.getAnnotationIndex(InputDocument.type);
    Iterator inputDocumentIterator = inputDocumentIndex.iterator();
    while (inputDocumentIterator.hasNext()) {
      Score score = new Score(aJCas);
      score.setComponentId(this.getClass().getName());
      score.setScore(1.0f);
      score.setBegin(0);
      score.setEnd(aJCas.getDocumentText().length());
      InputDocument inputDocumentAnnot = (InputDocument) inputDocumentIterator.next();
      // build and sort NGram arrayList
      ArrayList<Ngram> nGramArray = new ArrayList<Ngram>();
      FSIndex nGramIndex = aJCas.getAnnotationIndex(Ngram.type);
      Iterator nGramIterator = nGramIndex.iterator();
      while (nGramIterator.hasNext()) {
        nGramArray.add((Ngram) nGramIterator.next());
      }
      Collections.sort(nGramArray);
      // find questions
      FSArray questions = inputDocumentAnnot.getQuestions();
      FSArray scoreQuestions = new FSArray(aJCas, questions.size());
      for (int i = 0; i < questions.size(); i++) {
        Question questionAnnot = (Question) questions.get(i);
        Question scoreQuestionAnnot = (Question) questionAnnot.clone();
        scoreQuestionAnnot.setComponentId(this.getClass().getName());
        ArrayList<Ngram> questionNGramList = findNGram(aJCas, questionAnnot, nGramArray);
        // find answers
        FSArray passages = questionAnnot.getPassages();
        FSArray scorePassages = new FSArray(aJCas, passages.size());
        for (int j = 0; j < passages.size(); j++) {
          Passage passageAnnot = (Passage) passages.get(j);
          Passage scorePassageAnnot = (Passage) passageAnnot.clone();
          ArrayList<Ngram> passageNGramList = findNGram(aJCas, passageAnnot, nGramArray);
          double simScore = computeNGramScore(aJCas, questionNGramList, passageNGramList);
          scorePassageAnnot.setScore(simScore);
          scorePassageAnnot.setComponentId(this.getClass().getName());
          scorePassages.set(j, scorePassageAnnot);
        }
        scoreQuestionAnnot.setPassages(scorePassages);
        scoreQuestions.set(i, scoreQuestionAnnot);
      }
      score.setQuestions(scoreQuestions);
      score.addToIndexes();
    }
  }

  public ArrayList<Ngram> findNGram(JCas aJCas, Annotation annot, ArrayList<Ngram> allNgramArray) {
    ArrayList<Ngram> nGramList = new ArrayList<Ngram>();
    int k = 0;
    int findTokenFlag = 0;
    while(k < allNgramArray.size())
    {
      Ngram nGram = allNgramArray.get(k);
      if (inAnnot(nGram, annot)) {
        findTokenFlag = 1;
        nGramList.add(nGram);
        allNgramArray.remove(k);
      } else if(findTokenFlag == 1) {
        break;
      } else {
        k++;
      }
    }
    return nGramList;
  }

  public boolean isEqual(JCas aJCas, Ngram n1, Ngram n2) {
    String docText = aJCas.getDocumentText();
    if (n1.getN() == n2.getN()) {
      for (int i = 0; i < n1.getN(); i++) {
        Token t1 = (Token) n1.getTokens().get(i);
        Token t2 = (Token) n2.getTokens().get(i);
        if (!docText.substring(t1.getBegin(), t1.getEnd())
                .equals(docText.substring(t2.getBegin(), t2.getEnd()))) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  double computeNGramScore(JCas aJCas, ArrayList<Ngram> ngList1, ArrayList<Ngram> ngList2) {
    double overlapCount = 0;
    for (int i = 0; i < ngList1.size(); i++) {
      for (int j = 0; j < ngList2.size(); j++) {
        if (isEqual(aJCas, ngList1.get(i), ngList2.get(j))) {
          overlapCount += 1;
          break;
        }
      }
    }
    return overlapCount / ngList1.size();
  }

  public boolean inAnnot(Ngram ngram, Annotation annot) {
    return (ngram.getBegin() >= annot.getBegin() && ngram.getEnd() <= annot.getEnd());
  }
}
