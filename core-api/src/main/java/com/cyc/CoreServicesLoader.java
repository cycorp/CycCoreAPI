package com.cyc;

/*
 * #%L
 * File: CoreServicesLoader.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2017 Cycorp, Inc
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

import com.cyc.kb.spi.KbApiService;
import com.cyc.query.spi.QueryApiService;
import com.cyc.session.SessionManager;
import com.cyc.session.exception.SessionServiceException;
import com.cyc.session.spi.SessionApiService;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * This class is solely responsible for loading Core API services, such as 
 * {@link com.cyc.kb.spi.KbApiService}. Any other classes in this project which require 
 * Core API services should acquire them from this class.
 * 
 * <p>Note that the relevant service provider file in META-INF/services should be generated by the 
 * serviceloader-maven-plugin, specified in the provider project's pom.xml file.
 * 
 * @author nwinant
 */
public class CoreServicesLoader extends CycServicesLoader {
  
  //====|    Fields    |==========================================================================//
  
  /**
   * Initialization-on-demand holder for singleton.
   *
   * @see
   * <a href="https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">Wikipedia</a>
   */
  private static class InstanceHolder {

    //static final Logger LOG = LoggerFactory.getLogger(CoreServicesLoader.class);
    
    static final CoreServicesLoader INSTANCE = new CoreServicesLoader();
    
  }
  
  /**
   * We do not require implementation for all services on the classpath. This allows e.g. the KB 
   * Client (and its test suite) to be run without needing to load the Query Client.
   */
  private final boolean allowMissingServices = true;
  
  private final KbApiService KbApiService;
  private final QueryApiService queryApiService;
  private final SessionApiService sessionApiService;
  //private final List<QueryAnswerExplanationService> queryExplanationServices;
  
  //private static final ProofViewService PROOF_VIEW_FACTORY_SERVICE;
  /*
  static {
    try {
      KB_FACTORY_SERVICES = loadFactoryServiceProvider(KbApiService.class,
              ALLOW_MISSING_SERVICES);
      QUERY_FACTORY_SERVICE = loadFactoryServiceProvider(QueryApiService.class, 
              ALLOW_MISSING_SERVICES);
      SESSION_FACTORY_SERVICE = loadFactoryServiceProvider(SessionApiService.class, 
              ALLOW_MISSING_SERVICES);
      QUERY_EXPLANATION_FACTORY_SERVICES = loadFactoryServiceProviders(QueryAnswerExplanationService.class,
              ALLOW_MISSING_SERVICES);
      //PROOF_VIEW_FACTORY_SERVICE = findProofViewService(ALLOW_MISSING_SERVICES);
    } catch (Throwable t) {
      LOGGER.error(t.getMessage(), t);
      throw new ExceptionInInitializerError(t);
    }
  }
  */
  
  //====|    Construction    |====================================================================//
  
  private CoreServicesLoader() {
    KbApiService = getApiEntryPoint(KbApiService.class, allowMissingServices);
    queryApiService = getApiEntryPoint(QueryApiService.class, allowMissingServices);
    sessionApiService = getApiEntryPoint(SessionApiService.class, allowMissingServices);
    //queryExplanationServices = loadServiceProviders(QueryAnswerExplanationService.class, allowMissingServices);
    //PROOF_VIEW_FACTORY_SERVICE = findProofViewService(ALLOW_MISSING_SERVICES);
  }
  
  protected static CoreServicesLoader getInstance() {
    return InstanceHolder.INSTANCE;
  }
  
  //====|    Public    |==========================================================================//
  
  /*
  public static List<SessionManager> loadAllSessionManagers() throws SessionServiceException {
    final List<SessionManager> sessionMgrs = new ArrayList();
    final ServiceLoader<SessionManager> loader = ServiceLoader.load(SessionManager.class);
    for (SessionManager sessionMgr : loader) {
      if (!sessionMgr.isClosed()) {
        sessionMgrs.add(sessionMgr);
      }
    }
    return sessionMgrs;
  }
  */
  public static List<SessionManager> loadAllSessionManagerFactories() throws SessionServiceException {
    final List<SessionManager> sessionMgrs = new ArrayList();
    final ServiceLoader<SessionManager> loader = ServiceLoader.load(SessionManager.class);
    for (SessionManager sessionMgr : loader) {
      if (!sessionMgr.isClosed()) {
        sessionMgrs.add(sessionMgr);
      }
    }
    return sessionMgrs;
  }
  
