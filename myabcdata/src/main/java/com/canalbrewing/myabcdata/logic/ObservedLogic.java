package com.canalbrewing.myabcdata.logic;

import java.util.List;

import org.springframework.stereotype.Component;

import com.canalbrewing.myabcdata.model.ABC;
import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.Observed;

@Component
public interface ObservedLogic {
	
	List<Observed> getObservedByUser(int userId) throws Exception;
	
	Observed getObservedABCs(String observedId) throws Exception;
	
	List<Incident> getIncidentsByObserved(String observedId) throws Exception;
	
	ABC addObservedABC(String observedId, String typeCd, String typeValue) throws Exception;
	
	ABC updateObservedABC(String id, String typeCd, String typeValue, String activeFl) throws Exception;
}
