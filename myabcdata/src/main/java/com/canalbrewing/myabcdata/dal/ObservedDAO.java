package com.canalbrewing.myabcdata.dal;

import java.util.List;

import org.springframework.stereotype.Component;

import com.canalbrewing.myabcdata.model.ABC;
import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.Observed;

@Component
public interface ObservedDAO {
	
	List<Observed> getObservedByUser(int userId) throws Exception;
	
	Observed getObservedABCs(int observedId) throws Exception;
	
	List<Incident> getIncidentsByObserved(int observedId) throws Exception;
	
	ABC insertObservedABC(int observedId, ABC abc) throws Exception;
	
	ABC insertObservedLocation(int observedId, ABC abc) throws Exception;
	
	ABC updateObservedABC(ABC abc) throws Exception;
	
	ABC updateObservedLocation(ABC abc) throws Exception;
	
}
