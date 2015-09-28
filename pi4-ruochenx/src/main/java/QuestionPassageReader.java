import java.io.IOException;

import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.util.Progress;

/**
 * This Collection Reader serves as a reader to parse your input. This is just template code, so you
 * need to implement actual code.
 */
public class QuestionPassageReader extends CollectionReader_ImplBase {

  @Override
  public void getNext(CAS aCAS) throws IOException, CollectionException {
  }

  @Override
  public void close() throws IOException {
  }

  @Override
  public Progress[] getProgress() {
    return null;
  }

  @Override
  public boolean hasNext() throws IOException, CollectionException {
    return false;
  }

}
