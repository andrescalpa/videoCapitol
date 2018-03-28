package org.test.zk.database.datamodel;

import java.time.LocalDate;
import java.time.LocalTime;

public class CAuditableDataModel implements IAuditableDataModel{

	private static final long serialVersionUID = 2954053470032042286L;
	protected String strCreatedBy = null;
    protected LocalDate createdAtDate = null;
    protected LocalTime createdAtTime = null;

    protected String strUpdatedBy = null;
    protected LocalDate updatedAtDate = null;
    protected LocalTime updatedAtTime = null;
    
    
    public String getCreatedBy() {
        
        return strCreatedBy;
        
    }
    
    
    public void setCreatedBy( String strCreatedBy ) {
        
        this.strCreatedBy = strCreatedBy;
        
    }
    
    
    public LocalDate getCreatedAtDate() {
        
        return createdAtDate;
        
    }
    
    
    public void setCreatedAtDate( LocalDate createdAtDate ) {
        
        this.createdAtDate = createdAtDate;
        
    }
    
    
    public LocalTime getCreatedAtTime() {
        
        return createdAtTime;
        
    }
    
    
    public void setCreatedAtTime( LocalTime createdAtTime ) {
        
        this.createdAtTime = createdAtTime;
        
    }
    
    
    public String getUpdatedBy() {
        
        return strUpdatedBy;
        
    }
    
    
    public void setUpdatedBy( String strUpdatedBy ) {
        
        this.strUpdatedBy = strUpdatedBy;
        
    }
    
    
    public LocalDate getUpdatedAtDate() {
        
        return updatedAtDate;
        
    }
    
    
    public void setUpdatedAtDate( LocalDate updatedAtDate ) {
        
        this.updatedAtDate = updatedAtDate;
        
    }
    
    
    public LocalTime getUpdatedAtTime() {
        
        return updatedAtTime;
        
    }
    
    
    public void setUpdatedAtTime( LocalTime updatedAtTime ) {
        
        this.updatedAtTime = updatedAtTime;
        
    }

}
