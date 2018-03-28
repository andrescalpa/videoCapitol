package commonlibs.utils;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Textbox;


public class ZKUtilities {

	public static String getTextBoxValue( Textbox edTextBox) {
		
		String strResult = "";
		
		try {
			
			strResult = edTextBox.getValue();
			
		}
		catch ( Exception Ex ) {
			
			Ex.printStackTrace();
		}
		
		return strResult;		
		
	}

	public static boolean setTextBoxValue( Textbox edTextBox, String strValue) {
		
		boolean bResult = false;
		
		try {
			
			edTextBox.setValue( strValue );
			
			bResult = true;
			
		}
		catch ( Exception Ex ) {
			
			Ex.printStackTrace();
		}
		
		return bResult;		
		
	}
	
	public static Component getComponent( Component components[], String strClassName  ) {
	    
	    Component Result = null;
	    
	    for ( int intIndex=0; intIndex < components.length; intIndex++ ) {
	        
	        String strCurrentClassName = components[ intIndex ].getClass().getSimpleName();
	        
	        if ( strCurrentClassName.equalsIgnoreCase( strClassName ) ) {
	            
	            Result = components[ intIndex ];
	            break;
	            
	        }
	        
	    }
	    
	    return Result;
	    
	}
	
}
