package voogasalad.util.explorer;



import java.util.ResourceBundle;

/**
 * A class to contain public static references to resource bundles,
 * so that multiple classes requiring access to these properties do
 * not need to independently and repeatedly instantiate them.
 *
 */
public class VoogaBundles {
    
	public final static ResourceBundle extensionProperties = ResourceBundle.getBundle("voogasalad/util/explorer/properties/extensions");

}