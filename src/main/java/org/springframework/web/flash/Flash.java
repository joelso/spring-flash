package org.springframework.web.flash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.context.MessageSourceResolvable;

/**
 * Flash object that lives in the HTTP session. 
 * 
 * Contains a set of flash entries where each can have individual
 * states @see {@link FlashEntryState}. 
 * 
 * 
 * @author Joel Soderstrom <joel.soderstrom@gmail.com>
 *
 */
public final class Flash implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final String MAP_KEY_PREFIX = Flash.class.getName() + ".";
    private static final String SUCCESS_KEY = MAP_KEY_PREFIX  + "success";
    private static final String NOTICE_KEY = MAP_KEY_PREFIX  + "notice";
    private static final String ERROR_KEY = MAP_KEY_PREFIX  + "error";
    private static final String POPUP_ERROR_KEY = MAP_KEY_PREFIX  + "popup_error";
    
    /**
     * List that contains flash entries.
     */
    private List<FlashEntry> entries = new ArrayList<FlashEntry>();

    
    /**
     * Default constructor
     */
    public Flash() {}
    
    /**
     * Copy constructor that creates a deep
     * clone of this flash object,
     * @param flash
     */
    public Flash(Flash flash) {
 	    for(FlashEntry fe: flash.getEntries()) {
 	    	this.entries.add(new FlashEntry(fe));
 	    }
    }
    
    
    public List<FlashEntry> getSuccess() {
        return get(SUCCESS_KEY);
    }

    public void setSuccess(Object message) {
        set(SUCCESS_KEY, message);
    }

    public List<FlashEntry> getNotice() {
        return get(NOTICE_KEY);
    }

    public void setNotice(Object message) {
        set(NOTICE_KEY, message);
    }

    public List<FlashEntry> getError() {
        return get(ERROR_KEY);
    }

    public void setError(Object message) {
        set(ERROR_KEY, message);
    }
    
    public List<FlashEntry> getPopupError() {
        return get(POPUP_ERROR_KEY);
    }

    public void setPopupError(Object message) {
        set(POPUP_ERROR_KEY, message);
    }
    
    public void setPopupErrors(Collection<?> messages) {
    	for (Object object : messages) {
    		set(POPUP_ERROR_KEY, object);
		}
    }
    
    private void set(String key, Object value) {
        entries.add(new FlashEntry(key, value));
    }

    public List<FlashEntry> get(String key) {
    	List<FlashEntry> list = new ArrayList<FlashEntry>();
    	
        for (FlashEntry entry : entries) {
            if(entry.getKey().equals(key)) {
            	list.add(entry);
            }
        }
        
        return list;
    }

    public List<FlashEntry> getEntries() {
        return entries;
    }


    public enum FlashEntryState {
        CURRENT,
        SAVED,
        PREVIOUS
    }
    
    @Override
    public String toString() {
    	return new StringBuilder(super.toString())
    		.append(" Num entries: ")
    		.append(this.getEntries().size())
    		.toString();
    }

    public class FlashEntry {

        private FlashEntryState state;
        private final String key;
        private final Object value;

        public FlashEntry(final String key, final Object value) {
            this.key = key;
            this.value = value;
            this.state = FlashEntryState.CURRENT;
        }
        
        public FlashEntry(FlashEntry fe) {
        	this.state = fe.getState();
        	this.key = fe.getKey();
        	this.value = fe.getValue();
        }
        

        public FlashEntryState getState() {
            return state;
        }

        public void setState(FlashEntryState state) {
            this.state = state;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        /**
         * Checks whether value should be resolved using
         * message source.
         * @return
         */
        @Deprecated
        public boolean isMessageResolvable() {
        	return this.value instanceof MessageSourceResolvable;
        }
        
        @Override
        public String toString() {
            return value.toString();
        }

    }

}
