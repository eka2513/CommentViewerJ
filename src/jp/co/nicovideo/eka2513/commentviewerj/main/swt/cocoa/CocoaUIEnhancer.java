/*******************************************************************************
 * Copyright (c) 2008, 2009 Adobe Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Adobe Systems, Inc. - initial API and implementation
 *     IBM Corporation - cleanup
 *******************************************************************************/
package jp.co.nicovideo.eka2513.commentviewerj.main.swt.cocoa;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jp.co.nicovideo.eka2513.commentviewerj.main.swt.CommentViewerJMainWindow;
import jp.co.nicovideo.eka2513.commentviewerj.main.swt.SettingWindow;
import jp.co.nicovideo.eka2513.commentviewerj.main.swt.constants.GUIConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.C;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.cocoa.NSApplication;
import org.eclipse.swt.internal.cocoa.NSControl;
import org.eclipse.swt.internal.cocoa.NSMenu;
import org.eclipse.swt.internal.cocoa.NSMenuItem;
import org.eclipse.swt.internal.cocoa.NSString;
import org.eclipse.swt.internal.cocoa.OS;
import org.eclipse.swt.widgets.Display;

/**
 * The CocoaUIEnhancer provides the standard "About" and "Preference" menu items
 * and links them to the corresponding workbench commands.
 * This must be done in a MacOS X fragment because SWT doesn't provide an abstraction
 * for the (MacOS X only) application menu and we have to use MacOS specific natives.
 * The fragment is for the org.eclipse.ui plug-in because we need access to the
 * Workbench "About" and "Preference" actions.
 *
 * this class is not intended to be referenced by any client.
 *
 * @since 1.0
 */
public class CocoaUIEnhancer {

    /**
     *
     */
    private static final int kAboutMenuItem = 0;

    /**
     *
     */
    private static final int kPreferencesMenuItem = 2;

    /**
     *
     */
    private static final int kServicesMenuItem = 4;

    /**
     *
     */
    private static final int kHideApplicationMenuItem = 6;

    /**
     *
     */
    private static final int kQuitMenuItem = 10;

    /**
     *
     */
    static long sel_toolbarButtonClicked_;

    /**
     *
     */
    static long sel_preferencesMenuItemSelected_;

    /**
     *
     */
    static long sel_aboutMenuItemSelected_;

    /**
     * This callback is not freed.
     */
    static Callback proc3Args;

    /**
     *
     */
    static final byte[] SWT_OBJECT = { 'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0' };

    /**
     * @throws Exception
     */
    private void init() throws Exception {
        // These should either move out of Display or be accessible to this class.
        byte[] types = { '*', '\0' };
        int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;

        proc3Args = new Callback(getClass(), "actionProc", 3); //$NON-NLS-1$
        long proc3 = proc3Args.getAddress();
//        //call getAddress
//        Method getAddress = Callback.class
//                .getMethod("getAddress", new Class[0]);
//        Object o = null;
//        Object object = getAddress.invoke(proc3Args, o);
//        long proc3 = convertToLong(object);
        if (proc3 == 0)
            SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);

        Object object;
        //call objc_allocateClassPair
        Field field = OS.class.getField("class_NSObject");
        Object fieldObj = field.get(OS.class);
        object = invokeMethod(OS.class, "objc_allocateClassPair", new Object[] {
                fieldObj, "SWTCocoaEnhancerDelegate", wrapPointer(0) });
        long cls = convertToLong(object);

        invokeMethod(OS.class, "class_addIvar", new Object[] {
                wrapPointer(cls), SWT_OBJECT, wrapPointer(size),
                new Byte((byte) align), types });

