package com.cyc.query;

import com.cyc.session.CycSession;

/*
 * #%L
 * File: InferenceIdentifier.java
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

/**
 *
 * @author nwinant
 */
public interface InferenceIdentifier {

  void close();

  Integer getFirstProofId(Integer answerId);

  int getInferenceId();

  int getProblemStoreId();

  /**
   * Interrupt this inference.
   *
   * @param patience Give inference process this many seconds to halt gracefully,
   * after which terminate it with prejudice. A null value indicates infinite patience.
   */
  void interrupt(final Integer patience);

  String stringApiValue();
  
  CycSession getSession();
}
