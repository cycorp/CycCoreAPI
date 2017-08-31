package com.cyc.kb;

/*
 * #%L
 * File: Assertion.java
 * Project: Core API Object Specification
 * %%
 * Copyright (C) 2013 - 2017 Cycorp, Inc
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.DeleteException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;

import java.util.Collection;

/**
 * The top-level interface for assertions in a Cyc KB. Assertions are
 * first-class facts and rules stored directly as true (or occasionally false)
 * {@link Sentence}s in specific {@link Context}s.
 *
 * @author vijay
 */
public interface Assertion extends KbObject {

  /**
   * Direction determines the time when inference is performed for the given
   * assertion. :FORWARD triggers inference at the assert time. :BACKWARD defers
   * the inference until ask time.
   *
   * If set to Direction.AUTO, Facts will be asserted :FORWARD, Rules will be
   * asserted :BACKWARD and Assertions will be asserted :FORWARD.
   */
  public enum Direction {
    FORWARD,
    
    BACKWARD,
    
    /**
     * choose a valid direction based on the type of assertion 
     */
    AUTO
    
    // :code is a valid direction in SubL but we will not expose it in the API
    // CODE
  }

  /**
   * Strength is a component of the truth value. It can be :DEFAULT or
   * :MONOTONIC. A :MONOTONIC assertion is true under all conditions and a
   * :DEFAULT assertion is true but subject to exceptions. Most rules will be
   * "DEFAULT" true.
   *
   * If set to Strength.AUTO, all types of assertions will be asserted with
   * :DEFAULT strength.
   */
  public enum Strength {
    
    /**
     * True always and under all conditions
     */
    MONOTONIC,
    
    /**
     * Assumed true, but subject to exceptions
     */
    DEFAULT,
    
    AUTO
  }
  
  /**
   * the formula of this assertion. The sentence returned is a well-formed
   * formula in the assertion context.
   * 
   * @return the formula of this assertion
   */
  public Sentence getFormula();

  
  /**
   * the context associated with the assertion. The assertion is true in this
   * and any context that inherits this context.
   *
   * @return the context of the assertion
   *
   * NOTE: Even though we have the assertion object, when we try to retrieve a
   * context, we could still not find it, if the object is "stale". Currently we
   * do not synchronize the API objects with the KB. This method throws a
   * runtime exception if it couldn't find the context.
   */
  public Context getContext();

  /**
   * the collection of assertions that directly support this assertion.&nbsp;For
   * assertions that are not deduced, the collection will be empty.
   *
   * An assertion can be "asserted assertion", that is, asserted locally by a
   * cyclist. Or an assertion can be "deduced assertion", that is, the inference
   * engine has justification to believe that the sentence is true in a given
   * context. A deduced assertion has one or more independent arguments
   * (justifications) supporting the assertion. A deduction is a type of
   * argument for an assertion. Assertion by a cyclist is also an argument.
   *
   * A deduction has multiple "supports", some of these supports are due to HL
   * modules and others may be asserted or deduced assertions. This method
   * returns the "asserted assertions" that support the deductions, supporting
   * this assertion.
   *
   * @return a set of supporting assertions
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public Collection<Assertion> getSupportingAssertions()
          throws KbTypeException, CreateException;

  /**
   * Returns true if this assertion is supported by other assertions, false if
   * this assertion is not deduced from anything else. Note that an assertion
   * can be both a deduced assertion and an asserted assertion at the same time.
   *
   * @return true if this assertion is supported by other assertions
   */
  public Boolean isDeducedAssertion();

  /**
   * Is <code>this</code> a ground atomic formula? This will return true for
   * <code>Assertion</code>s that represent Cyc GAFs, and false for anything
   * else.
   *
   * @return true if and only if this KbObject is a ground atomic formula
   */
  public Boolean isGroundAtomicFormula();

  /**
   * Returns true if this assertion is asserted directly in the KB (i.e.&nbsp;at
   * least one of its supporting arguments is not deduced from something else).
   * Note that an assertion can be both a deduced assertion and an asserted
   * assertion at the same time.
   *
   * @return true if this assertion is asserted directly in the KB
   *
   */
  public Boolean isAssertedAssertion();

  /**
   * the direction of the assertion.
   *
   * @return the direction of the assertion.
   *
   */
  public Direction getDirection();

  /**
   * Change the direction of the assertion to the input <code>direction</code>. 
   * 
   * @param direction the direction of this assertion will be set to <code>direction</code>
   * 
   * @return  <code>this</code> assertion.  Even though the direction of the assertion
   * is different, it is still the same assertion.
   * 
   * @throws KbException If the direction can not be changed.
   */
  public Assertion changeDirection(Direction direction) 
          throws KbTypeException, CreateException, KbException;
  
  /**
   * Remove this <code>Assertion</code> from the KB. It should be noted that
   * deleting an <code>Assertion</code> from the KB does not mean that the
   * information contained in that that assertion will necessarily become
   * unknown to Cyc. For example, there are many cases where an Assertion may be
   * asserted but also be easily inferable. In those cases, the Assertion will
   * no longer be represented in Cyc as a separate assertion, but Cyc's
   * inference system will continue to behave as if it were present in the KB
   * because it is easily inferable.<p>
   * There are also cases where <code>Assertions</code> can not be deleted. The
   * most prominent of these cases is when the underlying assertion has been
   * forward deduced from some other assertion(s). In such cases, the only way
   * to remove the <code>Assertion</code> is to remove enough of the assertions
   * it is deduced from, to prevent it from being inferred (e.g from
   * {@link #getSupportingAssertions()}). As a general rule of thumb, if you
   * assert something to the Cyc KB, you will also be able to delete it. And you
   * can also delete the vast majority of assertions that were already present
   * in the KB. Extreme care should be taken when deleting Assertions that you
   * didn't create, since the process of deletion destructively modified the KB,
   * and deleting facts that Cyc critically relies on may cause it to behave in
   * erratic ways.
   *
   * @throws DeleteException when the underlying assertion can't be removed from
   * the KB.
   */
  @Override
  public void delete() throws DeleteException;

}
