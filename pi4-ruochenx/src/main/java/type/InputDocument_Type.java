
/* First created by JCasGen Mon Sep 28 15:29:46 EDT 2015 */
package type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;

/** Stores all the questions and its associated answer candidates.
 * Updated by JCasGen Mon Sep 28 17:16:05 EDT 2015
 * @generated */
public class InputDocument_Type extends ComponentAnnotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (InputDocument_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = InputDocument_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new InputDocument(addr, InputDocument_Type.this);
  			   InputDocument_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new InputDocument(addr, InputDocument_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = InputDocument.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("type.InputDocument");
 
  /** @generated */
  final Feature casFeat_passages;
  /** @generated */
  final int     casFeatCode_passages;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getPassages(int addr) {
        if (featOkTst && casFeat_passages == null)
      jcas.throwFeatMissing("passages", "type.InputDocument");
    return ll_cas.ll_getRefValue(addr, casFeatCode_passages);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPassages(int addr, int v) {
        if (featOkTst && casFeat_passages == null)
      jcas.throwFeatMissing("passages", "type.InputDocument");
    ll_cas.ll_setRefValue(addr, casFeatCode_passages, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getPassages(int addr, int i) {
        if (featOkTst && casFeat_passages == null)
      jcas.throwFeatMissing("passages", "type.InputDocument");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_passages), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_passages), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_passages), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setPassages(int addr, int i, int v) {
        if (featOkTst && casFeat_passages == null)
      jcas.throwFeatMissing("passages", "type.InputDocument");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_passages), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_passages), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_passages), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_questions;
  /** @generated */
  final int     casFeatCode_questions;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getQuestions(int addr) {
        if (featOkTst && casFeat_questions == null)
      jcas.throwFeatMissing("questions", "type.InputDocument");
    return ll_cas.ll_getRefValue(addr, casFeatCode_questions);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setQuestions(int addr, int v) {
        if (featOkTst && casFeat_questions == null)
      jcas.throwFeatMissing("questions", "type.InputDocument");
    ll_cas.ll_setRefValue(addr, casFeatCode_questions, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getQuestions(int addr, int i) {
        if (featOkTst && casFeat_questions == null)
      jcas.throwFeatMissing("questions", "type.InputDocument");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_questions), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_questions), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_questions), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setQuestions(int addr, int i, int v) {
        if (featOkTst && casFeat_questions == null)
      jcas.throwFeatMissing("questions", "type.InputDocument");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_questions), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_questions), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_questions), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public InputDocument_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_passages = jcas.getRequiredFeatureDE(casType, "passages", "uima.cas.FSArray", featOkTst);
    casFeatCode_passages  = (null == casFeat_passages) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_passages).getCode();

 
    casFeat_questions = jcas.getRequiredFeatureDE(casType, "questions", "uima.cas.FSArray", featOkTst);
    casFeatCode_questions  = (null == casFeat_questions) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_questions).getCode();

  }
}



    