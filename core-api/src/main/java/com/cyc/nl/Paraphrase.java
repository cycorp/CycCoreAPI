package com.cyc.nl;

import java.util.List;

/*
 * #%L
 * File: Paraphrase.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2018 Cycorp, Inc
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

/**
 * A class that bundles information about the rendering of a term in a natural language.
 *
 * @author baxter
 * @param <C> The CycL we're paraphrasing.
 */
public interface Paraphrase<C> extends Comparable<Paraphrase<C>> {


  @Override
  public boolean equals(Object obj);

  @Override
  public int hashCode();

  @Override
  public int compareTo(Paraphrase<C> object);

  /**
   * Returns the NL string paraphrasing the term.
   *
   * @return the NL string paraphrasing the term.
   */
  public String getString();

  @Override
  public String toString();

  /**
   * Returns the CycL term of which this is a paraphrase.
   *
   * @return the CycL term of which this is a paraphrase.
   */
  public C getCycl();
  
  /**
   * 
   * @param sub
   * @return this Paraphrase object.
   */
  public Paraphrase<C> addSubParaphrase(SubParaphrase sub);

  public List<SubParaphrase> getSubParaphrases ();
    
}