  public KbApiService getKbApiServices(boolean allowMissingServices) {
    if (!allowMissingServices && KbApiService == null) {
      throw new RuntimeException("Could not find a service provider for " 
              + KbApiService.class.getCanonicalName());
    }
    return KbApiService;
  }
  
  public KbApiService getKbApiServices() {
    return getKbApiServices(false);
  }
  
  public QueryApiService getQueryApiService(boolean allowMissingServices) {
    if (!allowMissingServices && queryApiService == null) {
      throw new RuntimeException("Could not find a service provider for "
              + QueryApiService.class.getCanonicalName());
    }
    return queryApiService;
  }
  
  public QueryApiService getQueryApiService() {
    return getQueryApiService(false);
  }
  
  public SessionApiService getSessionApiService(boolean allowMissingServices) {
    if (!allowMissingServices && sessionApiService == null) {
      throw new RuntimeException("Could not find a service provider for "
              + SessionApiService.class.getCanonicalName());
    }
    return sessionApiService;
  }
  
  public SessionApiService getSessionApiService() {
    return getSessionApiService(false);
  }
  
  @Override
  public <T> List<T> loadServiceProviders(Class<T> clazz, boolean allowMissingServices) {
    return super.loadServiceProviders(clazz, allowMissingServices);
  }
  
  //====|    QueryAnswerExplanationServices    |==================================================//
  /*
  private Class throwUnsupportedExplanationSpecException(QueryAnswerExplanationSpecification spec) {
    if (spec == null) {
      throw new NullPointerException(QueryAnswerExplanationSpecification.class.getCanonicalName() + " is null");
    }
    if (spec.forExplanationType() == null) {
      throw new NullPointerException(spec.getClass().getCanonicalName() + "#forExplanationType() returned null");
    }
    throw new UnsupportedOperationException("Could not find a " + QueryAnswerExplanationGenerator.class.getSimpleName() + " for " + spec);
  }
  
  private <T extends QueryAnswerExplanation>
          List<QueryAnswerExplanationService<T>> findExplanationServicesByExplanationType(
                  Class<T> explanationClazz) {
    final List<QueryAnswerExplanationService<T>> results = new ArrayList();
    for (QueryAnswerExplanationService service : getQueryExplanationFactoryServices()) {
      if (explanationClazz.equals(service.forExplanationType())) {
        results.add(service);
      }
    }
    return results;
  }
  
  public <T extends QueryAnswerExplanation>
          QueryAnswerExplanationService<T> findExplanationService(
                  QueryAnswer answer, QueryAnswerExplanationSpecification<T> spec) {
    for (QueryAnswerExplanationService<T> svc : findExplanationServicesByExplanationType(spec.forExplanationType())) {
      if (svc.isSuitableForSpecification(answer, spec)) {
        return svc;
      }
    }
    throwUnsupportedExplanationSpecException(spec);
    return null;
  }
  
  public List<QueryAnswerExplanationService> getQueryExplanationFactoryServices() {
    if (queryExplanationServices == null) {
      throw new RuntimeException("Could not find any service providers for " 
              + QueryAnswerExplanationService.class.getCanonicalName());
    }
    return queryExplanationServices;
  }
  */
  /*
  public static ProofViewService getProofViewFactoryService() {
    if (PROOF_VIEW_FACTORY_SERVICE == null) {
      throw new RuntimeException("Could not find a service provider for " 
              + ProofViewService.class.getCanonicalName());
    }
    return PROOF_VIEW_FACTORY_SERVICE;
  }
  
  private static ProofViewService findProofViewService(boolean allowMissingServices) {
    final List<ProofViewService> pvServices = new ArrayList();
    if ((QUERY_EXPLANATION_FACTORY_SERVICES != null) || !allowMissingServices) {
      for (QueryAnswerExplanationService service : getQueryExplanationFactoryServices()) {
        if (service instanceof ProofViewService) {
          pvServices.add((ProofViewService) service);
        }
      }
    }
    if (pvServices.size() != 1) {
      final String errMsg = "Expected exactly one provider for "
              + QueryAnswerExplanationService.class.getCanonicalName()
              + "<" + ProofView.class.getSimpleName() + ">"
              + " but found " + pvServices.size() + ": " + pvServices;
      if (pvServices.isEmpty() && allowMissingServices) {
        LOGGER.warn(errMsg);
      } else {
        throw new RuntimeException(errMsg);
      }
    }
    if (pvServices.isEmpty()) {
      return null;
    }
    return pvServices.get(0);
  }
  */
  
}
