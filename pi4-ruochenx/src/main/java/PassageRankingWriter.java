import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.xml.sax.SAXException;

import type.Score;
import type.Question;
import type.Ngram;
import type.Passage;

/**
 * This CAS Consumer serves as a writer to generate your output. This is just template code, so you
 * need to implement actual code.
 */
public class PassageRankingWriter extends CasConsumer_ImplBase {

  public static final String PARAM_OUTPUTDIR = "OutputDir";

  private File mOutputDir;

  public void initialize() throws ResourceInitializationException {
    mOutputDir = new File((String) getConfigParameterValue(PARAM_OUTPUTDIR));
    if (!mOutputDir.exists()) {
      mOutputDir.mkdirs();
    }
  }
  
  @Override
  public void processCas(CAS aCAS) throws ResourceProcessException {

    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }

    // retrieve the filename of the input file from the CAS
    File outFile = new File(mOutputDir, "passageRanking.txt");
    // write to output file
    try {
      writeResult(jcas, outFile);
    } catch (IOException e) {
      throw new ResourceProcessException(e);
    } catch (SAXException e) {
      throw new ResourceProcessException(e);
    }
  }
  
  private void writeResult(JCas aJCas, File name) throws IOException, SAXException {
    PrintWriter out = null;
    try {
      // write result in required format
      out = new PrintWriter(name);
    } finally {
      if (out != null) {
        FSIndex scoreIndex = aJCas.getAnnotationIndex(Score.type);
        Iterator scoreIterator = scoreIndex.iterator();
        while (scoreIterator.hasNext()) {
          Score score = (Score) scoreIterator.next();
          // rank questions according to Id
          FSArray questions = score.getQuestions();
          ArrayList<Question> questionsArray = new ArrayList<Question>(questions.size());
          for(int i=0; i<questions.size(); i++) {
            questionsArray.add((Question) questions.get(i));
          }
          Collections.sort(questionsArray);
          // iterate each passage over questions
          for(int i=0; i<questionsArray.size(); i++) {
            Question questionAnnot = questionsArray.get(i);
            // rank passages according to score
            FSArray passages = questionAnnot.getPassages();
            ArrayList<Passage> passageArray = new ArrayList<Passage>(passages.size());
            for(int j=0; j<passages.size(); j++) {
              passageArray.add((Passage) passages.get(j));
            }
            Collections.sort(passageArray);
            for(int j=0; j<passages.size(); j++) {
              Passage passageAnnot = passageArray.get(j);
              out.print(passageAnnot.getQuestion().getId() + " "
                      + passageAnnot.getSourceDocId() + " "
                      + passageAnnot.getScore() + " "
                      + passageAnnot.getText() + "\n");
            }
            }
          }
        }
        out.close();
      }
    }

}
