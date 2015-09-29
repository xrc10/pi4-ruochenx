package annotator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;

import type.InputDocument;
import type.Question;
import type.Passage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionPassageAnnotator extends JCasAnnotator_ImplBase {
  private Pattern questionPattern = Pattern.compile("(\\d+)\\sQUESTION\\s(.*)");

  private Pattern passagePattern = Pattern.compile("(\\d+)\\s(\\S*)\\s(-??\\d)\\s(.*)");

  public void process(JCas aJCas) {
    // construct InputDocument instance to contain question and passages
    InputDocument inputDocumentAnnot = new InputDocument(aJCas);
    inputDocumentAnnot.setComponentId(this.getClass().getName());
    inputDocumentAnnot.setScore(1.0f);
    inputDocumentAnnot.setBegin(0);
    // get document text
    String docText = aJCas.getDocumentText();
    inputDocumentAnnot.setEnd(docText.length());
    // use ArrayList of question to store questions
    ArrayList<Question> questionArray = new ArrayList<Question>();
    // search for question
    Matcher matcher = questionPattern.matcher(docText);
    // found all questions - create annotation
    while (matcher.find()) {
      Question questionAnnot = new Question(aJCas);
      questionAnnot.setBegin(matcher.start(2));
      questionAnnot.setEnd(matcher.end(2));
      questionAnnot.setComponentId(this.getClass().getName());
      questionAnnot.setScore(1.0f);
      questionAnnot.setId(matcher.group(1));
      questionAnnot.setSentence(matcher.group(2));
      questionAnnot.addToIndexes();
      questionArray.add(questionAnnot);
    }
    // use ArrayList of passage to store passages
    ArrayList<Passage> passageArray = new ArrayList<Passage>();
    // search for passages
    matcher = passagePattern.matcher(docText);
    while (matcher.find()) {
      // found one - create annotation
      Passage passageAnnot = new Passage(aJCas);
      passageAnnot.setBegin(matcher.start(4));
      passageAnnot.setEnd(matcher.end(4));
      passageAnnot.setComponentId(this.getClass().getName());
      passageAnnot.setScore(1.0f);
      passageAnnot.setSourceDocId(matcher.group(2));
      passageAnnot.setText(matcher.group(4));
      if (matcher.group(3).equals("-1")) {
        passageAnnot.setLabel(false);
      } else {
        passageAnnot.setLabel(true);
      }
      String questionId = matcher.group(1);
      Question questionForThisPassageAnnot = null;
      int tempFlag = 0;
      for (Question questionAnnot : questionArray) {
        if (questionAnnot.getId().equals(questionId)) {
          questionForThisPassageAnnot = questionAnnot;
          tempFlag = 1;
          break;
        }
      }
      if (tempFlag == 0) {
        System.out.println("Failed to Find Question!");
        System.out.println(questionId);
        System.out.println(matcher.group(4));
      }
      passageAnnot.setQuestion(questionForThisPassageAnnot);
      passageAnnot.addToIndexes();
      passageArray.add(passageAnnot);
    }
    // save passages to InputDocument instance
    FSArray allPassages = new FSArray(aJCas, passageArray.size());
    for (int i = 0; i < passageArray.size(); i++) {
      allPassages.set(i, passageArray.get(i));
    }
    inputDocumentAnnot.setPassages(allPassages);
    // set passages field for each question
    FSArray allQuestions = new FSArray(aJCas, questionArray.size());
    for (int i = 0; i < questionArray.size(); i++) {
      Question questionAnnot = questionArray.get(i);
      ArrayList<Passage> passagesForThisQuestion = new ArrayList<Passage>();
      for (int j = 0; j < passageArray.size(); j++) {
        if (passageArray.get(j).getQuestion().getId().equals(questionAnnot.getId())) {
          passagesForThisQuestion.add(passageArray.get(j));
        }
      }
      FSArray passages = new FSArray(aJCas, passagesForThisQuestion.size());
      for (int k = 0; k < passagesForThisQuestion.size(); k++) {
        passages.set(k, passagesForThisQuestion.get(k));
      }
      questionAnnot.setPassages(passages);
      allQuestions.set(i, questionAnnot);
    }
    inputDocumentAnnot.setQuestions(allQuestions);
    inputDocumentAnnot.addToIndexes();
  }
}
