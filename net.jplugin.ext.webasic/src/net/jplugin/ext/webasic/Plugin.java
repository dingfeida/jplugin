package net.jplugin.ext.webasic;

import java.util.Map;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.ClassDefine;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.webasic.api.IController;
import net.jplugin.ext.webasic.api.IControllerSet;
import net.jplugin.ext.webasic.api.ObjectDefine;
import net.jplugin.ext.webasic.api.WebFilter;
import net.jplugin.ext.webasic.impl.InitRequestInfoFilter;
import net.jplugin.ext.webasic.impl.WebDriver;
import net.jplugin.ext.webasic.impl.restm.RestMethodController;
import net.jplugin.ext.webasic.impl.restm.RestMethodControllerSet;
import net.jplugin.ext.webasic.impl.restm.RestMethodControllerSet4Invoker;
import net.jplugin.ext.webasic.impl.rests.ServiceControllerSet;
import net.jplugin.ext.webasic.impl.rmethod.RmethodControllerSet;
import net.jplugin.ext.webasic.impl.web.WebControllerSet;
import net.jplugin.ext.webasic.impl.web.webex.WebExControllerSet;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-2 下午04:57:02
 **/

public class Plugin extends AbstractPlugin{

	public static final String EP_WEBFILTER = "EP_WEBFILTER";
	public static final String EP_CONTROLLERSET = "EP_CONTROLLERSET";

	public static final String EP_WEBCONTROLLER = "EP_WEBCONTROLLER";
	public static final String EP_WEBEXCONTROLLER = "EP_WEBEXCONTROLLER";
	public static final String EP_RESTMETHOD = "EP_RESTMETHOD";
	public static final String EP_RESTSERVICE = "EP_RESTSERVICE";
	public static final String EP_REMOTECALL = "EP_REMOTECALL";


	public Plugin(){
		this.addExtensionPoint(ExtensionPoint.create(EP_CONTROLLERSET, IControllerSet.class));
		this.addExtensionPoint(ExtensionPoint.create(EP_WEBFILTER, WebFilter.class));
		this.addExtensionPoint(ExtensionPoint.create(EP_RESTSERVICE, ObjectDefine.class, true));
		this.addExtensionPoint(ExtensionPoint.create(EP_WEBCONTROLLER, ObjectDefine.class, true));
		this.addExtensionPoint(ExtensionPoint.create(EP_WEBEXCONTROLLER, ClassDefine.class, true));
		this.addExtensionPoint(ExtensionPoint.create(EP_REMOTECALL, ObjectDefine.class, true));
		this.addExtensionPoint(ExtensionPoint.create(EP_RESTMETHOD, ObjectDefine.class, true));
		
		this.addExtension(Extension.create(EP_WEBFILTER,"",InitRequestInfoFilter.class));
		this.addExtension(Extension.create(EP_CONTROLLERSET,"",WebControllerSet.class));
		this.addExtension(Extension.create(EP_CONTROLLERSET,"",ServiceControllerSet.class));
		this.addExtension(Extension.create(EP_CONTROLLERSET,"",RmethodControllerSet.class));
//		this.addExtension(Extension.create(EP_CONTROLLERSET,"",RestMethodControllerSet.class));
		this.addExtension(Extension.create(EP_CONTROLLERSET,"",RestMethodControllerSet4Invoker.class));
		this.addExtension(Extension.create(EP_CONTROLLERSET,"",WebExControllerSet.class));
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.AbstractPlugin#getPrivority()
	 */
	@Override
	public int getPrivority() {
		return CoreServicePriority.WEBSERVICE;
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IPlugin#init()
	 */
	public void init() {
		WebDriver.INSTANCE.init();
	}
}