        // Add the action callback
        invokeMethod(
                OS.class,
                "class_addMethod", new Object[] { wrapPointer(cls), wrapPointer(sel_toolbarButtonClicked_), wrapPointer(proc3), "@:@" }); //$NON-NLS-1$
        invokeMethod(OS.class, "class_addMethod", new Object[] {
                wrapPointer(cls),
                wrapPointer(sel_preferencesMenuItemSelected_),
                wrapPointer(proc3), "@:@" }); //$NON-NLS-1$
        invokeMethod(
                OS.class,
                "class_addMethod", new Object[] { wrapPointer(cls), wrapPointer(sel_aboutMenuItemSelected_), wrapPointer(proc3), "@:@" }); //$NON-NLS-1$

        invokeMethod(OS.class, "objc_registerClassPair",
                new Object[] { wrapPointer(cls) });
    }

    /**
     *
     */
    SWTCocoaEnhancerDelegate delegate;

    /**
     *
     */
    private long delegateJniRef;

    /**
     *
     */
    private String fAboutActionName;

    /**
     *
     */
    private String fQuitActionName;

    /**
     *
     */
    private String fHideActionName;

    private CommentViewerJMainWindow mainWindow;

    /**
     * Constructor
     *
     * @param actions Application event handler.
     */
    public CocoaUIEnhancer(CommentViewerJMainWindow shell) {

    	this.mainWindow = shell;

        fAboutActionName = GUIConstants.APPLICATION_NAME + " について";

        fHideActionName = GUIConstants.APPLICATION_NAME + " を隠す";

        fQuitActionName = GUIConstants.APPLICATION_NAME+" の終了";

        try {
            if (sel_preferencesMenuItemSelected_ == 0) {
                sel_preferencesMenuItemSelected_ = registerName("preferencesMenuItemSelected:"); //$NON-NLS-1$
                sel_aboutMenuItemSelected_ = registerName("aboutMenuItemSelected:"); //$NON-NLS-1$
                init();
            }
        } catch (Exception e) {
            // theoretically, one of SecurityException,Illegal*Exception,InvocationTargetException,NoSuch*Exception
            // not expected to happen at all.
            log(e);
        }
    }

    /**
     * @param name
     * @return
     * @throws Exception
     */
    private long registerName(String name) throws Exception {
        Class<OS> clazz = OS.class;
        Object object = invokeMethod(clazz, "sel_registerName",
                new Object[] { name });
        return convertToLong(object);
    }

    /**
     * Initialize application menu.
     */
    public void earlyStartup() {
        final Display display = Display.getDefault();
        display.syncExec(new Runnable() {
            public void run() {
                try {
                    delegate = new SWTCocoaEnhancerDelegate();
                    delegate.alloc().init();
                    //call OS.NewGlobalRef
                    Method method = OS.class.getMethod("NewGlobalRef",
                            new Class[] { Object.class });
                    Object object = method.invoke(OS.class,
                            new Object[] { CocoaUIEnhancer.this });
                    delegateJniRef = convertToLong(object);
                } catch (Exception e) {
                    // theoretically, one of SecurityException,Illegal*Exception,InvocationTargetException,NoSuch*Exception
                    // not expected to happen at all.
                    log(e);
                }
                if (delegateJniRef == 0)
                    SWT.error(SWT.ERROR_NO_HANDLES);
                try {
                    Field idField = SWTCocoaEnhancerDelegate.class
                            .getField("id");
                    Object idValue = idField.get(delegate);
                    invokeMethod(OS.class, "object_setInstanceVariable",
                            new Object[] { idValue, SWT_OBJECT,
                                    wrapPointer(delegateJniRef) });

                    hookApplicationMenu();

                    // schedule disposal of callback object
                    display.disposeExec(new Runnable() {
                        public void run() {
                            if (delegateJniRef != 0) {
                                try {
                                    invokeMethod(
                                            OS.class,
                                            "DeleteGlobalRef",
                                            new Object[] { wrapPointer(delegateJniRef) });
                                } catch (Exception e) {
                                    // theoretically, one of SecurityException,Illegal*Exception,InvocationTargetException,NoSuch*Exception
                                    // not expected to happen at all.
                                    log(e);
                                }
                            }
                            delegateJniRef = 0;

                            if (delegate != null)
                                delegate.release();
                            delegate = null;

                        }
                    });

                } catch (Exception e) {
                    // theoretically, one of SecurityException,Illegal*Exception,InvocationTargetException,NoSuch*Exception
                    // not expected to happen at all.
                    log(e);
                }
            }

        });
    }

    /**
     * Log exception.
     *
     * @param e
     */
    void log(Exception e) {
        System.err.println("Cocoa application menu"+e.getMessage());
    }

    /**
     *
     */
    private void hookApplicationMenu() {
        try {
            // create About Eclipse menu command
            NSMenu mainMenu = NSApplication.sharedApplication().mainMenu();
            NSMenuItem mainMenuItem = (NSMenuItem) invokeMethod(NSMenu.class,
                    mainMenu, "itemAtIndex", new Object[] { wrapPointer(0) });
            NSMenu appMenu = mainMenuItem.submenu();

            // add the about action
            NSMenuItem aboutMenuItem = (NSMenuItem) invokeMethod(NSMenu.class,
                    appMenu, "itemAtIndex",
                    new Object[] { wrapPointer(kAboutMenuItem) });
            aboutMenuItem.setTitle(NSString.stringWith(fAboutActionName));

            // rename the hide action if we have an override string
            if (fHideActionName != null) {
                NSMenuItem hideMenuItem = (NSMenuItem) invokeMethod(
                        NSMenu.class, appMenu, "itemAtIndex",
                        new Object[] { wrapPointer(kHideApplicationMenuItem) });
                hideMenuItem.setTitle(NSString.stringWith(fHideActionName));
            }

            // rename the quit action if we have an override string
            if (fQuitActionName != null) {
                NSMenuItem quitMenuItem = (NSMenuItem) invokeMethod(
                        NSMenu.class, appMenu, "itemAtIndex",
                        new Object[] { wrapPointer(kQuitMenuItem) });
                quitMenuItem.setTitle(NSString.stringWith(fQuitActionName));
            }

            // enable pref menu
            NSMenuItem prefMenuItem = (NSMenuItem) invokeMethod(NSMenu.class,
                    appMenu, "itemAtIndex",
                    new Object[] { wrapPointer(kPreferencesMenuItem) });
            prefMenuItem.setEnabled(true);

            // disable services menu
            NSMenuItem servicesMenuItem = (NSMenuItem) invokeMethod(
                    NSMenu.class, appMenu, "itemAtIndex",
                    new Object[] { wrapPointer(kServicesMenuItem) });
            servicesMenuItem.setEnabled(false);

            // Register as a target on the prefs items.
            prefMenuItem.setTarget(delegate);
            invokeMethod(
                    NSMenuItem.class,
                    prefMenuItem,
                    "setAction",
                    new Object[] { wrapPointer(sel_preferencesMenuItemSelected_) });
            aboutMenuItem.setTarget(delegate);
            invokeMethod(NSMenuItem.class, aboutMenuItem, "setAction",
                    new Object[] { wrapPointer(sel_aboutMenuItemSelected_) });
        } catch (Exception e) {
            // theoretically, one of SecurityException,Illegal*Exception,InvocationTargetException,NoSuch*Exception
            // not expected to happen at all.
            log(e);
        }
    }

    /**
     * This method is called when preferences menu item is selected.
     */
    void preferencesMenuItemSelected() {
    	new SettingWindow(mainWindow).open();
    }

    /**
     * This method is called when about menu item is selected.
     */
    void aboutMenuItemSelected() {
        new Thread(new AboutRunnable()).start();
    }

    /**
     * This method is called back from native.
     *
     * @param id
     * @param sel
     * @param arg0
     * @return
     * @throws Exception
     */
    public static int actionProc(int id, int sel, int arg0) throws Exception {
        return (int) actionProc((long) id, (long) sel, (long) arg0);
    }

    /**
     * @param id
     * @param sel
     * @param arg0
     * @return
     * @throws Exception
     */
    public static long actionProc(long id, long sel, long arg0)
            throws Exception {
        long[] jniRef = OS_object_getInstanceVariable(id, SWT_OBJECT);
        if (jniRef[0] == 0)
            return 0;

        CocoaUIEnhancer delegate = (CocoaUIEnhancer) invokeMethod(OS.class,
                "JNIGetObject", new Object[] { wrapPointer(jniRef[0]) });

        if (sel == sel_preferencesMenuItemSelected_) {
            delegate.preferencesMenuItemSelected();
        } else if (sel == sel_aboutMenuItemSelected_) {
            delegate.aboutMenuItemSelected();
        }

        return 0;
    }

    /*
     * The following methods reflectively call corresponding methods in the OS
     * class, using ints or longs as required based on platform.
     */

    /**
     *
     *
     * @param arg0
     * @return
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @category reflectively_called
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	static NSControl new_NSControl(long arg0) throws NoSuchMethodException,
            InstantiationException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        Class clazz = NSControl.class;
        Class PTR_CLASS = C.PTR_SIZEOF == 8 ? long.class : int.class;
        Constructor constructor = clazz
                .getConstructor(new Class[] { PTR_CLASS });
        return (NSControl) constructor
                .newInstance(new Object[] { wrapPointer(arg0) });
    }

    /**
     * Specialized method.  It's behavior is isolated and different enough from the usual invocation that custom code is warranted.
     * @param delegateId
     * @param name
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @category reflectively_called
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	static long[] OS_object_getInstanceVariable(long delegateId, byte[] name)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, SecurityException, NoSuchMethodException {
        Class clazz = OS.class;
        Method method = null;
        Class PTR_CLASS = C.PTR_SIZEOF == 8 ? long.class : int.class;
        if (PTR_CLASS == long.class) {
            method = clazz.getMethod("object_getInstanceVariable", new Class[] {
                    long.class, byte[].class, long[].class });
            long[] resultPtr = new long[1];
            method.invoke(null, new Object[] { new Long(delegateId), name,
                    resultPtr });
            return resultPtr;
        }
        method = clazz.getMethod("object_getInstanceVariable", new Class[] {
                int.class, byte[].class, int[].class });
        int[] resultPtr = new int[1];
        method.invoke(null, new Object[] { new Integer((int) delegateId), name,
                resultPtr });
        return new long[] { resultPtr[0] };
    }

    /**
     * @param object
     * @return
     * @category reflectively_called
     */
    long convertToLong(Object object) {
        if (object instanceof Integer) {
            Integer i = (Integer) object;
            return i.longValue();
        }
        if (object instanceof Long) {
            Long l = (Long) object;
            return l.longValue();
        }
        return 0;
    }

    /**
     * @param clazz
     * @param methodName
     * @param args
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @category reflectively_called
     */
    @SuppressWarnings("rawtypes")
	static Object invokeMethod(Class clazz, String methodName, Object[] args)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, SecurityException, NoSuchMethodException {
        return invokeMethod(clazz, null, methodName, args);
    }

    /**
     * @param clazz
     * @param target
     * @param methodName
     * @param args
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @category reflectively_called
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	static Object invokeMethod(Class clazz, Object target, String methodName,
            Object[] args) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            SecurityException, NoSuchMethodException {
        Class[] signature = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            Class thisClass = args[i].getClass();
            if (thisClass == Integer.class)
                signature[i] = int.class;
            else if (thisClass == Long.class)
                signature[i] = long.class;
            else if (thisClass == Byte.class)
                signature[i] = byte.class;
            else
                signature[i] = thisClass;
        }
        Method method = clazz.getMethod(methodName, signature);
        return method.invoke(target, args);
    }

    /**
     * @param value
     * @return
     * @category reflectively_called
     */
    @SuppressWarnings("rawtypes")
	static Object wrapPointer(long value) {
        Class PTR_CLASS = C.PTR_SIZEOF == 8 ? long.class : int.class;
        if (PTR_CLASS == long.class)
            return new Long(value);

        return new Integer((int) value);
    }
}