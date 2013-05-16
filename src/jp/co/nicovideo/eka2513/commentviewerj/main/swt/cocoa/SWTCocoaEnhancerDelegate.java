package jp.co.nicovideo.eka2513.commentviewerj.main.swt.cocoa;

import org.eclipse.swt.internal.cocoa.NSObject;

/**
 * Delegate wrapper of NSObject.
 */
public class SWTCocoaEnhancerDelegate extends NSObject {

    /**
     * Constructor
     */
    public SWTCocoaEnhancerDelegate() {
        super();
    }

    /**
     * Constructor
     * @param id
     */
    public SWTCocoaEnhancerDelegate(int id) {
        super(id);
    }
}