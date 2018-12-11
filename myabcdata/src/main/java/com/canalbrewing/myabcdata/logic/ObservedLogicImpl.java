package com.canalbrewing.myabcdata.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.canalbrewing.myabcdata.common.MyABCConstants;
import com.canalbrewing.myabcdata.dal.ObservedDAO;
import com.canalbrewing.myabcdata.model.ABC;
import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.Observed;

@Component
public class ObservedLogicImpl implements ObservedLogic {
	
	@Autowired
	private ObservedDAO observedDAO;
	
	public List<Observed> getObservedByUser(int userId) throws Exception
	{
		return observedDAO.getObservedByUser(userId);
	}
	
	public Observed getObservedABCs(String observedId) throws Exception
	{
		Observed observed = observedDAO.getObservedABCs(Integer.parseInt(observedId));
		observed.getLocations().add(0, new ABC(0, MyABCConstants.NA));
		
		return observed;
	}
	
	public List<Incident> getIncidentsByObserved(String observedId) throws Exception
	{
		return observedDAO.getIncidentsByObserved(Integer.parseInt(observedId));
	}
	
	public ABC addObservedABC(String observedId, String typeCd, String typeValue) throws Exception
	{
		ABC abc = new ABC();
		abc.setTypeCd(typeCd);
		abc.setTypeValue(typeValue);
		
		if ( ABC.LOCATION.equals(typeCd) )
		{
			return observedDAO.insertObservedLocation(Integer.parseInt(observedId), abc);
		}
		else
		{
			return observedDAO.insertObservedABC(Integer.parseInt(observedId), abc);
		}
	}
	
	public ABC updateObservedABC(String id, String typeCd, String typeValue, String activeFl) throws Exception
	{
		ABC abc = new ABC();
		abc.setValueId(Integer.parseInt(id));
		abc.setTypeCd(typeCd);
		abc.setTypeValue(typeValue);
		abc.setActiveFl(activeFl);
		
		if ( ABC.LOCATION.equals(typeCd) )
		{
			return observedDAO.updateObservedLocation(abc);
		}
		else
		{
			return observedDAO.updateObservedABC(abc);
		}
	}
	
}
